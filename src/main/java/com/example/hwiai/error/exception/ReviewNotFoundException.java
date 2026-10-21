package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends BasicCustomException{
    public ReviewNotFoundException(Long id) {
        super("Review not found with ID: " + id, HttpStatus.NOT_FOUND);
    }
}
