package com.example.ar_furniture_application.WebServices.Models;

import java.io.Serializable;

public class CatItem implements Serializable {

    private String Name;
    private String Description;
    private String Price;
    private String Rating;
    private String Categoty;
    private String StockQuantity;
    private String ImageURL;
    private String Material;
    private String Dimensions;
    private String Weight;


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

    public String getImageURL() {
        return ImageURL;
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




}
