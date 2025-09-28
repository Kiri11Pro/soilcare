package com.pronchenko.top.soilcare3.repository;

import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.FertilizerType;
import com.pronchenko.top.soilcare3.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Long> {
    List<Fertilizer> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    List<Fertilizer> findByFertilizerType(FertilizerType type);
    List<Fertilizer> findBySeason(Season season);
    List<Fertilizer> findByFertilizerTypeAndSeason(FertilizerType type, Season season);
    List<Fertilizer> findBySellerId(Long sellerId);
    Long countBySellerId(Long sellerId);
    @Query("SELECT f FROM Fertilizer f WHERE f.fertilizerRating > 0 ORDER BY f.fertilizerRating DESC")
    List<Fertilizer> findTopByOrderByFertilizerRatingDesc(@Param("limit") int limit);

}
