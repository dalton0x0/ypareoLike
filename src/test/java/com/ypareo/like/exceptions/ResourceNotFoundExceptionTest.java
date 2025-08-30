package com.ypareo.like.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundException_WithMessage() {

        String errorMessage = "User not found with id: 999";

        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testResourceNotFoundException_WithNullMessage() {

        String errorMessage = null;

        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
