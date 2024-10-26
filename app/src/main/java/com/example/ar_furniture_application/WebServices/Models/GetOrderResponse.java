package com.example.ar_furniture_application.WebServices.Models;

import java.util.List;

public class GetOrderResponse {
    private int OrderID;
    private int UserID;
    private String OrderDate;
    private double TotalAmount;
    private String ShippingAddress;
    private String PaymentMethod;
    private List<OrderItem> items;

    // Getters and setters

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    // ...

    public static class OrderItem {
        private int OrderDetailsID;
        private int FurnitureID;
        private String FurnitureName;
        private String FurnitureDescription;
        private int Quantity;
        private double ItemPrice;

        public int getOrderDetailsID() {
            return OrderDetailsID;
        }

        public void setOrderDetailsID(int orderDetailsID) {
            OrderDetailsID = orderDetailsID;
        }

        public int getFurnitureID() {
            return FurnitureID;
        }

        public void setFurnitureID(int furnitureID) {
            FurnitureID = furnitureID;
        }

        public String getFurnitureName() {
            return FurnitureName;
        }

        public void setFurnitureName(String furnitureName) {
            FurnitureName = furnitureName;
        }

        public String getFurnitureDescription() {
            return FurnitureDescription;
        }

        public void setFurnitureDescription(String furnitureDescription) {
            FurnitureDescription = furnitureDescription;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }

        public double getItemPrice() {
            return ItemPrice;
        }

        public void setItemPrice(double itemPrice) {
            ItemPrice = itemPrice;
        }

        // Getters and setters
        // ...
    }
}

