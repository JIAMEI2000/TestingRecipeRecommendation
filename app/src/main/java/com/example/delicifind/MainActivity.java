package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Adapters.RecipesByIngredientsAdapter;
import com.example.delicifind.Listeners.RecipeClickListener;
import com.example.delicifind.Listeners.RecipesByIngredientsResponseListener;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    TextView loginLink;
    Button createAccBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        logo = findViewById(R.id.logo);
        loginLink = findViewById(R.id.loginLink);
        createAccBtn = findViewById(R.id.createAccButton);

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, registerAccount.class));
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });


    }

}