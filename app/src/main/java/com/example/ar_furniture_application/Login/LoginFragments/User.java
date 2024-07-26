package com.example.ar_furniture_application.Login.LoginFragments;

public class User {
    public User(String name, String email, String password, String phone, String address, String registrationDate) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        Address = address;
        RegistrationDate = registrationDate;
    }

    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String Address;
    private String RegistrationDate;
    private boolean Auth;
    private String error;

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getError() {
        return error;
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

    public boolean getAuth(){return Auth;}
}
