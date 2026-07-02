package com.example.resueapp.security.excepion;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String phone) {
        super("Phone number already exists: " + phone);
    }
}