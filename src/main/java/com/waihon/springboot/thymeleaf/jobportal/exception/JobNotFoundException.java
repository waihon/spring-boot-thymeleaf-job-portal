package com.waihon.springboot.thymeleaf.jobportal.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
