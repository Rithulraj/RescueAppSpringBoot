package com.example.resueapp.rescue.exceptionhandler;

import com.example.resueapp.security.ApiResponse;
import com.example.resueapp.security.excepion.PhoneAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Setup a logger to record real system errors securely on the server
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 1. SAFE TO EXPOSE: Handles input validation failures.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        
        org.springframework.validation.BindingResult bindingResult = (ex instanceof MethodArgumentNotValidException) ?
                ((MethodArgumentNotValidException) ex).getBindingResult() : ((BindException) ex).getBindingResult();

        bindingResult.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", "VALIDATION_FAILED",
                        "timestamp", LocalDateTime.now().toString(),
                        "errors", errors
                ));
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>>
    handlePhoneAlreadyExists(
            PhoneAlreadyExistsException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                                .success(false)
                                .message(ex.getMessage())
                                .build()
                );
    }

    /**
     * 2. MUST HIDE: Catch-all for internal infrastructure crashes (S3, Postgres, NullPointers).
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleInternalSystemErrors(Exception ex) {
//        // 🌟 CRITICAL: Log the actual stack trace to your terminal/file system for debugging
//        logger.error("CRITICAL SYSTEM ERROR INTERCEPTED: ", ex);
//
//        // Return a completely generic, masked message to the client to hide system secrets
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Map.of(
//                        "status", "SERVER_ERROR",
//                        "timestamp", LocalDateTime.now().toString(),
//                        "message", "An unexpected error occurred on our end. Our engineering team has been notified."
//                ));
//    }
}