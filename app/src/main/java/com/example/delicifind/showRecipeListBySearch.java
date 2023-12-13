package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Adapters.RecipesByIngredientsAdapter;
import com.example.delicifind.Listeners.RecipeClickListener;
import com.example.delicifind.Listeners.RecipesByIngredientsResponseListener;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;

import java.util.List;

public class showRecipeListBySearch extends AppCompatActivity {
    private RecipesByIngredientsAdapter recipesByIngredientsAdapter;
    private RecyclerView recipeRecyclerView;
    private ProgressDialog dialog;
    private TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_list_by_search);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Recipe");

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        // Retrieve ingredients input from the previous activity
        List<String> ingredientsList = getIntent().getStringArrayListExtra("ingredients");

        // Use the ingredientsList to get recipes
        getRecipesByIngredients(ingredientsList);
    }

    private void getRecipesByIngredients(List<String> ingredientsList) {
        // Concatenate ingredients for API request
        StringBuilder concatenatedIngredients = new StringBuilder();
        for (String ingredient : ingredientsList) {
            concatenatedIngredients.append(ingredient).append(",");
        }

        RequestManager manager = new RequestManager(this);
        manager.getRecipesByPantryItems(concatenatedIngredients.toString(), recipesByIngredientsResponseListener);
    }

    private final RecipesByIngredientsResponseListener recipesByIngredientsResponseListener = new RecipesByIngredientsResponseListener() {
        @Override
        public void didFetch(List<RecipesByIngredientsApiResponse> response, String message) {
            dialog.dismiss();

            // Process the fetched recipes and update UI
            recipeRecyclerView = findViewById(R.id.recycler_recipe);
            recipeRecyclerView.setHasFixedSize(true);
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(showRecipeListBySearch.this, 1));
            recipesByIngredientsAdapter = new RecipesByIngredientsAdapter(showRecipeListBySearch.this, response, recipeClickListener);
            recipeRecyclerView.setAdapter(recipesByIngredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(showRecipeListBySearch.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(showRecipeListBySearch.this, showRecipeDetails.class)
                    .putExtra("id", id));
        }
    };
}