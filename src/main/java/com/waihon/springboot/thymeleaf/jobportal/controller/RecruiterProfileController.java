package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.RecruiterProfile;
import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.repository.UserRepository;
import com.waihon.springboot.thymeleaf.jobportal.service.RecruiterProfileService;
import com.waihon.springboot.thymeleaf.jobportal.util.FileUploadUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            recruiterProfile.ifPresent(profile ->
                    model.addAttribute("profile", profile));
        }

        return "recruiter-profile";
    }

    @PostMapping("/add-new")
    public String addNew(RecruiterProfile recruiterProfile,
                         @RequestParam("image")MultipartFile multipartFile,
                         Model model) {
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
