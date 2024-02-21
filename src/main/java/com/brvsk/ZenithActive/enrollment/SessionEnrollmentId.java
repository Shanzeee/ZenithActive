package com.brvsk.ZenithActive.enrollment;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionEnrollmentId implements Serializable {
    private UUID memberId;
    private UUID sessionId;


}
