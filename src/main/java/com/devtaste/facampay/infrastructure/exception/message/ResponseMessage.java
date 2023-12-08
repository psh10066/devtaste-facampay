package com.devtaste.facampay.infrastructure.exception.message;

public class ResponseMessage {

    public static final String SUCCESS_MSG = "API Call successful";

    public static String fieldRequired(String fieldName) {
        return fieldName + " is required";
    }
}
