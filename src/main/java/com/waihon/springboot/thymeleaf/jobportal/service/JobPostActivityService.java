package com.waihon.springboot.thymeleaf.jobportal.service;

import com.waihon.springboot.thymeleaf.jobportal.entity.*;
import com.waihon.springboot.thymeleaf.jobportal.exception.JobNotFoundException;
import com.waihon.springboot.thymeleaf.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job ID '" + id + "' not found."));
    }

    public void deleteOne(Integer id) {
        jobPostActivityRepository.deleteById(id);
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type,
                                        List<String> remote, LocalDate searchDate) {
        return Objects.isNull(searchDate) ?
                jobPostActivityRepository.searchWithoutDate(job, location, remote, type) :
                jobPostActivityRepository.search(job, location, remote, type, searchDate);
    }
}
