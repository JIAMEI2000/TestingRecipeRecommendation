package com.example.testingreciperecommendation;

import android.content.Context;
import android.util.Log;

import com.example.testingreciperecommendation.Listeners.RecipesByIngredientsResponseListener;
import com.example.testingreciperecommendation.Models.RecipesByIngredientsApiResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private DatabaseReference pantryDatabase;  // Firebase Database reference
    public RequestManager(Context context) { // constructor for the RequestManager class
        this.context = context;
        pantryDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("Pantry");
    }

    public void retrievePantryItems(PantryItemsResponseListener listener) {
        pantryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder ingredientsBuilder = new StringBuilder();
                boolean firstIngredient = true;

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String ingredient = childSnapshot.child("0").getValue(String.class);

                    if (ingredient != null) {
                        // Add a comma if it's not the first ingredient
                        if (!firstIngredient) {
                            ingredientsBuilder.append(",");
                        }

                        ingredientsBuilder.append(ingredient);
                        firstIngredient = false;
                    }
                }

                String concatenatedIngredients = ingredientsBuilder.toString();

                // Log the value here
                Log.d("RequestManager", "Concatenated Ingredients: " + concatenatedIngredients);

                listener.onPantryItemsRetrieved(concatenatedIngredients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onPantryItemsError(databaseError.getMessage());
            }
        });
    }

    public void getRecipesByPantryItems(String concatenatedIngredients, RecipesByIngredientsResponseListener listener) {
        CallRecipesByIngredients callRecipesByIngredients = retrofit.create(CallRecipesByIngredients.class);

        // Split the concatenatedIngredients into an array of ingredients
        String[] ingredientArray = concatenatedIngredients.split(",");

        // Construct the ingredients string
        StringBuilder ingredientsStringBuilder = new StringBuilder();
        for (String item : ingredientArray) {
            ingredientsStringBuilder.append(item).append(",");
        }
        String ingredients = ingredientsStringBuilder.toString();

        Call<List<RecipesByIngredientsApiResponse>> call = callRecipesByIngredients.callRecipeByIngredient(
                context.getString(R.string.api_key),
                "8",  // Adjust the number as needed
                ingredients
        );

        call.enqueue(new Callback<List<RecipesByIngredientsApiResponse>>() {
            @Override
            public void onResponse(Call<List<RecipesByIngredientsApiResponse>> call, Response<List<RecipesByIngredientsApiResponse>> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<RecipesByIngredientsApiResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public interface PantryItemsResponseListener {
        void onPantryItemsRetrieved(String concatenatedIngredients);
        void onPantryItemsError(String message);
    }

    private interface CallRecipesByIngredients{
        @GET("recipes/findByIngredients")
        Call<List<RecipesByIngredientsApiResponse>> callRecipeByIngredient(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("ingredients") String ingredients
        );
    }

}
