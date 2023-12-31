package com.example.delicifind.Controllers;

import android.content.Context;
import android.util.Log;

import com.example.delicifind.Listeners.KitchenItemsResponseListener;
import com.example.delicifind.Listeners.RecipeDetailsListener;
import com.example.delicifind.Listeners.RecipeNutrientsResponseListener;
import com.example.delicifind.Listeners.RecipesByIngredientsResponseListener;
import com.example.delicifind.Models.RecipeDetailsResponse;
import com.example.delicifind.Models.RecipeNutrientApiResponse;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;
import com.example.delicifind.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        pantryDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("User").child(auth.getCurrentUser().getUid()).child("SavedIngredient");
    }

    public void retrievePantryItems(KitchenItemsResponseListener listener) {
        pantryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder ingredientsBuilder = new StringBuilder();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String pName = childSnapshot.child("pName").getValue(String.class);

                    if (pName != null) {
                        // Add a comma if it's not the first ingredient
                        if (ingredientsBuilder.length() > 0) {
                            ingredientsBuilder.append(",");
                        }

                        ingredientsBuilder.append(pName);
                    }
                }

                String concatenatedIngredients = ingredientsBuilder.toString();

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

    public void getRecipeDetails(RecipeDetailsListener listener,int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });

    }

    public void getRecipeNutrients(int id, RecipeNutrientsResponseListener listener) {
        CallRecipeNutrients callRecipeNutrients = retrofit.create(CallRecipeNutrients.class);
        Call<RecipeNutrientApiResponse> call = callRecipeNutrients.callRecipeNutrients(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeNutrientApiResponse>() {
            @Override
            public void onResponse(Call<RecipeNutrientApiResponse> call, Response<RecipeNutrientApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeNutrientApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }


    private interface CallRecipesByIngredients{
        @GET("recipes/findByIngredients")
        Call<List<RecipesByIngredientsApiResponse>> callRecipeByIngredient(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("ingredients") String ingredients
        );
    }

    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallRecipeNutrients {
        @GET("recipes/{id}/nutritionWidget.json")
        Call<RecipeNutrientApiResponse> callRecipeNutrients(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
}
