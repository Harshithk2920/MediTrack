package com.airtribe.meditrack.strategy;

/**
 * Standard Full-Price Billing Strategy.
 */
public class StandardBilling implements BillingStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public double calculateAdjustedBase(double baseFee) {
        return baseFee;
    }

    @Override
    public String getStrategyName() {
        return "Standard Billing (Full Rate)";
    }
}
