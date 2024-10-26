package com.example.ar_furniture_application.WebServices.Models;

import java.util.List;

public class OrderRequest {
    private int UserID;
    private double TotalAmount;
    private String ShippingAddress;
    private String PaymentMethod;
    private List<OrderItem> items;

    public OrderRequest(int userID, double totalAmount, String shippingAddress, String paymentMethod, List<OrderItem> items) {
        this.UserID = userID;
        this.TotalAmount = totalAmount;
        this.ShippingAddress = shippingAddress;
        this.PaymentMethod = paymentMethod;
        this.items = items;
    }

    // Getters and setters for each field
    // ...
}

