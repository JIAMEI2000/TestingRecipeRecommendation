package com.example.delicifind.Listeners;

public interface PantryItemsResponseListener {

    void onPantryItemsRetrieved(String concatenatedIngredients);
    void onPantryItemsError(String message);
}
