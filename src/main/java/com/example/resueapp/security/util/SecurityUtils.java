package com.example.resueapp.security.util;

import com.example.resueapp.security.model.CustomUserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        return currentUser().getUserId();
    }

    public static CustomUserPrincipal currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
        return principal;
    }
}
