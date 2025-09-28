package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.*;
import com.pronchenko.top.soilcare3.service.FertilizerService;
import com.pronchenko.top.soilcare3.service.ReviewService;
import com.pronchenko.top.soilcare3.service.SellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/seller-dashboard")
public class SellerDashboardController {
    private final SellerService sellerService;
    private final FertilizerService fertilizerService;
    private final ReviewService reviewService;

    @Autowired
    public SellerDashboardController(SellerService sellerService,
                                     FertilizerService fertilizerService,
                                     ReviewService reviewService) {
        this.sellerService = sellerService;
        this.fertilizerService = fertilizerService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String sellerDashboard(Model model, HttpSession session,
                                  @RequestParam(required = false) Boolean updated) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/auth/login";
        }

        Seller seller = sellerService.getSellerByUser(currentUser);
        List<Fertilizer> sellerFertilizers = fertilizerService.getFertilizersBySellerId(seller.getId());
        List<Review> sellerReviews = reviewService.getReviewsBySellerId(seller.getId());

        model.addAttribute("seller", seller);
        model.addAttribute("fertilizers", sellerFertilizers);
        model.addAttribute("reviews", sellerReviews);

        if (Boolean.TRUE.equals(updated)) {
            model.addAttribute("success", "Профиль успешно обновлен");
        }

        return "seller/dashboard";
    }
    @GetMapping("/complete")
    public String completeRegistration() {
        return "seller/complete-registration";
    }


    @GetMapping("/edit")
    public String editSellerProfileForm(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/auth/login";
        }

        Seller seller = sellerService.getSellerByUser(currentUser);
        model.addAttribute("seller", seller);
        return "seller/edit-profile";
    }


    @PostMapping("/edit")
    public String updateSellerProfile(@ModelAttribute Seller sellerDetails,
                                      HttpSession session,
                                      Model model) {
        try {
            User currentUser = getCurrentUser(session);
            if (currentUser == null || currentUser.getRole() != Role.SELLER) {
                return "redirect:/auth/login";
            }

            Seller updatedSeller = sellerService.updateSellerForUser(currentUser, sellerDetails);
            return "redirect:/seller-dashboard?updated=true";

        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении профиля: " + e.getMessage());
            return "seller/edit-profile";
        }
    }


    @GetMapping("/fertilizers")
    public String sellerFertilizers(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/auth/login";
        }

        Seller seller = sellerService.getSellerByUser(currentUser);
        List<Fertilizer> fertilizers = fertilizerService.getFertilizersBySellerId(seller.getId());

        model.addAttribute("fertilizers", fertilizers);
        return "seller/my-fertilizers";
    }


    @GetMapping("/reviews")
    public String sellerReviews(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/auth/login";
        }

        Seller seller = sellerService.getSellerByUser(currentUser);
        List<Review> reviews = reviewService.getReviewsBySellerId(seller.getId());

        // Разделяем отзывы на продавца и на товары
        long sellerReviewsCount = reviews.stream()
                .filter(review -> review.getSeller() != null && review.getFertilizer() == null)
                .count();

        long productReviewsCount = reviews.stream()
                .filter(review -> review.getFertilizer() != null)
                .count();

        model.addAttribute("reviews", reviews);
        model.addAttribute("seller", seller);
        model.addAttribute("sellerReviewsCount", sellerReviewsCount);
        model.addAttribute("productReviewsCount", productReviewsCount);

        return "seller/my-reviews";
    }
    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser(session);
            if (currentUser == null || currentUser.getRole() != Role.SELLER) {
                return "redirect:/auth/login";
            }

            // Проверяем, что отзыв принадлежит текущему продавцу
            Seller seller = sellerService.getSellerByUser(currentUser);
            Review review = reviewService.getReviewById(id);

            if (((review.getSeller() != null) && (!Objects.equals(review.getSeller().getId(), seller.getId())) &&
                    review.getFertilizer() != null && !Objects.equals(review.getFertilizer().getSeller().getId(), seller.getId()))) {
                redirectAttributes.addFlashAttribute("error", "У вас нет прав для удаления этого отзыва");
                return "redirect:/seller-dashboard/reviews";
            }

            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Отзыв успешно удален");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении отзыва: " + e.getMessage());
        }

        return "redirect:/seller-dashboard/reviews";
    }

    @GetMapping("/statistics")
    public String sellerStatistics(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/auth/login";
        }

        Seller seller = sellerService.getSellerByUser(currentUser);
        List<Fertilizer> fertilizers = fertilizerService.getFertilizersBySellerId(seller.getId());
        List<Review> reviews = reviewService.getReviewsBySellerId(seller.getId());

        long totalFertilizers = fertilizers.size();
        double averageRating = seller.getRating();
        int totalReviews = reviews.size();

        model.addAttribute("totalFertilizers", totalFertilizers);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("seller", seller);

        return "seller/statistics";
    }
    @GetMapping("/fertilizers/add")
    public String showAddForm(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/auth/login";
        }
        Seller seller = sellerService.getSellerByUser(currentUser);


        model.addAttribute("fertilizer", new Fertilizer());
        model.addAttribute("seller", seller);


        return "fertilizer/add";
    }

    @PostMapping("/fertilizers/add")
    public String addFertilizer(@ModelAttribute Fertilizer fertilizer,
                                HttpSession session,
                                Model model) {
        try {
            User currentUser = getCurrentUser(session);
            if (currentUser == null || currentUser.getRole() != Role.SELLER) {
                return "redirect:/auth/login";
            }

            Seller seller = sellerService.getSellerByUser(currentUser);


            fertilizer.setSeller(seller);
            fertilizer.setFertilizerRating(0.0);
            fertilizer.setReviewCount(0);

            Fertilizer savedFertilizer = fertilizerService.saveFertilizer(fertilizer);
            return "redirect:/seller-dashboard/fertilizers?success=success";

        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении товара: " + e.getMessage());
            User currentUser = getCurrentUser(session);
            Seller seller = sellerService.getSellerByUser(currentUser);
            model.addAttribute("seller", seller);
            return "fertilizer/add";
        }
    }


    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}