package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMembershipException extends RuntimeException {
    public InvalidMembershipException() {
        super("No valid membership for session enrollment.");
    }
}
