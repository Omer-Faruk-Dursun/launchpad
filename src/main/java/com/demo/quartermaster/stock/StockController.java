package com.demo.quartermaster.stock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/stock/product")
    public Product getProduct() {
        Product wheat = new Product("Wheat", new WeightUnit(10));

        return wheat;
    }

    @PostMapping("/stock/product")
    public Long addProduct() {
        Product wheat = new Product("Wheat", new WeightUnit(10));

        return 4L;
    }


}
