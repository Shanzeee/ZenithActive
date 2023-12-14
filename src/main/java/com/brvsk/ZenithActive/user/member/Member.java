package com.brvsk.ZenithActive.user.member;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.loyalty.LoyaltyPoints;
import com.brvsk.ZenithActive.membership.Membership;
import com.brvsk.ZenithActive.review.Review;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends User {

    @OneToOne
    private Membership membership;
    private Integer height;
    private Integer weight;

    @ManyToMany
    @JoinTable(
            name = "member_course_enrollment",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> enrolledCourses = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private List<TrainingPlanRequest> trainingPlanRequestList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "training_plan_paths", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "path")
    private Set<String> trainingPlanPaths = new HashSet<>();

    @Column(length = 1000)
    private String qrCode;

    @OneToMany(mappedBy = "member")
    private Set<LoyaltyPoints> loyaltyPoints = new HashSet<>();


}
