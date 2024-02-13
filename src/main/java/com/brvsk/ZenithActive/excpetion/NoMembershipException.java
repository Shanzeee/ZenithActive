package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoMembershipException extends RuntimeException {
    public NoMembershipException(UUID memberId) {
        super("No membership found for the member with id: " + memberId);
    }

}
