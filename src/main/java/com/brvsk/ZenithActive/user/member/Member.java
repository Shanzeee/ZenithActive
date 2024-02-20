package com.brvsk.ZenithActive.user.member;

import com.brvsk.ZenithActive.enrollment.SessionEnrollment;
import com.brvsk.ZenithActive.loyalty.LoyaltyPoints;
import com.brvsk.ZenithActive.membership.Membership;
import com.brvsk.ZenithActive.review.course.ReviewCourse;
import com.brvsk.ZenithActive.review.instructor.ReviewInstructor;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    private Integer height;
    private Integer weight;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SessionEnrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<ReviewInstructor> reviewInstructors = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<ReviewCourse> reviewCourses = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<TrainingPlanRequest> trainingPlanRequestList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "training_plan_s3_keys", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "training_plan_s3_key")
    private Set<String> trainingPlanS3Keys = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<LoyaltyPoints> loyaltyPoints = new HashSet<>();

}
