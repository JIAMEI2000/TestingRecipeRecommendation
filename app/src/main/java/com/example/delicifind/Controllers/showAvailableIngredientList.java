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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.delicifind.Adapters.AvailableIngredientAdapter;
import com.example.delicifind.Models.AvailableIngredient;
import com.example.delicifind.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class showAvailableIngredientList extends AppCompatActivity {

    RecyclerView productRV;
    ArrayList<AvailableIngredient> poList;
    DatabaseReference poDatabase;
    AvailableIngredientAdapter poAdapter;
    ImageView addButton;
    TextView titleText;
    Spinner spinner;
    Button createBtn;
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_available_ingredient_list);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Available Ingredients List");

        productRV = findViewById(R.id.productRV);
        View productOptionView = getLayoutInflater().inflate(R.layout.available_ingredient, null);
        addButton = productOptionView.findViewById(R.id.addButton);

        poDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("AvailableIngredient");
        productRV.setHasFixedSize(true);
        productRV.setLayoutManager(new LinearLayoutManager(this));

        poList = new ArrayList<>();
        poAdapter = new AvailableIngredientAdapter(this, poList);
        productRV.setAdapter(poAdapter);

        //add product to kitchen
        poDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AvailableIngredient product = dataSnapshot.getValue(AvailableIngredient.class);
                    poList.add(product);

                }
                poAdapter.notifyDataSetChanged();
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
        poDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.add("All");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AvailableIngredient product = snapshot.getValue(AvailableIngredient.class);
                    String category = product.getCategory();
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
                    poAdapter.updateData(poList);
                } else {
                    // Filter and display products based on the selected category
                    displayProductsByCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        poAdapter.setOnAddButtonClickListener(new AvailableIngredientAdapter.OnAddButtonClickListener() {
            @Override
            public void onAddButtonClick(String poName, String category, String URL) {
                // Handle the button click here
                Intent intent = new Intent(showAvailableIngredientList.this, addIngredientToKitchen.class);
                intent.putExtra("product_name", poName);
                intent.putExtra("category", category);
                intent.putExtra("product_image_url", URL);

                startActivity(intent);
            }
        });

        createBtn = findViewById(R.id.createButton);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(showAvailableIngredientList.this, createNewIngredient.class));
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
        ArrayList<AvailableIngredient> searchList =new ArrayList<>();
        for(AvailableIngredient availableIngredient :poList){
            if(availableIngredient.getPoName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(availableIngredient);
            }
        }
        poAdapter.searchAvailableIngredients(searchList);
    }

    private void displayProductsByCategory(String selectedCategory) {
        ArrayList<AvailableIngredient> filteredProducts = new ArrayList<>();

        // Filter products by category
        for (AvailableIngredient product : poList) {
            if (product.getCategory().equals(selectedCategory)) {
                filteredProducts.add(product);
            }
        }

        // Update the RecyclerView with the filtered products
        poAdapter.updateData(filteredProducts);
    }
}