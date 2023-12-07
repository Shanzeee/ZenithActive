package com.brvsk.ZenithActive.notification.newsletter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "NewsletterSubscriber")
@Table(name = "newsletter_subscriber")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String email;
    private boolean confirmed;

}
