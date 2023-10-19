package com.example.testingreciperecommendation.Listeners;

public interface PantryItemsResponseListener {

    void onPantryItemsRetrieved(String concatenatedIngredients);
    void onPantryItemsError(String message);
}
