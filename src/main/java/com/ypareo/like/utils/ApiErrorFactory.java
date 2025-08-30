package com.ypareo.like.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ApiErrorFactory {

    public ResponseEntity<ApiError> buildApiError(Exception ex, HttpStatus status, HttpServletRequest request, List<ApiSubError> subErrors) {

        ApiError error = ApiError.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .subErrors(subErrors)
                .build();

        return ResponseEntity.status(status).body(error);
    }
}
