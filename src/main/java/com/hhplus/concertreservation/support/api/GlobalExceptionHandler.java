package com.hhplus.concertreservation.support.api;

import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.support.domain.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(CoreException e) {

        // 에러 로그 레벨에 따라 로그를 남긴다.
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error(e.getMessage(), e);
            case WARN -> log.warn(e.getMessage(), e);
            default -> log.info(e.getMessage(), e);
        }

        HttpStatus httpStatus = switch (e.getErrorType().getCode()) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case VALIDATION_ERROR, CLIENT_ERROR  -> HttpStatus.BAD_REQUEST;
            case BUSINESS_ERROR -> HttpStatus.OK;
            case AUTHENTICATION_ERROR -> HttpStatus.UNAUTHORIZED;
            case AUTHORIZATION_ERROR -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error(e.getMessage(), e);

        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("500", e.getMessage()));
    }
}
