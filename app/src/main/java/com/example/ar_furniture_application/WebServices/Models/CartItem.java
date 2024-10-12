package com.example.ar_furniture_application.WebServices.Models;

import java.util.List;

public class CartItem {
    private String CartItemID;
    private String CartID;
    private String FurnitureID;
    private String Quantity;
    private String Price;

    // Furniture details
    private String Name;
    private String Description;
    private String FurniturePrice;
    private String Rating;
    private String Category;
    private String StockQuantity;
    private String Material;
    private String Dimensions;
    private String Weight;

    // List of Image URLs
    private List<String> ImageURLs;

    // Constructor with all fields including multiple ImageURLs
    public CartItem(String cartItemID, String cartID, String furnitureID, String quantity, String price,
                    String furnitureName, String description, String furniturePrice, String rating,
                    String category, String stockQuantity, String material, String dimensions, String weight,
                    List<String> imageURLs) {
        CartItemID = cartItemID;
        CartID = cartID;
        FurnitureID = furnitureID;
        Quantity = quantity;
        Price = price;
        Name = furnitureName;
        Description = description;
        FurniturePrice = furniturePrice;
        Rating = rating;
        Category = category;
        StockQuantity = stockQuantity;
        Material = material;
        Dimensions = dimensions;
        Weight = weight;
        ImageURLs = imageURLs;
    }

    // Constructor with minimal fields (if needed)
    public CartItem(String cartID, String furnitureID, String quantity, String price) {
        CartID = cartID;
        FurnitureID = furnitureID;
        Quantity = quantity;
        Price = price;
    }

    // Getters for all fields
    public String getCartItemID() {
        return CartItemID;
    }

    public String getCartID() {
        return CartID;
    }

    public String getFurnitureID() {
        return FurnitureID;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getPrice() {
        return Price;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getFurniturePrice() {
        return FurniturePrice;
    }

    public String getRating() {
        return Rating;
    }

    public String getCategory() {
        return Category;
    }

    public String getStockQuantity() {
        return StockQuantity;
    }

    public String getMaterial() {
        return Material;
    }

    public String getDimensions() {
        return Dimensions;
    }

    public String getWeight() {
        return Weight;
    }

    public List<String> getImageURLs() {
        return ImageURLs;
    }
}
