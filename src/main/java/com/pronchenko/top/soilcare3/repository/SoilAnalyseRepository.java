package com.pronchenko.top.soilcare3.repository;
import com.pronchenko.top.soilcare3.entity.SoilAnalyse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;



@Repository
public interface SoilAnalyseRepository extends JpaRepository<SoilAnalyse,Long> {
    List<SoilAnalyse> findByUserId(Long userId);

}
