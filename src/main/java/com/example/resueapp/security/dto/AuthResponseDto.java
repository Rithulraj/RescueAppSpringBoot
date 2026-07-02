package com.example.resueapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDto {
    private String token; // The JWT string
    private String phone;
    private String role;
    private boolean firstLogin;
}