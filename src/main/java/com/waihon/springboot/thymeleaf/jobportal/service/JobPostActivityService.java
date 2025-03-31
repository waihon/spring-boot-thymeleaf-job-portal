package com.waihon.springboot.thymeleaf.jobportal.service;

import com.waihon.springboot.thymeleaf.jobportal.entity.*;
import com.waihon.springboot.thymeleaf.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobDto> getRecruiterJobs(int recruiter) {
        List<IRecruiterJob> recruiterJobs = jobPostActivityRepository.getRecruiterJobs(recruiter);
        List<RecruiterJobDto> recruiterJobDtos = new ArrayList<>();

        for (IRecruiterJob job : recruiterJobs) {
            JobLocation location = new JobLocation(job.getLocationId(), job.getCity(), job.getState(), job.getCountry());
            JobCompany company = new JobCompany(job.getCompanyId(), job.getName(), "");
            recruiterJobDtos.add(new RecruiterJobDto(job.getTotalCandidates(), job.getJobPostId(), job.getJobTitle(), location, company));
        }

        return recruiterJobDtos;
    }
}
