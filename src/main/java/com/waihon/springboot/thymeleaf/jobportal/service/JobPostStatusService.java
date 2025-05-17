package com.waihon.springboot.thymeleaf.jobportal.service;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;
import com.waihon.springboot.thymeleaf.jobportal.service.JobSeekerApplyService;
import com.waihon.springboot.thymeleaf.jobportal.service.JobSeekerSaveService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class JobPostStatusService {

    private final JobSeekerApplyService applyService;
    private final JobSeekerSaveService saveService;

    public JobPostStatusService(JobSeekerApplyService applyService, JobSeekerSaveService saveService) {
        this.applyService = applyService;
        this.saveService = saveService;
    }

    public void markStatuses(List<JobPostActivity> jobPosts, Object currentUserProfile) {
        JobSeekerProfile seekerProfile = (JobSeekerProfile) currentUserProfile;

        Set<Integer> appliedIds = applyService.getCandidateJobs(seekerProfile)
                .stream()
                .map(apply -> apply.getJob().getJobPostId())
                .collect(Collectors.toSet());

        Set<Integer> savedIds = saveService.getCandidateJobs(seekerProfile)
                .stream()
                .map(save -> save.getJob().getJobPostId())
                .collect(Collectors.toSet());

        for (JobPostActivity jobPost : jobPosts) {
            Integer id = jobPost.getJobPostId();
            jobPost.setActive(appliedIds.contains(id));
            jobPost.setSaved(savedIds.contains(id));
        }
    }
}
