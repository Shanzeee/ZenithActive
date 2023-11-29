package com.brvsk.ZenithActive.member;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.review.Review;
import com.brvsk.ZenithActive.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends User {

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
}
