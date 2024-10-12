package com.example.ar_furniture_application.Models;

public class User {
    public User(String userId,String cartId,String name, String email, String phone, String address, String registrationDate,String role) {
        UserID = userId;
        CartID = cartId;
        Name = name;
        Email = email;
        Phone = phone;
        Address = address;
        RegistrationDate = registrationDate;
        Role =role;
    }

    private String UserID;
    private String CartID;
    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String Address;
    private String Role;
    private String RegistrationDate;
    private boolean Auth;
    private String error;

    public String getUserID() {
        return UserID;
    }

    public String getCartID() {
        return CartID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAddress() {
        return Address;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public boolean isAuth() {
        return Auth;
    }

    public String getError() {
        return error;
    }
}
