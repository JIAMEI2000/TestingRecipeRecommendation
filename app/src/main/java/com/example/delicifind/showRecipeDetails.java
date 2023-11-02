package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Adapters.IngredientsAdapter;
import com.example.delicifind.Adapters.RecipeStepsAdapter;
import com.example.delicifind.Listeners.RecipeDetailsListener;
import com.example.delicifind.Models.AnalyzedInstruction;
import com.example.delicifind.Models.RecipeDetailsResponse;
import com.example.delicifind.Models.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class showRecipeDetails extends AppCompatActivity {
    int id;
    TextView titleText, recipeName, recipeSource;
    ImageView recipeImage;
    RecyclerView rvIngredients ,rvSteps;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;

    RecipeStepsAdapter recipeStepsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_details);

        titleText = findViewById(R.id.titleText);
        titleText.setText("RecipeDetails");

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener,id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Recipe Details...");
        dialog.show();

    }

    private void findViews(){
        recipeName = findViewById(R.id.recipeName);
        recipeSource = findViewById(R.id.recipeSource);
        recipeImage = findViewById(R.id.recipeImage);
        rvIngredients = findViewById(R.id.rvIngredients);
        rvSteps = findViewById(R.id.rvSteps);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            recipeName.setText(response.title);
            recipeSource.setText(response.sourceName);
            Picasso.get().load(response.image).into(recipeImage);

            rvIngredients.setHasFixedSize(true);
            rvIngredients.setLayoutManager(new LinearLayoutManager(showRecipeDetails.this,LinearLayoutManager.HORIZONTAL,false));
            ingredientsAdapter = new IngredientsAdapter(showRecipeDetails.this,response.extendedIngredients);
            rvIngredients.setAdapter(ingredientsAdapter);

//            rvSteps.setHasFixedSize(true);
//            rvSteps.setLayoutManager(new LinearLayoutManager(showRecipeDetails.this,LinearLayoutManager.VERTICAL,false));
//            recipeStepsAdapter = new RecipeStepsAdapter(showRecipeDetails.this, extractStepsFromInstructions(response.analyzedInstructions));
//            rvSteps.setAdapter(recipeStepsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(showRecipeDetails.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private List<Step> extractStepsFromInstructions(List<AnalyzedInstruction> instructions) {
        List<Step> steps = new ArrayList<>();

        for (AnalyzedInstruction instruction : instructions) {
            if (instruction.steps != null) {
                steps.addAll(instruction.steps);
            }
        }

        return steps;
    }


}