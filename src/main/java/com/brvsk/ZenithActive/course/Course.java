package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.instructor.Instructor;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
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
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    @OneToMany
    private List<Facility> facilities;
    @ManyToOne
    private Instructor instructor;
}
