package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.review.Review;
import com.brvsk.ZenithActive.review.course.ReviewCourse;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "courses")
@Entity(name = "Courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated
    @Column(nullable = false)
    private CourseType courseType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL
    )
    private Set<ReviewCourse> reviews = new HashSet<>();

}



