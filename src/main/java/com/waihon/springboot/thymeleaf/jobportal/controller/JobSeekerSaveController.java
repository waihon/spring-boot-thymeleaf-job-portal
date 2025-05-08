package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerSave;
import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.exception.JobNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.exception.JobSeekerNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.exception.UserNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.service.JobPostActivityService;
import com.waihon.springboot.thymeleaf.jobportal.service.JobSeekerProfileService;
import com.waihon.springboot.thymeleaf.jobportal.service.JobSeekerSaveService;
import com.waihon.springboot.thymeleaf.jobportal.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobSeekerSaveController(UserService userService,
                                   JobSeekerProfileService jobSeekerProfileService,
                                   JobPostActivityService jobPostActivityService,
                                   JobSeekerSaveService jobSeekerSaveService) {
        this.userService = userService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id,
                       JobSeekerSave jobSeekerSave,
                       RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        try {
            String currentUsername = authentication.getName();
            User user = userService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);

            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerSave = new JobSeekerSave();
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUser(seekerProfile.get());
            } else {
                throw new JobSeekerNotFoundException("Job Seeker not found for ID '" + user.getUserId() + "'.");
            }

            jobSeekerSaveService.addNew(jobSeekerSave);
            redirectAttributes.addFlashAttribute("success", "Job saved successfully.");

        } catch (UserNotFoundException | JobNotFoundException | JobSeekerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "Could not save job: " + ex.getMessage());
            return "redirect:/job-details-apply/" + id;
        }

        return "redirect:/job-details-apply/" + id;
    }

    @GetMapping("/saved-jobs")
    public String savedJobs(Model model) {
        model.addAttribute("currentPage", "saved-jobs");

        List<JobPostActivity> jobPosts = new ArrayList<>();
        Object currentUserProfile = userService.getCurrentUserProfile();

        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService
                .getCandidateJobs((JobSeekerProfile) currentUserProfile);
        for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
            jobPosts.add(jobSeekerSave.getJob());
        }

        model.addAttribute("jobPosts", jobPosts);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }
}
