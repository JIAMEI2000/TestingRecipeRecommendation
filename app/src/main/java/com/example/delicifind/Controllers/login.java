package com.example.delicifind.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.delicifind.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    Button loginBtn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.emailField);
        loginPassword = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if(!email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!password.isEmpty()){

                        progressDialog = new ProgressDialog(login.this);
                        progressDialog.setMessage("Logging in...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        auth.signInWithEmailAndPassword(email,password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(login.this, showRecipeList.class));
                                        startActivity(new Intent(login.this, showRecipeList.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(login.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        loginPassword.setError("Password cannot be empty");
                    }
                }else if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                }else{
                    loginEmail.setError("Please enter valid email ");
                }

            }
        });

    }
}