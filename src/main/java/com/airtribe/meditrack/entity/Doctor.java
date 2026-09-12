package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.Validator;

/**
 * Doctor entity extending Person and implementing Searchable interface.
 * Demonstrates Inheritance, Polymorphism (Method Overriding), and Encapsulation.
 */
public class Doctor extends Person implements Searchable<Doctor> {

    private static final long serialVersionUID = 1L;

    private Specialization specialization;
    private double consultationFee;
    private boolean available;

    public Doctor(String id, String name, int age, String gender, String contactNumber, String email,
                  Specialization specialization, double consultationFee, boolean available) {
        super(id, name, age, gender, contactNumber, email);
        this.specialization = specialization != null ? specialization : Specialization.GENERAL_PHYSICIAN;
        setConsultationFee(consultationFee);
        this.available = available;
    }

    public Doctor(String id, String name, Specialization specialization, double consultationFee) {
        super(id, name, 35, "9999999999");
        this.specialization = specialization;
        setConsultationFee(consultationFee);
        this.available = true;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        Validator.validatePositiveAmount(consultationFee, "Consultation Fee");
        this.consultationFee = consultationFee;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getDetails() {
        return String.format("Doctor [ID: %s, Name: Dr. %s, Spec: %s, Fee: $%.2f, Available: %s, Contact: %s]",
                getId(), getName(), specialization.getDisplayName(), consultationFee, available ? "YES" : "NO", getContactNumber());
    }

    @Override
    public boolean matchesQuery(String query) {
        if (query == null || query.trim().isEmpty()) return false;
        String q = query.toLowerCase().trim();
        return getId().toLowerCase().contains(q) ||
               getName().toLowerCase().contains(q) ||
               specialization.name().toLowerCase().contains(q) ||
               specialization.getDisplayName().toLowerCase().contains(q);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
