package com.waihon.springboot.thymeleaf.jobportal.service;

import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addNew(User user) {
        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));

        return userRepository.save(user);
    }

}
