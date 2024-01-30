package com.brvsk.ZenithActive.diet.mealprofile;

import com.brvsk.ZenithActive.diet.ingredient.Ingredient;
import com.brvsk.ZenithActive.diet.ingredient.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealProfileServiceImpl implements MealProfileService {

    private final MealProfileRepository mealProfileRepository;
    private final IngredientRepository ingredientRepository;
    private final MealProfileMapper mealProfileMapper;


    @Override
    public List<MealProfileResponse> getAllMealProfiles() {
        List<MealProfile> mealProfiles = mealProfileRepository.findAll();
        return mealProfiles.stream()
                .map(mealProfileMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MealProfileResponse getMealProfileById(UUID id) {
        MealProfile mealProfile = mealProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MealProfile not found with id: " + id));
        return mealProfileMapper.mapToResponse(mealProfile);
    }

    @Override
    public MealProfileResponse createMealProfile(MealProfileCreateRequest createRequest) {
        MealProfile mealProfile = mapToEntity(createRequest);

        MealProfile savedMealProfile = mealProfileRepository.save(mealProfile);

        return mealProfileMapper.mapToResponse(savedMealProfile);
    }

    @Override
    public MealProfileResponse updateMealProfile(UUID id, MealProfileCreateRequest updateRequest) {
        MealProfile existingMealProfile = mealProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MealProfile not found with id: " + id));

        mealProfileMapper.mapToEntity(updateRequest, existingMealProfile);

        List<Ingredient> ingredients = ingredientRepository.findAllById(updateRequest.getIngredientIds());
        existingMealProfile.setIngredients(ingredients);

        MealProfile updatedMealProfile = mealProfileRepository.save(existingMealProfile);

        return mealProfileMapper.mapToResponse(updatedMealProfile);
    }


    @Override
    public void deleteMealProfile(UUID id) {
        if (!mealProfileRepository.existsById(id)) {
            throw new EntityNotFoundException("MealProfile not found with id: " + id);
        }
        mealProfileRepository.deleteById(id);
    }

    private MealProfile mapToEntity(MealProfileCreateRequest request) {
        MealProfile mealProfile = MealProfile.builder()
                .name(request.getName())
                .mealCategory(request.getMealCategory())
                .allergens(request.getAllergens())
                .totalCalories(request.getTotalCalories())
                .totalFat(request.getTotalFat())
                .totalProtein(request.getTotalProtein())
                .totalCarbohydrates(request.getTotalCarbohydrates())
                .isVegetarian(request.isVegetarian())
                .isVegan(request.isVegan())
                .build();

        List<Ingredient> ingredients = ingredientRepository.findAllById(request.getIngredientIds());
        mealProfile.setIngredients(ingredients);

        return mealProfile;
    }
}
