package com.cloudcomputing.cloudcomputing.user.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)

public enum UserRole {
    ROLE_CUSTOMER, ROLE_BUSINESS, ROLE_ADMIN;
}
