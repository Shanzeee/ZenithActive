package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "diet")
@Entity(name = "Diet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DietDay> dailyMealPlans;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
