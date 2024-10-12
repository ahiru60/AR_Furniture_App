package com.example.ar_furniture_application.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {

    private final OnClickListener onClickListener;
    private final List<CatItem> catItems;

    public CatalogAdapter(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.catItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_cat_item, parent, false);
        return new CatalogViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
        CatItem item = catItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return catItems.size();
    }

    public void addData(List<CatItem> newCatItems) {
        if (newCatItems != null && !newCatItems.isEmpty()) {
            int startPosition = catItems.size();
            catItems.addAll(newCatItems);
            notifyItemRangeInserted(startPosition, newCatItems.size());
        }
    }

    public void setCatItems(List<CatItem> catItems) {
        this.catItems.clear();
        if (catItems != null && !catItems.isEmpty()) {
            this.catItems.addAll(catItems);
        }
        notifyDataSetChanged();
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {

        private final TextView productName, productQuantity, price;
        private final RatingBar ratingBar;
        private final ImageButton addToCart;
        private final ImageView productImage;
        private final OnClickListener onClickListener;

        public CatalogViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;

            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            price = itemView.findViewById(R.id.price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            productImage = itemView.findViewById(R.id.productImageView);
        }

        public void bind(CatItem item) {
            // Load product image
            List<String> imageURLs = item.getImageURLs();
            if (imageURLs != null && !imageURLs.isEmpty()) {
                Picasso.get().load(imageURLs.get(0)).into(productImage);
            } else {
                productImage.setImageResource(R.drawable.img_sample_product);
            }

            // Set product details
            productName.setText(item.getName());
            ratingBar.setRating(item.getRating());

            // Set stock quantity with error handling
            try {
                int stock = Integer.parseInt(item.getStockQuantity());
                String stockText = stock < 30 ? "Only " + stock + " left" : "In Stock";
                productQuantity.setText(stockText);
            } catch (NumberFormatException e) {
                productQuantity.setText("Stock unavailable");
            }

            // Set price
            price.setText(item.getPrice());

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onItemClickItem(item);
                }
            });

            addToCart.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onAddToCartClick(item);
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClickItem(CatItem item);
        void onAddToCartClick(CatItem item);
    }
}
