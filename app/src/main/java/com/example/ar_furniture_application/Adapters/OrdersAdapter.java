package com.example.ar_furniture_application.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.Models.GetOrderResponse;
import com.example.ar_furniture_application.WebServices.Models.OrderResponse;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<GetOrderResponse> orders;

    public OrdersAdapter(List<GetOrderResponse> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        GetOrderResponse order = orders.get(position);
        holder.orderID.setText("Order ID: " + order.getOrderID());
        holder.totalAmount.setText("Total Amount: " + order.getTotalAmount());

        // You can add more details here
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, totalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderIDTextView);
            totalAmount = itemView.findViewById(R.id.totalAmountTextView);
        }
    }
}

