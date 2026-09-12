package com.airtribe.meditrack.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Enum representing medical specializations.
 * Demonstrates Enums with custom fields, methods, and rule matching.
 */
public enum Specialization {

    GENERAL_PHYSICIAN("General Physician", Arrays.asList("fever", "cough", "cold", "flu", "fatigue", "weakness")),
    CARDIOLOGY("Cardiology", Arrays.asList("chest pain", "heart", "palpitations", "shortness of breath", "bp", "blood pressure")),
    DERMATOLOGY("Dermatology", Arrays.asList("skin", "rash", "acne", "itching", "allergy", "hair loss")),
    PEDIATRICS("Pediatrics", Arrays.asList("child", "infant", "baby", "pediatric")),
    NEUROLOGY("Neurology", Arrays.asList("headache", "migraine", "dizziness", "seizure", "numbness", "brain")),
    ORTHOPEDICS("Orthopedics", Arrays.asList("bone", "joint", "fracture", "back pain", "knee pain", "arthritis"));

    private final String displayName;
    private final List<String> keywords;

    Specialization(String displayName, List<String> keywords) {
        this.displayName = displayName;
        this.keywords = keywords;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Map string name to Specialization enum safely.
     */
    public static Specialization fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return GENERAL_PHYSICIAN;
        }
        for (Specialization spec : Specialization.values()) {
            if (spec.name().equalsIgnoreCase(text.trim()) || spec.displayName.equalsIgnoreCase(text.trim())) {
                return spec;
            }
        }
        return GENERAL_PHYSICIAN;
    }
}
