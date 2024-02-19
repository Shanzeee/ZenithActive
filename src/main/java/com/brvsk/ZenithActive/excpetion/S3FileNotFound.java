package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class S3FileNotFound extends RuntimeException{

    public S3FileNotFound(String S3key) {
        super("S3 file with key "+S3key+" not found");
    }
}
