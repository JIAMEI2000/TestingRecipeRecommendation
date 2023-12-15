package com.example.delicifind.Listeners;

import com.example.delicifind.Models.RecipeNutrientApiResponse;

public interface RecipeNutrientsResponseListener {
    void didFetch(RecipeNutrientApiResponse response, String message);
    void didError(String message);
}
