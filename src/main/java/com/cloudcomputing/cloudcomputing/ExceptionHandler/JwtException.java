package com.cloudcomputing.cloudcomputing.ExceptionHandler;

public class JwtException extends RuntimeException{

    public JwtException(String message){
        super(message);
    }
}
