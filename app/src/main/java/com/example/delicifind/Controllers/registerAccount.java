package com.example.delicifind.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Models.User;
import com.example.delicifind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerAccount extends AppCompatActivity {

    Button createAcc;
    EditText name,email,password;
    TextView loginLink;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        auth = FirebaseAuth.getInstance();
        createAcc = findViewById(R.id.createAccButton);
        name = findViewById(R.id.nameField);
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        loginLink = findViewById(R.id.loginLink);

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = name.getText().toString().trim();
                String uEmail = email.getText().toString().trim();
                String uPassword = password.getText().toString().trim();

                if(uName.isEmpty()){
                    name.setError("Name cannot be empty");
                }
                if(uEmail.isEmpty()){
                    email.setError("Email cannot be empty");
                }

                if(uPassword.isEmpty()){
                    password.setError("Password cannot be empty");
                }else{
                    progressDialog = new ProgressDialog(registerAccount.this);
                    progressDialog.setMessage("Signing Up...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                insertUser();
                                progressDialog.dismiss();
                                Toast.makeText(registerAccount.this, "Register Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(registerAccount.this, login.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(registerAccount.this, "Failed to Register" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerAccount.this, login.class);
                startActivity(intent);
            }
        });
    }

    private void insertUser(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Recipe").child("User").child(auth.getCurrentUser().getUid()).child("Profile");

        String uName = name.getText().toString();
        String uEmail = email.getText().toString();
        String uPassword = password.getText().toString();

        User user = new User(uName,uEmail,uPassword);
        user.setName(uName);
        user.setEmail(uEmail);
        user.setPassword(uPassword);

        reference.setValue(user);
    }
}