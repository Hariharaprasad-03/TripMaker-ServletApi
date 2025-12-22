package com.zsgs.busbooking.stratergy;

import com.zsgs.busbooking.enums.BusType;

public class PricingStratergyFactory {

    public PricingStratergy getPricingStratergy(BusType busType) {

        return switch (busType) {

            case BusType.NORMAL -> new OrdinaryPricingStratergy();

            case BusType.AC_SLEEPER -> new AcSleeperPricingStratergy();

            case BusType.AC_SEATER -> new AcSeaterPricingStratergy();

            case BusType.DELUXE -> new DeluxePricingStratergy();

            default -> throw new IllegalArgumentException(
                    "No pricing strategy for bus type: " + busType
            );
        };
    }
}
