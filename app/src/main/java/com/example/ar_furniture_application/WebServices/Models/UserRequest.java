package com.example.ar_furniture_application.WebServices.Models;

public class UserRequest {
    private int UserID;

    public UserRequest(int userID) {
        this.UserID = userID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        this.UserID = userID;
    }
}

