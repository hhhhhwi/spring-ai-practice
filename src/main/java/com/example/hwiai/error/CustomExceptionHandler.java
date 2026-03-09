package com.example.hwiai.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.hwiai.error.exception.UniqueConstraintViolationException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<CustomExceptionResponse>
    handleUniqueConstraintViolationException(UniqueConstraintViolationException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new CustomExceptionResponse(e.getMessage()));
    }
}
