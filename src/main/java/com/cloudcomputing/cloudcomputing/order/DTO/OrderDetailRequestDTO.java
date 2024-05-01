package com.cloudcomputing.cloudcomputing.order.DTO;

public record OrderDetailRequestDTO(
         Long productId,
         int quantity
) {
}
