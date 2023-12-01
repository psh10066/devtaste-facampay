package com.devtaste.facampay.infrastructure.exception;

public class BadRequestApiException extends RuntimeException {
    public BadRequestApiException(String message) {
        super(message);
    }
}
