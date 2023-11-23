package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.delicifind.Adapters.ProductOptionAdapter;
import com.example.delicifind.Models.ProductOption;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class showProductOption extends AppCompatActivity {

    RecyclerView productRV;
    ArrayList<ProductOption> poList;
    DatabaseReference poDatabase;
    ProductOptionAdapter poAdapter;
    ImageView addButton;
    TextView titleText;
    Spinner spinner;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_option);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Available Ingredients List");

        productRV = findViewById(R.id.productRV);
        View productOptionView = getLayoutInflater().inflate(R.layout.product_option, null);
        addButton = productOptionView.findViewById(R.id.addButton);

        poDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("ProductOption");
        productRV.setHasFixedSize(true);
        productRV.setLayoutManager(new LinearLayoutManager(this));

        poList = new ArrayList<>();
        poAdapter = new ProductOptionAdapter(this, poList);
        productRV.setAdapter(poAdapter);

        //add product to kitchen
        poDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductOption product = dataSnapshot.getValue(ProductOption.class);
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductOption product = snapshot.getValue(ProductOption.class);
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

                // Filter and display products based on the selected category
                displayProductsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        poAdapter.setOnAddButtonClickListener(new ProductOptionAdapter.OnAddButtonClickListener() {
            @Override
            public void onAddButtonClick(String poName, String category, String URL) {
                // Handle the button click here
                Intent intent = new Intent(showProductOption.this, addProductToPantry.class);
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
                startActivity(new Intent(showProductOption.this, createNewProduct.class));
            }
        });
    }

    private void displayProductsByCategory(String selectedCategory) {
        ArrayList<ProductOption> filteredProducts = new ArrayList<>();

        // Filter products by category
        for (ProductOption product : poList) {
            if (product.getCategory().equals(selectedCategory)) {
                filteredProducts.add(product);
            }
        }

        // Update the RecyclerView with the filtered products
        poAdapter.updateData(filteredProducts);
    }
}