package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.RecruiterProfile;
import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.repository.UserRepository;
import com.waihon.springboot.thymeleaf.jobportal.service.RecruiterProfileService;
import com.waihon.springboot.thymeleaf.jobportal.util.FileUploadUtil;
import com.waihon.springboot.thymeleaf.jobportal.validation.OnCreate;
import com.waihon.springboot.thymeleaf.jobportal.validation.OnUpdate;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UserRepository userRepository;
    private final RecruiterProfileService recruiterProfileService;

    @Autowired
    public RecruiterProfileController(UserRepository userRepository, RecruiterProfileService recruiterProfileService) {
        this.userRepository = userRepository;
        this.recruiterProfileService = recruiterProfileService;
    }


    @GetMapping("")
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));

            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(user.getUserId());
            recruiterProfile.ifPresent(profile -> {
                model.addAttribute("profile", profile);
                Boolean newProfile = !StringUtils.hasLength(profile.getFirstName()) ||
                        !StringUtils.hasLength(profile.getLastName());
                model.addAttribute("newProfile", newProfile);
            });
        }

        return "recruiter-profile";
    }

    @PostMapping("/add-new")
    public String addNew(@Validated(OnUpdate.class) @ModelAttribute("profile") RecruiterProfile recruiterProfile,
                         BindingResult result,
                         @RequestParam("image")MultipartFile multipartFile,
                         Model model) {
        if (result.hasErrors()) {
            Boolean newProfile = !StringUtils.hasLength(recruiterProfile.getFirstName()) ||
                    !StringUtils.hasLength(recruiterProfile.getLastName());
            model.addAttribute("newProfile", newProfile);

            return "recruiter-profile"; // re-render form with errors
        }

        // Associate recruiter profile with existing user account
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof  AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
            recruiterProfile.setUser(user);
            recruiterProfile.setUserAccountId(user.getUserId());
        }

        model.addAttribute("profile", recruiterProfile);

        // Set image name in recruiter profile
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }

        // Save recruiter profile to DB
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);

        // Read profile image from request's multipart file and save image
        // on the server in directory: photos/recruiter
        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/dashboard";
    }

}
