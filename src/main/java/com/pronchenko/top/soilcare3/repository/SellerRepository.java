package com.pronchenko.top.soilcare3.repository;

import com.pronchenko.top.soilcare3.entity.Seller;
import com.pronchenko.top.soilcare3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUser(User user);


    boolean existsByUserId(Long userId);


    @Query("SELECT COUNT(s) > 0 FROM Seller s WHERE s.user.id = :userId")
    boolean existsByUser_Id(@Param("userId") Long userId);

    boolean existsByEmail(String email);

    Optional<Seller> findByEmail(String email);

    List<Seller> findByCompanyNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String companyName, String description);
}
