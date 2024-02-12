package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.enrollment.SessionEnrollment;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "session")
@Entity(name = "Session")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @Min(0)
    @Column(nullable = false)
    private Integer groupSize;

    @Column(nullable = false)
    private LocalDate localDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SessionEnrollment> enrollments = new HashSet<>();

}
