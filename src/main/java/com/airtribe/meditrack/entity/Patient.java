package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Patient entity extending Person and implementing Cloneable and Searchable.
 * Demonstrates Deep Copy Cloning semantics, Encapsulation, and Polymorphism.
 */
public class Patient extends Person implements Searchable<Patient> {

    private static final long serialVersionUID = 1L;

    private String bloodGroup;
    private List<String> medicalHistory;

    public Patient(String id, String name, int age, String gender, String contactNumber, String email,
                   String bloodGroup, List<String> medicalHistory) {
        super(id, name, age, gender, contactNumber, email);
        this.bloodGroup = bloodGroup != null ? bloodGroup : "Unknown";
        this.medicalHistory = medicalHistory != null ? new ArrayList<>(medicalHistory) : new ArrayList<>();
    }

    public Patient(String id, String name, int age, String contactNumber, String bloodGroup) {
        super(id, name, age, contactNumber);
        this.bloodGroup = bloodGroup;
        this.medicalHistory = new ArrayList<>();
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public List<String> getMedicalHistory() {
        return Collections.unmodifiableList(medicalHistory);
    }

    public void addMedicalRecord(String record) {
        if (record != null && !record.trim().isEmpty()) {
            this.medicalHistory.add(record.trim());
        }
    }

    /**
     * Demonstrates Deep Copy semantics for Cloneable.
     * Deeply clones the medicalHistory ArrayList to ensure zero reference leakage.
     */
    @Override
    public Patient clone() {
        Patient cloned = (Patient) super.clone();
        // Deep copy of mutable list object
        cloned.medicalHistory = new ArrayList<>(this.medicalHistory);
        return cloned;
    }

    @Override
    public String getDetails() {
        return String.format("Patient [ID: %s, Name: %s, Age: %d, Gender: %s, BloodGroup: %s, Contact: %s, History Count: %d]",
                getId(), getName(), getAge(), getGender(), bloodGroup, getContactNumber(), medicalHistory.size());
    }

    @Override
    public boolean matchesQuery(String query) {
        if (query == null || query.trim().isEmpty()) return false;
        String q = query.toLowerCase().trim();
        return getId().toLowerCase().contains(q) ||
               getName().toLowerCase().contains(q) ||
               bloodGroup.toLowerCase().contains(q) ||
               String.valueOf(getAge()).equalsIgnoreCase(q);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
