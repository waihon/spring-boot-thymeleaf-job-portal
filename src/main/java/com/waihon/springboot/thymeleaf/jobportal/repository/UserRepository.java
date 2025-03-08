package com.waihon.springboot.thymeleaf.jobportal.repository;

import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
