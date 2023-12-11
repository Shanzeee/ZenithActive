package com.brvsk.ZenithActive.user.instructor;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.review.Review;
import com.brvsk.ZenithActive.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor extends User {

    @Column(length = 1000)
    private String description;

    @Enumerated
    private List<Speciality> specialities;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "instructor")
    private Set<Review> reviews = new HashSet<>();
}
