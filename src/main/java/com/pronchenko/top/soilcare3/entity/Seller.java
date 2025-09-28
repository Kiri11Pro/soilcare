package com.pronchenko.top.soilcare3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String city;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String description;
    private double rating;
    private Integer reviewCount = 0;
    private Integer yearsOnMarket;
    private String logoUrl;
    private String website;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Fertilizer> fertilizers = new ArrayList<>();
    private LocalDateTime registrationDate;
    private Boolean isActive = true;
    private String contactPerson;
    private String inn;
    private String bankAccount;
    @PrePersist
    protected void onCreate() {
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
    }
}
