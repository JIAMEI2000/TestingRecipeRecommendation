package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class editPantry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView ingredient, category, quantity, expiryDate, purchasedDate;
    Button decrementBtn, incrementBtn, expiryDatePickerBtn, purchasedDatePickerBtn, updateButton;
    CircleImageView pantryImage;
    DatePickerFragment purchasedDatePicker, expiryDatePicker;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pantry);

        ingredient = findViewById(R.id.productText);
        category = findViewById(R.id.categoryText);
        quantity = findViewById(R.id.quantityText);
        purchasedDate = findViewById(R.id.displayPurchasedDate);
        expiryDate = findViewById(R.id.displayExpiryDate);
        decrementBtn = findViewById(R.id.decrementButton);
        incrementBtn = findViewById(R.id.incrementButton);
        purchasedDatePickerBtn = findViewById(R.id.datepickerButton1);
        expiryDatePickerBtn = findViewById(R.id.datepickerButton2);
        updateButton = findViewById(R.id.updateBtn);
        pantryImage = findViewById(R.id.poImage);

        // Retrieve the ingredient details from the intent
        String key = getIntent().getStringExtra("key");
        String imageURL = getIntent().getStringExtra("imageURL");
        String ingredientName = getIntent().getStringExtra("ingredientName");
        String categoryName = getIntent().getStringExtra("category");
        String quantityNum = getIntent().getStringExtra("quantity");
        String purchasedDateOn = getIntent().getStringExtra("purchasedDate");
        String expiryDateOn = getIntent().getStringExtra("expiryDate");

        // Set the product name and category in the TextViews
//        key.setText(key);
        ingredient.setText(ingredientName);
        category.setText(categoryName);
        quantity.setText(quantityNum);
        purchasedDate.setText(purchasedDateOn);
        expiryDate.setText(expiryDateOn);

        Glide.with(this)
                .load(imageURL)
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark) // You can set a placeholder image
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal) // You can set an error image
                .into(pantryImage);
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                quantity.setText("" + count);
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 1)
                    count = 1;
                else
                    count--;
                quantity.setText("" + count);
            }
        });

        purchasedDatePicker = new DatePickerFragment();
        expiryDatePicker = new DatePickerFragment();
        purchasedDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedDatePicker.show(getSupportFragmentManager(), "purchasedDatePicker");
            }
        });

        expiryDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiryDatePicker.show(getSupportFragmentManager(), "expiryDatePicker");
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePantryData();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        // The tag is used to distinguish between purchased and expiry date pickers
        if (getSupportFragmentManager().findFragmentByTag("purchasedDatePicker") != null) {
            purchasedDate.setText(currentDateString);
        } else if (getSupportFragmentManager().findFragmentByTag("expiryDatePicker") != null) {
            expiryDate.setText(currentDateString);
        }
    }

    private void updatePantryData() {

        String key = getIntent().getStringExtra("key");
        String imageURL = getIntent().getStringExtra("imageURL");
        String ingredientName = getIntent().getStringExtra("ingredientName");
        String categoryName = getIntent().getStringExtra("category");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Initialize the DatabaseReference
        DatabaseReference pantryDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("User").child(auth.getCurrentUser().getUid()).child("Pantry");

        // Retrieve the updated data
        String updatedQuantityNum = quantity.getText().toString();
        String updatedPurchasedDateOn = purchasedDate.getText().toString();
        String updatedExpiryDateOn = expiryDate.getText().toString();

        // Create a map for the updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("pName", ingredientName);
        updatedData.put("category", categoryName);
        updatedData.put("quantity", updatedQuantityNum);
        updatedData.put("purchasedDate", updatedPurchasedDateOn);
        updatedData.put("expiryDate", updatedExpiryDateOn);
        updatedData.put("URL", imageURL);

        // Update the data in the database
        pantryDatabase.child(key).updateChildren(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(editPantry.this, "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editPantry.this, "Error while Updating", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}