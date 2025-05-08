package com.waihon.springboot.thymeleaf.jobportal.exception;

public class JobSeekerNotFoundException extends RuntimeException {
    public JobSeekerNotFoundException(String message) {
        super(message);
    }
}
