package com.devtaste.facampay.infrastructure.exception.handler;

import com.devtaste.facampay.infrastructure.exception.CustomException;
import com.devtaste.facampay.infrastructure.exception.message.ResponseMessage;
import com.devtaste.facampay.infrastructure.exception.response.ErrorResponse;
import com.devtaste.facampay.infrastructure.exception.response.type.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청
     * HttpStatus 400
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(CustomException e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(e.getErrorType());
    }

    /**
     * 존재하지 않는 경로
     * HttpStatus 404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> NoResourceFoundException(NoResourceFoundException e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.NOT_FOUND);
    }

    /**
     * 허용되지 않는 방법
     * HttpStatus 405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.METHOD_NOT_ALLOWED);
    }

    /**
     * Validation 실패 (@RequestBody Json Parsing 실패)
     * HttpStatus 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(Exception e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.BAD_REQUEST, e.getMessage());
    }

    /**
     * Entity 조회 실패
     * HttpStatus 400
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundDataException(EntityNotFoundException e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.BAD_REQUEST, e.getMessage());
    }

    /**
     * Validation 실패 (@RequestBody, @ModelAttribute @Valid 유효성 검사 실패)
     * HttpStatus 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("error : ", e);
        // @Valid 어노테이션의 message 속성으로 return
        return ErrorResponse.toEntity(ErrorType.BAD_REQUEST, e.getFieldError().getDefaultMessage());
    }

    /**
     * Validation 실패 (@RequestParam(required = true)일 때 필드 누락)
     * HttpStatus 400
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.BAD_REQUEST, ResponseMessage.fieldRequired(e.getParameterName()));
    }

    /**
     * Validation 실패 (@RequestPart(required = true)일 때 필드 누락)
     * HttpStatus 400
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestPartException(MissingServletRequestPartException e) {
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.BAD_REQUEST, ResponseMessage.fieldRequired(e.getRequestPartName()));
    }

    /**
     * Validation 실패 (@Validated가 존재하는 Controller의 @RequestParam, @RequestPart @Valid 유효성 검사 실패)
     * HttpStatus 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException e) {
        log.info("error : ", e);
        // @Valid 어노테이션의 message 속성으로 return
        return ErrorResponse.toEntity(ErrorType.BAD_REQUEST, e.getConstraintViolations().iterator().next().getMessage());
    }

    /**
     * 알 수 없는 오류(내부 서버 오류)
     * httpStatus 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("error : " + e);
        log.info("error : ", e);
        return ErrorResponse.toEntity(ErrorType.INTERNAL_SERVER_ERROR);
    }
}
