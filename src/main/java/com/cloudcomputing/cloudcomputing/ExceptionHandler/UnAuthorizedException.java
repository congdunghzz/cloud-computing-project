package com.cloudcomputing.cloudcomputing.ExceptionHandler;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message){
        super(message);
    }
}
