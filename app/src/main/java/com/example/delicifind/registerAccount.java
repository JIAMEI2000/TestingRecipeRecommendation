package com.example.delicifind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registerAccount extends AppCompatActivity {

    Button createAcc;
    EditText name,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        createAcc = findViewById(R.id.createAccButton);
        name = findViewById(R.id.nameField);
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerAccount.this, showRecipeList.class));
            }
        });
    }
}