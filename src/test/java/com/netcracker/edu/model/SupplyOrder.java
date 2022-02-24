package com.netcracker.edu.model;


import java.io.Serializable;
import java.util.List;

public class SupplyOrder implements Serializable {

    private List<Product> products;

    private String name;

    public SupplyOrder() {
    }

    public SupplyOrder(List<Product> products, String name) {
        this.products = products;
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
