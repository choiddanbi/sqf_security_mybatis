package com.study.SpringSecurity.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidException extends RuntimeException {

    @Getter
    private List<FieldError> fieldErrors;

    // 생성자
    public ValidException(String message, List<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
}
