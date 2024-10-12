package com.example.ar_furniture_application.WebServices.Models;

import java.util.List;

public class UserCartItemsTemp {
    private static List<CartItem> cartItems;

    public UserCartItemsTemp(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
