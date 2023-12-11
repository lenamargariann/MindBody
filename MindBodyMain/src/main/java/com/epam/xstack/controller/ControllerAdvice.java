package com.epam.xstack.controller;

import com.epam.xstack.service.impl.AppMetricsService;
import com.epam.xstack.service.impl.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final AppMetricsService appMetricsService;

    private final LoginAttemptService loginAttemptService;

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        appMetricsService.addErrorCount();
        log.error("ResponseStatusException occurred: {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.toString(e.getReason(), "")));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e) {
        appMetricsService.addErrorCount();
        log.error("Internal server error: {}", e.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        appMetricsService.addErrorCount();
        return ResponseEntity.badRequest().body(Map.of("fieldError", "'" + Objects.requireNonNull(ex.getFieldError()).getRejectedValue() + "' is not valid value for " + ex.getFieldError().getField() + " field."));

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleAccessDeniedException(BadCredentialsException e, HttpServletRequest request) {
        loginAttemptService.loginFailed(request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message: %s".formatted(e.getMessage()));
    }

}
