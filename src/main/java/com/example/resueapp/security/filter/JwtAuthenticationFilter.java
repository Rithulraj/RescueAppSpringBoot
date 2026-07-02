package com.example.resueapp.security.filter;

import com.example.resueapp.security.entity.UserRole;
import com.example.resueapp.security.model.CustomUserPrincipal;
import com.example.resueapp.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (!jwtUtils.isTokenExpired(token)) {
                    Long userId = jwtUtils.extractUserId(token);
                    String phone = jwtUtils.extractPhone(token);
                    UserRole role = UserRole.valueOf(jwtUtils.extractRole(token));
                    boolean firstLogin = jwtUtils.isFirstLogin(token);

                    CustomUserPrincipal principal =
                            new CustomUserPrincipal(
                                    userId,
                                    phone,
                                    role,
                                    firstLogin,
                                    null // Password not needed for already authenticated JWT
                            );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal, null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Invalid or malformed token — do not set authentication, let Spring Security handle the 401
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}