package com.airtribe.meditrack.exception;

/**
 * Exception thrown when input data validation fails.
 * Unchecked custom exception demonstrating custom exception hierarchy.
 */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
