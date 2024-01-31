package com.brvsk.ZenithActive.diet.mealprefernce;

import java.util.UUID;

public interface MemberMealPreferenceService {
    MemberMealPreference createMemberMealPreference(MemberMealPreferenceCreateRequest request);
    void deleteMemberMealPreference(UUID referenceId);
}
