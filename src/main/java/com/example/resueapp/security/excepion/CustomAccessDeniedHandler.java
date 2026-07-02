package com.example.resueapp.security.excepion;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, 
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // 1. Set the correct HTTP status code to 403 Forbidden
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        // 2. Build a clear, helpful JSON string payload for the frontend
        String jsonPayload = String.format(
            "{\n" +
            "  \"status\": \"FORBIDDEN\",\n" +
            "  \"timestamp\": \"%s\",\n" +
            "  \"message\": \"Access Denied. Your account role does not have permission to view or modify this resource.\"\n" +
            "}", 
            LocalDateTime.now().toString()
        );

        // 3. Write out the message stream back to the client
        response.getWriter().write(jsonPayload);
    }
}