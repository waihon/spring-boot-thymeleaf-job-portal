package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.entity.UserType;
import com.waihon.springboot.thymeleaf.jobportal.service.UserService;
import com.waihon.springboot.thymeleaf.jobportal.service.UserTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserTypeService userTypeService;
    private final UserService userService;

    @Autowired
    public UserController(UserTypeService userTypeService, UserService userService) {
        this.userTypeService = userTypeService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        List<UserType> userTypes = userTypeService.getAll();

        model.addAttribute("userTypes", userTypes);
        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid User user) {
        userService.addNew(user);

        return "dashboard";
    }

}
