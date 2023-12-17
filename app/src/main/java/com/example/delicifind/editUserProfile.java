package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editUserProfile extends AppCompatActivity {

    TextView titleText;
    EditText updatedName, updatedEmail, updatedPassword;
    Button saveBtn;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Edit Profile");

        updatedName = findViewById(R.id.nameField);
        updatedEmail = findViewById(R.id.emailField);
        updatedPassword = findViewById(R.id.passwordField);

        saveBtn = findViewById(R.id.saveBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Recipe").child("User");
        String userID = user.getUid();


        // Retrieve original user details from Firebase
        reference.child(userID).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User originalUser = snapshot.getValue(User.class);

                if (originalUser != null) {
                    // Display original user details in EditText fields
                    updatedName.setText(originalUser.getName());
                    updatedEmail.setText(originalUser.getEmail());
                    updatedPassword.setText(originalUser.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(editUserProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated details from EditText fields
                String newName = updatedName.getText().toString().trim();
                String newEmail = updatedEmail.getText().toString().trim();
                String newPassword = updatedPassword.getText().toString().trim();

                // Update user details in Firebase
                reference.child(userID).child("Profile").setValue(new User(newName, newEmail, newPassword));

                // Update email and password in Firebase Authentication
                user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email updated successfully
                        } else {
                            // Handle error
                            Toast.makeText(editUserProfile.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password updated successfully
                        } else {
                            // Handle error
                            Toast.makeText(editUserProfile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                // Display a success message
                Toast.makeText(editUserProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(editUserProfile.this, showUserProfile.class);
                startActivity(intent);
            }
        });

    }
}