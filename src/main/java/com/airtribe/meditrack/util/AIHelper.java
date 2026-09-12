package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Smart AI Helper providing rule-based symptom recommendation and doctor matching.
 * Demonstrates Stream API filtering, rule matching logic, and slot auto-suggestion.
 */
public final class AIHelper {

    private AIHelper() {
        // Utility Class
    }

    /**
     * Infer target Specialization based on symptom keyword matching rules.
     *
     * @param symptomDescription Natural language symptom description
     * @return Recommended Doctor Specialization
     */
    public static Specialization analyzeSymptoms(String symptomDescription) {
        if (symptomDescription == null || symptomDescription.trim().isEmpty()) {
            return Specialization.GENERAL_PHYSICIAN;
        }

        String input = symptomDescription.toLowerCase();
        Map<Specialization, Integer> scoreMap = new HashMap<>();

        for (Specialization spec : Specialization.values()) {
            int score = 0;
            for (String keyword : spec.getKeywords()) {
                if (input.contains(keyword.toLowerCase())) {
                    score += 2;
                }
            }
            if (score > 0) {
                scoreMap.put(spec, score);
            }
        }

        if (scoreMap.isEmpty()) {
            return Specialization.GENERAL_PHYSICIAN;
        }

        // Return highest scoring specialization
        return scoreMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Specialization.GENERAL_PHYSICIAN);
    }

    /**
     * Filter available doctors matching symptom analysis and rank by consultation fee / availability.
     */
    public static List<Doctor> recommendDoctors(String symptoms, List<Doctor> allDoctors) {
        Specialization targetSpec = analyzeSymptoms(symptoms);
        
        List<Doctor> matched = allDoctors.stream()
                .filter(d -> d.getSpecialization() == targetSpec && d.isAvailable())
                .sorted(Comparator.comparingDouble(Doctor::getConsultationFee))
                .collect(Collectors.toList());

        // Fallback to General Physicians if no specialist is available
        if (matched.isEmpty()) {
            matched = allDoctors.stream()
                    .filter(d -> d.getSpecialization() == Specialization.GENERAL_PHYSICIAN && d.isAvailable())
                    .collect(Collectors.toList());
        }

        return matched;
    }

    /**
     * Auto-suggest next available appointment time slots for a given doctor.
     */
    public static List<LocalDateTime> suggestAppointmentSlots(Doctor doctor, int numberOfSlots) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime current = LocalDateTime.now().plusDays(1).with(LocalTime.of(9, 0)); // Tomorrow at 9 AM

        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(current.plusHours(i * 2L)); // Every 2 hours
        }
        return slots;
    }
}
