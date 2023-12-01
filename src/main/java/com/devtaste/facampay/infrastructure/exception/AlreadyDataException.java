package com.devtaste.facampay.infrastructure.exception;

public class AlreadyDataException extends RuntimeException{
    public AlreadyDataException() {
        super("");
    }
    public AlreadyDataException(String message) {
        super(message);
    }
}