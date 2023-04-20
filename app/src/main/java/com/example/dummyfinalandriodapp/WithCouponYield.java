package com.example.dummyfinalandriodapp;

public class WithCouponYield implements YieldCalculation{

    @Override
    public double yieldToMaturity(Bond bond) {
        double rUp = 1;
        double rDown = 1e-10;
        double delta = rUp - rDown;
        double rMiddle;
        double frUp, frDown, frMiddle;

        while (delta > 1e-5){
            rMiddle = 0.5*(rUp + rDown);
            frMiddle = f(bond, rMiddle);
            frUp = f(bond, rUp);
            frDown = f(bond, rDown);
            if (frMiddle*frUp > 0){
                rUp = rMiddle;
            }
            else if (frMiddle*frDown > 0){
                rDown = rMiddle;
            }
            delta = rUp - rDown;
        }

        return 0.5*(rUp + rDown);
    }

    private double f (Bond bond, double r){
        double faceValue = bond.getFaceValue();
        double sellingPrice = bond.getSellingPrice();
        double duration = bond.getDuration();
        double interestPayment = bond.getInterestPayment();

        double secondTerm = (1 - Math.pow(1/(1+ r), duration))/r;

        double thirdTerm = (faceValue / Math.pow((1 + r), duration));

        return sellingPrice - interestPayment*secondTerm - thirdTerm;

    }
}
