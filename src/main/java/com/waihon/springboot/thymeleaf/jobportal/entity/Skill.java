package com.waihon.springboot.thymeleaf.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "skills")
public class Skill {
    // Create a stub first for the purpose of defining JobSeekerProfile.

    @Id
    private int id;

    private int jobSeekerProfile;

}
