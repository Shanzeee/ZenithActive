package com.brvsk.ZenithActive.diet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<Diet, UUID> {
}
