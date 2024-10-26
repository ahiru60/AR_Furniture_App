package com.example.ar_furniture_application.WebServices.Models;

public class ProductViewLog {
    private String UserID;
    private String Message;
    public ProductViewLog(String userID, String Message) {
        this.UserID = userID;
        this.Message = Message;
    }
}
