package com.pronchenko.top.soilcare3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "fertilizer")
public class Fertilizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private FertilizerType fertilizerType;
    private String description;
    private double nContent;
    private double pContent;
    private double kContent;
    private String applicationRate;
    @Enumerated(EnumType.STRING)
    private Season season;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    private String imageUrl;
    @ManyToOne
    private Seller seller;
    private double sellerRating;
    private double fertilizerRating = 0.0;
    private Integer reviewCount = 0;
    @OneToMany(mappedBy = "fertilizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
