package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.Seller;
import com.pronchenko.top.soilcare3.service.FertilizerService;
import com.pronchenko.top.soilcare3.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final FertilizerService fertilizerService;
    private final SellerService sellerService;

    @Autowired
    public SearchController(FertilizerService fertilizerService, SellerService sellerService) {
        this.fertilizerService = fertilizerService;
        this.sellerService = sellerService;
    }

    @GetMapping
    public String search(@RequestParam String q, Model model) {
        List<Fertilizer> fertilizers = fertilizerService.searchFertilizers(q);
        List<Seller> sellers = sellerService.searchSellers(q);

        model.addAttribute("query", q);
        model.addAttribute("fertilizers", fertilizers);
        model.addAttribute("sellers", sellers);
        model.addAttribute("totalResults", fertilizers.size() + sellers.size());

        return "search/results";
    }
}
