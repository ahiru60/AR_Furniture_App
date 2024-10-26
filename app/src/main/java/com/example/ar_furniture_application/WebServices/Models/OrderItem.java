package com.example.ar_furniture_application.WebServices.Models;

public class OrderItem {
    private int FurnitureID;
    private int Quantity;
    private double Price;

    public OrderItem(int furnitureID, int quantity, double price) {
        this.FurnitureID = furnitureID;
        this.Quantity = quantity;
        this.Price = price;
    }

    // Getters and setters for each field
    // ...
}
