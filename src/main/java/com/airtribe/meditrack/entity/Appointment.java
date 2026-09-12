package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.Validator;
import java.time.LocalDateTime;

/**
 * Entity modeling an Appointment between a Patient and a Doctor.
 * Extends MedicalEntity and implements Cloneable for Deep Copying.
 */
public class Appointment extends MedicalEntity implements Cloneable {

    private static final long serialVersionUID = 1L;

    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;

    public Appointment(String appointmentId, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String notes) {
        super(appointmentId);
        Validator.validateNonEmptyString(appointmentId, "Appointment ID");
        Validator.validateNotNull(patient, "Patient");
        Validator.validateNotNull(doctor, "Doctor");
        Validator.validateNotNull(appointmentDateTime, "Appointment Date/Time");
        
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.CONFIRMED;
        this.notes = notes != null ? notes : "";
    }

    public String getAppointmentId() {
        return getId();
    }

    public void setAppointmentId(String appointmentId) {
        setId(appointmentId);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        Validator.validateNotNull(patient, "Patient");
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        Validator.validateNotNull(doctor, "Doctor");
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String getDetails() {
        return toString();
    }

    /**
     * Deep Copy implementation for Appointment.
     * Clones nested Patient object so that clone mutations are fully isolated.
     */
    @Override
    public Appointment clone() {
        Appointment cloned = (Appointment) super.clone();
        if (this.patient != null) {
            cloned.patient = this.patient.clone();
        }
        return cloned;
    }

    @Override
    public String toString() {
        return String.format("Appointment [ID: %s, Patient: %s (%s), Doctor: Dr. %s (%s), Time: %s, Status: %s, Notes: %s]",
                getId(), patient.getName(), patient.getId(), doctor.getName(), doctor.getId(),
                DateUtil.format(appointmentDateTime), status, notes);
    }
}
