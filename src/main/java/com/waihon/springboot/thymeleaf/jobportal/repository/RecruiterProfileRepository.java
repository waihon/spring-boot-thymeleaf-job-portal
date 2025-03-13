package com.waihon.springboot.thymeleaf.jobportal.repository;

import com.waihon.springboot.thymeleaf.jobportal.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {
}
