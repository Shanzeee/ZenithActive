package com.brvsk.ZenithActive.enrollment;

import com.brvsk.ZenithActive.session.Session;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "session_enrollemnt")
@Entity(name = "SessionEnrollment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionEnrollment {
    @EmbeddedId
    private SessionEnrollmentId id;

    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    private LocalDateTime enrollmentDate;

    public SessionEnrollment(SessionEnrollmentId id, Session session, Member member) {
        this.id = id;
        this.session = session;
        this.member = member;
    }
}
