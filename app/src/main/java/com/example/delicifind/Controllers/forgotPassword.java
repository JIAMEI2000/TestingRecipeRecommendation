package com.example.delicifind.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class forgotPassword extends AppCompatActivity {

    EditText currentEmail;
    Button resetBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Recipe").child("User");


        currentEmail = findViewById(R.id.emailField);
        resetBtn = findViewById(R.id.resetBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPassword();

                Intent intent = new Intent(forgotPassword.this, login.class);
                startActivity(intent);
            }
        });

    }

    private void resetPassword(){
        String email = currentEmail.getText().toString();


        if(email.isEmpty()){
            currentEmail.setError("Email is required!");
            currentEmail.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            currentEmail.setError("Please provide valid email!");
            currentEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(forgotPassword.this, "Check your email to reset your password.", Toast.LENGTH_SHORT).show();

                    // Update the password in the Realtime Database
                    updatePasswordInDatabase();
                }
                else{
                    Toast.makeText(forgotPassword.this, "Something wrong happened. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePasswordInDatabase() {
        String newPassword = "newResetPassword"; // Replace with the actual new password

        // Update the password in the Realtime Database
        reference.child(user.getUid()).child("Profile").child("password").setValue(newPassword);
    }
}