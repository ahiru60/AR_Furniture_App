package com.example.ar_furniture_application.Home.Home_Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.R;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ProductListsViewHolder> {
    private OnItemClickListener onItemClickListener;
    public CatalogAdapter(OnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public CatalogAdapter.ProductListsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
        return new ProductListsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogAdapter.ProductListsViewHolder holder, int position) {
        holder.text.setText("Product Name");
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick());
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class ProductListsViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ConstraintLayout productItem;

        public ProductListsViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView);
            productItem= itemView.findViewById(R.id.product_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}
