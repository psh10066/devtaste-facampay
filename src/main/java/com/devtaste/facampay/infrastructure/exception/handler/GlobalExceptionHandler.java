package com.devtaste.facampay.infrastructure.exception.handler;

import com.devtaste.facampay.infrastructure.exception.AlreadyDataException;
import com.devtaste.facampay.infrastructure.exception.BadRequestApiException;
import com.devtaste.facampay.infrastructure.exception.UnauthorizedException;
import com.devtaste.facampay.infrastructure.exception.message.ResponseMessage;
import com.devtaste.facampay.infrastructure.exception.response.ErrorResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청
     * HttpStatus 400
     */
    @ExceptionHandler(BadRequestApiException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse BadRequestApiException(HttpServletRequest request, BadRequestApiException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    /**
     * 인증 실패
     * HttpStatus 401
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse UnauthorizedException(HttpServletRequest request, UnauthorizedException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    /**
     * 허용되지 않는 방법(Request Method - GET, POST, PUT, DELETE)
     * HttpStatus 405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse HttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    /**
     * Input Request 실패(HttpMessageNotReadable)
     * HttpStatus 405
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse HttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, ResponseMessage.HTTP_MESSAGE_NOT_READABLE_MSG.replace("{msg}", Objects.requireNonNull(e.getMessage())));
    }

    /**
     * Validation 실패 (RequestBody)
     * HttpStatus 417
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse MethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, e.getFieldError().getDefaultMessage());
    }

    /**
     * Validation 실패 (RequestParam)
     * HttpStatus 417
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse MissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ResponseMessage.EXPECTATION_FAILED_MSG.replace("{FieldName}", e.getParameterName()));
    }

    /**
     * Validation 실패 (multipart/form-data)
     * HttpStatus 417
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse MissingServletRequestPartException(HttpServletRequest request, MissingServletRequestPartException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ResponseMessage.EXPECTATION_FAILED_MSG.replace("{FieldName}", e.getRequestPartName()));
    }

    /**
     * Validation 실패 (ModelAttribute)
     * HttpStatus 417
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse BindException(HttpServletRequest request, BindException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ResponseMessage.EXPECTATION_FAILED_MSG.replace("{FieldName}", e.getFieldError().getField()));
    }

    /**
     * Validation 실패 (formValidation)
     * HttpStatus 417
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse ValidationException(HttpServletRequest request, ValidationException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, e.getMessage());
    }

    /**
     * Validation 실패
     * HttpStatus 417
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse ConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, e.getMessage());
    }

    /**
     * 처리할 수 없는 엔티티 (이미 존재하는 데이터로 인해 처리 불가, 저장/수정/삭제 실패)
     * HttpStatus 422
     */
    @ExceptionHandler(AlreadyDataException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse AlreadyDataException(HttpServletRequest request, AlreadyDataException e) {
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : ResponseMessage.ALREADY_DATA_MSG);
    }

    /**
     * 알수 없는 오류(내부 서버 오류)
     * httpStatus 500
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse Exception(HttpServletRequest request, Exception e) {
        log.error("error : " + e);
        log.info("error : ", e);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR_MSG);
    }
}
