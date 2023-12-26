package com.example.delicifind.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delicifind.Models.AvailableIngredient;
import com.example.delicifind.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvailableIngredientAdapter extends RecyclerView.Adapter<AvailableIngredientAdapter.MyViewHolder>{

    Context context;
    ArrayList<AvailableIngredient> list;

    private OnAddButtonClickListener addButtonClickListener;

    public interface OnAddButtonClickListener {
        void onAddButtonClick(String poName, String category, String URL);
    }

    public void setOnAddButtonClickListener(OnAddButtonClickListener listener) {
        this.addButtonClickListener = listener;
    }

    public AvailableIngredientAdapter(Context context, ArrayList<AvailableIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AvailableIngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.available_ingredient,parent,false);
        return new AvailableIngredientAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableIngredientAdapter.MyViewHolder holder, int position) {

        AvailableIngredient availableIngredient = list.get(position);
        holder.poName.setText(availableIngredient.getPoName());

        Glide.with(holder.poImage.getContext())
                .load(availableIngredient.getURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.poImage);

        // Set a click listener for the "Add" button
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addButtonClickListener != null) {
                    addButtonClickListener.onAddButtonClick(availableIngredient.getPoName(), availableIngredient.getCategory(), availableIngredient.getURL());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchAvailableIngredients(ArrayList<AvailableIngredient> searchList){
        list = searchList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView poName;
        CircleImageView poImage;
        ImageView addButton;

        public MyViewHolder(@NonNull View productOptionView) {
            super(productOptionView);

            poName = productOptionView.findViewById(R.id.poName);
            poImage = productOptionView.findViewById(R.id.poImage);
            addButton = productOptionView.findViewById(R.id.addButton);

        }
    }

    public void updateData(ArrayList<AvailableIngredient> filteredProducts) {
        list = filteredProducts;
        notifyDataSetChanged();
    }
}
