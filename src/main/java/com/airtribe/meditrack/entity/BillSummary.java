package com.airtribe.meditrack.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Immutable class representing a read-only snapshot of a Bill.
 * Demonstrates Immutability design rules: final class, final fields, no setters, thread-safe.
 */
public final class BillSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String billId;
    private final String appointmentId;
    private final String patientName;
    private final String doctorName;
    private final double baseAmount;
    private final double taxAmount;
    private final double totalAmount;
    private final boolean paid;
    private final String strategyName;
    private final LocalDateTime issuedAt;

    public BillSummary(String billId, String appointmentId, String patientName, String doctorName,
                       double baseAmount, double taxAmount, double totalAmount, boolean paid,
                       String strategyName, LocalDateTime issuedAt) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.baseAmount = baseAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paid = paid;
        this.strategyName = strategyName;
        this.issuedAt = issuedAt != null ? issuedAt : LocalDateTime.now();
    }

    public String getBillId() {
        return billId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    @Override
    public String toString() {
        return String.format("BillSummary [BillID: %s, AptID: %s, Patient: %s, Doctor: Dr. %s, Total: $%.2f, Paid: %s]",
                billId, appointmentId, patientName, doctorName, totalAmount, paid ? "PAID" : "UNPAID");
    }
}
