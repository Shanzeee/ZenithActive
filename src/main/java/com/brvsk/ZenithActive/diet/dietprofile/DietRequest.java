package com.brvsk.ZenithActive.diet.dietprofile;

import com.brvsk.ZenithActive.diet.mealprofile.Allergy;
import com.brvsk.ZenithActive.user.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "diet_request")
@Entity(name = "DietRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DietRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID memberId;

    private Gender gender;
    private int age;
    private double weight;
    private double height;
    private ActivityLevel activityLevel;
    private DietGoal dietGoal;

    @ElementCollection(targetClass = Allergy.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "diet_request_allergies", joinColumns = @JoinColumn(name = "diet_request_id"))
    @Column(name = "allergy")
    private List<Allergy> allergies;
    private boolean isVegetarian;
    private boolean isVegan;

    private int days;

}
