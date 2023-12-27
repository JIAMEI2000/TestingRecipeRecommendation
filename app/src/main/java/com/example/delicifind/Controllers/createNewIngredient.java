package com.example.delicifind.Controllers;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.delicifind.Adapters.AvailableIngredientAdapter;
import com.example.delicifind.Models.AvailableIngredient;
import com.example.delicifind.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;


public class createNewIngredient extends AppCompatActivity {
    ArrayList<AvailableIngredient> poList;
    AvailableIngredientAdapter poAdapter;
    ImageView imageView;
    Button selectBtn, saveBtn, cancelBtn;
    StorageReference storageReference;
    LinearProgressIndicator progressIndicator;
    Uri image;
    EditText productInput;
    Spinner catSelectBox;
    TextView titleText;

    DatabaseReference databaseReference;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    image = result.getData().getData();
                    saveBtn.setEnabled(true);
                    Glide.with(getApplicationContext()).load(image).into(imageView);
                }
            } else {
                Toast.makeText(createNewIngredient.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ingredient);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Create New Ingredient");

        FirebaseApp.initializeApp(createNewIngredient.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        selectBtn = findViewById(R.id.selectFromGalleryBtn);
//        uploadBtn = findViewById(R.id.uploadBtn);
        imageView = findViewById(R.id.imageView);
//        progressIndicator = findViewById(R.id.progressBar);
        catSelectBox = findViewById(R.id.catSelectBox);
        productInput = findViewById(R.id.productInput);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelButton);

        poList = new ArrayList<>();
        poAdapter = new AvailableIngredientAdapter(this, poList);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });


        // Create an ArrayAdapter for the Spinner with predefined categories
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.add("Vegetables");
        categoryAdapter.add("Meat");
        categoryAdapter.add("Seafood");
        categoryAdapter.add("Fruits");
        categoryAdapter.add("Dairy Products");
        categoryAdapter.add("Dry Staples");
        categoryAdapter.add("Seasonings");
        catSelectBox.setAdapter(categoryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child("AvailableIngredient");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the product details
                String productName = productInput.getText().toString();
                String category = catSelectBox.getSelectedItem().toString(); // Get the selected category
                if (image == null || productName.isEmpty()) {
                    Toast.makeText(createNewIngredient.this, "Please select an image and enter a product name", Toast.LENGTH_SHORT).show();
                } else {
                    // Upload the image to Firebase Storage
                    InsertProductInfo(image, productName, category);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(createNewIngredient.this, showAvailableIngredientList.class));
            }
        });
    }


    private void InsertProductInfo(Uri image, String productName, String category) {
        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());

        reference.putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Toast.makeText(createNewIngredient.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                    // Get the download URL of the uploaded image
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Create a new ProductOption instance
                        AvailableIngredient availableIngredient = new AvailableIngredient(productName, category, imageUrl);

                        // Insert the product details into the Realtime Database
                        databaseReference.push().setValue(availableIngredient)
                                .addOnSuccessListener(aVoid -> {
                                    // After successfully writing to the database, fetch the latest data
                                    Toast.makeText(createNewIngredient.this, "Ingredient Created Successfully!", Toast.LENGTH_SHORT).show();
                                    fetchLatestData();

                                })
                                .addOnFailureListener(e -> {
                                    // Handle database write failure
                                    Toast.makeText(createNewIngredient.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                                });

                    }).addOnFailureListener(e -> {
                        // Handle any errors related to getting the download URL
                        Toast.makeText(createNewIngredient.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                    });

                })
                .addOnFailureListener(e -> {
                    // Handle errors related to image upload
                    Toast.makeText(createNewIngredient.this, "Error while uploading image", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchLatestData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list and add the fetched data to it
                poList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AvailableIngredient availableIngredient = dataSnapshot.getValue(AvailableIngredient.class);
                    poList.add(availableIngredient);
                }

                // Notify the adapter of the data change
                poAdapter.notifyDataSetChanged();

                // Navigate to the showProductOption activity
                startActivity(new Intent(createNewIngredient.this, showAvailableIngredientList.class));
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

}