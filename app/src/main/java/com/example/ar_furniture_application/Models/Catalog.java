package com.example.ar_furniture_application.Models;

import java.util.List;
import java.util.ArrayList;

public class Catalog {
    private List<Product> products;

    // Constructor
    public Catalog() {
        this.products = new ArrayList<>();
    }

    // Getters and Setters
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Methods
    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(String productId) {
        this.products.removeIf(product -> product.getProductId().equals(productId));
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(updatedProduct.getProductId())) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    public boolean checkAvailability(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId) && product.getStockQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    public List<Product> listProducts() {
        return new ArrayList<>(products);
    }

    public List<Product> searchProducts(String query) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductName().contains(query) || product.getDescription().contains(query)) {
                result.add(product);
            }
        }
        return result;
    }

    public Product getProductDetails(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public double calculateRevenue() {
        double totalRevenue = 0;
        for (Product product : products) {
            totalRevenue += product.getPrice() * product.getStockQuantity();
        }
        return totalRevenue;
    }

    public void validateProduct(Product product) {
        // Implement validation logic here
    }

    public void archiveProduct(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                product.setStatus("Archived");
                return;
            }
        }
    }

    public void importCatalogData(List<Product> importedProducts) {
        this.products.addAll(importedProducts);
    }
}

