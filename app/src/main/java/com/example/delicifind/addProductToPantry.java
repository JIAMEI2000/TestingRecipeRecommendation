package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class addProductToPantry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView product,category,quantity,expiryDate,purchasedDate,poUrl;
    Button decrementBtn,incrementBtn,expiryDatePickerBtn, purchasedDatePickerBtn,addToPantryButton;
    DatabaseReference poDatabase;
    CircleImageView poImage;
    TextView titleText;
    DatePickerFragment purchasedDatePicker, expiryDatePicker;
    int count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_pantry);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Add Ingredient To Kitchen");

        product = findViewById(R.id.productText);
        category = findViewById(R.id.categoryText);
        quantity = findViewById(R.id.quantityText);
        purchasedDate = findViewById(R.id.displayPurchasedDate);
        expiryDate = findViewById(R.id.displayExpiryDate);
        decrementBtn = findViewById(R.id.decrementButton);
        incrementBtn = findViewById(R.id.incrementButton);
        purchasedDatePickerBtn = findViewById(R.id.datepickerButton1);
        expiryDatePickerBtn = findViewById(R.id.datepickerButton2);
        addToPantryButton = findViewById(R.id.addToPantryBtn);
        poImage = findViewById(R.id.poImage);

        // Retrieve the product name and category from the intent
        String productName = getIntent().getStringExtra("product_name");
        String productCategory = getIntent().getStringExtra("category");
        String productImageUrl = getIntent().getStringExtra("product_image_url");

        // Set the product name and category in the TextViews
        product.setText(productName);
        category.setText(productCategory);

        Glide.with(this)
                .load(productImageUrl)
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark) // You can set a placeholder image
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal) // You can set an error image
                .into(poImage);

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                quantity.setText(""+count);
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count<=1)
                    count=1;
                else
                    count--;
                quantity.setText(""+count);
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

        addToPantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPantryData();
                startActivity(new Intent(addProductToPantry.this, showPantryList.class));
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

    private void insertPantryData()
    {
        String productImageUrl = getIntent().getStringExtra("product_image_url");

        Map<String,Object> map = new HashMap<>();
        map.put("pName",product.getText().toString());
        map.put("category",category.getText().toString());
        map.put("quantity",quantity.getText().toString());
        map.put("purchasedDate",purchasedDate.getText().toString());
        map.put("expiryDate",expiryDate.getText().toString());
        map.put("URL", productImageUrl);

        poDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("Pantry").push();

        poDatabase.setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addProductToPantry.this, "Data Inserted Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addProductToPantry.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}