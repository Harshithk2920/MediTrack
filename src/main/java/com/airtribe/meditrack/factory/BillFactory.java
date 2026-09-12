package com.airtribe.meditrack.factory;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.strategy.BillingStrategy;
import com.airtribe.meditrack.strategy.DiscountBilling;
import com.airtribe.meditrack.strategy.InsuranceBilling;
import com.airtribe.meditrack.strategy.StandardBilling;
import com.airtribe.meditrack.util.IdGenerator;

/**
 * Factory Pattern implementation for generating different Bill instances based on billing type.
 */
public final class BillFactory {

    public enum BillType {
        STANDARD,
        INSURANCE,
        SENIOR_DISCOUNT
    }

    private BillFactory() {
        // Utility Factory
    }

    /**
     * Factory method creating a Bill with an appropriate Strategy.
     *
     * @param appointment Target appointment
     * @param type Desired bill type
     * @return Formatted Bill instance
     */
    public static Bill createBill(Appointment appointment, BillType type) {
        String billId = IdGenerator.getInstance().generateBillId();
        BillingStrategy strategy;

        switch (type) {
            case INSURANCE:
                strategy = new InsuranceBilling(0.20); // 20% co-pay
                break;
            case SENIOR_DISCOUNT:
                strategy = new DiscountBilling(0.15); // 15% discount
                break;
            case STANDARD:
            default:
                strategy = new StandardBilling();
                break;
        }

        return new Bill(billId, appointment, strategy);
    }
}
