package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.*;
import com.waihon.springboot.thymeleaf.jobportal.exception.JobNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.exception.JobSeekerNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.exception.UserNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.service.*;
import com.waihon.springboot.thymeleaf.jobportal.constants.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.waihon.springboot.thymeleaf.jobportal.constants.SecurityConstants.RECRUITER_ROLE;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UserService userService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService,
                                    UserService userService,
                                    JobSeekerApplyService jobSeekerApplyService,
                                    RecruiterProfileService recruiterProfileService,
                                    JobSeekerProfileService jobSeekerProfileService,
                                    JobSeekerSaveService jobSeekerSaveService) {
        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        JobPostActivity jobPost = jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getJobCandidates(jobPost);
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getJobCandidates(jobPost);

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(RECRUITER_ROLE))) {
            RecruiterProfile  recruiter = recruiterProfileService.getCurrentRecruiterProfile();
            if (recruiter != null) {
                // For displaying a list of job seekers who have applifed for job
                model.addAttribute("jobSeekerApplyList", jobSeekerApplyList);
                // For determining whether to show Delete job button
                model.addAttribute("jobHasApplies", jobSeekerApplyList.size() > 0);
                model.addAttribute("jobHasSaves", jobSeekerSaveList.size() > 0);
            }
        } else {
            JobSeekerProfile jobSeeker = jobSeekerProfileService.getCurrentSeekerProfile();
            if (jobSeeker != null) {
                boolean applied = false;
                for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                    if (jobSeekerApply.getUser().getUserAccountId() == jobSeeker.getUserAccountId()) {
                        applied = true;
                        break;
                    }
                }
                boolean saved = false;
                for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                    if (jobSeekerSave.getUser().getUserAccountId() == jobSeeker.getUserAccountId()) {
                        saved = true;
                        break;
                    }
                }
                // For displaying Already Applied button instead of Apply Now button
                model.addAttribute("alreadyApplied", applied);
                // For displaying Already Saved button instead of Save Job button
                model.addAttribute("alreadySaved", saved);
                // For displaying Apply Now and Save Job buttons
                JobSeekerApply jobSeekerApply = new JobSeekerApply();
                model.addAttribute("jobSeekerApply", jobSeekerApply);
            }
        }

        // For displaying job details
        model.addAttribute("jobPost", jobPost);
        // For displaying user information on the header
        Object user = userService.getCurrentUserProfile();
        model.addAttribute("user", user);
        String currentUsername = authentication.getName();
        model.addAttribute("username", currentUsername);

        return "job-details";
    }

    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id,
                        JobSeekerApply jobSeekerApply,
                        RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        try {
            String currentUsername = authentication.getName();
            User user = userService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);

            if (jobSeekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerApply = new JobSeekerApply();
                jobSeekerApply.setUser(jobSeekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            } else {
                throw new JobSeekerNotFoundException("Job Seeker not found for ID '" + user.getUserId() + "'.");
            }

            jobSeekerApplyService.addNew(jobSeekerApply);
            redirectAttributes.addFlashAttribute("success", "Job applied successfully.");

        } catch (UserNotFoundException | JobNotFoundException | JobSeekerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "Could not apply job: " + ex.getMessage());
            return "redirect:/job-details-apply/" + id;
        }

        return "redirect:/job-details-apply/" + id;
    }

}
