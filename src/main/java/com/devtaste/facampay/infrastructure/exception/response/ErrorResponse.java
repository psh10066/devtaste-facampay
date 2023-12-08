package com.devtaste.facampay.infrastructure.exception.response;

import com.devtaste.facampay.infrastructure.exception.response.type.ErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int rt;
    private String rtMsg;

    public static ResponseEntity<ErrorResponse> toEntity(ErrorType errorType) {
        return ResponseEntity
            .status(errorType.getHttpStatus())
            .body(new ErrorResponse(errorType.getCode(), errorType.getMessage()));
    }

    public static ResponseEntity<ErrorResponse> toEntity(ErrorType errorType, String rtMsg) {
        return ResponseEntity
            .status(errorType.getHttpStatus())
            .body(new ErrorResponse(errorType.getCode(), rtMsg));
    }
}
