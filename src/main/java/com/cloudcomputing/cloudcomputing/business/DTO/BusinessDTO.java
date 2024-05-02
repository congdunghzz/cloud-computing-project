package com.cloudcomputing.cloudcomputing.business.DTO;


import java.sql.Date;

public record BusinessDTO(
         Long id,
         String name,
         Date dob,
         String email,
         String phone
) {
}
