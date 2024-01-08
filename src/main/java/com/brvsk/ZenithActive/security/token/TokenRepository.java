package com.brvsk.ZenithActive.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    List<Token> findAllByUserUserIdAndRevokedIsFalseAndExpiredIsFalse(UUID userId);
    Optional<Token> findByToken(String token);
}