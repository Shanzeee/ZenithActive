package com.brvsk.ZenithActive.notification.newsletter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NewsletterRepository extends JpaRepository<NewsletterSubscriber, UUID> {
    boolean existsByEmail (String email);
    Optional<NewsletterSubscriber> findByEmail (String email);
    List<NewsletterSubscriber> findAllByConfirmedIsTrue ();
}
