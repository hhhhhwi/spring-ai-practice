package com.example.hwiai.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.hwiai.error.exception.BasicCustomException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BasicCustomException.class)
    public ResponseEntity<CustomExceptionResponse>
    handleBasicCustomException(BasicCustomException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new CustomExceptionResponse(e.getMessage()));
    }
}
