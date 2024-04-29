package com.cloudcomputing.cloudcomputing.ExceptionHandler;

public class ProductStockException extends RuntimeException{
    public ProductStockException(String message){
        super(message);
    }
}
