package com.brvsk.ZenithActive.notification.newsletter;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
class NewsletterServiceImplTest {

    @Autowired
    private NewsletterService newsletterService;

    @MockBean
    private NewsletterRepository newsletterRepository;

    @MockBean
    private EmailSender emailSender;

    @Test
    void givenValidSubscriptionDetails_whenSubscribeNewsletter_thenSubscriberAddedAndConfirmationEmailSent() {
        // Given
        String firstName = "John";
        String email = "john@example.com";

        when(newsletterRepository.existsByEmail(email)).thenReturn(false);

        // When
        newsletterService.subscribeNewsletter(firstName, email);

        // Then
        verify(newsletterRepository).save(any());
        verify(emailSender).sendNewsletterConfirmationEmail(firstName, email);
    }

    @Test
    void givenExistingSubscriber_whenSubscribeNewsletter_thenNoActionTaken() {
        // Given
        String firstName = "Jane";
        String email = "jane@example.com";

        when(newsletterRepository.existsByEmail(email)).thenReturn(true);

        // When
        newsletterService.subscribeNewsletter(firstName, email);

        // Then
        verify(newsletterRepository, never()).save(any());
        verify(emailSender, never()).sendNewsletterConfirmationEmail(any(), any());
    }

    @Test
    void givenExistingSubscriber_whenUnsubscribeNewsletter_thenSubscriberDeleted() {
        // Given
        String email = "existing@example.com";
        NewsletterSubscriber subscriber = new NewsletterSubscriber(UUID.randomUUID(), "John", email, true);

        when(newsletterRepository.findByEmail(email)).thenReturn(Optional.of(subscriber));

        // When
        newsletterService.unsubscribeNewsletter(email);

        // Then
        verify(newsletterRepository).delete(subscriber);
    }

    @Test
    void givenNonexistentSubscriber_whenUnsubscribeNewsletter_thenSubscriberNotFoundExceptionThrown() {
        // Given
        String email = "nonexistent@example.com";

        when(newsletterRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> newsletterService.unsubscribeNewsletter(email))
                .isInstanceOf(SubscriberNotFoundException.class);
    }

    @Test
    void givenConfirmedSubscriber_whenConfirmNewsletterSubscription_thenAlreadySubscribedExceptionThrown() {
        // Given
        String email = "confirmed@example.com";
        NewsletterSubscriber subscriber = new NewsletterSubscriber(UUID.randomUUID(), "Alice", email, true);

        when(newsletterRepository.findByEmail(email)).thenReturn(Optional.of(subscriber));

        // When/Then
        assertThatThrownBy(() -> newsletterService.confirmNewsletterSubscription(email))
                .isInstanceOf(AlreadySubscribedException.class);
    }

    @Test
    void givenUnconfirmedSubscriber_whenConfirmNewsletterSubscription_thenConfirmationMessageReturned() {
        // Given
        String email = "unconfirmed@example.com";
        NewsletterSubscriber subscriber = new NewsletterSubscriber(UUID.randomUUID(), "Bob", email, false);

        when(newsletterRepository.findByEmail(email)).thenReturn(Optional.of(subscriber));

        // When
        String confirmationMessage = newsletterService.confirmNewsletterSubscription(email);

        // Then
        assertThat(confirmationMessage).isEqualTo("thanks for subscribing");
        assertThat(subscriber.isConfirmed()).isTrue();
        verify(newsletterRepository).save(subscriber);
    }
}
