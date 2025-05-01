package com.waihon.springboot.thymeleaf.jobportal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// This annotation can be used on classes, not just fields or methods.
@Target({ ElementType.TYPE })
// The annotation is available at runtime, which is required for Spring validation.
@Retention(RetentionPolicy.RUNTIME)
// Links this annotation to a class that contains the validation logic.
@Constraint(validatedBy = AtLeastOneSkillValidator.class)
// Declares a new annotation named @AtLeastOneSkill
public @interface AtLeastOneSkill {
    // Default error message if validation fails (can be overriden when used).
    String message() default "At least one valid skill is required.";
    // Allows grouping contraints for conditional validation (e.g. OnCreate, OnUpdate).
    Class<?>[] groups() default {};
    // Not often used; allows attaching metadata to the annotation (e.g. severity level).
    Class<? extends Payload>[] payload() default {};
}
