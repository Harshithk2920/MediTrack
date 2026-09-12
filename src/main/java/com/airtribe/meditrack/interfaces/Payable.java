package com.airtribe.meditrack.interfaces;

import com.airtribe.meditrack.constants.Constants;

/**
 * Payable interface modeling billable entities.
 * Demonstrates default interface methods for tax calculation.
 */
public interface Payable {

    /**
     * Calculate the base amount before taxes or discounts.
     *
     * @return Base cost amount
     */
    double getBaseAmount();

    /**
     * Calculate final total payable amount after taxes and discounts.
     *
     * @return Total final payment amount
     */
    double calculateTotalAmount();

    /**
     * Mark the bill/item as paid.
     */
    void markAsPaid();

    /**
     * Check if payment is settled.
     *
     * @return true if paid
     */
    boolean isPaid();

    /**
     * Java 8 default method to calculate tax on base amount.
     *
     * @param amount Base taxable amount
     * @return Tax amount based on system TAX_RATE
     */
    default double calculateTax(double amount) {
        if (amount < 0) {
            return 0.0;
        }
        return amount * Constants.TAX_RATE;
    }
}
