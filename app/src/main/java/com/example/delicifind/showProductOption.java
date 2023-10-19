package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.delicifind.Adapters.PantryAdapter;
import com.example.delicifind.Adapters.ProductOptionAdapter;
import com.example.delicifind.Models.ProductOption;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showProductOption extends AppCompatActivity {

    RecyclerView productRV;
    ArrayList<ProductOption> poList;
    DatabaseReference poDatabase;
    ProductOptionAdapter poAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_option);

        productRV = findViewById(R.id.productRV);
        poDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("ProductOption");
        productRV.setHasFixedSize(true);
        productRV.setLayoutManager(new LinearLayoutManager(this));

        poList = new ArrayList<>();
        poAdapter = new ProductOptionAdapter(this, poList);
        productRV.setAdapter(poAdapter);

        poDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ProductOption product = dataSnapshot.getValue(ProductOption.class);
                    poList.add(product);
                }
                poAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}