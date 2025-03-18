package com.waihon.springboot.thymeleaf.jobportal.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Once logged in successfully:
        // o Retrieve the user details object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("The username " + username + " is logged in.");

        // o Check the roles for the user
        boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Job Seeker"));
        boolean hasRecruiterRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Recruiter"));

        // o If job seeker or recruiter role then send them to dashboard page
        if (hasRecruiterRole || hasJobSeekerRole) {
            response.sendRedirect("/dashboard/");
        }


    }

}
