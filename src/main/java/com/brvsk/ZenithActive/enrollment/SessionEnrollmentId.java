package com.brvsk.ZenithActive.enrollment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
public class SessionEnrollmentId implements Serializable {
    private UUID memberId;
    private UUID sessionId;


}
