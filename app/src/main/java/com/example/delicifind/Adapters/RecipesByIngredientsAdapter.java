package com.example.delicifind.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicifind.Listeners.RecipeClickListener;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;
import com.example.delicifind.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesByIngredientsAdapter extends RecyclerView.Adapter<RecipesByIngredientsViewHolder>{  //to adapt a list of Recipe items to a RecyclerView.
    Context context;
    List<RecipesByIngredientsApiResponse> list;
    RecipeClickListener listener;

    public RecipesByIngredientsAdapter(Context context, List<RecipesByIngredientsApiResponse> list, RecipeClickListener listener) {   //constructor
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipesByIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipesByIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_by_ingredients, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesByIngredientsViewHolder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.title.setSelected(true);
//        holder.usedCount.setText(list.get(position).usedIngredientCount+" Used Ingredient");
//        holder.missedCount.setText(list.get(position).missedIngredientCount+" Missing Ingredient");
        Picasso.get().load(list.get(position).image).into(holder.foodImage);

        holder.recipe_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}
class RecipesByIngredientsViewHolder extends RecyclerView.ViewHolder{
    CardView recipe_list_container;
    TextView title, usedCount, missedCount;
    ImageView foodImage;

    public RecipesByIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        recipe_list_container = itemView.findViewById(R.id.recipe_list_container);
        title = itemView.findViewById(R.id.textView_title);
//        usedCount = itemView.findViewById(R.id.textView_usedCount);
//        missedCount = itemView.findViewById(R.id.textView_MissedCount);
        foodImage = itemView.findViewById(R.id.imageView_food);

    }
}