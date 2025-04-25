package com.waihon.springboot.thymeleaf.jobportal.entity;

import com.waihon.springboot.thymeleaf.jobportal.validation.AdvancedCheck;
import com.waihon.springboot.thymeleaf.jobportal.validation.BasicCheck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "job_companies")
public class JobCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Company Name is required.", groups = BasicCheck.class)
    @Length(min = 10, message = "Company Name is too short (minimum 10 characters)", groups = AdvancedCheck.class)
    private String name;

    private String logo;

    public JobCompany() {
    }

    public JobCompany(Integer id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "JobCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }

}
