package com.example.delicifind.Models;

public class AvailableIngredient {

    String poName,category,URL;

    AvailableIngredient(){

    }
    public AvailableIngredient(String poName, String category, String URL) {
        this.poName = poName;
        this.category = category;
        this.URL = URL;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
