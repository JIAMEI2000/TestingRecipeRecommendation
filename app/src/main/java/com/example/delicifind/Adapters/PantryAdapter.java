package com.example.delicifind.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.delicifind.Models.Pantry;
import com.example.delicifind.R;
import com.example.delicifind.editPantry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.MyViewHolder>{

    Context context;

    ArrayList<Pantry> list;

    public PantryAdapter(Context context, ArrayList<Pantry> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pantry_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryAdapter.MyViewHolder holder, int position) {

        Pantry pantry = list.get(position);
        holder.pName.setText(pantry.getpName());
        holder.quantity.setText(pantry.getQuantity());
        holder.purchasedDate.setText(pantry.getPurchasedDate());
        holder.expiryDate.setText(pantry.getExpiryDate());

        Glide.with(holder.pImage.getContext())
                .load(pantry.getURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.pImage);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.pName.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can't be undo");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        int position = holder.getAdapterPosition();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child("Pantry").child(pantry.getKey());
                        // Remove the item from Firebase
                        databaseReference.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context,"Ingredient is removed",Toast.LENGTH_SHORT).show();
                                        list.remove(pantry);

                                        // Notify the adapter of the data change
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"Failed to remove ingredient",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.pName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, editPantry.class)
                        .putExtra("key",pantry.getKey().toString())
                        .putExtra("imageURL",pantry.getURL().toString())
                        .putExtra("ingredientName",pantry.getpName().toString())
                        .putExtra("category",pantry.getCategory().toString())
                        .putExtra("quantity",pantry.getQuantity().toString())
                        .putExtra("purchasedDate",pantry.getPurchasedDate().toString())
                        .putExtra("expiryDate",pantry.getExpiryDate().toString()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchSavedIngredients(ArrayList<Pantry> searchList){
        list = searchList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pName,quantity,purchasedDate,expiryDate;
        CircleImageView pImage;

        ImageView deleteBtn,editBtn;

        public MyViewHolder(@NonNull View pantryItemView) {
            super(pantryItemView);

            pName = pantryItemView.findViewById(R.id.pName);
            quantity = pantryItemView.findViewById(R.id.displayQuantity);
            purchasedDate = pantryItemView.findViewById(R.id.displayPurchasedDate);
            expiryDate = pantryItemView.findViewById(R.id.displayExpiryDate);
            pImage = pantryItemView.findViewById(R.id.pImage);
            deleteBtn = pantryItemView.findViewById(R.id.deleteButton);
            editBtn = pantryItemView.findViewById(R.id.editButton);

        }
    }

    public void updateData(ArrayList<Pantry> filteredProducts) {
        list = filteredProducts;
        notifyDataSetChanged();
    }
}
