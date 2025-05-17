package com.waihon.springboot.thymeleaf.jobportal.service;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerApply;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerSave;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JobPostStatusServiceTest {

    private JobSeekerApplyService applyService;
    private JobSeekerSaveService saveService;
    private JobPostStatusService statusService;

    @BeforeEach
    void setUp() {
        applyService = mock(JobSeekerApplyService.class);
        saveService = mock(JobSeekerSaveService.class);
        statusService = new JobPostStatusService(applyService, saveService);
    }

    @Test
    void testMarkStatuses() {
        JobSeekerProfile profile = new JobSeekerProfile();

        JobPostActivity post1 = new JobPostActivity();
        post1.setJobPostId(1);
        JobPostActivity post2 = new JobPostActivity();
        post2.setJobPostId(2);
        List<JobPostActivity> posts = List.of(post1, post2);

        JobSeekerApply apply = new JobSeekerApply();
        apply.setJob(post1); // user applied to post1

        JobSeekerSave save = new JobSeekerSave();
        save.setJob(post2);  // user saved post2

        when(applyService.getCandidateJobs(profile)).thenReturn(List.of(apply));
        when(saveService.getCandidateJobs(profile)).thenReturn(List.of(save));

        statusService.markStatuses(posts, profile);

        assertTrue(post1.isActive());
        assertFalse(post1.isSaved());

        assertFalse(post2.isActive());
        assertTrue(post2.isSaved());
    }
}
