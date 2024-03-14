package com.brvsk.ZenithActive.notification.newsletter;

import com.brvsk.ZenithActive.excpetion.AlreadySubscribedException;
import com.brvsk.ZenithActive.excpetion.SubscriberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;


    @GetMapping("/confirm/{email}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> confirmNewsletterSubscription(@PathVariable String email) {
        try {
            String confirmationMessage = newsletterService.confirmNewsletterSubscription(email);
            return ResponseEntity.ok(confirmationMessage);
        } catch (SubscriberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found");
        } catch (AlreadySubscribedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Subscriber already confirmed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
        }
    }

    @Transactional
    @DeleteMapping("/unsubscribe/{email}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> unsubscribeNewsletter(@PathVariable String email) {
        try {
            newsletterService.unsubscribeNewsletter(email);
            return ResponseEntity.ok("Unsubscribed successfully");
        } catch (SubscriberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
        }
    }
}
