package com.example.delicifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

    ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_option);

        productRV = findViewById(R.id.productRV);
        View productOptionView = getLayoutInflater().inflate(R.layout.product_option, null);
        addButton = productOptionView.findViewById(R.id.addButton);

        poDatabase = FirebaseDatabase.getInstance().getReference("Recipe").child("ProductOption");
        productRV.setHasFixedSize(true);
        productRV.setLayoutManager(new LinearLayoutManager(this));

        poList = new ArrayList<>();
        poAdapter = new ProductOptionAdapter(this, poList);
        productRV.setAdapter(poAdapter);

        poAdapter.setOnAddButtonClickListener(new ProductOptionAdapter.OnAddButtonClickListener() {
            @Override
            public void onAddButtonClick(String poName, String category) {
                // Handle the button click here
                Intent intent = new Intent(showProductOption.this, addProductToPantry.class);
                intent.putExtra("product_name", poName);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

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