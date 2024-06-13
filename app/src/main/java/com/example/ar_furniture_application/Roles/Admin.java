package com.example.ar_furniture_application.Roles;

public class Admin extends Role {
    // Constructor
    public Admin(String userName, String password, String emailAddress, String firstName, String lastName, String phoneNumber, String address, String[] orderHistory) {
        super(userName, password, emailAddress, firstName, lastName, phoneNumber, address, orderHistory);
    }

    // Methods
    public void grantPermission(String userId) {
        // Implement grant permission logic here
    }

    public void revokePermission(String userId) {
        // Implement revoke permission logic here
    }

    public void generateReport() {
        // Implement generate report logic here
    }
}

