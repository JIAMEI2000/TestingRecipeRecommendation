package com.example.delicifind;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;


public class createNewProduct extends AppCompatActivity {
    ImageView uploadImage;
    Button selectBtn, uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_product);

        selectBtn = findViewById(R.id.selectFromGalleryBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadImage = findViewById(R.id.imageView);

    }

}