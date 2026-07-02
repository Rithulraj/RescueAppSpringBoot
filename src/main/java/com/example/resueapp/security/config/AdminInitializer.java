package com.example.resueapp.security.config;

import com.example.resueapp.security.entity.User;
import com.example.resueapp.security.entity.UserRole;
import com.example.resueapp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByRole(UserRole.SUPER_ADMIN)) {
            log.info("System Admin already exists.");
            return;
        }

        User admin = User.builder()
                .name("System Admin")
                .phone("9999999999")
                .password(passwordEncoder.encode("Admin@123"))
                .role(UserRole.SUPER_ADMIN)
                .firstLogin(false)
                .build();

        userRepository.save(admin);
        log.info("System Admin created successfully with phone 9999999999");
    }
}