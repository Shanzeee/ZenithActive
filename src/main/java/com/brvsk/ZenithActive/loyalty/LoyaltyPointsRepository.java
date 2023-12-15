package com.brvsk.ZenithActive.loyalty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface LoyaltyPointsRepository extends JpaRepository<LoyaltyPoints, UUID> {

    @Query("SELECT SUM(lp.pointsAmount) FROM LoyaltyPoints lp WHERE lp.member.userId = :memberId")
    int sumPointsByMemberId(@Param("memberId") UUID memberId);

    @Query("SELECT COUNT(lp) FROM LoyaltyPoints lp WHERE lp.receivedAt BETWEEN :startDate AND :endDate")
    long countPointsInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
