package com.brvsk.ZenithActive.notification.newsletter;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsletterServiceImpl implements NewsletterService{

    private final NewsletterRepository newsletterRepository;
    private final EmailSender emailSender;

    @Override
    public void subscribeNewsletter(String firstName, String email) {
        if (newsletterRepository.existsByEmail(email)){
            return;
        }
        NewsletterSubscriber newsletterSubscriber = new NewsletterSubscriber(
                UUID.randomUUID(),
                firstName,
                email,
                false
        );
        newsletterRepository.save(newsletterSubscriber);

        emailSender.sendNewsletterConfirmationEmail(firstName, email);
    }

    @Transactional
    @Override
    public void unsubscribeNewsletter(String email) {
        NewsletterSubscriber subscriber = newsletterRepository.findByEmail(email)
                .orElseThrow( () -> new SubscriberNotFoundException(email));

        newsletterRepository.delete(subscriber);
    }

    @Override
    public String confirmNewsletterSubscription(String email) {
        NewsletterSubscriber subscriber = newsletterRepository.findByEmail(email)
                .orElseThrow( () -> new SubscriberNotFoundException(email));

        if (subscriber.isConfirmed()){
            throw new AlreadySubscribedException(email);
        }

        subscriber.setConfirmed(true);

        newsletterRepository.save(subscriber);

        return "thanks for subscribing";
    }


    @Scheduled(cron = "0 0 20 * * THU")
    public void sendWeeklyNewsletter() {
        List<NewsletterSubscriber> subscribers = getAllSubscribers();
        List<String> newsletterContents = prepareNewsletterContents();

        for (NewsletterSubscriber subscriber : subscribers) {
            emailSender.sendWeeklyNewsletter(subscriber, newsletterContents);
        }
    }

    private List<String> prepareNewsletterContents() {
        List<String> newsletterContents = new ArrayList<>();

        newsletterContents.add("Promotion 1 - Get 20% off on gym accessories!");
        newsletterContents.add("Promotion 2 - Sign up for personal training and get a free fitness assessment!");
        newsletterContents.add("News 1 - New gym equipment has arrived!");
        newsletterContents.add("News 2 - Join our fitness challenge and win exciting prizes!");
        newsletterContents.add("Tip 1 - Stay hydrated during your workout!");
        newsletterContents.add("Tip 2 - Incorporate strength training into your routine for better results!");

        return newsletterContents;
    }

    private List<NewsletterSubscriber> getAllSubscribers() {
        return newsletterRepository.findAllByConfirmedIsTrue();
    }
}
