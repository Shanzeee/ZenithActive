package com.brvsk.ZenithActive.trainingplan.request.entity;

import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "TrainingPlanRequest")
@Table(name = "training_plan_request")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlanRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private boolean created;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private TrainingPlanRequestMemberInfo memberInfo;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
