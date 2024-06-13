package com.example.ar_furniture_application.Roles;

public class LogisticsManager extends Role {
    private String emails;

    // Constructor
    public LogisticsManager(String userName, String password, String emailAddress, String firstName, String lastName, String phoneNumber, String address, String[] orderHistory, String emails) {
        super(userName, password, emailAddress, firstName, lastName, phoneNumber, address, orderHistory);
        this.emails = emails;
    }

    // Getters and Setters
    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    // Methods
    public void viewSupportRequest(String requestId) {
        // Implement view support request logic here
    }

    public void email(String emailContent) {
        // Implement email logic here
    }

    public void generateReport() {
        // Implement generate report logic here
    }
}

