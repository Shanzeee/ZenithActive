package com.brvsk.ZenithActive.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    List<Membership> findByMember_UserIdAndStartDateBeforeAndEndDateAfter(UUID memberId, LocalDate currentDate, LocalDate currentDateAgain);

    List<Membership> findByMember_UserIdAndEndDateAfterOrderByEndDateDesc(UUID memberId, LocalDate currentDate);
}
