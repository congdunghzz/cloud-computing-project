package com.cloudcomputing.cloudcomputing.ExceptionHandler;

public record ErrorResponse(
        String message,
        int statusCode
) {
}
