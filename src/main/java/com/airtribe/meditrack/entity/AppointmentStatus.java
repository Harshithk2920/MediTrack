package com.airtribe.meditrack.entity;

/**
 * Enum modeling appointment lifecycle states.
 */
public enum AppointmentStatus {
    PENDING("Pending Confirmation"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AppointmentStatus fromString(String text) {
        if (text == null) return PENDING;
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.name().equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        return PENDING;
    }
}
