package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BasicCustomException{
    public ProductNotFoundException(Long id) {
        super("Product not found with ID: " + id, HttpStatus.NOT_FOUND);
    }
}
