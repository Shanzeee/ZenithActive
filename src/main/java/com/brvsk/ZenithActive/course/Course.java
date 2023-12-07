package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
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
    private CourseType courseType;
    private String name;
    private String description;
    private Integer groupSize;
    @ManyToMany(mappedBy = "enrolledCourses", fetch = FetchType.EAGER)
    private Set<Member> enrolledMembers = new HashSet<>();
    @OneToMany(mappedBy = "course")
    private Set<Review> reviews = new HashSet<>();
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}



