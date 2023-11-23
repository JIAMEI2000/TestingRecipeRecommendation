package com.example.delicifind.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delicifind.Models.Pantry;
import com.example.delicifind.Models.ProductOption;
import com.example.delicifind.R;


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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pName,quantity,purchasedDate,expiryDate;
        CircleImageView pImage;

        public MyViewHolder(@NonNull View pantryItemView) {
            super(pantryItemView);

            pName = pantryItemView.findViewById(R.id.pName);
            quantity = pantryItemView.findViewById(R.id.displayQuantity);
            purchasedDate = pantryItemView.findViewById(R.id.displayPurchasedDate);
            expiryDate = pantryItemView.findViewById(R.id.displayExpiryDate);
            pImage = pantryItemView.findViewById(R.id.pImage);

        }
    }

    public void updateData(ArrayList<Pantry> filteredProducts) {
        list = filteredProducts;
        notifyDataSetChanged();
    }
}
