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

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsViewHolder> {
    private final List<Step> steps;
    private final Context context;

    public RecipeStepsAdapter(Context context, List<Step> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_instructions, parent, false);
        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.stepNumberTextView.setText("Step " + step.number);
        holder.recipeStepsTextView.setText(step.step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}

class RecipeStepsViewHolder extends RecyclerView.ViewHolder {
    TextView stepNumberTextView, recipeStepsTextView;

    public RecipeStepsViewHolder(@NonNull View itemView) {
        super(itemView);
        stepNumberTextView = itemView.findViewById(R.id.stepsNumber);
        recipeStepsTextView = itemView.findViewById(R.id.recipeSteps);
    }
}