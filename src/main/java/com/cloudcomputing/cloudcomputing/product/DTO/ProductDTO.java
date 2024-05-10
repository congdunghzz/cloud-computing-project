package com.cloudcomputing.cloudcomputing.product.DTO;

public record ProductDTO(
        Long id,
        String name,
        double price,
        String image,
        String business
    ) {

}
