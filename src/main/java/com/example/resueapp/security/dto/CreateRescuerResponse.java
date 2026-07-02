package com.example.resueapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRescuerResponse {

    private Long id;

    private String name;

    private String phone;

    private String temporaryPassword;
}