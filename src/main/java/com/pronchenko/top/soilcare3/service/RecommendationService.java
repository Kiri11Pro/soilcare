package com.pronchenko.top.soilcare3.service;

import com.pronchenko.top.soilcare3.entity.ElementType;
import com.pronchenko.top.soilcare3.entity.Priority;
import com.pronchenko.top.soilcare3.entity.Recommendation;
import com.pronchenko.top.soilcare3.entity.SoilAnalyse;
import com.pronchenko.top.soilcare3.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }


    public Recommendation getById(Long recommendationId) {
        return recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + recommendationId));
    }

}
