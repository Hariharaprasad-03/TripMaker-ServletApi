package com.zsgs.busbooking.stratergy;

public class AcSleeperPricingStratergy implements  PricingStratergy {

    private double pricingFacotor = 2.30 ;

    @Override
    public double calculatePrice(int distance) {
        return pricingFacotor * distance;
    }
}
