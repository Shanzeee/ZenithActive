package com.brvsk.ZenithActive.review.instructor;

import com.brvsk.ZenithActive.review.Rating;
import com.brvsk.ZenithActive.review.Review;
import com.brvsk.ZenithActive.user.instructor.Instructor;
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
public class ReviewInstructor extends Review {

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Builder
    public ReviewInstructor(UUID id, Member member, Rating rating, String comment, LocalDate timestamp, Instructor instructor) {
        super(id, member, rating, comment, timestamp);
        this.instructor = instructor;
    }

}
