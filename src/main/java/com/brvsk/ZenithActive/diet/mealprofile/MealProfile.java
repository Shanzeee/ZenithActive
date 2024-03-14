package com.brvsk.ZenithActive.diet.mealprofile;

import com.brvsk.ZenithActive.diet.ingredient.Ingredient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "meal_profile")
@Entity(name = "MealProfile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_profile_seq")
    @SequenceGenerator(name = "meal_profile_seq", sequenceName = "meal_profile_seq", allocationSize = 1)
    private UUID id;

    private String name;


    @ManyToMany
    @JoinTable(
            name = "meal_profile_ingredients",
            joinColumns = @JoinColumn(name = "meal_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @Enumerated(EnumType.STRING)
    private MealCategory mealCategory;

    @ElementCollection(targetClass = Allergy.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "meal_profile_allergens", joinColumns = @JoinColumn(name = "meal_profile_id"))
    @Column(name = "allergen")
    private List<Allergy> allergens;

    private double totalCalories;
    private double totalFat;
    private double totalProtein;
    private double totalCarbohydrates;

    private boolean isVegetarian;
    private boolean isVegan;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MealProfile that = (MealProfile) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
