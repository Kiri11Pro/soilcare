package com.pronchenko.top.soilcare3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private LocalDateTime registrationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Seller seller;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SoilAnalyse> soilAnalyses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}
