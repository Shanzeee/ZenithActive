package com.brvsk.ZenithActive.diet.mealprefernce;

import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileRepository;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberMealPreferenceServiceImpl implements MemberMealPreferenceService{

    private final MemberMealPreferenceRepository memberMealPreferenceRepository;
    private final MemberRepository memberRepository;
    private final MealProfileRepository mealProfileRepository;

    @Override
    public MemberMealPreference createMemberMealPreference(MemberMealPreferenceCreateRequest request) {
        MemberMealPreference memberMealPreference = toEntity(request);

        return memberMealPreferenceRepository.save(memberMealPreference);
    }

    @Override
    public void deleteMemberMealPreference(UUID referenceId) {
        memberMealPreferenceRepository.deleteById(referenceId);
    }

    private MemberMealPreference toEntity(MemberMealPreferenceCreateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow( () -> new UserNotFoundException(request.getMemberId()));
        MealProfile mealProfile = mealProfileRepository.findById(request.getMealProfileId())
                .orElseThrow(() -> new EntityNotFoundException("Meal profile with id: "+ request.getMealProfileId()+" not exists"));

        return MemberMealPreference.builder()
                .member(member)
                .mealProfile(mealProfile)
                .preferenceScore(request.getPreferenceScore())
                .build();
    }

}
