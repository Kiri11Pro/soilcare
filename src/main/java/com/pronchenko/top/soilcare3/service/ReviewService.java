package com.pronchenko.top.soilcare3.service;

import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.Review;
import com.pronchenko.top.soilcare3.repository.FertilizerRepository;
import com.pronchenko.top.soilcare3.repository.ReviewRepository;
import com.pronchenko.top.soilcare3.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final FertilizerRepository fertilizerRepository; // Используем репозиторий вместо сервиса
    private final SellerRepository sellerRepository; // Используем репозиторий вместо сервиса

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         FertilizerRepository fertilizerRepository,
                         SellerRepository sellerRepository) {
        this.reviewRepository = reviewRepository;
        this.fertilizerRepository = fertilizerRepository;
        this.sellerRepository = sellerRepository;
    }

    public Review saveReview(Review review, Long fertilizerId, Long sellerId) {
        if (fertilizerId != null) {
            review.setFertilizer(fertilizerRepository.findById(fertilizerId)
                    .orElseThrow(() -> new RuntimeException("Удобрение не найдено")));
        }
        if (sellerId != null) {
            review.setSeller(sellerRepository.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Продавец не найден")));
        }

        return reviewRepository.save(review);

    }
    public List<Review> findRandomReviews(int limit) {
        return reviewRepository.findRandomReviews(limit);
    }



    public List<Review> getReviewsByFertilizerId(Long fertilizerId) {
        return reviewRepository.findByFertilizerId(fertilizerId);
    }

    public List<Review> getReviewsBySellerId1(Long sellerId) {
        return reviewRepository.findBySellerId(sellerId);
    }

    public Double getAverageRatingForFertilizer(Long fertilizerId) {
        Double rating = reviewRepository.findAverageRatingByFertilizerId(fertilizerId);
        return rating != null ? Math.round(rating * 10.0) / 10.0 : 0.0;
    }

    public Double getAverageRatingForSeller(Long sellerId) {
        Double rating = reviewRepository.findAverageRatingBySellerId(sellerId);
        return rating != null ? Math.round(rating * 10.0) / 10.0 : 0.0;
    }



    public Integer getReviewCountForFertilizer(Long fertilizerId) {
        Long count = reviewRepository.countByFertilizerId(fertilizerId);
        return count != null ? count.intValue() : 0;
    }

    public Integer getReviewCountForSeller(Long sellerId) {
        Long count = reviewRepository.countBySellerId(sellerId);
        return count != null ? count.intValue() : 0;
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }


    public List<Review> getReviewsBySellerId(Long sellerId) {
        return reviewRepository.findBySellerIdOrFertilizerSellerId(sellerId, sellerId);
    }
}
