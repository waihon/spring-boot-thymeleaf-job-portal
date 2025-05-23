package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.constants.SecurityConstants;
import com.waihon.springboot.thymeleaf.jobportal.dto.RecruiterJobDto;
import com.waihon.springboot.thymeleaf.jobportal.dto.SearchFilter;
import com.waihon.springboot.thymeleaf.jobportal.entity.*;
import com.waihon.springboot.thymeleaf.jobportal.enums.CrudMode;
import com.waihon.springboot.thymeleaf.jobportal.service.*;
import com.waihon.springboot.thymeleaf.jobportal.util.StringUtil;
import com.waihon.springboot.thymeleaf.jobportal.validation.ValidationSequence;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.waihon.springboot.thymeleaf.jobportal.constants.SecurityConstants.RECRUITER_ROLE;

@Controller
public class JobPostActivityController {

    private final UserService userService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobPostStatusService jobPostStatusService;

    @Autowired
    public JobPostActivityController(UserService userService,
                                     JobPostActivityService jobPostActivityService,
                                     JobSeekerApplyService jobSeekerApplyService,
                                     JobSeekerSaveService jobSeekerSaveService,
                                     JobPostStatusService jobPostStatusService) {
        this.userService = userService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobPostStatusService = jobPostStatusService;
    }

    @GetMapping("/dashboard")
    public String searchJobs(@ModelAttribute SearchFilter searchFilter, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        String currentUsername = authentication.getName();
        Object currentUserProfile = userService.getCurrentUserProfile();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(RECRUITER_ROLE))) {
            int recruiterId = ((RecruiterProfile)currentUserProfile).getUserAccountId();

            List<RecruiterJobDto> recruiterJobDtos = jobPostActivityService.getRecruiterJobs(recruiterId);

            model.addAttribute("jobPosts", recruiterJobDtos);
        } else {
            LocalDate searchDate = resolveSearchDate(searchFilter);
            List<String> employmentTypes = resolveEmploymentTypes(searchFilter);
            List<String> workModels = resolveWorkModels(searchFilter);

            boolean isEmptySearch = isSearchEmpty(searchFilter, searchDate, employmentTypes, workModels);

            List<JobPostActivity> jobPosts = isEmptySearch
                    ? jobPostActivityService.getAll()
                    : jobPostActivityService.search(
                    searchFilter.getJob(),
                    searchFilter.getLocation(),
                    employmentTypes,
                    workModels,
                    searchDate
            );
            model.addAttribute("jobPosts", jobPosts);

            jobPostStatusService.markStatuses(jobPosts, currentUserProfile);

        }

        saveSearchAttributes(model, searchFilter);
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("username", currentUsername);
        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    @GetMapping("/global-search")
    public String globalSearch(@ModelAttribute SearchFilter searchFilter, Model model) {
        saveSearchAttributes(model, searchFilter);

        LocalDate searchDate = resolveSearchDate(searchFilter);
        List<String> employmentTypes = resolveEmploymentTypes(searchFilter);
        List<String> workModels = resolveWorkModels(searchFilter);

        boolean isEmptySearch = isSearchEmpty(searchFilter, searchDate, employmentTypes, workModels);

        List<JobPostActivity> jobPosts = isEmptySearch
                ? jobPostActivityService.getAll()
                : jobPostActivityService.search(
                        searchFilter.getJob(),
                        searchFilter.getLocation(),
                        employmentTypes,
                        workModels,
                        searchDate
        );

        model.addAttribute("jobPosts", jobPosts);

        return "global-search";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model, HttpServletRequest request) {
        String returnUrl = resolveReturnUrl(request, CrudMode.CREATE);
        model.addAttribute("returnUrl", returnUrl);

        model.addAttribute("currentPage", "add-jobs");

        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", userService.getCurrentUserProfile());

        return "add-jobs";
    }

    @PostMapping("/dashboard/add-new")
    public String addNew(@Validated(ValidationSequence.class) @ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity,
                         BindingResult result,
                         Model model,
                         HttpServletRequest request) {
        return saveJob(null, jobPostActivity, result, model, request, CrudMode.CREATE);
    }

    @GetMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model,
                          HttpServletRequest request) {
        String returnUrl = resolveReturnUrl(request, CrudMode.UPDATE);
        model.addAttribute("returnUrl", returnUrl);

        model.addAttribute("currentPage", "edit-jobs");

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);

        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", userService.getCurrentUserProfile());

        return "edit-jobs";
    }

    @PostMapping("/dashboard/save-edit/{id}")
    public String saveEdit(@PathVariable("id") int id,
                           @Validated(ValidationSequence.class) @ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request) {
        return saveJob(id, jobPostActivity, result, model, request, CrudMode.UPDATE);
    }

    @DeleteMapping("/dashboard/delete-job/{id}")
    public String deleteJob(@PathVariable("id") int id) {
        jobPostActivityService.deleteOne(id);

        return "redirect:/dashboard";
    }

    @ModelAttribute
    public SearchFilter defaultSearchFilter() {
        return new SearchFilter(); // with defaults if desired
    }

    private String saveJob(Integer jobId,
                           @Validated(ValidationSequence.class) @ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request,
                           CrudMode mode) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(err ->
                    System.out.println("❗ Validation error: " + err.getDefaultMessage())
            );

            String returnUrl = resolveReturnUrl(request, mode);
            model.addAttribute("returnUrl", returnUrl);

            return mode == CrudMode.CREATE ? "add-jobs": "edit-jobs"; // re-render form with errors
        }

        User user = userService.getCurrentUser();
        if (user != null) {
            jobPostActivity.setPostedBy(user);
        }
        jobPostActivity.setPostedDate(new Date());

        model.addAttribute("jobPostActivity", jobPostActivity);

        JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);

        return "redirect:/dashboard";
    }

    private String resolveReturnUrl(HttpServletRequest request, CrudMode mode) {
        String referer = request.getHeader("Referer");
        String returnUrl;
        if (mode == CrudMode.CREATE)
            returnUrl = (referer != null && !referer.endsWith("/dashboard/add") &&
                    !referer.endsWith("/dashboard/add-new"))
                    ? referer
                    : "/dashboard"; // Default fallback
        else {
            Integer jobId = StringUtil.extractLastNumber(referer);
            returnUrl = (referer != null && !referer.contains("/dashboard/edit") &&
                    !referer.contains("/dashboard/save-edit"))
                    ? referer
                    : (jobId != null ? "/job-details-apply/" + jobId : "/dashboard"); // Default fallback
        }

        System.out.println("referer: " + referer);
        System.out.println("refernUrl: " + returnUrl);

        return returnUrl;
    }

    private void saveSearchAttributes(Model model, SearchFilter searchFilter) {
        // Employment type
        model.addAttribute("partTime", "Part-time".equals(searchFilter.getPartTime()));
        model.addAttribute("fullTime", "Full-time".equals(searchFilter.getFullTime()));
        model.addAttribute("freelance", "Freelance".equals(searchFilter.getFreelance()));

        // Work model
        model.addAttribute("remoteOnly", "Remote-Only".equals(searchFilter.getRemoteOnly()));
        model.addAttribute("officeOnly", "Office-Only".equals(searchFilter.getOfficeOnly()));
        model.addAttribute("partialRemote", "Partial-Remote".equals(searchFilter.getPartialRemote()));

        // Date posted
        model.addAttribute("today", searchFilter.isToday());
        model.addAttribute("days7", searchFilter.isDays7());
        model.addAttribute("days30", searchFilter.isDays30());

        // Search boxes
        model.addAttribute("job", searchFilter.getJob());
        model.addAttribute("location", searchFilter.getLocation());
    }

    private LocalDate resolveSearchDate(SearchFilter filter) {
        if (filter.isDays30()) return LocalDate.now().minusDays(30);
        if (filter.isDays7()) return LocalDate.now().minusDays(7);
        if (filter.isToday()) return LocalDate.now();
        return null;
    }

    private List<String> resolveEmploymentTypes(SearchFilter filter) {
        if (filter.getPartTime() == null && filter.getFullTime() == null && filter.getFreelance() == null) {
            return Arrays.asList("Part-time", "Full-time", "Freelance");
        }
        return Stream.of(filter.getPartTime(), filter.getFullTime(), filter.getFreelance())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<String> resolveWorkModels(SearchFilter filter) {
        if (filter.getRemoteOnly() == null && filter.getOfficeOnly() == null && filter.getPartialRemote() == null) {
            return Arrays.asList("Remote-Only", "Office-Only", "Partial-Remote");
        }
        return Stream.of(filter.getRemoteOnly(), filter.getOfficeOnly(), filter.getPartialRemote())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean isSearchEmpty(SearchFilter filter, LocalDate date, List<String> types, List<String> models) {
        return date == null &&
                types.size() == 3 &&
                models.size() == 3 &&
                !StringUtils.hasText(filter.getJob()) &&
                !StringUtils.hasText(filter.getLocation());
    }

}
