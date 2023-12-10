package com.brvsk.ZenithActive.membership;

import com.brvsk.ZenithActive.user.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "Membership")
@Table(name = "membership")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private MembershipType membershipType;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne
    private Member member;
}
