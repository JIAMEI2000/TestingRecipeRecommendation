package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Adapters.RecipesByIngredientsAdapter;
import com.example.delicifind.Listeners.RecipeClickListener;
import com.example.delicifind.Listeners.RecipesByIngredientsResponseListener;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class showRecipeList extends AppCompatActivity {

    ProgressDialog dialog;
    RequestManager manager;
    RecipesByIngredientsAdapter recipesByIngredientsAdapter;
    RecyclerView recipeRecyclerView;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_list);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_recipe);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_recipe) {
                return true;
            } else if (item.getItemId() == R.id.bottom_kitchen) {
                startActivity(new Intent(getApplicationContext(), showPantryList.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), showUserProfile.class));
                finish();
                return true;
            }
            return false;
        });

        titleText = findViewById(R.id.titleText);
        titleText.setText("Recipe");

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        manager = new RequestManager(this);
        manager.retrievePantryItems(new com.example.delicifind.Listeners.PantryItemsResponseListener() {
            @Override
            public void onPantryItemsRetrieved(String concatenatedIngredients) {
                // Now that you have the pantry items, proceed to get recipes
                manager.getRecipesByPantryItems(concatenatedIngredients, recipesByIngredientsResponseListener);
            }

            @Override
            public void onPantryItemsError(String message) {
                // Handle error
                Toast.makeText(showRecipeList.this, message, Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();

    }

    private final RecipesByIngredientsResponseListener recipesByIngredientsResponseListener = new RecipesByIngredientsResponseListener() {

        @Override
        public void didFetch(List<RecipesByIngredientsApiResponse> response, String message) {
            dialog.dismiss();

//            Log.d("Response", "Recipe Count: " + response.size());

            recipeRecyclerView = findViewById(R.id.recycler_recipe);
            recipeRecyclerView.setHasFixedSize(true);
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(showRecipeList.this,1));
            recipesByIngredientsAdapter = new RecipesByIngredientsAdapter(showRecipeList.this,response,recipeClickListener);
            recipeRecyclerView.setAdapter(recipesByIngredientsAdapter);
        }
        @Override
        public void didError(String message) {
            Toast.makeText(showRecipeList.this,message,Toast.LENGTH_LONG).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
//            Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(showRecipeList.this,showRecipeDetails.class)
                    .putExtra("id",id));
        }
    };
}