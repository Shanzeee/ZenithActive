package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.dietprofile.ActivityLevel;
import com.brvsk.ZenithActive.diet.dietprofile.DietGoal;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequestRepository;
import com.brvsk.ZenithActive.diet.mealprefernce.MemberMealPreferenceRepository;
import com.brvsk.ZenithActive.diet.mealprofile.Allergy;
import com.brvsk.ZenithActive.diet.mealprofile.MealCategory;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.user.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DietRecommendationServiceTest {

    @Mock
    private DietRequestRepository dietRequestRepository;

    @Mock
    private MemberMealPreferenceRepository memberMealPreferenceRepository;

    @InjectMocks
    private DietRecommendationService dietRecommendationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindSimilarDietRequests() {
        // Given
        DietRequest targetRequest = createDietRequest(24,70,170,Gender.MALE,ActivityLevel.EXTRA_ACTIVE);
        List<DietRequest> allDietRequests = Arrays.asList(
                createDietRequest(27,70,174,Gender.MALE,ActivityLevel.EXTRA_ACTIVE),
                createDietRequest(23,70,173,Gender.MALE,ActivityLevel.EXTRA_ACTIVE)
                );
        when(dietRequestRepository.findAll()).thenReturn(allDietRequests);

        // When
        Map<UUID, Double> similarityScores = dietRecommendationService.findSimilarDietRequests(targetRequest);

        // Then
        assertNotNull(similarityScores);
    }

    @Test
    public void testFindSimilarDietRequests_EmptyList() {
        // Given
        when(dietRequestRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        Map<UUID, Double> result = dietRecommendationService.findSimilarDietRequests(new DietRequest());

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRecommendMealForCategory() {
        // Given
        List<MealProfile> meals = Arrays.asList(creatMeal("breakfast"), creatMeal("good breakfast"));
        DietRequest dietRequest = createDietRequest(24,70,170,Gender.MALE,ActivityLevel.EXTRA_ACTIVE);
        Map<UUID, Double> similarDietRequests = new HashMap<>();
        similarDietRequests.put(UUID.fromString("eabbbdc3-d569-4cdb-b33f-939902838242"), 1.0);
        similarDietRequests.put(UUID.fromString("aabbbdc3-d569-4cdb-b33f-939902838242"), 5.0);

        when(dietRecommendationService.findSimilarDietRequests(dietRequest)).thenReturn(similarDietRequests);

        // When
        Optional<MealProfile> recommendedMeal = dietRecommendationService.recommendMealForCategory(meals, dietRequest);

        // Then
        assertTrue(recommendedMeal.isPresent());
    }


    @Test
    public void testRecommendMealForCategory_EmptyMeals() {
        // Given
        List<MealProfile> meals = Collections.emptyList();
        DietRequest dietRequest = new DietRequest();

        // When
        Optional<MealProfile> result = dietRecommendationService.recommendMealForCategory(meals, dietRequest);

        // Then
        assertTrue(result.isEmpty());
    }


    private MealProfile creatMeal(String name) {
        return MealProfile.builder()
                .mealCategory(MealCategory.BREAKFAST)
                .name(name)
                .isVegan(false)
                .allergens(List.of(Allergy.LACTOSE))
                .isVegetarian(false)
                .build();

    }

    private Member createMember(String firstName) {
        Member member = new Member();
        member.setUserId(UUID.randomUUID());
        member.setFirstName(firstName);
        return member;
    }

    private DietRequest createDietRequest(int age, double kg, double cm, Gender gender, ActivityLevel activityLevel) {
        return DietRequest.builder()
                .member(createMember("member"))
                .dietGoal(DietGoal.MAINTENANCE)
                .allergies(List.of(Allergy.EGG))
                .age(age)
                .height(cm)
                .weight(kg)
                .gender(gender)
                .isVegan(false)
                .isVegetarian(false)
                .activityLevel(activityLevel)
                .build();
    }
}