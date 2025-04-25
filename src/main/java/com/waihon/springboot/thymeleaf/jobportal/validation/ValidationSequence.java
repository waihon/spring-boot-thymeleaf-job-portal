package com.waihon.springboot.thymeleaf.jobportal.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({BasicCheck.class, AdvancedCheck.class})
public interface ValidationSequence { }
