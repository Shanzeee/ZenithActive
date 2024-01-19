package com.brvsk.ZenithActive.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException(UUID reviewedEntityId) {
        super("Review to entity with id: "+ reviewedEntityId + "already exists");
    }
}
