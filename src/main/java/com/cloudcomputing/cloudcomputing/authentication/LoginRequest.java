package com.cloudcomputing.cloudcomputing.authentication;

public record LoginRequest(
        String userEmail,
        String password
) {
}
