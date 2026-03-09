package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class BasicCustomException extends RuntimeException {
    private final HttpStatus httpStatus;

    public BasicCustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
