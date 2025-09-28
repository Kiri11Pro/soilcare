package com.pronchenko.top.soilcare3.service;

import com.pronchenko.top.soilcare3.entity.Role;
import com.pronchenko.top.soilcare3.entity.Seller;
import com.pronchenko.top.soilcare3.entity.User;
import com.pronchenko.top.soilcare3.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SellerService sellerService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       SellerService sellerService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sellerService = sellerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User saveUser(User user) {
        // Базовая валидация
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Логин не может быть пустым");
        }
        if (user.getPassword() == null || user.getPassword().length() < 3) {
            throw new RuntimeException("Пароль должен содержать минимум 3 символа");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new RuntimeException("Некорректный email");
        }

        User savedUser = userRepository.save(user);


        if (savedUser.getRole() == Role.SELLER) {
            createSellerForUser(savedUser);
        }

        return savedUser;
    }

    private void createSellerForUser(User user) {

        if (!sellerService.sellerExistsForUser(user.getId())) {
            Seller seller = Seller.builder()
                    .user(user)
                    .companyName(user.getFirstName() + " " + user.getLastName() + " Store")
                    .email(user.getEmail())
                    .phone("Не указан")
                    .city("Не указан")
                    .contactPerson(user.getFirstName() + " " + user.getLastName())
                    .role(Role.SELLER)
                    .description("Новый продавец")
                    .rating(0.0)
                    .reviewCount(0)
                    .yearsOnMarket(0)
                    .build();

            sellerService.saveSeller(seller);
            System.out.println("Создан новый продавец для пользователя: " + user.getUsername());
        }
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @PostConstruct
//    public void initDemoUsers() {
//        try {
//
//            if (!userExists("user")) {
//                User user = User.builder()
//                        .username("user")
//                        .password(passwordEncoder.encode("password"))
//                        .email("user@example.com")
//                        .firstName("Обычный")
//                        .lastName("Пользователь")
//                        .role(Role.USER)
//                        .build();
//                userRepository.save(user);
//            }
//
//
//            if (!userExists("seller")) {
//                User seller = User.builder()
//                        .username("seller")
//                        .password(passwordEncoder.encode("password"))
//                        .email("seller@example.com")
//                        .firstName("Иван")
//                        .lastName("Продавец")
//                        .role(Role.SELLER)
//                        .build();
//                userRepository.save(seller);
//            }
//
//
//            if (!userExists("admin")) {
//                User admin = User.builder()
//                        .username("admin")
//                        .password(passwordEncoder.encode("password"))
//                        .email("admin@example.com")
//                        .firstName("Администратор")
//                        .lastName("Системы")
//                        .role(Role.ADMIN)
//                        .build();
//                userRepository.save(admin);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Ошибка при создании демо-пользователей: " + e.getMessage());
//        }
//    }
}
