package com.brvsk.ZenithActive.notification.newsletter;

import org.springframework.transaction.annotation.Transactional;

public interface NewsletterService {
    void subscribeNewsletter(String firstName, String email);
    @Transactional
    void unsubscribeNewsletter(String email);
    String confirmNewsletterSubscription(String email);
}
