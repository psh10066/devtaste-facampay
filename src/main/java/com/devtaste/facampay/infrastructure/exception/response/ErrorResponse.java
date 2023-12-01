package com.devtaste.facampay.infrastructure.exception.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int rt;
    private String rtMsg;

    public static ErrorResponse of(HttpStatus httpStatus, String rtMsg) {
        return new ErrorResponse(httpStatus.value(), rtMsg);
    }
}
