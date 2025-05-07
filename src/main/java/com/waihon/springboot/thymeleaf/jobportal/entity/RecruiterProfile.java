package com.waihon.springboot.thymeleaf.jobportal.entity;

import com.waihon.springboot.thymeleaf.jobportal.validation.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "recruiter_profiles")
public class RecruiterProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    // RecruiterProfile.userAccountId is automatically mapped to User.userId
    @MapsId
    private User user;

    @NotBlank(groups = OnUpdate.class, message = "First Name is required")
    private String firstName;

    @NotBlank(groups = OnUpdate.class, message = "Last Name is required")
    private String lastName;

    private String city;
    private String state;
    private String country;
    private String company;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    public RecruiterProfile() {
    }

    public RecruiterProfile(User user) {
        this.user = user;
    }

    public RecruiterProfile(int userAccountId, User user, String firstName, String lastName,
                            String city, String state, String country, String company, String profilePhoto) {
        this.userAccountId = userAccountId;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.company = company;
        this.profilePhoto = profilePhoto;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Transient
    public String getPhotoImagePath() {
        if (!StringUtils.hasText(profilePhoto) || userAccountId == null) return null;
        return "/photos/recruiter/" + userAccountId + "/" + profilePhoto;
    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "userAccountId=" + userAccountId +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }

}
