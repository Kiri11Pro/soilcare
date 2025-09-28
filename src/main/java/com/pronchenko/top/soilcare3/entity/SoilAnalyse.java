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
@Table(name = "soil_analyse")
public class SoilAnalyse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Double ph;
    private Double nitrogen;
    private Double phosphorus;
    private Double potassium;
    private Double organicMatter;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "soilAnalyse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recommendation> recommendations = new ArrayList<>();
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
