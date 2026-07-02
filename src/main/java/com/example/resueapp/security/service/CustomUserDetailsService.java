package com.example.resueapp.security.service;

import com.example.resueapp.security.entity.User;
import com.example.resueapp.security.model.CustomUserPrincipal;
import com.example.resueapp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));

        return new CustomUserPrincipal(
                user.getId(),
                user.getPhone(),
                user.getRole(),
                user.isFirstLogin(),
                user.getPassword()
        );
    }
}
