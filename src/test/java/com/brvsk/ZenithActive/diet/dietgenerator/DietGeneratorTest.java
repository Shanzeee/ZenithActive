//package com.brvsk.ZenithActive.diet.dietgenerator;
//
//import com.brvsk.ZenithActive.diet.DietDay;
//import com.brvsk.ZenithActive.diet.DietDayMeal;
//import com.brvsk.ZenithActive.diet.dietprofile.ActivityLevel;
//import com.brvsk.ZenithActive.diet.dietprofile.DietGoal;
//import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
//import com.brvsk.ZenithActive.diet.mealprofile.Allergy;
//import com.brvsk.ZenithActive.diet.mealprofile.MealCategory;
//import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
//import com.brvsk.ZenithActive.diet.mealprofile.MealProfileService;
//import com.brvsk.ZenithActive.user.Gender;
//import com.brvsk.ZenithActive.user.member.Member;
//import org.junit.Before;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.*;
//
//import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class DietGeneratorTest {
//
//    @Mock
//    private MealProfileService mealProfileService;
//
//    @Mock
//    private DietRecommendationService dietRecommendationService;
//
//    @InjectMocks
//    private DietGenerator dietGenerator;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void whenGenerateDiet_givenSpecificDays_thenCorrectNumberOfDietDaysGenerated() {
//        // Given
//        DietRequest dietRequest = createDietRequest(24, 70, 170, Gender.MALE, ActivityLevel.EXTRA_ACTIVE);
//        dietRequest.setDays(7);
//        List<MealProfile> preparedMeals = prepareMealProfilesForAllCategories();
//
//        Mockito.when(mealProfileService.filterMeals(Mockito.any(DietRequest.class)))
//                .thenReturn(preparedMeals);
//
//        // When
//        List<DietDay> generatedDietDays = dietGenerator.generateDiet(dietRequest);
//
//        // Then
//        assertEquals(7, generatedDietDays.size());
//    }
//
//    @Test
//    public void whenGenerateDiet_thenUsesUniqueMealProfilesForEachDay() {
//        // Given
//        DietRequest dietRequest = createDietRequest(24,70,170,Gender.MALE,ActivityLevel.EXTRA_ACTIVE);
//        dietRequest.setDays(3);
//
//
//        List<MealProfile> mealProfiles = prepareMealProfilesForAllCategories();
//
//        Mockito.when(mealProfileService.filterMeals(Mockito.any(DietRequest.class)))
//                .thenReturn(mealProfiles);
//
//
//        Mockito.when(dietRecommendationService.recommendMealForCategory(Mockito.anyList(), Mockito.any(DietRequest.class)))
//                .thenAnswer(invocation -> Optional.of(mealProfiles.get(new Random().nextInt(mealProfiles.size()))));
//
//        // When
//        List<DietDay> generatedDietDays = dietGenerator.generateDiet(dietRequest);
//
//        // Then
//        long uniqueMealsCount = generatedDietDays.stream()
//                .flatMap(dietDay -> dietDay.getDietDayMeals().stream())
//                .map(DietDayMeal::getMealProfile)
//                .distinct()
//                .count();
//
//        Assertions.assertTrue(uniqueMealsCount >= mealProfiles.size());
//    }
//
//    @Test
//    public void whenGenerateDiet_thenMealsAdjustedToTotalCaloricNeeds() {
//        // Given
//        DietRequest dietRequest = new DietRequest();
//        dietRequest.setDays(1);
//        dietRequest.setWeight(70);
//        dietRequest.setHeight(180);
//        dietRequest.setAge(25);
//        dietRequest.setGender(Gender.MALE);
//        dietRequest.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
//        dietRequest.setDietGoal(DietGoal.WEIGHT_LOSS);
//
//        List<MealProfile> preparedMeals = prepareMealProfilesForAllCategories();
//        Mockito.when(mealProfileService.filterMeals(Mockito.any(DietRequest.class)))
//                .thenReturn(preparedMeals);
//
//        // When
//        List<DietDay> generatedDietDays = dietGenerator.generateDiet(dietRequest);
//
//        // Then
//        Assertions.assertNotNull(generatedDietDays);
//        Assertions.assertFalse(generatedDietDays.isEmpty());
//    }
//
//    private List<MealProfile> prepareMealProfilesForAllCategories() {
//        List<MealProfile> mealProfiles = new ArrayList<>();
//        Random random = new Random();
//
//        for (MealCategory category : MealCategory.values()) {
//            for (int i = 1; i <= 5; i++) {
//                double randomCalories = 200 + (300 * random.nextDouble());
//                MealProfile meal = MealProfile.builder()
//                        .id(UUID.randomUUID())
//                        .name(category.toString() + " Meal " + i)
//                        .mealCategory(category)
//                        .isVegan(false)
//                        .allergens(new ArrayList<>())
//                        .isVegetarian(false)
//                        .totalCalories(randomCalories)
//                        .build();
//                mealProfiles.add(meal);
//            }
//        }
//        return mealProfiles;
//    }
//
//
//    private MealProfile creatMeal(String name) {
//        return MealProfile.builder()
//                .mealCategory(MealCategory.BREAKFAST)
//                .name(name)
//                .isVegan(false)
//                .allergens(List.of(Allergy.LACTOSE))
//                .isVegetarian(false)
//                .build();
//
//    }
//
//    private Member createMember(String firstName) {
//        Member member = new Member();
//        member.setUserId(UUID.randomUUID());
//        member.setFirstName(firstName);
//        return member;
//    }
//
//    private DietRequest createDietRequest(int age, double kg, double cm, Gender gender, ActivityLevel activityLevel) {
//        return DietRequest.builder()
//                .member(createMember("member"))
//                .dietGoal(DietGoal.MAINTENANCE)
//                .allergies(List.of(Allergy.EGG))
//                .age(age)
//                .height(cm)
//                .weight(kg)
//                .gender(gender)
//                .isVegan(false)
//                .isVegetarian(false)
//                .activityLevel(activityLevel)
//                .build();
//    }
//
//
//}