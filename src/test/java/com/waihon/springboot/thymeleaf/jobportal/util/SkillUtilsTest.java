package com.waihon.springboot.thymeleaf.jobportal.util;

import com.waihon.springboot.thymeleaf.jobportal.entity.Skill;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkillUtilsTest {

    @Test
    void testFilterEmptySkills_removesEmptySkills() {
        Skill skill1 = new Skill();
        skill1.setName("Java");
        skill1.setYearsOfExperience("3");
        skill1.setExperienceLevel("Intermediate");

        Skill skill2 = new Skill();
        skill2.setName("");  // blank name, should be removed
        skill2.setYearsOfExperience("1");
        skill2.setExperienceLevel("Beginner");

        Skill skill3 = new Skill();
        skill3.setName("Python");
        skill3.setYearsOfExperience("2");
        skill3.setExperienceLevel("Advance");

        Skill skill4 = new Skill();
        skill4.setName(null);  // null name, should be removed

        Skill skill5 = new Skill();
        skill5.setName(" ");  // blank name, should be removed

        List<Skill> skills = List.of(skill1, skill2, skill3, skill4, skill5);

        List<Skill> result = SkillUtils.filterEmptySkills(skills);

        assertEquals(2, result.size());
        assertTrue(result.contains(skill1));
        assertTrue(result.contains(skill3));
    }

    @Test
    void testFilterEmptySkills_handlesNullList() {
        List<Skill> result = SkillUtils.filterEmptySkills(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterEmptySkills_handlesNullSkillObjects() {
        List<Skill> skills = new ArrayList<>();
        skills.add(null);
        skills.add(new Skill());  // blank skill

        List<Skill> result = SkillUtils.filterEmptySkills(skills);
        assertTrue(result.isEmpty());
    }
}
