package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.delicifind.Adapters.PantryAdapter;
import com.example.delicifind.Models.Pantry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class showPantryList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Pantry> list;
    DatabaseReference pantryDatabase;
    PantryAdapter pantryAdapter;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pantry_list);

        titleText = findViewById(R.id.titleText);
        titleText.setText("Kitchen Inventory");

        recyclerView = findViewById(R.id.pantryRV);
        pantryDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("Pantry");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        pantryAdapter = new PantryAdapter(this, list);
        recyclerView.setAdapter(pantryAdapter);

        pantryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Pantry pantry = dataSnapshot.getValue(Pantry.class);
                    list.add(pantry);
                }
                pantryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}