package com.brvsk.ZenithActive.excpetion;

public class MissingJwtTokenException extends RuntimeException {
    public MissingJwtTokenException(String message) {
        super(message);
    }
}
