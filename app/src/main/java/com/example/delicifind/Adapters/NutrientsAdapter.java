package com.example.delicifind.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicifind.Models.Nutrient;
import com.example.delicifind.R;

import java.util.List;

public class NutrientsAdapter extends RecyclerView.Adapter<NutrientsAdapter.ViewHolder> {

    private final List<Nutrient> nutrients;

    public NutrientsAdapter(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_nutrients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nutrient nutrient = nutrients.get(position);
        holder.nameTextView.setText(nutrient.name);
        holder.amountTextView.setText(String.valueOf(nutrient.amount));
        holder.unitTextView.setText(nutrient.unit);
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView amountTextView;
        TextView unitTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nutrientName);
            amountTextView = itemView.findViewById(R.id.nutrientAmount);
            unitTextView = itemView.findViewById(R.id.nutrientUnit);
        }
    }
}
