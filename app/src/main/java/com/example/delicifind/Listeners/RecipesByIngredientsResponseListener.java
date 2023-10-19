package com.example.delicifind.Listeners;

import com.example.delicifind.Models.RecipesByIngredientsApiResponse;

import java.util.List;


public interface RecipesByIngredientsResponseListener {

    void didFetch(List<RecipesByIngredientsApiResponse> response, String message);
    void didError(String message);
}
