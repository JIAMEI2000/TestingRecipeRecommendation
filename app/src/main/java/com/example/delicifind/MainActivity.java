package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Adapters.RecipesByIngredientsAdapter;
import com.example.delicifind.Listeners.RecipesByIngredientsResponseListener;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;
    private Button pButton,poButton;
    RequestManager manager;

    RecipesByIngredientsAdapter recipesByIngredientsAdapter;
    RecyclerView recipeRecyclerView;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Recipe");

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        manager = new RequestManager(this);
//        manager.getRecipesByIngredients(recipesByIngredientsResponseListener);
        manager.retrievePantryItems(new com.example.delicifind.Listeners.PantryItemsResponseListener() {
            @Override
            public void onPantryItemsRetrieved(String concatenatedIngredients) {
                // Now that you have the pantry items, proceed to get recipes
                manager.getRecipesByPantryItems(concatenatedIngredients, recipesByIngredientsResponseListener);
            }

            @Override
            public void onPantryItemsError(String message) {
                // Handle error
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();

        pButton = findViewById(R.id.showPantryBtn);
        pButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, showPantryList.class));
        });

        poButton = findViewById(R.id.showProductBtn);
        poButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, showProductOption.class));
        });

    }

    private final RecipesByIngredientsResponseListener recipesByIngredientsResponseListener = new RecipesByIngredientsResponseListener() {

        @Override
        public void didFetch(List<RecipesByIngredientsApiResponse> response, String message) {
            dialog.dismiss();

//            Log.d("Response", "Recipe Count: " + response.size());

            recipeRecyclerView = findViewById(R.id.recycler_recipe);
            recipeRecyclerView.setHasFixedSize(true);
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
            recipesByIngredientsAdapter = new RecipesByIngredientsAdapter(MainActivity.this,response);
            recipeRecyclerView.setAdapter(recipesByIngredientsAdapter);
        }
        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
        }
    };
}