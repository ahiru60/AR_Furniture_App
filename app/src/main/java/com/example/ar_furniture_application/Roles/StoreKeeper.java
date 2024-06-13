package com.example.ar_furniture_application.Roles;

public class StoreKeeper extends Role {
    private String inventory;

    // Constructor
    public StoreKeeper(String userName, String password, String emailAddress, String firstName, String lastName, String phoneNumber, String address, String[] orderHistory, String inventory) {
        super(userName, password, emailAddress, firstName, lastName, phoneNumber, address, orderHistory);
        this.inventory = inventory;
    }

    // Getters and Setters
    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    // Methods
    public void addProducts(String productId) {
        // Implement add products logic here
    }

    public void updateProducts(String productId) {
        // Implement update products logic here
    }

    public void generateReport() {
        // Implement generate report logic here
    }
}

