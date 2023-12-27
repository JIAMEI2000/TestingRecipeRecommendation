package com.example.delicifind.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicifind.Models.ExtendedIngredient;
import com.example.delicifind.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsViewHolder>{

    Context context;
    List<ExtendedIngredient> list;

    public RecipeIngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecipeIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_ingredients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsViewHolder holder, int position) {
        holder.ingredientsName.setText(list.get(position).name);
        holder.ingredientsName.setSelected(true);
        holder.ingredientsQuantity.setText(list.get(position).original);
        holder.ingredientsQuantity.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+list.get(position).image).into(holder.ingredientsImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder{

    TextView ingredientsQuantity, ingredientsName;
    ImageView ingredientsImage;
    public RecipeIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientsImage = itemView.findViewById(R.id.ingredientsImage);
        ingredientsName = itemView.findViewById(R.id.ingredientsName);
        ingredientsQuantity = itemView.findViewById(R.id.ingredientsQuantity);

    }
}