package com.zsgs.busbooking.stratergy;

public class OrdinaryPricingStratergy implements PricingStratergy{

    private double pricingFactor = 0.85;

    @Override
    public double calculatePrice(int distance) {
        return pricingFactor * distance;
    }
}
