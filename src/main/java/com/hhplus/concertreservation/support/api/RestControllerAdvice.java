package com.hhplus.concertreservation.support.api;

import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.support.domain.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(CoreException e) {
        return ResponseEntity.status(400).body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse("500", e.getMessage()));
    }
}
