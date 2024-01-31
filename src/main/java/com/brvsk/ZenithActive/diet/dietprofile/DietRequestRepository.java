package com.brvsk.ZenithActive.diet.dietprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DietRequestRepository extends JpaRepository<DietRequest, UUID> {
}
