package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.Review;
import com.pronchenko.top.soilcare3.entity.User;
import com.pronchenko.top.soilcare3.service.FertilizerService;
import com.pronchenko.top.soilcare3.service.ReviewService;
import com.pronchenko.top.soilcare3.service.SellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final FertilizerService fertilizerService;
    private final SellerService sellerService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            FertilizerService fertilizerService,
                            SellerService sellerService) {
        this.reviewService = reviewService;
        this.fertilizerService = fertilizerService;
        this.sellerService = sellerService;
    }


    @GetMapping("/fertilizer/{fertilizerId}/add")
    public String showAddFertilizerReviewForm(@PathVariable Long fertilizerId, Model model,
                                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("fertilizerId", fertilizerId);
        model.addAttribute("review", new Review());
        return "reviews/add-fertilizer-review";
    }


    @GetMapping("/seller/{sellerId}/add")
    public String showAddSellerReviewForm(@PathVariable Long sellerId, Model model,
                                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("sellerId", sellerId);
        model.addAttribute("review", new Review());
        return "reviews/add-seller-review";
    }

    @PostMapping("/fertilizer/save")
    public String saveFertilizerReview(@ModelAttribute Review review,
                                       @RequestParam Long fertilizerId,
                                       @RequestParam Integer rating,
                                       HttpSession session,
                                       Model model) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/auth/login";
            }
            review.setUser(currentUser);
            review.setRating(rating);
            reviewService.saveReview(review, fertilizerId, null);
            fertilizerService.updateFertilizerRating(fertilizerId);
            return "redirect:/fertilizers/" + fertilizerId;
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при сохранении отзыва: " + e.getMessage());
            model.addAttribute("fertilizerId", fertilizerId);
            return "reviews/add-fertilizer-review";
        }
    }

    @PostMapping("/seller/save")
    public String saveSellerReview(@ModelAttribute Review review,
                                   @RequestParam Long sellerId,
                                   @RequestParam Integer rating,
                                   HttpSession session,
                                   Model model) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/auth/login";
            }
            review.setUser(currentUser);
            review.setRating(rating);
            reviewService.saveReview(review, null, sellerId);
            sellerService.updateSellerRating(sellerId);
            return "redirect:/sellers/" + sellerId;
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при сохранении отзыва: " + e.getMessage());
            model.addAttribute("sellerId", sellerId);
            return "reviews/add-seller-review";
        }
    }


    @GetMapping("/fertilizer/{fertilizerId}")
    public String viewFertilizerReviews(@PathVariable Long fertilizerId, Model model) {
        model.addAttribute("reviews", reviewService.getReviewsByFertilizerId(fertilizerId));
        model.addAttribute("averageRating", reviewService.getAverageRatingForFertilizer(fertilizerId));
        model.addAttribute("reviewCount", reviewService.getReviewCountForFertilizer(fertilizerId));
        model.addAttribute("fertilizerId", fertilizerId);
        return "reviews/fertilizer-reviews";
    }


    @GetMapping("/seller/{sellerId}")
    public String viewSellerReviews(@PathVariable Long sellerId, Model model) {
        List<Review> reviews = reviewService.getReviewsBySellerId(sellerId);
        Double averageRating = reviewService.getAverageRatingForSeller(sellerId);
        Integer reviewCount = reviewService.getReviewCountForSeller(sellerId);
        Map<Integer, Integer> ratingDistribution = calculateRatingDistribution(reviews);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("sellerId", sellerId);
        model.addAttribute("ratingDistribution", ratingDistribution);
        return "reviews/seller-reviews";
    }

    private Map<Integer, Integer> calculateRatingDistribution(List<Review> reviews) {
        Map<Integer, Integer> distribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, 0);
        }
        for (Review review : reviews) {
            int rating = review.getRating();
            distribution.put(rating, distribution.get(rating) + 1);
        }
        if (!reviews.isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                int count = distribution.get(i);
                int percentage = (int) Math.round((count * 100.0) / reviews.size());
                distribution.put(i, percentage);
            }
        }
        return distribution;
    }
}
