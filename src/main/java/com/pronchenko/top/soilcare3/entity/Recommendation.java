package com.pronchenko.top.soilcare3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String elementName;
    @Enumerated(EnumType.STRING)
    private ElementType elementType;
    private String reason;
    private double currentLevel;
    private String targetLevel;
    private Double deficitAmount;
    private String unit;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soil_analyse_id")
    private SoilAnalyse soilAnalyse;
    @ElementCollection
    private List<String> solutions = new ArrayList<>();
    @ElementCollection
    private List<String> applicationRates = new ArrayList<>();
    @ManyToMany
    private List<Fertilizer> recommendedFertilizers = new ArrayList<>();

    public void addSolution(String solution, String applicationRate) {
        this.solutions.add(solution);
        this.applicationRates.add(applicationRate);
    }

    public void addSolution(String solution) {
        this.solutions.add(solution);
        this.applicationRates.add("");
    }
}
