package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequestRepository;
import com.brvsk.ZenithActive.diet.mealprefernce.MemberMealPreferenceRepository;
import com.brvsk.ZenithActive.diet.mealprofile.Allergy;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DietRecommendationService {
    private final DietRequestRepository dietRequestRepository;
    private final MemberMealPreferenceRepository memberMealPreferenceRepository;

    public Map<UUID, Double> findSimilarDietRequests(DietRequest targetDietRequest) {
        List<DietRequest> allDietRequests = dietRequestRepository.findAll();
        Map<UUID, Double> similarityScores = new HashMap<>();

        for (DietRequest dietRequest : allDietRequests) {
            double score = calculateSimilarityScore(targetDietRequest, dietRequest);
            similarityScores.put(dietRequest.getMember().getUserId(), score);
        }

        return similarityScores;
    }

    private double calculateSimilarityScore(DietRequest target, DietRequest other) {
        double score = 0.0;

        if (target.getGender() == other.getGender()) score += 1.0;
        if (target.getActivityLevel() == other.getActivityLevel()) score += 1.0;
        if (target.getDietGoal() == other.getDietGoal()) score += 1.0;
        if (target.isVegetarian() == other.isVegetarian()) score += 1.0;
        if (target.isVegan() == other.isVegan()) score += 1.0;

        score += calculateAgeSimilarity(target.getAge(), other.getAge());
        score += calculatePhysicalSimilarity(target.getWeight(), other.getWeight(), target.getHeight(), other.getHeight());
        score += calculateAllergySimilarity(target.getAllergies(), other.getAllergies());

        return score;
    }

    private double calculateAgeSimilarity(int age1, int age2) {
        return Math.abs(age1 - age2) <= 5 ? 1.0 : 0.0;
    }

    private double calculatePhysicalSimilarity(double weight1, double weight2, double height1, double height2) {
        double weightDifference = Math.abs(weight1 - weight2);
        double heightDifference = Math.abs(height1 - height2);

        return (weightDifference <= 10 && heightDifference <= 10) ? 1.0 : 0.0;
    }

    private double calculateAllergySimilarity(List<Allergy> allergies1, List<Allergy> allergies2) {
        long commonAllergies = allergies1.stream().filter(allergies2::contains).count();
        return commonAllergies > 0 ? 1.0 : 0.0;
    }

    public MealProfile recommendMealForCategory(List<MealProfile> meals, DietRequest dietRequest) {

        Map<UUID, Double> similarDietRequests = findSimilarDietRequests(dietRequest);
        return meals.stream()
                .max((meal1, meal2) -> compareMeals(meal1, meal2, similarDietRequests))
                .orElse(null);
    }

    private int compareMeals(MealProfile meal1, MealProfile meal2, Map<UUID, Double> similarDietRequests) {
        double score1 = getMealScore(meal1, similarDietRequests);
        double score2 = getMealScore(meal2, similarDietRequests);

        return Double.compare(score1, score2);
    }

    private double getMealScore(MealProfile meal, Map<UUID, Double> similarDietRequests) {
        return similarDietRequests.entrySet().stream()
                .filter(entry -> memberMealPreferenceRepository.userPrefersMeal(entry.getKey(), meal.getId()))
                .mapToDouble(Map.Entry::getValue)
                .sum();
    }
}
