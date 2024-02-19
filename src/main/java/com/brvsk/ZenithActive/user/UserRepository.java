package com.brvsk.ZenithActive.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.profileImageId = :profileImageId WHERE u.userId = :userId")
    void updateCustomerProfileImageId(@Param("userId") UUID userId, @Param("profileImageId") String profileImageId);
}
