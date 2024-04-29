package com.cloudcomputing.cloudcomputing.user.DTO;

import com.cloudcomputing.cloudcomputing.user.enums.Gender;
import com.cloudcomputing.cloudcomputing.user.enums.UserRole;



import java.sql.Date;

public record UserDTO(
         Long id,
         String name,
         Gender gender,
         Date dob,
         String email,
         String phone,
         UserRole role
) {
}
