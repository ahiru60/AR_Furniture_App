package com.example.ar_furniture_application.Home.Home_Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.Models.CatItem;

import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ProductListsViewHolder> {
    private OnItemClickListener onItemClickListener;

    private List<CatItem> catItems = new ArrayList<CatItem>();
    public CatalogAdapter(OnItemClickListener onItemClickListener){
        //this.catItems = catItems;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public CatalogAdapter.ProductListsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cat_item, parent, false);
        return new ProductListsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogAdapter.ProductListsViewHolder holder, int position) {
        CatItem item = catItems.get(position);
        holder.productName.setText(catItems.get(position).getName());
        holder.ratingBar.setRating(catItems.get(position).getRating());
        int stock =Integer.parseInt(catItems.get(position).getStockQuantity());
        if(stock<30){
            holder.productQuantity.setText("Only "+stock+" left");
        }
        holder.price.setText(catItems.get(position).getPrice());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return catItems.size();
    }

    public void addData(List<CatItem> newCatItems) {
        catItems.addAll(newCatItems);
        notifyDataSetChanged();
    }

    public void setCatItems(List<CatItem> catItems) {
        this.catItems = catItems;
        notifyDataSetChanged();
    }

    public static class ProductListsViewHolder extends RecyclerView.ViewHolder {

        TextView productName,productQuantity,price;
        RatingBar ratingBar;
        ImageButton addToCart;
        ConstraintLayout productItem;

        public ProductListsViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            price= itemView.findViewById(R.id.price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            productItem= itemView.findViewById(R.id.product_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CatItem item);
    }
}
