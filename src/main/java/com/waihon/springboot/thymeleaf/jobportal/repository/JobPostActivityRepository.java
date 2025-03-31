package com.waihon.springboot.thymeleaf.jobportal.repository;

import com.waihon.springboot.thymeleaf.jobportal.entity.IRecruiterJob;
import com.waihon.springboot.thymeleaf.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, Integer> {

    @Query(value = " SELECT COUNT(s.user_id) AS totalCandidates, j.job_post_id AS jobPostId, " +
            " j.job_title AS jobTitle, l.id AS locationId, l.city, l.state, l.country, " +
            " c.id AS companyId, c.name" +
            " FROM job_post_activities j " +
            " INNER JOIN job_locations l " +
            " ON j.job_location_id = l.id " +
            " INNER JOIN job_companies c " +
            " ON j.job_company_id = c.id " +
            " LEFT JOIN job_seeker_applies s " +
            " ON s.job_id = j.job_post_id " +
            " WHERE j.posted_by_id = :recruiter " +
            " GROUP BY j.job_post_id", nativeQuery = true)
    List<IRecruiterJob> getRecruiterJobs(@Param("recruiter") int recruiter);

}
