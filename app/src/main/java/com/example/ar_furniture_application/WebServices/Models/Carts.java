package com.example.ar_furniture_application.WebServices.Models;

public class Carts {
    
    public Carts(String cartID, String userID, String createdDate) {
        CartID = cartID;
        UserID = userID;
        CreatedDate = createdDate;
    }

    String CartID;
    String UserID;
    String CreatedDate;
}
