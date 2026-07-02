package com.example.resueapp.security.controller;

import com.example.resueapp.security.ApiResponse;
import com.example.resueapp.security.dto.CreateAdminRequest;
import com.example.resueapp.security.dto.CreateAdminResponse;
import com.example.resueapp.security.dto.CreateRescuerRequest;
import com.example.resueapp.security.dto.CreateRescuerResponse;
import com.example.resueapp.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/rescuers")
    public ResponseEntity<ApiResponse<CreateRescuerResponse>>
    createRescuer(
            @RequestBody @Valid CreateRescuerRequest request
    ) {
        CreateRescuerResponse response = userService.createRescuer(request);
        return ResponseEntity.ok(
                ApiResponse.<CreateRescuerResponse>builder()
                        .success(true)
                        .message("Rescuer created successfully")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/admins")
    public ResponseEntity<ApiResponse<CreateAdminResponse>>
    createAdmin(
            @RequestBody @Valid CreateAdminRequest request
    ) {
        CreateAdminResponse response = userService.createAdmin(request);
        return ResponseEntity.ok(
                ApiResponse.<CreateAdminResponse>builder()
                        .success(true)
                        .message("Admin created successfully")
                        .data(response)
                        .build()
        );
    }
}