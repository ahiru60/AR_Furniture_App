package com.example.ar_furniture_application.Cart.Cart_Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.OrdersAdapter;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.GetOrderResponse;
import com.example.ar_furniture_application.WebServices.Models.OrderResponse;
import com.example.ar_furniture_application.WebServices.Models.UserRequest;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private static final String TAG = "OrdersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize RecyclerView
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = view.findViewById(R.id.ordersProgressBar);
        errorTextView = view.findViewById(R.id.ordersErrorTextView);
        // Call the API to get orders
        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        // Create an instance of Retrofit service
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        UserSession userSession = new UserSession(getActivity());
        int userID = Integer.parseInt(userSession.getCurrentUser().getUserID());
        // Create a request object (replace with actual UserID)
        UserRequest userRequest = new UserRequest(userID);  // Replace with actual UserID

        // Make the API call
        Call<List<GetOrderResponse>> call = apiService.getOrders(userRequest);
        call.enqueue(new Callback<List<GetOrderResponse>>() {
            @Override
            public void onResponse(Call<List<GetOrderResponse>> call, Response<List<GetOrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetOrderResponse> orders = response.body();
                    progressBar.setVisibility(View.GONE);
                    // Set the adapter with the retrieved orders
                    ordersAdapter = new OrdersAdapter(orders);
                    ordersRecyclerView.setAdapter(ordersAdapter);
                } else {
                    try {
                        Log.d(TAG, "onResponse: failed to get orders " + response.errorBody().string());
                        progressBar.setVisibility(View.GONE);
                        errorTextView.setText("No orders found");
                        Toast.makeText(getActivity(), "No orders found", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    
                }
            }

            @Override
            public void onFailure(Call<List<GetOrderResponse>> call, Throwable t) {
                // Handle API failure
                progressBar.setVisibility(View.GONE);
                errorTextView.setText("Failed to get orders");
                errorTextView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Failed to fetch orders: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
