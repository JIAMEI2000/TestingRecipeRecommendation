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
import com.example.delicifind.Models.SavedIngredient;
import com.example.delicifind.R;
import com.example.delicifind.Controllers.editKitchen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.MyViewHolder>{

    Context context;

    ArrayList<SavedIngredient> list;

    public KitchenAdapter(Context context, ArrayList<SavedIngredient> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.saved_ingredient,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KitchenAdapter.MyViewHolder holder, int position) {

        SavedIngredient savedIngredient = list.get(position);
        holder.pName.setText(savedIngredient.getpName());
        holder.quantity.setText(savedIngredient.getQuantity());
        holder.purchasedDate.setText(savedIngredient.getPurchasedDate());
        holder.expiryDate.setText(savedIngredient.getExpiryDate());

        Glide.with(holder.pImage.getContext())
                .load(savedIngredient.getURL())
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
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child("User").child(auth.getCurrentUser().getUid()).child("SavedIngredient").child(savedIngredient.getKey());
                        // Remove the item from Firebase
                        databaseReference.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context,"Ingredient is removed",Toast.LENGTH_SHORT).show();
                                        list.remove(savedIngredient);

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
                context.startActivity(new Intent(context, editKitchen.class)
                        .putExtra("key", savedIngredient.getKey().toString())
                        .putExtra("imageURL", savedIngredient.getURL().toString())
                        .putExtra("ingredientName", savedIngredient.getpName().toString())
                        .putExtra("category", savedIngredient.getCategory().toString())
                        .putExtra("quantity", savedIngredient.getQuantity().toString())
                        .putExtra("purchasedDate", savedIngredient.getPurchasedDate().toString())
                        .putExtra("expiryDate", savedIngredient.getExpiryDate().toString()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchSavedIngredients(ArrayList<SavedIngredient> searchList){
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

    public void updateData(ArrayList<SavedIngredient> filteredProducts) {
        list = filteredProducts;
        notifyDataSetChanged();
    }
}
