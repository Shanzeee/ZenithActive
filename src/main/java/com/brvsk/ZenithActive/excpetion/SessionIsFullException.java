package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessionIsFullException extends RuntimeException {
    public SessionIsFullException(UUID sessionId) {
        super("Session with id " + sessionId + " is already full");
    }
}
