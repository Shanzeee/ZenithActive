package com.brvsk.ZenithActive.review;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "Review")
@Table(name = "review")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
    private Rating instructorRating;
    private String instructorComment;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private Rating courseRating;
    private String courseComment;

    @CreationTimestamp
    private LocalDate timestamp;
}
