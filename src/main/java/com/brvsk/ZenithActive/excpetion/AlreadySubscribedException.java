package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadySubscribedException extends RuntimeException{
    public AlreadySubscribedException(final String email) {
        super("Email " + email + "is already confrimed");
    }
}
