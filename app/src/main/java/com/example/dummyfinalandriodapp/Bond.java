package com.example.dummyfinalandriodapp;

public class Bond {

    private double sellingPrice;
    private double faceValue;
    private double interestPayment;
    private double duration;

    private YieldCalculation yieldCalculation;

    private Bond (double sellingPrice, double faceValue, double interestPayment, double duration){
        if (sellingPrice <= 0 || faceValue <= 0 || interestPayment < 0 || duration <= 0 ){
            throw new IllegalArgumentException();
        }
        this.sellingPrice = sellingPrice;
        this.faceValue = faceValue;
        this.interestPayment = interestPayment;
        this.duration = duration;
    }

    static class BondBuilder {
        private double sellingPrice = 1000;
        private double faceValue = 1000;
        private double interestPayment = 10;
        private double duration =1;

        BondBuilder(){}

        BondBuilder setSellingPrice (double sellingPrice){
            this.sellingPrice = sellingPrice;
            return this;
        }
        BondBuilder setFaceValue (double faceValue){
            this.faceValue = faceValue;
            return this;
        }
        BondBuilder setInterestPayment (double interestPayment){
            this.interestPayment = interestPayment;
            return this;
        }
        BondBuilder setDuration (double duration){
            this.duration = duration;
            return this;
        }

        Bond createBond(){
            return new Bond(sellingPrice,faceValue, interestPayment, duration);
        }

    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getFaceValue() {
        return faceValue;
    }

    public double getInterestPayment() {
        return interestPayment;
    }

    public double getDuration() {
        return duration;
    }

    public void setYieldCalculation(YieldCalculation yieldCalculation) {
        this.yieldCalculation = yieldCalculation;
    }

    double calculateYTM(){
        return yieldCalculation.yieldToMaturity(this);
    }
}
