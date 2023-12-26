package com.example.delicifind.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class searchRecipesByIngredients extends AppCompatActivity {
    private LinearLayout ingredientContainer;
    private Button addIngredientBtn,searchRecipesBtn;
    private TextView titleText;
    private int ingredientCount = 0;
    private boolean isFirstIngredientAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes_by_ingredients);

        ingredientContainer = findViewById(R.id.llIngredientContainer);
        addIngredientBtn = findViewById(R.id.btnAddIngredient);
        searchRecipesBtn = findViewById(R.id.searchRecipeButton);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_recipe) {
                startActivity(new Intent(getApplicationContext(), showRecipeList.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_search) {
                return true;
            } else if (item.getItemId() == R.id.bottom_kitchen) {
                startActivity(new Intent(getApplicationContext(), showKitchenList.class));
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
        titleText.setText("Search Recipe By Ingredients");

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredientEditText();
            }
        });

        searchRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve ingredients from EditText fields
                List<String> ingredientsList = getIngredientsList();

                if (ingredientsList.isEmpty()){
                    Toast.makeText(searchRecipesByIngredients.this, "Please enter at least one ingredient.", Toast.LENGTH_SHORT).show();
                }else {
                    // Pass the ingredients to the next activity
                    Intent intent = new Intent(searchRecipesByIngredients.this, showRecipeListBySearch.class);
                    intent.putStringArrayListExtra("ingredients", new ArrayList<>(ingredientsList));
                    startActivity(intent);
                }
            }
        });
    }

    private void addIngredientEditText() {

        if (!isFirstIngredientAdded) {
            // Remove the instruction TextView if it's still present
            ingredientContainer.removeView(findViewById(R.id.instructionTextView));
            isFirstIngredientAdded = true;
        }

        // Increment the ingredient count
        ingredientCount++;

        // Create a new TextView for the ingredient number
        TextView ingredientNumberTextView = new TextView(this);
        ingredientNumberTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ingredientNumberTextView.setText("Ingredient " + ingredientCount + " :");
        ingredientNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);  // Set text size
        ingredientNumberTextView.setTextColor(getResources().getColor(R.color.black));  // Set text color
        ingredientNumberTextView.setPadding(10, 5, 10, 5);  // Add padding

        // Add the new TextView to the container
        ingredientContainer.addView(ingredientNumberTextView);

        // Create a new EditText
        EditText newIngredientEditText = new EditText(this);
        newIngredientEditText.setId(View.generateViewId());  // Set a unique ID for the EditText
        newIngredientEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.edit_text_height)));
        newIngredientEditText.setHint("Enter Ingredient");

        newIngredientEditText.setBackgroundResource(R.drawable.custom_border);  // Set background
        newIngredientEditText.setPadding(10, 10, 10, 10);  // Add padding
        newIngredientEditText.setGravity(Gravity.TOP);  // Set gravity
        newIngredientEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);  // Set text size
        newIngredientEditText.setTextColor(getResources().getColor(R.color.black));  // Set text color
        newIngredientEditText.setSingleLine(true);

        // Set marginTop for each ingredient (adjust the value as needed)
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) newIngredientEditText.getLayoutParams();
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.margin_top), 0, 0);
        newIngredientEditText.setLayoutParams(params);

        // Add the new EditText to the container
        ingredientContainer.addView(newIngredientEditText);
    }

    private List<String> getIngredientsList() {
        List<String> ingredientsList = new ArrayList<>();
        for (int i = 0; i < ingredientContainer.getChildCount(); i++) {
            View childView = ingredientContainer.getChildAt(i);
            if (childView instanceof EditText) {
                String ingredient = ((EditText) childView).getText().toString().trim();
                if (!ingredient.isEmpty()) {
                    ingredientsList.add(ingredient);
                }
            }
        }
        return ingredientsList;
    }
}