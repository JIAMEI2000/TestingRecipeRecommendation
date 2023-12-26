package com.example.delicifind.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.delicifind.Adapters.KitchenAdapter;
import com.example.delicifind.Models.SavedIngredient;
import com.example.delicifind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class showKitchenList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SavedIngredient> list;
    DatabaseReference pantryDatabase;
    KitchenAdapter kitchenAdapter;
    TextView titleText;
    FloatingActionButton floatingActionButton;
    Spinner spinner;
    SearchView searchBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kitchen_list);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_kitchen);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_kitchen) {
                return true;
            } else if (item.getItemId() == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), searchRecipesByIngredients.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_recipe) {
                startActivity(new Intent(getApplicationContext(), showRecipeList.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), showUserProfile.class));
                finish();
                return true;
            }
            return false;
        });


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(showKitchenList.this, showAvailableIngredientList.class));
            }
        });

        titleText = findViewById(R.id.titleText);
        titleText.setText("Kitchen Inventory");

        recyclerView = findViewById(R.id.pantryRV);
        auth = FirebaseAuth.getInstance();
        pantryDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("User").child(auth.getCurrentUser().getUid()).child("SavedIngredient");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        kitchenAdapter = new KitchenAdapter(this, list);
        recyclerView.setAdapter(kitchenAdapter);

        pantryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SavedIngredient savedIngredient = dataSnapshot.getValue(SavedIngredient.class);
                    savedIngredient.setKey(dataSnapshot.getKey());
                    list.add(savedIngredient);
                }
                kitchenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Define a list to hold categories
        List<String> categories = new ArrayList<>();

        spinner = findViewById(R.id.catSelectBox);

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);

        // Read categories from the database and add them to the list
        pantryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.add("All");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SavedIngredient savedIngredient = snapshot.getValue(SavedIngredient.class);
                    String category = savedIngredient.getCategory();
                    if (!categories.contains(category)) {
                        categories.add(category);
                    }
                }
                // Notify the adapter of the data change
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected category
                String selectedCategory = categories.get(position);

                // Check if "All" is selected
                if ("All".equals(selectedCategory)) {
                    // Display all pantry items
                    kitchenAdapter.updateData(list);
                } else {
                    // Filter and display products based on the selected category
                    displayPantryByCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBar = findViewById(R.id.searchBar);
        // Set the focus change listener to enable focus when the user clicks on the SearchView
        searchBar.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchBar.setFocusable(true);
                searchBar.setFocusableInTouchMode(true);
            }
        });
        searchBar.clearFocus();

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });
    }

    private void searchList(String text){
        ArrayList<SavedIngredient> searchList =new ArrayList<>();
        for(SavedIngredient savedIngredient :list){
            if(savedIngredient.getpName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(savedIngredient);
            }
        }
        kitchenAdapter.searchSavedIngredients(searchList);
    }

    private void displayPantryByCategory(String selectedCategory) {
        ArrayList<SavedIngredient> filteredProducts = new ArrayList<>();

        // Filter pantry by category
        for (SavedIngredient savedIngredient : list) {
            if (savedIngredient.getCategory().equals(selectedCategory)) {
                filteredProducts.add(savedIngredient);
            }
        }

        // Update the RecyclerView with the filtered pantry
        kitchenAdapter.updateData(filteredProducts);
    }
}