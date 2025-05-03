package com.waihon.springboot.thymeleaf.jobportal.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public RedirectView handleUserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("customError", ex.getMessage());
        return new RedirectView("/login");
    }

    @ExceptionHandler(Exception.class)
    public RedirectView handleOtherExceptions(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("customError", "An unexpected error occurred: " + ex.getMessage());
        return new RedirectView("/login");
    }
}
