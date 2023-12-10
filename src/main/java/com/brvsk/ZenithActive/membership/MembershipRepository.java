package com.brvsk.ZenithActive.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
}
