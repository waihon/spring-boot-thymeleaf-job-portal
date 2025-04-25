package com.waihon.springboot.thymeleaf.jobportal.entity;

import com.waihon.springboot.thymeleaf.jobportal.validation.AdvancedCheck;
import com.waihon.springboot.thymeleaf.jobportal.validation.BasicCheck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "job_locations")
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "City is required.", groups = BasicCheck.class)
    private String city;

    @NotBlank(message = "State is required.", groups = BasicCheck.class)
    private String state;

    @NotBlank(message = "Country is required.", groups = BasicCheck.class)
    private String country;

    public JobLocation() {
    }

    public JobLocation(Integer id, String city, String state, String country) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "JobLocation{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
