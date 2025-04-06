package com.waihon.springboot.thymeleaf.jobportal.repository;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerApply;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByUser(JobSeekerProfile user);

    List<JobSeekerApply> findByJob(JobPostActivity job);

}
