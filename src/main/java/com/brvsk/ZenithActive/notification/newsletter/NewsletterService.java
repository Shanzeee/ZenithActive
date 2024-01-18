package com.brvsk.ZenithActive.notification.newsletter;

public interface NewsletterService {
    void subscribeNewsletter(String firstName, String email);
    void unsubscribeNewsletter(String email);
    String confirmNewsletterSubscription(String email);
}
