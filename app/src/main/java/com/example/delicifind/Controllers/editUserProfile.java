package com.example.delicifind.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicifind.Models.User;
import com.example.delicifind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Edit Profile");

        updatedName = findViewById(R.id.nameField);
//        updatedEmail = findViewById(R.id.emailField);
        updatedPassword = findViewById(R.id.passwordField);

        saveBtn = findViewById(R.id.saveBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Recipe").child("User");
        userID = user.getUid();


        // Retrieve original user details from Firebase
        reference.child(userID).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User originalUser = snapshot.getValue(User.class);

                if (originalUser != null) {
                    // Display original user details in EditText fields
                    updatedName.setText(originalUser.getName());
//                    updatedEmail.setText(originalUser.getEmail());
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
                // Show the password dialog
                showPasswordDialog();
            }
        });
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Current Password");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentPassword = input.getText().toString().trim();
                // Call the method to update email and password with the obtained current password
                updateProfile(currentPassword);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateProfile(String currentPassword) {
        if (user != null) {
            // Re-authenticate the user before updating email and password
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> reauthTask) {
                    if (reauthTask.isSuccessful()) {
                        // Re-authentication successful, now update email and password
                        String newName = updatedName.getText().toString().trim();
//                        String newEmail = updatedEmail.getText().toString().trim();
                        String newPassword = updatedPassword.getText().toString().trim();

                        reference.child(userID).child("Profile").child("name").setValue(newName);

//                        user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(editUserProfile.this, "Email Updated!", Toast.LENGTH_SHORT).show();
//                                // Now update the name in Firebase Realtime Database
//                                reference.child(userID).child("Profile").child("email").setValue(newEmail);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(editUserProfile.this, "Failed to update email", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(editUserProfile.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                                reference.child(userID).child("Profile").child("password").setValue(newPassword);

                                Intent intent = new Intent(editUserProfile.this, showUserProfile.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(editUserProfile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Handle re-authentication failure
                        Toast.makeText(editUserProfile.this, "Re-authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}