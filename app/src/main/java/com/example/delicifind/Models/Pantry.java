package com.example.delicifind.Models;

public class Pantry {
    String pName,quantity,purchasedDate,expiryDate,category,URL;

    Pantry()
    {

    }

    public Pantry(String pName, String quantity, String purchasedDate,String expiryDate, String category, String URL) {
        this.pName = pName;
        this.quantity = quantity;
        this.purchasedDate = purchasedDate;
        this.expiryDate = expiryDate;
        this.category = category;
        this.URL = URL;
    }

    public String getpName() {
        return pName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCategory() {
        return category;
    }

    public String getURL() {
        return URL;
    }
}
