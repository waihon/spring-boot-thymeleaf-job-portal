package com.waihon.springboot.thymeleaf.jobportal.controller;

import com.waihon.springboot.thymeleaf.jobportal.entity.User;
import com.waihon.springboot.thymeleaf.jobportal.entity.UserType;
import com.waihon.springboot.thymeleaf.jobportal.service.UserService;
import com.waihon.springboot.thymeleaf.jobportal.service.UserTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Security;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserTypeService userTypeService;
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
    public String userRegistration(@Valid User user, Model model, HttpServletRequest request) {
        Optional<User> optionalUser = userService.getUserByEmail(user.getEmail());

        if (optionalUser.isPresent()) {
            model.addAttribute("error",
                    "Email already registered, try to log in or register with other email.");
            List<UserType> userTypes = userTypeService.getAll();
            model.addAttribute("userTypes", userTypes);
            // Use passed-in `user` instead of a new `user` to retain previous input.
            model.addAttribute("user", user);

            return "register";
        }

        String username = user.getEmail();
        // For creating an authentication token, a clear password prior to persisting the user is required.
        // After persisting the user, the password field contains encoded password.
        // Therefore, storing a clear password in a variable for usage later on.
        String password = user.getPassword();
        userService.addNew(user);

        // Auto log in a newly registered user.
        loginRegisteredUser(request, username, password);

        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    private void loginRegisteredUser(HttpServletRequest request, String username, String password) {
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(username, password);
        token.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
