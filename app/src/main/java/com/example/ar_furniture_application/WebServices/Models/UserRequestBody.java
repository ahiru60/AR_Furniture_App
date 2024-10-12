package com.example.ar_furniture_application.WebServices.Models;

public class UserRequestBody {
    private String id;
    private String CartID;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String role;

    public UserRequestBody(String email) {
        this.email = email;
    }
    public UserRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public UserRequestBody(String name, String phone,String address, String email, String password, String role) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters (if needed)

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

