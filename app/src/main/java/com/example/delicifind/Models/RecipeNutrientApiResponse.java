package com.example.delicifind.Models;

import java.util.ArrayList;

public class RecipeNutrientApiResponse {
    public String calories;
    public String carbs;
    public String fat;
    public String protein;
    public ArrayList<Nutrient> nutrients;
    public WeightPerServing weightPerServing;
    public long expires;
    public boolean isStale;
}
