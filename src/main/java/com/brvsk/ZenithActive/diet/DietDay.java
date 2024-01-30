package com.brvsk.ZenithActive.diet;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "diet_day")
@Entity(name = "DietDay")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DietDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "diet_day_id")
    private List<DietDayMeal> dietDayMeals;


}
