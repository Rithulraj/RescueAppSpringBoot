package com.example.resueapp.security.filter;

import com.example.resueapp.security.model.CustomUserPrincipal;
import com.example.resueapp.security.entity.UserRole;
import org.springframework.security.core.Authentication;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class FirstLoginFilter extends GenericFilter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. Check if user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            
            String uri = httpRequest.getRequestURI();
            if (uri.equals("/api/v1/auth/change-password")) {
                chain.doFilter(request, response);
                return;
            }

            if (principal.getRole() == UserRole.RESCUER && principal.isFirstLogin()) {
                // Block the request right here and return a 403 Forbidden status
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"status\": \"ACCESS_DENIED\", \"message\": \"Reset your password.\"}");
                return; // Breaks the chain, stopping execution instantly
            }
        }

        // If they are an Admin OR an approved Rescuer, let them pass through!
        chain.doFilter(request, response);
    }
}