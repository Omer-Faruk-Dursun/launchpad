package com.tchefs.quartermaster.stock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/stock/product")
    public String getStock() {
        // Creating different products
        Product wheat = new Product("Wheat", new WeightUnit(10)); // 10 kg of Wheat

        return wheat.toString();
    }

}
