package com.brvsk.ZenithActive.notification.newsletter;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;


    @GetMapping("/confirm/{email}")
    public String confirmNewsletterSubscription(@PathVariable String email) {
        return newsletterService.confirmNewsletterSubscription(email);
    }

    @Transactional
    @DeleteMapping("/unsubscribe/{email}")
    public void unsubscribeNewsletter(@PathVariable String email) {
        newsletterService.unsubscribeNewsletter(email);
    }
}
