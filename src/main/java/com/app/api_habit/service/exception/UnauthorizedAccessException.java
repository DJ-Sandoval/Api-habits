package com.app.api_habit.service.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String resourceName, Long resourceId) {
        super(String.format("No tienes permiso para acceder al %s con ID: %d",
                resourceName, resourceId));
    }
}
