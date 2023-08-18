package com.teamprogress.backend.rest;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.rest.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    //example:
    @ExceptionHandler(CookForMeException.class)
    public ResponseEntity<ErrorResponse> handleCookException(CookForMeException ex) {
        var errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.of(errorCode));
    }

}
