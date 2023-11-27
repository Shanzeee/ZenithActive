package com.brvsk.ZenithActive.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "users")
@Entity(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    @Enumerated
    private Gender gender;

}
