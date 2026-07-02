package com.example.resueapp.security.service;

import com.example.resueapp.security.dto.CreateAdminRequest;
import com.example.resueapp.security.dto.CreateAdminResponse;
import com.example.resueapp.security.dto.CreateRescuerRequest;
import com.example.resueapp.security.dto.CreateRescuerResponse;
import com.example.resueapp.security.entity.User;
import com.example.resueapp.security.entity.UserRole;
import com.example.resueapp.security.excepion.PhoneAlreadyExistsException;
import com.example.resueapp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateRescuerResponse createRescuer(
            CreateRescuerRequest request
    ) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }
        String tempPassword = generateTempPassword();

        User user = User.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(tempPassword))
                .role(UserRole.RESCUER)
                .firstLogin(true)
                .build();

        User savedUser = userRepository.save(user);

        return new CreateRescuerResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getPhone(),
                tempPassword
        );
    }

    public CreateAdminResponse createAdmin(
            CreateAdminRequest request
    ) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }

        String tempPassword = generateTempPassword();

        User admin = User.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(tempPassword))
                .role(UserRole.ADMIN)
                .firstLogin(true)
                .build();

        User savedAdmin = userRepository.save(admin);

        return new CreateAdminResponse(
                savedAdmin.getId(),
                savedAdmin.getName(),
                savedAdmin.getPhone(),
                tempPassword
        );
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}