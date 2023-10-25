package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class addProductToPantry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView product,category,quantity,expiryDate;
    Button decrementBtn,incrementBtn,datePickerBtn;
    DatabaseReference poDatabase;

    int count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_pantry);

        product = findViewById(R.id.productText);
        category = findViewById(R.id.categoryText);
        quantity = findViewById(R.id.quantityText);
        expiryDate = findViewById(R.id.displayExpiryDate);
        decrementBtn = findViewById(R.id.decrementButton);
        incrementBtn = findViewById(R.id.incrementButton);
        datePickerBtn = findViewById(R.id.datepickerButton);

//        poDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("ProductOption");

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
}