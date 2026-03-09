package com.example.hwiai.error.exception;

import org.springframework.http.HttpStatus;

import com.example.hwiai.util.UniqueConstraints;

public class UniqueConstraintViolationException extends BasicCustomException{

    public UniqueConstraintViolationException(String constraintName) {
        super(buildMessage(constraintName), HttpStatus.CONFLICT);
    }
    
    private static String buildMessage(String constraintName) {
        String[] columnNames = UniqueConstraints.getColumnNamesByConstraintName(constraintName);
        if (columnNames == null) {
            return "유니크 제약 조건 위반: " + constraintName;
        }
        return String.join(", ", columnNames) + " 값에 대해 unique 하지 않은 값이 등록될 수 없습니다.";
    }
}
