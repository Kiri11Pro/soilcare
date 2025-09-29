package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.*;
import com.pronchenko.top.soilcare3.service.RecommendationService;
import com.pronchenko.top.soilcare3.service.SoilAnalyseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/soilAnalysis")
public class SoilAnalyseController {
    private final SoilAnalyseService soilAnalyseService;
    private final RecommendationService recommendationService;

    @Autowired
    public SoilAnalyseController(SoilAnalyseService soilAnalyseService,
                                 RecommendationService recommendationService) {
        this.soilAnalyseService = soilAnalyseService;
        this.recommendationService= recommendationService;
    }

    @GetMapping("/{id}")
    public String showAnalysis(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        SoilAnalyse soilAnalysis = soilAnalyseService.getById(id);
        model.addAttribute("soilAnalysis", soilAnalysis);
        return "soilAnalyse/details";
    }

    @PostMapping("/save")
    public String saveAnalyse(@ModelAttribute("soilAnalysis") SoilAnalyse soilAnalyse,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        soilAnalyse.setUser(currentUser);
        SoilAnalyse savedAnalyse = soilAnalyseService.saveWithRecommendations(soilAnalyse);
        redirectAttributes.addAttribute("id", savedAnalyse.getId());
        return "redirect:{id}";
    }

    @GetMapping("/recommendations/{id}")
    public String showRecommendations(@PathVariable Long id, Model model,
                                      HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        SoilAnalyse soilAnalysis = soilAnalyseService.getByIdWithRecommendations(id);
        model.addAttribute("soilAnalysis", soilAnalysis);
        model.addAttribute("soilRecommendations", soilAnalysis.getRecommendations());
        List<Fertilizer> recommendedFertilizers = soilAnalysis.getRecommendations().stream()
                .flatMap(rec -> rec.getRecommendedFertilizers().stream()).distinct()
                .collect(Collectors.toList());
        model.addAttribute("recommendedFertilizers", recommendedFertilizers);

        return "soilAnalyse/recommendations";
    }




}

