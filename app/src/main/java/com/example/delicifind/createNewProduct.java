package com.example.delicifind;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.delicifind.Models.ProductOption;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class createNewProduct extends AppCompatActivity {
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
            if(result.getResultCode()==RESULT_OK){
                if(result.getData()!=null){
                    image = result.getData().getData();
                    saveBtn.setEnabled(true);
                    Glide.with(getApplicationContext()).load(image).into(imageView);
                }
            }else{
                Toast.makeText(createNewProduct.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_product);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Create New Product");

        FirebaseApp.initializeApp(createNewProduct.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        selectBtn = findViewById(R.id.selectFromGalleryBtn);
//        uploadBtn = findViewById(R.id.uploadBtn);
        imageView = findViewById(R.id.imageView);
//        progressIndicator = findViewById(R.id.progressBar);
        catSelectBox = findViewById(R.id.catSelectBox);
        productInput = findViewById(R.id.productInput);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelButton);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage(image);
//            }
//        });

        // Create an ArrayAdapter for the Spinner with predefined categories
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.add("Vegetables");
        categoryAdapter.add("Animal Products");
        categoryAdapter.add("Meat");
        categoryAdapter.add("Seafood");
        categoryAdapter.add("Fruits");
        categoryAdapter.add("Dairy Products");
        catSelectBox.setAdapter(categoryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child("ProductOption");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the product details
                String productName = productInput.getText().toString();
                String category = catSelectBox.getSelectedItem().toString(); // Get the selected category
                if (image == null || productName.isEmpty()) {
                    Toast.makeText(createNewProduct.this, "Please select an image and enter a product name", Toast.LENGTH_SHORT).show();
                } else {
                    // Upload the image to Firebase Storage
                    InsertProductInfo(image, productName, category);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(createNewProduct.this, showProductOption.class));
            }
        });
    }

//    private void uploadImage(Uri image){
//        StorageReference reference = storageReference.child("images/"+ UUID.randomUUID().toString());
//        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(createNewProduct.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(createNewProduct.this, "There was an error while uploading image", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressIndicator.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
//                progressIndicator.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
//            }
//        });
//    }

    private void InsertProductInfo(Uri image, String productName, String category) {
        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());

        reference.putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Toast.makeText(createNewProduct.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                    // Get the download URL of the uploaded image
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Create a new ProductOption instance
                        ProductOption productOption = new ProductOption(productName, category, imageUrl);

                        // Insert the product details into the Realtime Database
                        databaseReference.push().setValue(productOption);

                        // Return to the previous activity
                        finish();
                    }).addOnFailureListener(e -> {
                        // Handle any errors related to getting the download URL
                        Toast.makeText(createNewProduct.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                    });

                })
                .addOnFailureListener(e -> {
                    // Handle errors related to image upload
                    Toast.makeText(createNewProduct.this, "There was an error while uploading image", Toast.LENGTH_SHORT).show();
                });
    }
}