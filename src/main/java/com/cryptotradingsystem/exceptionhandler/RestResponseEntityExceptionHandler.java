package com.cryptotradingsystem.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleIllegalArgumentExceptionAndIllegalStateException(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(400)
                .header("error_message", ex.getMessage())
                .build();
    }
}
