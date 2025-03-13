package com.waihon.springboot.thymeleaf.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String experienceLevel;
    private String yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "job_seeker_profile")
    private int jobSeekerProfile;

    public Skill() {
    }

    public Skill(int id, String name, String experienceLevel, String yearsOfExperience, int jobSeekerProfile) {
        this.id = id;
        this.name = name;
        this.experienceLevel = experienceLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.jobSeekerProfile = jobSeekerProfile;
    }

}
