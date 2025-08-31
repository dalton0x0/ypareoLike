package com.ypareo.like.exceptions;

import com.ypareo.like.utils.ApiError;
import com.ypareo.like.utils.ApiErrorFactory;
import com.ypareo.like.utils.ApiSubError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiErrorFactory apiErrorFactory;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return apiErrorFactory.buildApiError(ex, HttpStatus.NOT_FOUND, request, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return apiErrorFactory.buildApiError(ex, HttpStatus.BAD_REQUEST, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiSubError> subErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ApiSubError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();
        return apiErrorFactory.buildApiError(ex, HttpStatus.BAD_REQUEST, request, subErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        return apiErrorFactory.buildApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }
}
