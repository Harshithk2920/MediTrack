package com.airtribe.meditrack.exception;

/**
 * Custom checked exception thrown when an appointment lookup fails.
 * Demonstrates exception chaining.
 */
public class AppointmentNotFoundException extends Exception {

    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
