package com.example.resueapp.security.service;

import com.example.resueapp.security.dto.AuthResponseDto;
import com.example.resueapp.security.dto.ChangePasswordRequest;
import com.example.resueapp.security.dto.LoginRequestDto;
import com.example.resueapp.security.entity.User;
import com.example.resueapp.security.model.CustomUserPrincipal;
import com.example.resueapp.security.excepion.InvalidPasswordException;
import com.example.resueapp.security.repository.UserRepository;
import com.example.resueapp.security.util.JwtUtils;
import com.example.resueapp.security.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.resueapp.security.util.SecurityUtils.currentUser;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Automatically provided by Spring Security

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponseDto signIn(LoginRequestDto request) {
        // 1. Verify credentials and get authenticated user
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()
                )
        );

        // 2. Extract the user principal from the authentication result
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        // 3. Generate a secure token
        String token = jwtUtils.generateToken(principal);

        // 4. Return the complete authentication payload
        return AuthResponseDto.builder()
                .token(token)
                .phone(principal.getPhone())
                .role(principal.getRole().name())
                .firstLogin(principal.isFirstLogin())
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + currentUser().getPhone()));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);
    }
}

