package com.pronchenko.top.soilcare3.repository;

import com.pronchenko.top.soilcare3.entity.ElementType;
import com.pronchenko.top.soilcare3.entity.Priority;
import com.pronchenko.top.soilcare3.entity.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findBySoilAnalyseId(Long soilAnalyseId);

    @Query("SELECT r FROM Recommendation r WHERE r.soilAnalyse.user.id = :userId ORDER BY r.priority DESC, r.soilAnalyse.createdAt DESC")
    List<Recommendation> findLatestByUserId(@Param("userId") Long userId);
}

