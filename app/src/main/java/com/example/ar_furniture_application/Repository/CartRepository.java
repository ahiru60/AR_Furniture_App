package com.example.ar_furniture_application.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {

    private static final String TAG = "CartRepository";
    private final ApiService apiService;

    public CartRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getCartItems(User user,
                             MutableLiveData<List<CartItem>> cartItemsLiveData,
                             MutableLiveData<String> errorLiveData) {
        Call<List<CartItem>> call = apiService.getCartItems(user);
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemsLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "getCartItems failed: " + response.code());
                    errorLiveData.setValue("No items found");
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Log.e(TAG, "getCartItems error: " + t.getMessage());
                errorLiveData.setValue("Failed to load items: " + t.getMessage());
            }
        });
    }

    public void deleteCartItems(List<CartItem> items,
                                MutableLiveData<Boolean> successLiveData,
                                MutableLiveData<String> errorLiveData) {
        Call<ResponseBody> call = apiService.deleteCartItems(items);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    successLiveData.setValue(true);
                } else {
                    Log.e(TAG, "deleteCartItems failed: " + response.code());
                    errorLiveData.setValue("Failed to remove items");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "deleteCartItems error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }
}
