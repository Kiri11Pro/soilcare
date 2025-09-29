package com.pronchenko.top.soilcare3.controller;

import com.pronchenko.top.soilcare3.entity.Fertilizer;
import com.pronchenko.top.soilcare3.entity.FertilizerType;
import com.pronchenko.top.soilcare3.entity.Season;
import com.pronchenko.top.soilcare3.entity.Seller;
import com.pronchenko.top.soilcare3.service.FertilizerService;
import com.pronchenko.top.soilcare3.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fertilizers")
public class FertilizerController {
    private final FertilizerService fertilizerService;
    private final SellerService sellerService;

    @Autowired
    public FertilizerController(FertilizerService fertilizerService,
                                SellerService sellerService) {
        this.fertilizerService = fertilizerService;
        this.sellerService = sellerService;
    }


    @GetMapping("/catalog")
    public String showCatalog(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "15") int size, Model model,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) FertilizerType type,
                              @RequestParam(required = false) Season season) {

        List<Fertilizer> allFertilizers;

        if (search != null && !search.isEmpty()) {
            allFertilizers = fertilizerService.searchFertilizers(search);
        } else if (type != null || season != null) {
            allFertilizers = fertilizerService.filterFertilizers(type, season);
        } else {
            allFertilizers = fertilizerService.getAllFertilizers();
        }
        int start = page * size;
        int end = Math.min(start + size, allFertilizers.size());
        List<Fertilizer> fertilizers = allFertilizers.subList(start, end);

        model.addAttribute("fertilizers", fertilizers);
        model.addAttribute("fertilizerTypes", FertilizerType.values());
        model.addAttribute("seasons", Season.values());
        model.addAttribute("searchTerm", search);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedSeason", season);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) allFertilizers.size() / size));
        model.addAttribute("totalFertilizers", allFertilizers.size());
        return "fertilizer/catalog";
    }

    @GetMapping("/{id}")
    public String showFertilizerDetails(@PathVariable Long id, Model model) {
        Fertilizer fertilizer = fertilizerService.getFertilizerById(id);
        model.addAttribute("fertilizer", fertilizer);
        return "fertilizer/details";
    }

    @GetMapping("/add")
    public String showAddFertilizerForm(Model model) {
        model.addAttribute("fertilizer", new Fertilizer());
        model.addAttribute("allSellers", sellerService.getAllSellers());
        model.addAttribute("fertilizerTypes", FertilizerType.values());
        model.addAttribute("seasons", Season.values());
        return "fertilizer/add";
    }

    @PostMapping("/save")
    public String saveFertilizer(@ModelAttribute Fertilizer fertilizer,
                                 @RequestParam Long sellerId,
                                 Model model) {
        try {
            Seller seller = sellerService.getSellerById(sellerId);
            fertilizer.setSeller(seller);
            fertilizer.setSellerRating(seller.getRating());

            fertilizerService.saveFertilizer(fertilizer);
            return "redirect:/fertilizers/catalog";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при сохранении: " + e.getMessage());
            model.addAttribute("allSellers", sellerService.getAllSellers());
            model.addAttribute("fertilizerTypes", FertilizerType.values());
            model.addAttribute("seasons", Season.values());
            return "fertilizer/add";
        }
    }


    @GetMapping("/list")
    public String listFertilizers(Model model) {
        model.addAttribute("fertilizers", fertilizerService.getAllFertilizers());
        return "fertilizer/list";
    }
}