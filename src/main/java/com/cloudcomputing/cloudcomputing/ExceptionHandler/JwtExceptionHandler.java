package com.cloudcomputing.cloudcomputing.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(NotFoundException e){
        int statusCode = HttpStatus.FORBIDDEN.value();
        ErrorResponse response = new ErrorResponse(e.getMessage(), statusCode);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
