package com.ypareo.like.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiErrorFactoryTest {

    @InjectMocks
    private ApiErrorFactory apiErrorFactory;

    @Test
    void testBuildApiError_WithoutSubErrors() {

        Exception ex = new RuntimeException("Test error message");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ApiError> response = apiErrorFactory.buildApiError(ex, status, request, null);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiError apiError = response.getBody();
        assertNotNull(apiError);
        assertEquals(400, apiError.getStatus());
        assertEquals("Bad Request", apiError.getError());
        assertEquals("Test error message", apiError.getMessage());
        assertEquals("/api/test", apiError.getPath());
        assertNotNull(apiError.getTimestamp());
        assertNull(apiError.getSubErrors());
    }

    @Test
    void testBuildApiError_WithSubErrors() {

        Exception ex = new RuntimeException("Validation failed");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpServletRequest request = mock(HttpServletRequest.class);

        List<ApiSubError> subErrors = List.of(
                ApiSubError.builder().field("name").message("Name is required").build(),
                ApiSubError.builder().field("email").message("Email is invalid").build()
        );

        when(request.getRequestURI()).thenReturn("/api/users");

        ResponseEntity<ApiError> response = apiErrorFactory.buildApiError(ex, status, request, subErrors);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiError apiError = response.getBody();
        assertNotNull(apiError);
        assertEquals(400, apiError.getStatus());
        assertEquals("Validation failed", apiError.getMessage());
        assertEquals("/api/users", apiError.getPath());
        assertNotNull(apiError.getTimestamp());
        assertNotNull(apiError.getSubErrors());
        assertEquals(2, apiError.getSubErrors().size());
        assertEquals("name", apiError.getSubErrors().get(0).getField());
        assertEquals("Name is required", apiError.getSubErrors().get(0).getMessage());
    }

    @Test
    void testBuildApiError_WithDifferentHttpStatus() {

        Exception ex = new RuntimeException("Not found");
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/users/999");

        ResponseEntity<ApiError> response = apiErrorFactory.buildApiError(ex, status, request, null);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ApiError apiError = response.getBody();
        assertNotNull(apiError);
        assertEquals(404, apiError.getStatus());
        assertEquals("Not Found", apiError.getError());
    }
}
