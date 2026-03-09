package com.example.hwiai.error;

public class CustomExceptionResponse {
    private String message;

    public CustomExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
