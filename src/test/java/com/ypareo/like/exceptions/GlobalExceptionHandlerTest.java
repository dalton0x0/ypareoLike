package com.ypareo.like.exceptions;

import com.ypareo.like.utils.ApiError;
import com.ypareo.like.utils.ApiErrorFactory;
import com.ypareo.like.utils.ApiSubError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private ApiErrorFactory apiErrorFactory;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleResourceNotFoundException() {

        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");
        ApiError apiError = ApiError.builder()
                .status(404)
                .error("Not Found")
                .message("User not found")
                .path("/api/users/999")
                .build();

        ResponseEntity<ApiError> expectedResponse = ResponseEntity.status(404).body(apiError);

        when(apiErrorFactory.buildApiError(ex, HttpStatus.NOT_FOUND, request, null))
                .thenReturn(expectedResponse);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleResourceNotFoundException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getMessage());

        verify(apiErrorFactory, times(1)).buildApiError(ex, HttpStatus.NOT_FOUND, request, null);
    }

    @Test
    void testHandleBadRequestException() {

        BadRequestException ex = new BadRequestException("Invalid input");
        ApiError apiError = ApiError.builder()
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .build();

        ResponseEntity<ApiError> expectedResponse = ResponseEntity.status(400).body(apiError);

        when(apiErrorFactory.buildApiError(ex, HttpStatus.BAD_REQUEST, request, null))
                .thenReturn(expectedResponse);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleBadRequestException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().getMessage());

        verify(apiErrorFactory, times(1)).buildApiError(ex, HttpStatus.BAD_REQUEST, request, null);
    }

    @Test
    void testHandleMethodArgumentNotValidException_WithExactSubErrors() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = List.of(
                new FieldError("user", "name", "Name is required"),
                new FieldError("user", "email", "Email is invalid")
        );

        List<ApiSubError> expectedSubErrors = List.of(
                ApiSubError.builder().field("name").message("Name is required").build(),
                ApiSubError.builder().field("email").message("Email is invalid").build()
        );

        ApiError apiError = ApiError.builder()
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .subErrors(expectedSubErrors)
                .build();

        ResponseEntity<ApiError> expectedResponse = ResponseEntity.status(400).body(apiError);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        when(apiErrorFactory.buildApiError(
                eq(ex),
                eq(HttpStatus.BAD_REQUEST),
                eq(request),
                argThat(subErrors ->
                        subErrors != null &&
                                subErrors.size() == 2 &&
                                subErrors.getFirst().getField().equals("name") &&
                                subErrors.get(1).getField().equals("email")
                )
        )).thenReturn(expectedResponse);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getSubErrors());
        assertEquals(2, response.getBody().getSubErrors().size());
        assertEquals("name", response.getBody().getSubErrors().get(0).getField());
        assertEquals("email", response.getBody().getSubErrors().get(1).getField());
    }

    @Test
    void testHandleRuntimeException() {

        RuntimeException ex = new RuntimeException("Unexpected error");
        ApiError apiError = ApiError.builder()
                .status(500)
                .error("Internal Server Error")
                .message("Unexpected error")
                .build();

        ResponseEntity<ApiError> expectedResponse = ResponseEntity.status(500).body(apiError);

        when(apiErrorFactory.buildApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, null))
                .thenReturn(expectedResponse);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleRuntimeException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error", response.getBody().getMessage());

        verify(apiErrorFactory, times(1)).buildApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }
}
