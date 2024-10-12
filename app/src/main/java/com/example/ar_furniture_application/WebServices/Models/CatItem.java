package com.example.ar_furniture_application.WebServices.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class CatItem implements Serializable {
    private String FurnitureID;
    private String Name;
    private String Description;
    private String Price;
    private String Rating;
    private String Categoty;
    private String StockQuantity;
    @Expose
    private List<String> ImageURLs;
    private String Material;
    private String Dimensions;
    private String Weight;
    private String objectURL;

    // Additional fields for ar_visualization table
    private String slug;
    private String ModelURL;
    private String texturesURL;

    // Getters for existing fields
    public String getObjectURL() {
        return objectURL;
    }

    public String getFurnitureID() {
        return FurnitureID;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getPrice() {
        return Price;
    }

    public float getRating() {
        return Float.parseFloat(Rating);
    }

    public String getCategoty() {
        return Categoty;
    }

    public String getStockQuantity() {
        return StockQuantity;
    }

    public List<String> getImageURLs() {
        return ImageURLs;
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

    // Getters for the additional fields
    public String getSlug() {
        return slug;
    }

    public String getModelURL() {
        return ModelURL;
    }

    public String getTexturesURL() {
        return texturesURL;
    }
}
