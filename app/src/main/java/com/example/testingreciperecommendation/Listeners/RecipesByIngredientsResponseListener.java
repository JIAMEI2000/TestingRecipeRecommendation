package com.example.testingreciperecommendation.Listeners;

import com.example.testingreciperecommendation.Models.RecipesByIngredientsApiResponse;

import java.util.List;


public interface RecipesByIngredientsResponseListener {

    void didFetch(List<RecipesByIngredientsApiResponse> response, String message);
    void didError(String message);
}
