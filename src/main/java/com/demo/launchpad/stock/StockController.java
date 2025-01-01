package com.demo.launchpad.stock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/stock/product")
    public Product getProduct() {
        Product bar = new Product("Bar");

        return bar;
    }

    @PostMapping("/stock/product")
    public Long addProduct() {
        Product foo = new Product("Foo");

        return 4L;
    }


}
