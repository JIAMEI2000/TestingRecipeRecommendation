package com.example.delicifind.Listeners;

import com.example.delicifind.Models.RecipeDetailsResponse;

public interface RecipeDetailsCallback {

    void onDetailsFetched(RecipeDetailsResponse detailsResponse);

    void onError(String errorMessage);
}
