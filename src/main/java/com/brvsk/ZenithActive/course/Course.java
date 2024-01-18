package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.review.Review;
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

    @Min(0)
    @Column(nullable = false)
    private Integer groupSize;

    @ManyToMany(
            mappedBy = "enrolledCourses",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<Member> enrolledMembers = new HashSet<>();

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL
    )
    private Set<Review> reviews = new HashSet<>();

    @Column(nullable = false)
    private LocalDate localDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}



