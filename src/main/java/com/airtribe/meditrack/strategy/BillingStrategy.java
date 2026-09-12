package com.airtribe.meditrack.strategy;

import java.io.Serializable;

/**
 * Strategy Pattern Interface for flexible billing calculations.
 */
public interface BillingStrategy extends Serializable {

    /**
     * Calculate total billable amount given raw doctor consultation fee.
     *
     * @param baseFee Consultation base cost
     * @return Discounted/Adjusted base amount
     */
    double calculateAdjustedBase(double baseFee);

    /**
     * Get strategy description name.
     *
     * @return Strategy title
     */
    String getStrategyName();
}
