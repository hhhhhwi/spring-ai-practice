package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BasicCustomException{
    public ProductNotFoundException(Long id) {
        super("제품이 존재하지 않습니다. ID : " + id, HttpStatus.NOT_FOUND);
    }
}
