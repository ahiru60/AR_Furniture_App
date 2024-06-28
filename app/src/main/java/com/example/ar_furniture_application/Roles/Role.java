package com.example.ar_furniture_application.Roles;

public class Role {
    private String role;
    private String userId;
    private String name;
    private String password;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String[] orderHistory;

    // Constructor
    public Role(String name,String password, String emailAddress, String phoneNumber, String address,String role) {
        this.role = role;
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public String getUserEmail(){return emailAddress;}
    public String getUserName() {
        return name;
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(String[] orderHistory) {
        this.orderHistory = orderHistory;
    }

    // Methods
    public void login() {
        // Implement login logic here
    }

    public void updateProfile(String firstName, String lastName, String phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
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

    public void deleteAccount() {
        // Implement delete account logic here
    }

    public enum Roles {
        ADMIN,
        USER,
        GUEST,
        LOGISTICS,
        STORE_KEEPER
    }

}
