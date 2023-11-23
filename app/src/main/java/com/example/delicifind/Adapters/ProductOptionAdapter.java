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
import com.example.delicifind.Models.Pantry;
import com.example.delicifind.Models.ProductOption;
import com.example.delicifind.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductOptionAdapter extends RecyclerView.Adapter<ProductOptionAdapter.MyViewHolder>{

    Context context;
    ArrayList<ProductOption> list;

    private OnAddButtonClickListener addButtonClickListener;

    public interface OnAddButtonClickListener {
        void onAddButtonClick(String poName, String category, String URL);
    }

    public void setOnAddButtonClickListener(OnAddButtonClickListener listener) {
        this.addButtonClickListener = listener;
    }

    public ProductOptionAdapter(Context context, ArrayList<ProductOption> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductOptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_option,parent,false);
        return new ProductOptionAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOptionAdapter.MyViewHolder holder, int position) {

        ProductOption productOption = list.get(position);
        holder.poName.setText(productOption.getPoName());

        Glide.with(holder.poImage.getContext())
                .load(productOption.getURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.poImage);

        // Set a click listener for the "Add" button
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addButtonClickListener != null) {
                    addButtonClickListener.onAddButtonClick(productOption.getPoName(),productOption.getCategory(),productOption.getURL());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchAvailableIngredients(ArrayList<ProductOption> searchList){
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

    public void updateData(ArrayList<ProductOption> filteredProducts) {
        list = filteredProducts;
        notifyDataSetChanged();
    }
}
