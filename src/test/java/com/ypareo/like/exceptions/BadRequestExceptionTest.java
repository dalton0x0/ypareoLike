package com.ypareo.like.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testBadRequestException_WithMessage() {

        String errorMessage = "Invalid input data";

        BadRequestException exception = new BadRequestException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testBadRequestException_WithEmptyMessage() {

        String errorMessage = "";

        BadRequestException exception = new BadRequestException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}
