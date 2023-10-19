package com.example.delicifind.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicifind.Models.RecipesByIngredientsApiResponse;
import com.example.delicifind.R;

import java.util.List;

public class RecipesByIngredientsAdapter extends RecyclerView.Adapter<RecipesByIngredientsViewHolder>{  //to adapt a list of Recipe items to a RecyclerView.
    Context context;
    List<RecipesByIngredientsApiResponse> list;


    public RecipesByIngredientsAdapter(Context context, List<RecipesByIngredientsApiResponse> list) {   //constructor
        this.context = context;
        this.list = list;

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
        holder.usedCount.setText(list.get(position).usedIngredientCount+" Used Ingredient");
        holder.missedCount.setText(list.get(position).missedIngredientCount+" Missed Ingredient");


    }

    @Override
    public int getItemCount() {

        return list.size();
//        int itemCount = list.size();
//        Log.d("Adapter", "Item Count: " + itemCount);
//        return itemCount;
    }
}
class RecipesByIngredientsViewHolder extends RecyclerView.ViewHolder{
    CardView recipe_list_container;
    TextView title, usedCount, missedCount;

    public RecipesByIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        recipe_list_container = itemView.findViewById(R.id.recipe_list_container);
        title = itemView.findViewById(R.id.textView_title);
        usedCount = itemView.findViewById(R.id.textView_usedCount);
        missedCount = itemView.findViewById(R.id.textView_MissedCount);

    }
}