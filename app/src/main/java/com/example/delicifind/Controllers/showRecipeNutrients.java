package com.example.delicifind.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.delicifind.Adapters.NutrientsAdapter;
import com.example.delicifind.Listeners.RecipeNutrientsResponseListener;
import com.example.delicifind.Models.RecipeNutrientApiResponse;
import com.example.delicifind.Models.WeightPerServing;
import com.example.delicifind.R;

public class showRecipeNutrients extends AppCompatActivity {
    int id;
    RequestManager manager;
    TextView weightAmount,weightUnit,titleText;
    RecyclerView rvNutrients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_nutrients);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Nutrition Information");

        weightAmount = findViewById(R.id.weightAmount);
        weightUnit = findViewById(R.id.weightUnit);
        rvNutrients = findViewById(R.id.recycler_recipeNutrients);

        id = getIntent().getIntExtra("id",0);
        manager = new RequestManager(this);

        // Retrieve and display nutrition information
        retrieveNutritionInformation();
    }
    private void retrieveNutritionInformation() {
        manager.getRecipeNutrients(id, new RecipeNutrientsResponseListener() {
            @Override
            public void didFetch(RecipeNutrientApiResponse response, String message) {
                // Display Weight Per Serving
                WeightPerServing weightPerServing = response.weightPerServing;
                if (weightPerServing != null) {
                    weightAmount.setText(String.valueOf(weightPerServing.amount));
                    weightUnit.setText(weightPerServing.unit);
                }

                // Display Nutrients
                NutrientsAdapter nutrientsAdapter = new NutrientsAdapter(response.nutrients);
                rvNutrients.setAdapter(nutrientsAdapter);
                rvNutrients.setLayoutManager(new LinearLayoutManager(showRecipeNutrients.this));
            }

            @Override
            public void didError(String message) {
                // Handle error
            }
        });
    }
}