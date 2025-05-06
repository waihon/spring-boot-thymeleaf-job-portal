package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;
import com.waihon.springboot.thymeleaf.jobportal.entity.RecruiterProfile;
import com.waihon.springboot.thymeleaf.jobportal.entity.Skill;
import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.exception.FileUploadException;
import com.waihon.springboot.thymeleaf.jobportal.exception.UserNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.service.JobSeekerProfileService;
import com.waihon.springboot.thymeleaf.jobportal.service.UserService;
import com.waihon.springboot.thymeleaf.jobportal.util.FileDownloadUtil;
import com.waihon.springboot.thymeleaf.jobportal.util.FileUploadUtil;
import com.waihon.springboot.thymeleaf.jobportal.util.SkillUtils;
import com.waihon.springboot.thymeleaf.jobportal.validation.OnUpdate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private JobSeekerProfileService jobSeekerProfileService;
    private UserService userService;
    private Validator validator;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService,
                                      UserService userService,
                                      Validator validator) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping("")
    public String jobSeekerProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        model.addAttribute("currentPage", "job-seeker-profile");

        try {
            JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
            List<Skill> skills = new ArrayList<>();
            User user = userService.findByEmail(authentication.getName());

            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            if (seekerProfile.isPresent()) {
                jobSeekerProfile = seekerProfile.get();
                if (jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skill());
                    jobSeekerProfile.setSkills(skills);
                }
                Boolean isNewProfile = !StringUtils.hasText(jobSeekerProfile.getFirstName()) ||
                        !StringUtils.hasText(jobSeekerProfile.getLastName());
                model.addAttribute("isNewProfile", isNewProfile);
            } else {
                throw new UserNotFoundException("Job Seek Profile with ID '" + user.getUserId() + "' is not found.");
            }

            model.addAttribute("profile", jobSeekerProfile);
            model.addAttribute("skills", skills);
            model.addAttribute("canContinue", true);

        } catch (UserNotFoundException ex) {
            model.addAttribute("profile", new JobSeekerProfile());
            model.addAttribute("skills", new ArrayList<Skill>());
            model.addAttribute("canContinue", false);
            model.addAttribute("globalError", ex.getMessage());
        }

        return "job-seeker-profile";
    }

    @PostMapping("/add-new")
    public String addNew(@ModelAttribute("profile") JobSeekerProfile jobSeekerProfile,
                         BindingResult result,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        // On the front-end, there are 2 resume-related input of type file and text, respectively.
        // When the file input is not empty, update the underlying resume field, which is bound
        // to text input on the front-end.
        // This is placed near to top ahead of manual validation beneath it.
        String resumeName = "";
        if (pdf != null && !pdf.isEmpty()) {
            if (!Objects.equals(pdf.getOriginalFilename(), "")) {
                resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
                jobSeekerProfile.setResume(resumeName);
            }
        }

        // Manually trigger group-specific validation after resume is set
        Set<ConstraintViolation<JobSeekerProfile>> violations = validator.validate(jobSeekerProfile, OnUpdate.class);
        for (ConstraintViolation<JobSeekerProfile> v : violations) {
            String field = v.getPropertyPath().toString();

            if (field == null || field.isBlank()) {
                // Class-level constraint - use reject (global error)
                result.reject("", v.getMessage());
            } else {
                // Field-level constraint - use rejectValue
                result.rejectValue(field, "", v.getMessage());
            }
        }

        if (result.hasErrors()) {
            Boolean isNewProfile = !StringUtils.hasText(jobSeekerProfile.getFirstName()) ||
                    !StringUtils.hasText(jobSeekerProfile.getLastName());
            model.addAttribute("isNewProfile", isNewProfile);

            return "job-seeker-profile"; // re-render form with errors
        }

        try {
            String imageName = "";
            if (!Objects.equals(image.getOriginalFilename(), "")) {
                imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            }

            String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
            if (!Objects.equals(image.getOriginalFilename(), "")) {
                FileUploadUtil.saveFile(uploadDir, imageName, image);
            }
            if (!Objects.equals(pdf.getOriginalFilename(), "")) {
                FileUploadUtil.saveFile(uploadDir, resumeName, pdf);
            }

            User user = userService.findByEmail(authentication.getName());
            jobSeekerProfile.setUser(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());

            List<Skill> filteredSkills = SkillUtils.filterEmptySkills(jobSeekerProfile.getSkills());
            jobSeekerProfile.setSkills(filteredSkills);

            for (Skill skill : filteredSkills) {
                skill.setJobSeekerProfile(jobSeekerProfile);
            }

            if (StringUtils.hasText(imageName)) {
                jobSeekerProfile.setProfilePhoto(imageName);
            }

            JobSeekerProfile seekerProfile = jobSeekerProfileService.addNew(jobSeekerProfile);

            model.addAttribute("profile", jobSeekerProfile);

        } catch (UserNotFoundException | FileUploadException ex) {
            model.addAttribute("globalError", ex.getMessage());
            model.addAttribute("canContinue", true);

            // Return to the profile form with error
            return "job-seeker-profile";
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model) {
        Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(id);

        model.addAttribute("profile", seekerProfile.get());

        return "job-seeker-profile";
    }

    @GetMapping("/download-resume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName,
                                            @RequestParam(value = "userId") String userId) {
        FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource = fileDownloadUtil.getFileAsResource("photos/candidate/" + userId, fileName);
        } catch (IOException io) {
            return ResponseEntity.badRequest().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

}
