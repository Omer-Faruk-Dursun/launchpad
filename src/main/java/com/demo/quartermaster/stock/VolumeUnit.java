package com.demo.quartermaster.stock;

public class VolumeUnit implements Unit {
    private double quantityInLiters;

    public VolumeUnit(double quantityInLiters) {
        this.quantityInLiters = quantityInLiters;
    }

    @Override
    public String getUnit() {
        return "liters";
    }

    @Override
    public double getQuantity() {
        return quantityInLiters;
    }

    @Override
    public String toString() {
        return quantityInLiters + " " + getUnit();
    }
}

