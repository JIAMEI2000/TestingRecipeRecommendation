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
import com.example.delicifind.Listeners.RecipeDetailsCallback;
import com.example.delicifind.Listeners.RecipeDetailsListener;
import com.example.delicifind.Models.RecipeDetailsResponse;
import com.example.delicifind.Models.RecipesByIngredientsApiResponse;
import com.example.delicifind.R;
import com.example.delicifind.Controllers.RequestManager;
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
        // Create a callback instance
        RecipeDetailsCallback detailsCallback = new RecipeDetailsCallback() {
            @Override
            public void onDetailsFetched(RecipeDetailsResponse detailsResponse) {
                // Update the UI or perform any other actions with the fetched detailsResponse
                int readyInMinutes = detailsResponse != null ? detailsResponse.readyInMinutes : 0;
                holder.readyInMinutes.setText("Ready in " + readyInMinutes + " minutes");
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error, e.g., display an error message
            }
        };

        // Call the method with the callback
        getDetailsResponseForItem(list.get(position).id, detailsCallback);

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

    // Helper method to fetch RecipeDetailsResponse for a given recipe ID
    private void getDetailsResponseForItem(int recipeId, RecipeDetailsCallback callback) {
        // Assuming you have an instance of RequestManager
        RequestManager manager = new RequestManager(context);

        // Call the getRecipeDetails method to fetch the details for the given recipeId
        manager.getRecipeDetails(new RecipeDetailsListener() {
            @Override
            public void didFetch(RecipeDetailsResponse response, String message) {
                // Pass the result to the callback
                callback.onDetailsFetched(response);
            }

            @Override
            public void didError(String message) {
                // Pass the error message to the callback
                callback.onError(message);
            }
        }, recipeId);
    }
}



class RecipesByIngredientsViewHolder extends RecyclerView.ViewHolder{
    CardView recipe_list_container;
    TextView title, usedCount, missedCount, readyInMinutes;
    ImageView foodImage;

    public RecipesByIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        recipe_list_container = itemView.findViewById(R.id.recipe_list_container);
        title = itemView.findViewById(R.id.textView_title);
//        usedCount = itemView.findViewById(R.id.textView_usedCount);
//        missedCount = itemView.findViewById(R.id.textView_MissedCount);
        foodImage = itemView.findViewById(R.id.imageView_food);
        readyInMinutes = itemView.findViewById(R.id.textView_readyInMinutes);

    }
}