package com.pronchenko.top.soilcare3.service;

import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.FertilizerType;
import com.pronchenko.top.soilcare3.entity.Season;
import com.pronchenko.top.soilcare3.repository.FertilizerRepository;
import com.pronchenko.top.soilcare3.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FertilizerService {
    private final FertilizerRepository fertilizerRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public FertilizerService(FertilizerRepository fertilizerRepository,
                             ReviewRepository reviewRepository) {
        this.fertilizerRepository = fertilizerRepository;
        this.reviewRepository = reviewRepository;
    }

    public Fertilizer saveFertilizer(Fertilizer fertilizer) {
        return fertilizerRepository.save(fertilizer);
    }

    public List<Fertilizer> getAllFertilizers() {
        return fertilizerRepository.findAll();
    }

    public Fertilizer getFertilizerById(Long id) {
        Fertilizer fertilizer = fertilizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Удобрение не найдено с id: " + id));


        updateFertilizerRating(id);

        return fertilizer;
    }

    public void updateFertilizerRating(Long fertilizerId) {
        Fertilizer fertilizer = fertilizerRepository.findById(fertilizerId)
                .orElseThrow(() -> new RuntimeException("Удобрение не найдено"));

        Double avgRating = reviewRepository.findAverageRatingByFertilizerId(fertilizerId);
        if (avgRating != null) {
            fertilizer.setFertilizerRating(avgRating);

            Long count = reviewRepository.countByFertilizerId(fertilizerId);
            fertilizer.setReviewCount(count != null ? count.intValue() : 0);

            fertilizerRepository.save(fertilizer);
        }
    }
    public List<Fertilizer> findTopFertilizers(int limit) {
        return fertilizerRepository.findTopByOrderByFertilizerRatingDesc(limit);
    }


    public List<Fertilizer> searchFertilizers(String searchTerm) {
        return fertilizerRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm);
    }

    public List<Fertilizer> filterFertilizers(FertilizerType type, Season season) {
        if (type != null && season != null) {
            return fertilizerRepository.findByFertilizerTypeAndSeason(type, season);
        } else if (type != null) {
            return fertilizerRepository.findByFertilizerType(type);
        } else if (season != null) {
            return fertilizerRepository.findBySeason(season);
        }
        return getAllFertilizers();
    }
    public List<Fertilizer> getFertilizersBySellerId(Long sellerId) {
        return fertilizerRepository.findBySellerId(sellerId);
    }

    public Long countFertilizersBySellerId(Long sellerId) {
        return fertilizerRepository.countBySellerId(sellerId);
    }


}