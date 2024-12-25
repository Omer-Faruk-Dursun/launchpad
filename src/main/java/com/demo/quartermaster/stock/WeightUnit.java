package com.demo.quartermaster.stock;

public class WeightUnit implements Unit {
    private double quantityInKg;

    public WeightUnit(double quantityInKg) {
        this.quantityInKg = quantityInKg;
    }

    @Override
    public String getUnit() {
        return "kg";
    }

    @Override
    public double getQuantity() {
        return quantityInKg;
    }

    @Override
    public String toString() {
        return quantityInKg + " " + getUnit();
    }
}

