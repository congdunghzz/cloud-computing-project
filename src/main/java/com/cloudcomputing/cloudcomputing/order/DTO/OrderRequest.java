package com.cloudcomputing.cloudcomputing.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String name;
    private List<OrderDetailRequestDTO> orderDetailRequestDTOs;
}
