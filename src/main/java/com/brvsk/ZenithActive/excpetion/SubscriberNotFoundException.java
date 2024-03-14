package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriberNotFoundException extends RuntimeException{
    public SubscriberNotFoundException(final String email) {
        super("Subscriber with mail: " + email + " not found");
    }
}