package com.cloudcomputing.cloudcomputing.ExceptionHandler;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
