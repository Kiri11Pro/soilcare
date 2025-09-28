package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.dto.LoginRequest;
import com.pronchenko.top.soilcare3.entity.Role;

import com.pronchenko.top.soilcare3.entity.User;
import com.pronchenko.top.soilcare3.service.AuthService;
import com.pronchenko.top.soilcare3.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                        HttpSession session,
                        Model model) {
        try {
            User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            session.setAttribute("user", user);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("loginRequest", new LoginRequest());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam String confirmPassword,
                           HttpSession session,
                           Model model) {
        try {
            User registeredUser = authService.register(user, confirmPassword);
            session.setAttribute("user", registeredUser);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "auth/register";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }


}