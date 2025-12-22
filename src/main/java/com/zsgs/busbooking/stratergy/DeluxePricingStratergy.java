package com.zsgs.busbooking.stratergy;

public class DeluxePricingStratergy implements  PricingStratergy{

    private double pricingFactor = 0.80 ;

    @Override
    public double calculatePrice(int distance) {
        return distance * pricingFactor;
    }
}
