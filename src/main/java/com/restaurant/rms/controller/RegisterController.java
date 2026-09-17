package com.restaurant.rms.controller;

import com.restaurant.rms.entity.User;
import com.restaurant.rms.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "That username is already taken. Please choose another.");
            model.addAttribute("user", user);
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER"); // customers can only self-register as CUSTOMER, never ADMIN
        userService.save(user);

        return "redirect:/login?registered=true";
    }

}
