package com.waihon.springboot.thymeleaf.jobportal.util;

import com.waihon.springboot.thymeleaf.jobportal.entity.Skill;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SkillUtils {

    public static List<Skill> filterEmptySkills(List<Skill> skills) {
        if (skills == null) {
            return List.of();
        }

        return skills.stream()
                .filter(skill -> skill != null &&
                        skill.getName() != null && !skill.getName().isBlank() &&
                        skill.getYearsOfExperience() != null && !skill.getYearsOfExperience().isBlank() &&
                        skill.getExperienceLevel() != null && !skill.getExperienceLevel().isBlank()
                )
                .collect(Collectors.toList());
    }

}
