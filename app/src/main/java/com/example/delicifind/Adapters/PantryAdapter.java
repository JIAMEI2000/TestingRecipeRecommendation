package com.example.delicifind.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicifind.Models.Pantry;
import com.example.delicifind.R;


import java.util.ArrayList;

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pName,quantity;
        public MyViewHolder(@NonNull View pantryItemView) {
            super(pantryItemView);

            pName = pantryItemView.findViewById(R.id.pName);
            quantity = pantryItemView.findViewById(R.id.quantity);

        }
    }
}
