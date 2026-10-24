package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class AnalysisPreconditionException extends BasicCustomException {
    public AnalysisPreconditionException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
