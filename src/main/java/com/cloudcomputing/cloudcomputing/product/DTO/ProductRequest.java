package com.cloudcomputing.cloudcomputing.product.DTO;

import org.springframework.web.multipart.MultipartFile;

public record ProductRequest(
        Long id,
        String name,
        double price,
        int stock,
        MultipartFile image
){

}