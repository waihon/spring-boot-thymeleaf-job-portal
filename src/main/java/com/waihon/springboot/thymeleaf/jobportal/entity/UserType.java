package com.waihon.springboot.thymeleaf.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_types")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userTypeId;

    private String userTypeName;

    @OneToMany(targetEntity = User.class, mappedBy = "userTypeId", cascade = CascadeType.ALL)
    private List<User> users;

    public UserType() {
    }

    public UserType(int userTypeId, String userTypeName, List<User> users) {
        this.userTypeId = userTypeId;
        this.userTypeName = userTypeName;
        this.users = users;
    }
}
