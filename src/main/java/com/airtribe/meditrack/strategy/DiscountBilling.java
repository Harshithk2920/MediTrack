package com.airtribe.meditrack.strategy;

/**
 * Promotional / Senior Citizen Discount Billing Strategy (e.g. 15% discount).
 */
public class DiscountBilling implements BillingStrategy {

    private static final long serialVersionUID = 1L;

    private final double discountRate;

    public DiscountBilling(double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountBilling() {
        this(0.15); // Default 15% discount
    }

    @Override
    public double calculateAdjustedBase(double baseFee) {
        return baseFee * (1.0 - discountRate);
    }

    @Override
    public String getStrategyName() {
        return String.format("Discount Billing (%.0f%% Off)", discountRate * 100);
    }
}
