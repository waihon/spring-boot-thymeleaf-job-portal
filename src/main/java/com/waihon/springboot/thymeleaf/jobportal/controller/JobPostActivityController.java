package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import com.waihon.springboot.thymeleaf.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobPostActivityController {

    private final UserService userService;

    @Autowired
    public JobPostActivityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String searchJobs(Model model) {
        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
        }

        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", userService.getCurrentUserProfile());

        return "add-jobs";
    }

}
