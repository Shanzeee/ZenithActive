package com.brvsk.ZenithActive.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @Email
    private String email;
    @Enumerated
    private Gender gender;

}
