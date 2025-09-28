package com.pronchenko.top.soilcare3.service;

import com.pronchenko.top.soilcare3.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    public User authenticate(String username, String password) {
        try {
            User user = userService.findByUsername(username);


             if (!passwordEncoder.matches(password, user.getPassword())) {
                 throw new RuntimeException("Неверный пароль");
             }

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Неверные учетные данные");
        }
    }

    public User register(User user, String confirmPassword) {
        try {

            if (!user.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Пароли не совпадают");
            }


            if (userService.userExists(user.getUsername())) {
                throw new RuntimeException("Пользователь с таким логином уже существует");
            }


            if (userService.emailExists(user.getEmail())) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }


            user.setPassword(passwordEncoder.encode(user.getPassword()));


            return userService.saveUser(user);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при регистрации: " + e.getMessage());
        }
    }
}