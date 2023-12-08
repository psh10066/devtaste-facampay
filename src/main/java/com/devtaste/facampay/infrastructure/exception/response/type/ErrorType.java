package com.devtaste.facampay.infrastructure.exception.response.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    NOT_FOUND(HttpStatus.NOT_FOUND, -1, HttpStatus.NOT_FOUND.getReasonPhrase()),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, -2, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, -3, "잘못된 요청입니다."),

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, -701, "존재하지 않는 사용자입니다."),
    NOT_FOUND_PAYMENT(HttpStatus.BAD_REQUEST, -702, "존재하지 않는 결제 정보입니다."),
    NOT_FOUND_STORE_TO_USER(HttpStatus.BAD_REQUEST, -703, "가입되지 않은 사용자입니다."),
    EXIST_WAITING_PAYMENT(HttpStatus.BAD_REQUEST, -704, "해당 사용자에게 대기중인 결제 요청이 존재합니다."),
    SHORTAGE_OF_MONEY(HttpStatus.BAD_REQUEST, -705, "잔액이 부족합니다."),
    FINISHED_PAYMENT(HttpStatus.BAD_REQUEST, -706, "종료된 결제 요청입니다."),
    NOT_CANCELABLE_PAYMENT(HttpStatus.BAD_REQUEST, -707, "대기중인 결제만 취소할 수 있습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -9999, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

}
