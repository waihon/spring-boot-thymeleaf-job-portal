package com.waihon.springboot.thymeleaf.jobportal.service;

import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.repository.UserRepository;
import com.waihon.springboot.thymeleaf.jobportal.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));

        return new CustomUserDetails(user);
    }

}
