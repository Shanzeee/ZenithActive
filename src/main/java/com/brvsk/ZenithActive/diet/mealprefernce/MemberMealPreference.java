package com.brvsk.ZenithActive.diet.mealprefernce;

import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_meal_preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberMealPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "meal_profile_id")
    private MealProfile mealProfile;

    @Column(name = "preference_score", columnDefinition = "DECIMAL(3,1) CHECK (preference_score >= 0 AND preference_score <= 5)")
    private double preferenceScore;
}
