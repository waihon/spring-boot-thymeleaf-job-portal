package com.waihon.springboot.thymeleaf.jobportal.validation;

import com.waihon.springboot.thymeleaf.jobportal.entity.JobSeekerProfile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneSkillValidator implements ConstraintValidator<AtLeastOneSkill, JobSeekerProfile> {

    @Override
    public boolean isValid(JobSeekerProfile profile, ConstraintValidatorContext context) {
        if (profile.getSkills() == null) {
            return false;
        }

        return profile.getSkills().stream().anyMatch(skill ->
                skill != null &&
                        (skill.getName() != null && !skill.getName().trim().isEmpty() ||
                                skill.getYearsOfExperience() != null && !skill.getYearsOfExperience().trim().isEmpty() ||
                                skill.getExperienceLevel() != null && !skill.getExperienceLevel().trim().isEmpty())
        );
    }
}
