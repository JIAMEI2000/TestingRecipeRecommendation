package com.example.delicifind.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicifind.Models.Step;
import com.example.delicifind.R;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsViewHolder>{

    Context context;

    List<Step> stepList;

    public RecipeStepsAdapter(Context context, List<Step> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeStepsViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_instructions,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        holder.stepsNumber.setText(stepList.get(position).number);
        holder.stepsNumber.setSelected(true);
        holder.recipeSteps.setText(stepList.get(position).step);
        holder.recipeSteps.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }
}

class RecipeStepsViewHolder extends RecyclerView.ViewHolder{

    TextView stepsNumber, recipeSteps;
    public RecipeStepsViewHolder(@NonNull View itemView) {
        super(itemView);
        stepsNumber = itemView.findViewById(R.id.stepsNumber);
        recipeSteps = itemView.findViewById(R.id.recipeSteps);

    }
}