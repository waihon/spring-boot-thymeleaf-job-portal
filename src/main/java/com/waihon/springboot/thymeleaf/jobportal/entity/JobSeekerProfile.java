package com.waihon.springboot.thymeleaf.jobportal.entity;

import com.waihon.springboot.thymeleaf.jobportal.validation.AtLeastOneSkill;
import com.waihon.springboot.thymeleaf.jobportal.validation.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

import java.util.List;

@Entity
@Table(name = "job_seeker_profiles")
@AtLeastOneSkill(groups = OnUpdate.class)
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private User user;

    @NotBlank(groups = OnUpdate.class, message = "First Name is required.")
    private String firstName;

    @NotBlank(groups = OnUpdate.class, message = "Last Name is required.")
    private String lastName;

    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;

    @NotBlank(groups = OnUpdate.class, message = "Resume is required.")
    private String resume;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    // targetEntity is not required as Java already infers Skill from List<Skill>.
    // mappedBy refers to the actual field name in Skill, not the column name.
    // orphanRemoval = true may be added to remove Skill records automatically
    // if they are removed from the list in JobSeekerProfile.
    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    public JobSeekerProfile() {
    }

    public JobSeekerProfile(User user) {
        this.user = user;
    }

    public JobSeekerProfile(Integer userAccountId, User user, String firstName, String lastName,
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

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Transient
    public String getPhotoImagePath() {
        if (!StringUtils.hasText(profilePhoto) || userAccountId == null) return null;

        return "/photos/candidate/" + userAccountId + "/" + profilePhoto;
    }

    @Override
    public String toString() {
        return "JobSeekerProfile{" +
                "userAccountId=" + userAccountId +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", workAuthorization='" + workAuthorization + '\'' +
                ", employmentType='" + employmentType + '\'' +
                ", resume='" + resume + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}
