package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.Review;
import com.pronchenko.top.soilcare3.entity.SoilAnalyse;
import com.pronchenko.top.soilcare3.entity.User;
import com.pronchenko.top.soilcare3.service.ReviewService;
import com.pronchenko.top.soilcare3.service.SoilAnalyseService;
import com.pronchenko.top.soilcare3.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;
    private final SoilAnalyseService soilAnalyseService;
    private final ReviewService reviewService;

    @Autowired
    public UserController(UserService userService,
                          SoilAnalyseService soilAnalyseService,
                          ReviewService reviewService) {
        this.userService = userService;
        this.soilAnalyseService = soilAnalyseService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String userProfile(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<SoilAnalyse> allAnalyses = soilAnalyseService.getAnalysesByUserId(currentUser.getId());
        List<Review> allReviews = reviewService.getReviewsByUserId(currentUser.getId());


        List<SoilAnalyse> recentAnalyses = allAnalyses.stream()
                .limit(3)
                .collect(Collectors.toList());


        List<Review> recentReviews = allReviews.stream()
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("user", currentUser);
        model.addAttribute("analyses", allAnalyses);
        model.addAttribute("reviews", allReviews);
        model.addAttribute("recentAnalyses", recentAnalyses);
        model.addAttribute("recentReviews", recentReviews);

        return "user/profile";
    }

    @GetMapping("/edit")
    public String editProfileForm(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", currentUser);
        return "user/edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute User userDetails,
                                HttpSession session,
                                Model model) {
        try {
            User currentUser = getCurrentUser(session);
            if (currentUser == null) {
                return "redirect:/auth/login";
            }


            currentUser.setFirstName(userDetails.getFirstName());
            currentUser.setLastName(userDetails.getLastName());
            currentUser.setEmail(userDetails.getEmail());
            currentUser.setPhone(userDetails.getPhone());
            currentUser.setAddress(userDetails.getAddress());
            currentUser.setCity(userDetails.getCity());
            return "redirect:/profile?updated=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении профиля: " + e.getMessage());
            return "user/edit-profile";
        }
    }

    @GetMapping("/analyses")
    public String userAnalyses(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<SoilAnalyse> allAnalyses = soilAnalyseService.getAnalysesByUserId(currentUser.getId());
        List<Review> allReviews = reviewService.getReviewsByUserId(currentUser.getId());

        model.addAttribute("user", currentUser);
        model.addAttribute("analyses", allAnalyses);
        model.addAttribute("reviews", allReviews);
        return "user/my-analysis";
    }

    @GetMapping("/reviews")
    public String userReviews(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<SoilAnalyse> allAnalyses = soilAnalyseService.getAnalysesByUserId(currentUser.getId());
        List<Review> allReviews = reviewService.getReviewsByUserId(currentUser.getId());

        model.addAttribute("user", currentUser);
        model.addAttribute("analyses", allAnalyses);
        model.addAttribute("reviews", allReviews);

        return "user/reviews";
    }
    @GetMapping("/favorites")
    public String userFavorites(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<SoilAnalyse> allAnalyses = soilAnalyseService.getAnalysesByUserId(currentUser.getId());
        List<Review> allReviews = reviewService.getReviewsByUserId(currentUser.getId());

        model.addAttribute("user", currentUser);
        model.addAttribute("analyses", allAnalyses);
        model.addAttribute("reviews", allReviews);

        return "user/favorites";
    }

    @GetMapping("/settings")
    public String userSettings(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<SoilAnalyse> allAnalyses = soilAnalyseService.getAnalysesByUserId(currentUser.getId());
        List<Review> allReviews = reviewService.getReviewsByUserId(currentUser.getId());

        model.addAttribute("user", currentUser);
        model.addAttribute("analyses", allAnalyses);
        model.addAttribute("reviews", allReviews);

        return "user/settings";
    }

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
