package com.brvsk.ZenithActive.trainingplan.request.entity;

import com.brvsk.ZenithActive.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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
    private Member member;
    @Embedded
    private TrainingPlanRequestMemberInfo memberInfo;
    @CreationTimestamp
    private LocalDate createdAt;
}
