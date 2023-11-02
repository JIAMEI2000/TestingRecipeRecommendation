package com.example.delicifind.Listeners;

import com.example.delicifind.Models.RecipeDetailsResponse;


public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
