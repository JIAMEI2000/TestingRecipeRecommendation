package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class showUserProfile extends AppCompatActivity {

    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_recipe);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_recipe) {
                startActivity(new Intent(getApplicationContext(), showRecipeList.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_kitchen) {
                startActivity(new Intent(getApplicationContext(), showPantryList.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                return true;
            }
            return false;
        });

        titleText = findViewById(R.id.titleText);
        titleText.setText("Profile");
    }
}