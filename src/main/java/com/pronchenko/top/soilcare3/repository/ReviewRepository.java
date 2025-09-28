package com.pronchenko.top.soilcare3.repository;

import com.pronchenko.top.soilcare3.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFertilizerId(Long fertilizerId);
    List<Review> findBySellerId(Long sellerId);
    List<Review> findByUserId(Long userId);

        Page<Review> findByFertilizerIdOrderByCreatedAtDesc(Long fertilizerId, Pageable pageable);

    // Случайная выборка для PostgreSQL (все отзывы)
    @Query(value = "SELECT * FROM reviews ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Review> findRandomReviews(@Param("limit") int limit);

    // Если хотите только отзывы с определенным рейтингом (например, 4+)
    @Query(value = "SELECT * FROM reviews WHERE rating >= 4 ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Review> findRandomTopReviews(@Param("limit") int limit);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.fertilizer.id = :fertilizerId")
    Double findAverageRatingByFertilizerId(@Param("fertilizerId") Long fertilizerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.seller.id = :sellerId")
    Double findAverageRatingBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.fertilizer.id = :fertilizerId")
    Long countByFertilizerId(@Param("fertilizerId") Long fertilizerId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.seller.id = :sellerId")
    Long countBySellerId(@Param("sellerId") Long sellerId);// Находит отзывы на продавца И на товары этого продавца
    @Query("SELECT r FROM Review r WHERE r.seller.id = :sellerId OR (r.fertilizer IS NOT NULL AND r.fertilizer.seller.id = :sellerId)")
    List<Review> findBySellerIdOrFertilizerSellerId(@Param("sellerId") Long sellerId, @Param("sellerId") Long sellerId2);

    // Отзывы только на продавца
    List<Review> findBySellerIdAndFertilizerIsNull(Long sellerId);

    // Отзывы только на товары продавца
    List<Review> findByFertilizerSellerId(Long sellerId);


}
