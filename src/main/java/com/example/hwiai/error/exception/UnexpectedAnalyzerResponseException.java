package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedAnalyzerResponseException extends BasicCustomException {
    public UnexpectedAnalyzerResponseException(Long reviewId, Long characteristicId) {
        super("Analyzer returned an unexpected reviewId=" + reviewId
                + " for characteristicId=" + characteristicId, HttpStatus.BAD_GATEWAY);
    }
}
