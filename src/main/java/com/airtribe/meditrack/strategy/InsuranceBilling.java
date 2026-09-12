package com.airtribe.meditrack.strategy;

/**
 * Insurance Billing Strategy (Co-Pay model: Patient pays co-pay percentage, e.g. 20%).
 */
public class InsuranceBilling implements BillingStrategy {

    private static final long serialVersionUID = 1L;

    private final double copayPercentage;

    public InsuranceBilling(double copayPercentage) {
        this.copayPercentage = copayPercentage;
    }

    public InsuranceBilling() {
        this(0.20); // Default 20% co-pay
    }

    @Override
    public double calculateAdjustedBase(double baseFee) {
        return baseFee * copayPercentage;
    }

    @Override
    public String getStrategyName() {
        return String.format("Insurance Co-Pay Billing (%.0f%% Patient Share)", copayPercentage * 100);
    }
}
