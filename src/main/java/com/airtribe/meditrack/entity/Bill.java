package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Payable;
import com.airtribe.meditrack.strategy.BillingStrategy;
import com.airtribe.meditrack.strategy.StandardBilling;
import com.airtribe.meditrack.util.Validator;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Bill entity implementing Payable interface and utilizing Strategy Pattern.
 * Demonstrates Polymorphism (Method Overriding & Interface implementation).
 */
public class Bill implements Payable, Serializable {

    private static final long serialVersionUID = 1L;

    private String billId;
    private Appointment appointment;
    private double baseAmount;
    private double taxAmount;
    private double totalAmount;
    private boolean isPaid;
    private BillingStrategy strategy;
    private LocalDateTime issuedAt;

    public Bill(String billId, Appointment appointment, BillingStrategy strategy) {
        Validator.validateNonEmptyString(billId, "Bill ID");
        Validator.validateNotNull(appointment, "Appointment");
        
        this.billId = billId;
        this.appointment = appointment;
        this.strategy = strategy != null ? strategy : new StandardBilling();
        this.issuedAt = LocalDateTime.now();
        this.isPaid = false;

        recalculateBill();
    }

    public Bill(String billId, Appointment appointment) {
        this(billId, appointment, new StandardBilling());
    }

    public final void recalculateBill() {
        double rawFee = appointment.getDoctor().getConsultationFee();
        this.baseAmount = strategy.calculateAdjustedBase(rawFee);
        // Using Payable default method calculateTax
        this.taxAmount = calculateTax(this.baseAmount);
        this.totalAmount = this.baseAmount + this.taxAmount;
    }

    public String getBillId() {
        return billId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public BillingStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(BillingStrategy strategy) {
        this.strategy = strategy != null ? strategy : new StandardBilling();
        recalculateBill();
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    @Override
    public double getBaseAmount() {
        return baseAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    @Override
    public double calculateTotalAmount() {
        return totalAmount;
    }

    @Override
    public void markAsPaid() {
        this.isPaid = true;
    }

    @Override
    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Create an immutable snapshot of this bill summary.
     *
     * @return Immutable BillSummary instance
     */
    public BillSummary toSummary() {
        return new BillSummary(billId, appointment.getAppointmentId(),
                appointment.getPatient().getName(), appointment.getDoctor().getName(),
                baseAmount, taxAmount, totalAmount, isPaid, strategy.getStrategyName(), issuedAt);
    }

    @Override
    public String toString() {
        return String.format("Bill [ID: %s, Appointment: %s, Patient: %s, Strategy: %s, Base: $%.2f, Tax: $%.2f, Total: $%.2f, Paid: %s]",
                billId, appointment.getAppointmentId(), appointment.getPatient().getName(),
                strategy.getStrategyName(), baseAmount, taxAmount, totalAmount, isPaid ? "YES" : "NO");
    }
}
