package com.waihon.springboot.thymeleaf.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "job_seeker_profiles")
public class JobSeekerProfile {

    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private User user;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;
    private String resume;
    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @OneToMany(targetEntity = Skill.class, mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL)
    private List<Skill> skills;

    public JobSeekerProfile() {
    }

    public JobSeekerProfile(User user) {
        this.user = user;
    }

    public JobSeekerProfile(int userAccountId, User user, String firstName, String lastName,
                            String city, String state, String country, String workAuthorization,
                            String employmentType, String resume, String profilePhoto, List<Skill> skills) {
        this.userAccountId = userAccountId;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.workAuthorization = workAuthorization;
        this.employmentType = employmentType;
        this.resume = resume;
        this.profilePhoto = profilePhoto;
        this.skills = skills;
    }

}
