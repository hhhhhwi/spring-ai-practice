package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends BasicCustomException{
    public ReviewNotFoundException(Long id) {
        super("리뷰가 존재하지 않습니다. ID : " + id, HttpStatus.NOT_FOUND);
    }
}
