package com.demo.quartermaster.stock;

public class CountUnit implements Unit {
    private int quantity;

    public CountUnit(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getUnit() {
        return "count";
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return quantity + " " + getUnit();
    }
}

