package com.example.delicifind.Models;

import java.util.ArrayList;

public class RecipesByIngredientsApiResponse{

    public int id;
    public String title;
    public String image;
    public String imageType;
    public int usedIngredientCount;
    public int missedIngredientCount;
    public ArrayList<MissedIngredient> missedIngredients;
    public ArrayList<UsedIngredient> usedIngredients;
    public ArrayList<Object> unusedIngredients;
    public int likes;
    public int readyInMinutes;

}
