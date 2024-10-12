package com.example.ar_furniture_application.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.CartItemListViewHolder> {
    private List<CartItem> items = new ArrayList<>();
    private List<CartItem> checkedItems = new ArrayList<>();
    private boolean isSelectMode =false;
    private CartOnClickListener cartOnClickListener;

    public CartItemListAdapter(CartOnClickListener cartOnClickListener) {
        this.cartOnClickListener = cartOnClickListener;
    }

    @NonNull
    @Override
    public CartItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new CartItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemListViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
        holder.price.setText(items.get(position).getPrice());
        if(items.get(position).getImageURLs().size()>0){
            Picasso.get().load(items.get(position).getImageURLs().get(0)).into(holder.itemImage);
        }
        else {
            holder.itemImage.setImageResource(R.drawable.img_sample_product);
        }
        if(isSelectMode){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CartItemListViewHolder extends RecyclerView.ViewHolder {
        private View orderItem;
        private CheckBox checkBox;
        private TextView itemName;
        private TextView price;
        private ImageView itemImage;
        private Spinner itemsAmountSpner;

        public CartItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);
            itemsAmountSpner = itemView.findViewById(R.id.countSpner);
            orderItem = itemView.findViewById(R.id.orderItem);
            checkBox = itemView.findViewById(R.id.checkbox);
            itemImage = itemView.findViewById(R.id.itemImage);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedItems.add(items.get(getLayoutPosition()));
                }
            });

            orderItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectMode = true;
                    cartOnClickListener.ItemsOnLongClick();
                    notifyDataSetChanged();
                    return false;
                }
            });
        }

    }

    public void addData(List<CartItem> items) {
        this.items = items;
    }
    public interface CartOnClickListener{
        void ItemsOnLongClick();
        void deleteBtnOnClick();
    }

    public List<CartItem> getItems(){
        return items;
    }

    public boolean getSelectedMode(){
        return isSelectMode;
    }

    public void setSelectedMode(boolean isSelectMode){
        this.isSelectMode = isSelectMode;
    }
    public List<CartItem> getCheckedItems(){
        return checkedItems;
    }

}
