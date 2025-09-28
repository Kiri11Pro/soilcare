package com.pronchenko.top.soilcare3.service;


import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.Seller;
import com.pronchenko.top.soilcare3.entity.User;

import com.pronchenko.top.soilcare3.repository.ReviewRepository;
import com.pronchenko.top.soilcare3.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository, ReviewRepository reviewRepository) {
        this.sellerRepository = sellerRepository;
        this.reviewRepository = reviewRepository;
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public boolean sellerExistsForUser(Long userId) {
        return sellerRepository.existsByUserId(userId);
    }

    public Seller getSellerByUser(User user) {
        return sellerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Продавец не найден"));
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продавец не найден"));
    }
    public List<Seller> searchSellers(String searchTerm) {
        return sellerRepository.findByCompanyNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller updateSellerForUser(User user, Seller sellerDetails) {
        Seller seller = getSellerByUser(user);

        seller.setCompanyName(sellerDetails.getCompanyName());
        seller.setContactPerson(sellerDetails.getContactPerson());
        seller.setEmail(sellerDetails.getEmail());
        seller.setPhone(sellerDetails.getPhone());
        seller.setAddress(sellerDetails.getAddress());
        seller.setCity(sellerDetails.getCity());
        seller.setWebsite(sellerDetails.getWebsite());
        seller.setDescription(sellerDetails.getDescription());
        seller.setYearsOnMarket(sellerDetails.getYearsOnMarket());
        seller.setLogoUrl(sellerDetails.getLogoUrl());

        return sellerRepository.save(seller);
    }
    public void updateSellerRating(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(
                ()->new RuntimeException("Продавец не найден"));
        Double avgRating = reviewRepository.findAverageRatingBySellerId(sellerId);
        if (avgRating != null) {
            seller.setRating(avgRating);
            Long count = reviewRepository.countBySellerId(sellerId);
            seller.setReviewCount(count!=null?count.intValue():0);
            sellerRepository.save(seller);
        }
    }
}



