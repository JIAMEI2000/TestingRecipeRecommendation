package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addProductToPantry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView product,category,quantity,expiryDate;
    Button decrementBtn,incrementBtn,datePickerBtn,addToPantryButton;
    DatabaseReference poDatabase;

    TextView titleText;
    int count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_pantry);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Add Product To Kitchen");

        product = findViewById(R.id.productText);
        category = findViewById(R.id.categoryText);
        quantity = findViewById(R.id.quantityText);
        expiryDate = findViewById(R.id.displayExpiryDate);
        decrementBtn = findViewById(R.id.decrementButton);
        incrementBtn = findViewById(R.id.incrementButton);
        datePickerBtn = findViewById(R.id.datepickerButton);
        addToPantryButton = findViewById(R.id.addToPantryBtn);

        // Retrieve the product name and category from the intent
        String productName = getIntent().getStringExtra("product_name");
        String productCategory = getIntent().getStringExtra("category");

        // Set the product name and category in the TextViews
        product.setText(productName);
        category.setText(productCategory);

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

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        addToPantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPantryData();
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
        expiryDate.setText(currentDateString);
    }

    private void insertPantryData()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("pName",product.getText().toString());
        map.put("category",category.getText().toString());
        map.put("quantity",quantity.getText().toString());
        map.put("expiryDate",expiryDate.getText().toString());

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