package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.dietgenerator.DietGenerator;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileMapper;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileResponseSimple;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService{

    private final DietRepository dietRepository;
    private final DietGenerator dietGenerator;
    private final DietMapper dietMapper;
    private final MemberRepository memberRepository;
    private final MealProfileMapper mealProfileMapper;

    @Override
    public void createDiet(DietRequest dietRequest) {
        List<DietDay> dietDayList = dietGenerator.generateDiet(dietRequest);

        Member member = memberRepository.findById(dietRequest.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(dietRequest.getMemberId()));

        Diet diet = Diet
                .builder()
                .dailyMealPlans(dietDayList)
                .member(member)
                .build();

        dietRepository.save(diet);
    }

    @Override
    public List<DietResponse> getDietsForMember(UUID memberId) {
        List<Diet> diets = dietRepository.findByMember_UserId(memberId);
        return diets.stream()
                .map(dietMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MealProfileResponseSimple> getMealsForDiets(UUID memberId) {

        List<Diet> dietsForMember = dietRepository.findByMember_UserId(memberId);

        return dietsForMember.stream()
                .flatMap(diet -> diet.getDailyMealPlans().stream())
                .flatMap(dietDay -> dietDay.getDietDayMeals().stream())
                .map(DietDayMeal::getMealProfile)
                .distinct()
                .map(mealProfileMapper::mapToResponseSimple)
                .collect(Collectors.toList());
    }


}
