package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.util.DataStore;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service managing Doctor operations and analytical queries using Java 8 Streams.
 */
public class DoctorService {

    private final DataStore<Doctor> doctorStore;

    public DoctorService(DataStore<Doctor> doctorStore) {
        this.doctorStore = doctorStore;
    }

    public void addDoctor(Doctor doctor) {
        doctorStore.add(doctor);
    }

    public Optional<Doctor> getDoctorById(String id) {
        return doctorStore.getById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    public boolean removeDoctor(String id) {
        return doctorStore.remove(id);
    }

    // --- STREAM ANALYTICAL QUERIES ---

    public List<Doctor> getDoctorsBySpecialization(Specialization specialization) {
        return doctorStore.stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorStore.stream()
                .filter(Doctor::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Doctor> getDoctorsSortedByFee(boolean ascending) {
        Comparator<Doctor> feeComparator = Comparator.comparingDouble(Doctor::getConsultationFee);
        if (!ascending) {
            feeComparator = feeComparator.reversed();
        }
        return doctorStore.getSorted(feeComparator);
    }
}
