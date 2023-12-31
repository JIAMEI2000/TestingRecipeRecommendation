package com.example.delicifind.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Adapters.RecipeIngredientsAdapter;
import com.example.delicifind.Adapters.RecipeStepsAdapter;
import com.example.delicifind.Listeners.RecipeDetailsListener;
import com.example.delicifind.Models.AnalyzedInstruction;
import com.example.delicifind.Models.RecipeDetailsResponse;
import com.example.delicifind.Models.Step;
import com.example.delicifind.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class showRecipeDetails extends AppCompatActivity {
    int id;
    TextView titleText, recipeName, recipeSource;
    ImageView recipeImage;
    RecyclerView rvIngredients, rvSteps;
    RequestManager manager;
    ProgressDialog dialog;
    RecipeIngredientsAdapter recipeIngredientsAdapter;
    RecipeStepsAdapter recipeStepsAdapter;
    Button btnNutritionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_details);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Recipe Details");

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener,id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Recipe Details...");
        dialog.show();

        btnNutritionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(showRecipeDetails.this, showRecipeNutrients.class)
                        .putExtra("id",id));
            }
        });

    }

    private void findViews(){
        recipeName = findViewById(R.id.recipeName);
        recipeSource = findViewById(R.id.recipeSource);
        recipeImage = findViewById(R.id.recipeImage);
        rvIngredients = findViewById(R.id.rvIngredients);
        rvSteps = findViewById(R.id.rvSteps);
        btnNutritionInfo = findViewById(R.id.btnNutritionInfo);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            recipeName.setText(response.title);
            recipeSource.setText(response.sourceName);
            Picasso.get().load(response.image).into(recipeImage);

            rvIngredients.setHasFixedSize(true);
            rvIngredients.setLayoutManager(new LinearLayoutManager(showRecipeDetails.this,LinearLayoutManager.VERTICAL,false));
            recipeIngredientsAdapter = new RecipeIngredientsAdapter(showRecipeDetails.this,response.extendedIngredients);
            rvIngredients.setAdapter(recipeIngredientsAdapter);

            rvSteps.setHasFixedSize(true);
            rvSteps.setLayoutManager(new LinearLayoutManager(showRecipeDetails.this,LinearLayoutManager.VERTICAL,false));
            recipeStepsAdapter = new RecipeStepsAdapter(showRecipeDetails.this, extractStepsFromInstructions(response.analyzedInstructions));
            rvSteps.setAdapter(recipeStepsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(showRecipeDetails.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private List<Step> extractStepsFromInstructions(ArrayList<AnalyzedInstruction> analyzedInstructions) {
        List<Step> steps = new ArrayList<>();
        if (analyzedInstructions != null && analyzedInstructions.size() > 0) {
            AnalyzedInstruction instruction = analyzedInstructions.get(0); // Assuming there's only one set of instructions
            if (instruction != null && instruction.steps != null) {
                steps.addAll(instruction.steps);
            }
        }
        return steps;
    }


}