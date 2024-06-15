package com.example.ar_furniture_application.Roles;

public class Customer extends Role {
    private String paymentInformation;

    // Constructor
    public Customer(String userName, String password, String emailAddress, String firstName, String lastName, String phoneNumber, String address, String[] orderHistory, String paymentInformation) {
        super(userName, password, emailAddress, firstName, lastName, phoneNumber, address, orderHistory);
        this.paymentInformation = paymentInformation;
    }

    // Getters and Setters
    public String getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    // Methods
    public void viewOrderHistory() {
        // Implement view order history logic here
    }

    public void placeOrder(String orderId) {
        // Implement place order logic here
    }

    public void addPaymentMethod(String paymentMethod) {
        // Implement add payment method logic here
    }

    public void removePaymentMethod(String paymentMethod) {
        // Implement remove payment method logic here
    }

    public void resetPassword() {
        // Implement reset password logic here
    }

    public void verifyEmail() {
        // Implement verify email logic here
    }
}

