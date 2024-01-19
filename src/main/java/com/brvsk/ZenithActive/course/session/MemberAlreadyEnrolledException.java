package com.brvsk.ZenithActive.course.session;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberAlreadyEnrolledException extends RuntimeException {
    public MemberAlreadyEnrolledException(final UUID memberId, final UUID sessionId) {
        super("Member with id " + memberId + " is already enrolled in session with id " + sessionId);
    }
}
