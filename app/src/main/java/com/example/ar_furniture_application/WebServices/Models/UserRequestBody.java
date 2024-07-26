package com.example.ar_furniture_application.WebServices.Models;

public class UserRequestBody {
    private String email;
    private String password;
    private String name;
    private String role;

    public UserRequestBody(String email) {
        this.email = email;
    }
    public UserRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserRequestBody(String name, String email, String password, String role) {
        this.name = name;
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

