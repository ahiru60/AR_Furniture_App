package com.example.ar_furniture_application.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.GetOrderResponse;
import com.example.ar_furniture_application.WebServices.Models.OrderRequest;
import com.example.ar_furniture_application.WebServices.Models.OrderResponse;
import com.example.ar_furniture_application.WebServices.Models.UserRequest;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    private static final String TAG = "OrderRepository";
    private final ApiService apiService;

    public OrderRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void placeOrder(OrderRequest orderRequest,
                           MutableLiveData<OrderResponse> orderResponseLiveData,
                           MutableLiveData<String> errorLiveData) {
        Call<OrderResponse> call = apiService.placeOrder(orderRequest);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderResponseLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "placeOrder failed: " + response.code());
                    errorLiveData.setValue("Failed to place order");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.e(TAG, "placeOrder error: " + t.getMessage());
                errorLiveData.setValue("Error occurred while placing order");
            }
        });
    }

    public void getOrders(UserRequest userRequest,
                          MutableLiveData<List<GetOrderResponse>> ordersLiveData,
                          MutableLiveData<String> errorLiveData) {
        Call<List<GetOrderResponse>> call = apiService.getOrders(userRequest);
        call.enqueue(new Callback<List<GetOrderResponse>>() {
            @Override
            public void onResponse(Call<List<GetOrderResponse>> call, Response<List<GetOrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ordersLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "getOrders failed: " + response.code());
                    errorLiveData.setValue("No orders found");
                }
            }

            @Override
            public void onFailure(Call<List<GetOrderResponse>> call, Throwable t) {
                Log.e(TAG, "getOrders error: " + t.getMessage());
                errorLiveData.setValue("Failed to get orders: " + t.getMessage());
            }
        });
    }
}
