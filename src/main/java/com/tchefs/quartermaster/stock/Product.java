package com.tchefs.quartermaster.stock;

public class Product {
    private String name;
    private Unit unit;

    public Product(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', unit=" + unit + "}";
    }
}
