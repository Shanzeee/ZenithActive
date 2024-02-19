package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserProfileImageNotFound extends RuntimeException{
    public UserProfileImageNotFound(final UUID userId) {
        super("user with id "+userId+" profile image not found");
    }
}