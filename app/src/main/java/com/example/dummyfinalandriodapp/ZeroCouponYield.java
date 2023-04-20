package com.example.dummyfinalandriodapp;

public class ZeroCouponYield implements YieldCalculation{
    @Override
    public double yieldToMaturity(Bond bond) {
        double faceValue = bond.getFaceValue();
        double sellingPrice = bond.getSellingPrice();
        double duration = bond.getDuration();

        return Math.pow(faceValue/sellingPrice, 1/duration) -1;
    }

//    @Override
//    public double yieldToMaturity(Bond bond) {
//        return Math.pow(bond.getFaceValue()/ bond.getSellingPrice(), 1/ bond.getDuration()) - 1;
//    }
}