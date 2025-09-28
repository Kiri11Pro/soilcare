package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.Review;
import com.pronchenko.top.soilcare3.entity.SoilAnalyse;
import com.pronchenko.top.soilcare3.service.FertilizerService;
import com.pronchenko.top.soilcare3.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private FertilizerService fertilizerService;
    @Autowired
    private ReviewService reviewService;
    @GetMapping({"/", "/index", "/home","/soil-analysis"})
    public String home(Model model) {
        model.addAttribute("soilAnalysis", new SoilAnalyse());
        model.addAttribute("pageTitle", "SoilCare - Анализ почвы");
        List<Fertilizer> popularFertilizers = fertilizerService.findTopFertilizers(4);
        model.addAttribute("popularFertilizers", popularFertilizers);
        List<Review> randomReviews = reviewService.findRandomReviews(3);
        model.addAttribute("randomReviews", randomReviews);
        return "index";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }
    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }
    @GetMapping("/knowledge-base")
    public String knowledgeBase() {
        return "knowledge-base";
    }
    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

}
