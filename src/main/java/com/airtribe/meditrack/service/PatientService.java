package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.DataStore;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service managing Patient records.
 * Demonstrates Method Overloading Polymorphism for search functionality and Stream filtering.
 */
public class PatientService {

    private final DataStore<Patient> patientStore;

    public PatientService(DataStore<Patient> patientStore) {
        this.patientStore = patientStore;
    }

    public void registerPatient(Patient patient) {
        patientStore.add(patient);
    }

    public Optional<Patient> getPatientById(String id) {
        return patientStore.getById(id);
    }

    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    public boolean removePatient(String id) {
        return patientStore.remove(id);
    }

    // --- METHOD OVERLOADING DEMONSTRATION FOR POLYMORPHISM ---

    /**
     * Search overloading #1: Search patient by exact Patient ID.
     */
    public Optional<Patient> searchPatient(String id) {
        return patientStore.getById(id);
    }

    /**
     * Search overloading #2: Search patients by name (partial or exact).
     */
    public List<Patient> searchPatient(String name, boolean exactMatch) {
        return patientStore.stream()
                .filter(p -> exactMatch ? p.getName().equalsIgnoreCase(name)
                        : p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search overloading #3: Search patients by age range.
     */
    public List<Patient> searchPatient(int minAge, int maxAge) {
        return patientStore.stream()
                .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    /**
     * Search patients by blood group.
     */
    public List<Patient> searchByBloodGroup(String bloodGroup) {
        return patientStore.stream()
                .filter(p -> p.getBloodGroup().equalsIgnoreCase(bloodGroup))
                .collect(Collectors.toList());
    }
}
