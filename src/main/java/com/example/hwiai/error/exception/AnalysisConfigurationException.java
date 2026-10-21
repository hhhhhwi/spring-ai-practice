package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class AnalysisConfigurationException extends BasicCustomException {
    public AnalysisConfigurationException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
