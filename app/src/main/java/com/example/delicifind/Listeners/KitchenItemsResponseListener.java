package com.example.delicifind.Listeners;

public interface KitchenItemsResponseListener {

    void onPantryItemsRetrieved(String concatenatedIngredients);
    void onPantryItemsError(String message);
}
