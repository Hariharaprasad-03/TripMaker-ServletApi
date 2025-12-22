package com.zsgs.busbooking.stratergy;

public class AcSeaterPricingStratergy implements  PricingStratergy{

    private  double pricingFactor = 1.30 ;

    @Override
    public double calculatePrice(int distance) {
        return distance * pricingFactor;
    }
}
