package com.brvsk.ZenithActive.review.course;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.review.Rating;
import com.brvsk.ZenithActive.review.Review;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class ReviewCourse extends Review{

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Builder
    public ReviewCourse(UUID id, Member member, Rating rating, String comment, LocalDate timestamp, Course course) {
        super(id, member, rating, comment, timestamp);
        this.course = course;
    }
}

