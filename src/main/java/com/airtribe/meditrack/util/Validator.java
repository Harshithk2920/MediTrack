package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;
import java.util.regex.Pattern;

/**
 * Centralized Validation Utility class for MediTrack.
 * Demonstrates static utility methods, regex validation, and custom exception throwing.
 */
public final class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

    private Validator() {
        // Prevent instantiation
    }

    public static void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new InvalidDataException(fieldName + " cannot be null.");
        }
    }

    public static void validateNonEmptyString(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " cannot be empty or blank.");
        }
    }

    public static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidDataException("Invalid age: " + age + ". Age must be between 0 and 150.");
        }
    }

    public static void validateEmail(String email) {
        validateNonEmptyString(email, "Email");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidDataException("Invalid email format: " + email);
        }
    }

    public static void validatePhone(String phone) {
        validateNonEmptyString(phone, "Phone number");
        String sanitized = phone.replaceAll("[\\s\\-\\(\\)]", "");
        if (!PHONE_PATTERN.matcher(sanitized).matches()) {
            throw new InvalidDataException("Invalid phone number format: " + phone);
        }
    }

    public static void validatePositiveAmount(double amount, String fieldName) {
        if (amount < 0.0) {
            throw new InvalidDataException(fieldName + " cannot be negative: " + amount);
        }
    }
}
