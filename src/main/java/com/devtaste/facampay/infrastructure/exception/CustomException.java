package com.devtaste.facampay.infrastructure.exception;

import com.devtaste.facampay.infrastructure.exception.response.type.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
