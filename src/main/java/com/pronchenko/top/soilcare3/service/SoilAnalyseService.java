package com.pronchenko.top.soilcare3.service;

import com.pronchenko.top.soilcare3.entity.*;
import com.pronchenko.top.soilcare3.repository.FertilizerRepository;
import com.pronchenko.top.soilcare3.repository.RecommendationRepository;
import com.pronchenko.top.soilcare3.repository.SoilAnalyseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoilAnalyseService {
    private final SoilAnalyseRepository soilAnalyseRepository;
    private final RecommendationRepository recommendationRepository;
    private final FertilizerRepository fertilizerRepository;

    @Autowired
    public SoilAnalyseService(SoilAnalyseRepository soilAnalyseRepository,
                              RecommendationRepository recommendationRepository,
                              FertilizerRepository fertilizerRepository) { // Добавляем
        this.soilAnalyseRepository = soilAnalyseRepository;
        this.recommendationRepository = recommendationRepository;
        this.fertilizerRepository = fertilizerRepository; // Добавляем
    }

    public SoilAnalyse saveWithRecommendations(SoilAnalyse soilAnalyse) {

        SoilAnalyse savedAnalyse = soilAnalyseRepository.save(soilAnalyse);


        List<Recommendation> recommendations = generateRecommendations(savedAnalyse);


        recommendationRepository.saveAll(recommendations);

        return savedAnalyse;
    }

    public SoilAnalyse getByIdWithRecommendations(Long id) {
        return soilAnalyseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));

    }

    private List<Recommendation> generateRecommendations(SoilAnalyse soilAnalyse) {
        List<Recommendation> recommendations = new ArrayList<>();
        List<Fertilizer> allFertilizers = fertilizerRepository.findAll();
        if (soilAnalyse.getNitrogen() < 20) {
            Recommendation rec = createNitrogenRecommendation(soilAnalyse);
            rec.setRecommendedFertilizers(allFertilizers.stream()
                    .filter(f -> f.getNContent() > 0)
                    .sorted((f1, f2) -> Double.compare(f2.getNContent(), f1.getNContent()))
                    .limit(3)
                    .collect(Collectors.toList()));
            recommendations.add(rec);
        }
        if (soilAnalyse.getPhosphorus() < 15) {
            Recommendation rec = createPhosphorusRecommendation(soilAnalyse);
            rec.setRecommendedFertilizers(allFertilizers.stream()
                    .filter(f -> f.getPContent() > 0)
                    .sorted((f1, f2) -> Double.compare(f2.getPContent(), f1.getPContent()))
                    .limit(3)
                    .collect(Collectors.toList()));
            recommendations.add(rec);
        }
        if (soilAnalyse.getPotassium() < 100) {
            Recommendation rec = createPotassiumRecommendation(soilAnalyse);
            rec.setRecommendedFertilizers(allFertilizers.stream()
                    .filter(f -> f.getKContent() > 0)
                    .sorted((f1, f2) -> Double.compare(f2.getKContent(), f1.getKContent()))
                    .limit(3)
                    .collect(Collectors.toList()));
            recommendations.add(rec);
        }
        return recommendations;
    }


    private Recommendation createNitrogenRecommendation(SoilAnalyse soilAnalyse) {
        Recommendation rec = new Recommendation();
        rec.setElementName("Азот (N)");
        rec.setElementType(ElementType.MACRO_NUTRIENT);
        rec.setReason("Низкое содержание азота для продуктивного роста растений");
        rec.setCurrentLevel(soilAnalyse.getNitrogen());
        rec.setTargetLevel("20-40");
        rec.setDeficitAmount(25.0 - soilAnalyse.getNitrogen());
        rec.setUnit("mg/kg");
        rec.setPriority(Priority.HIGH);
        rec.setSoilAnalyse(soilAnalyse);
        rec.addSolution("Аммиачная селитра", "20-30 г/м²");
        rec.addSolution("Мочевина (карбамид)", "15-25 г/м²");
        rec.addSolution("Компост", "3-5 кг/м²");
        return rec;
    }

    private Recommendation createPhosphorusRecommendation(SoilAnalyse soilAnalyse) {
        Recommendation rec = new Recommendation();
        rec.setElementName("Фосфор (P)");
        rec.setElementType(ElementType.MACRO_NUTRIENT);
        rec.setReason("Дефицит фосфора ограничивает развитие корневой системы");
        rec.setCurrentLevel(soilAnalyse.getPhosphorus());
        rec.setTargetLevel("15-25");
        rec.setDeficitAmount(20.0 - soilAnalyse.getPhosphorus());
        rec.setUnit("mg/kg");
        rec.setPriority(Priority.HIGH);
        rec.setSoilAnalyse(soilAnalyse);
        rec.addSolution("Суперфосфат", "30-40 г/м²");
        rec.addSolution("Фосфоритная мука", "40-60 г/м²");
        return rec;
    }

    private Recommendation createPotassiumRecommendation(SoilAnalyse soilAnalyse) {
        Recommendation rec = new Recommendation();
        rec.setElementName("Калий (K)");
        rec.setElementType(ElementType.MACRO_NUTRIENT);
        rec.setReason("Недостаток калия снижает устойчивость растений к болезням");
        rec.setCurrentLevel(soilAnalyse.getPotassium());
        rec.setTargetLevel("100-200");
        rec.setDeficitAmount(150.0 - soilAnalyse.getPotassium());
        rec.setUnit("mg/kg");
        rec.setPriority(Priority.MEDIUM);
        rec.setSoilAnalyse(soilAnalyse);
        rec.addSolution("Калийная соль", "20-30 г/м²");
        rec.addSolution("Сульфат калия", "15-25 г/м²");
        rec.addSolution("Древесная зола", "100-200 г/м²");
        return rec;
    }

    private Recommendation createAcidicPhRecommendation(SoilAnalyse soilAnalyse) {
        Recommendation rec = new Recommendation();
        rec.setElementName("Кислотность (pH)");
        rec.setElementType(ElementType.PH_LEVEL);
        rec.setReason("Кислая почва ограничивает доступность питательных веществ");
        rec.setCurrentLevel(soilAnalyse.getPh());
        rec.setTargetLevel("6.0-7.5");
        rec.setDeficitAmount(6.5 - soilAnalyse.getPh());
        rec.setUnit("pH");
        rec.setPriority(Priority.HIGH);
        rec.setSoilAnalyse(soilAnalyse);
        rec.addSolution("Доломитовая мука", "300-500 г/м²");
        rec.addSolution("Известь гашеная", "200-400 г/м²");
        rec.addSolution("Древесная зола", "100-200 г/м²");
        return rec;
    }

    private Recommendation createAlkalinePhRecommendation(SoilAnalyse soilAnalyse) {
        Recommendation rec = new Recommendation();
        rec.setElementName("Кислотность (pH)");
        rec.setElementType(ElementType.PH_LEVEL);
        rec.setReason("Щелочная почва затрудняет усвоение микроэлементов");
        rec.setCurrentLevel(soilAnalyse.getPh());
        rec.setTargetLevel("6.0-7.5");
        rec.setDeficitAmount(soilAnalyse.getPh() - 7.0);
        rec.setUnit("pH");
        rec.setPriority(Priority.MEDIUM);
        rec.setSoilAnalyse(soilAnalyse);
        rec.addSolution("Сера коллоидная", "50-100 г/м²");
        rec.addSolution("Верховой торф", "5-10 кг/м²");
        rec.addSolution("Хвойный опад", "3-5 кг/м²");
        return rec;
    }

    private Recommendation createOrganicMatterRecommendation(SoilAnalyse soilAnalyse) {
        Recommendation rec = new Recommendation();
        rec.setElementName("Органическое вещество");
        rec.setElementType(ElementType.ORGANIC_MATTER);
        rec.setReason("Низкое содержание органики ухудшает структуру почвы");
        rec.setCurrentLevel(soilAnalyse.getOrganicMatter());
        rec.setTargetLevel("2.5-4.0");
        rec.setDeficitAmount(3.0 - soilAnalyse.getOrganicMatter());
        rec.setUnit("%");
        rec.setPriority(Priority.MEDIUM);
        rec.setSoilAnalyse(soilAnalyse);
        rec.addSolution("Компост", "5-10 кг/м²");
        rec.addSolution("Перегной", "3-5 кг/м²");
        rec.addSolution("Сидераты", "Посев с заделкой в почву");
        return rec;
    }


    public SoilAnalyse getById(Long id) {
        return soilAnalyseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soil analysis not found with id: " + id));
    }
    public List<SoilAnalyse> getAnalysesByUserId(Long userId) {
        return soilAnalyseRepository.findByUserId(userId);
    }
}
