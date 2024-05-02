package com.cloudcomputing.cloudcomputing.product.DTO;

public record ProductDTO(
        Long id,
        String name,
        double price,
        int stock,
        String image,
        String business
    ) {

}
