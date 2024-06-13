package com.example.ar_furniture_application.Product;

import java.util.Date;
import java.util.List;

public class Product {
    private String productId;
    private String productName;
    private String description;
    private double price;
    private List<String> categories;
    private String brand;
    private String model;
    private int stockQuantity;
    private String sku;
    private List<String> images;
    private int attributeCount;
    private Date creationDate;
    private Date lastUpdateDate;
    private String status;

    // Constructor
    public Product(String productId, String productName, String description, double price, List<String> categories, String brand, String model, int stockQuantity, String sku, List<String> images, int attributeCount, Date creationDate, Date lastUpdateDate, String status) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.brand = brand;
        this.model = model;
        this.stockQuantity = stockQuantity;
        this.sku = sku;
        this.images = images;
        this.attributeCount = attributeCount;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getAttributeCount() {
        return attributeCount;
    }

    public void setAttributeCount(int attributeCount) {
        this.attributeCount = attributeCount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Methods
    public void addProduct(String productId, String productName, String description, double price, List<String> categories, String brand, String model, int stockQuantity, String sku, List<String> images, int attributeCount, Date creationDate, Date lastUpdateDate, String status) {
        // Implement add product logic here
    }

    public void removeProduct(String productId) {
        // Implement remove product logic here
    }

    public void updateProduct(String productId, String productName, String description, double price, List<String> categories, String brand, String model, int stockQuantity, String sku, List<String> images, int attributeCount, Date creationDate, Date lastUpdateDate, String status) {
        // Implement update product logic here
    }

    public void checkAvailability(String productId) {
        // Implement check availability logic here
    }

    public void listProducts() {
        // Implement list products logic here
    }

    public void searchProducts(String query) {
        // Implement search products logic here
    }

    public void getProductDetails(String productId) {
        // Implement get product details logic here
    }

    public void calculateRevenue() {
        // Implement calculate revenue logic here
    }

    public void validateProduct(String productId) {
        // Implement validate product logic here
    }

    public void archiveProduct(String productId) {
        // Implement archive product logic here
    }
}

