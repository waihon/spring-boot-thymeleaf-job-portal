package com.waihon.springboot.thymeleaf.jobportal.repository;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    List<JobSeekerSave> findByUser(JobSeekerProfile user);

    List<JobSeekerSave> findByJob(JobPostActivity job);

}
