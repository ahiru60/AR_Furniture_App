package com.example.ar_furniture_application.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ar_furniture_application.Models.CaptureResponse;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.Keyword;
import com.example.ar_furniture_application.WebServices.Models.ProductViewLog;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private static final String TAG = "ProductRepository";
    private final ApiService apiService;

    public ProductRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getProducts(String userId,
                            MutableLiveData<List<CatItem>> productsLiveData,
                            MutableLiveData<String> errorLiveData) {
        Call<List<CatItem>> call = apiService.getProducts(userId);
        call.enqueue(new Callback<List<CatItem>>() {
            @Override
            public void onResponse(Call<List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "getProducts failed: " + response.code());
                    errorLiveData.setValue("Failed to get products");
                }
            }

            @Override
            public void onFailure(Call<List<CatItem>> call, Throwable t) {
                Log.e(TAG, "getProducts error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void searchProducts(String userId, String name,
                               MutableLiveData<List<CatItem>> productsLiveData,
                               MutableLiveData<String> errorLiveData) {
        Call<List<CatItem>> call = apiService.searchItmes(userId, name);
        call.enqueue(new Callback<List<CatItem>>() {
            @Override
            public void onResponse(Call<List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "searchProducts failed: " + response.code());
                    errorLiveData.setValue("Failed to search products");
                }
            }

            @Override
            public void onFailure(Call<List<CatItem>> call, Throwable t) {
                Log.e(TAG, "searchProducts error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void searchKeywords(String keyword,
                               MutableLiveData<List<Keyword>> keywordsLiveData) {
        Call<List<Keyword>> call = apiService.searchKeywords(keyword);
        call.enqueue(new Callback<List<Keyword>>() {
            @Override
            public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    keywordsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Keyword>> call, Throwable t) {
                Log.e(TAG, "searchKeywords error: " + t.getMessage());
            }
        });
    }

    public void getCaptureBySlug(String slug,
                                 MutableLiveData<CaptureResponse> captureLiveData,
                                 MutableLiveData<String> errorLiveData) {
        Call<CaptureResponse> call = apiService.getCaptureBySlug(slug);
        call.enqueue(new Callback<CaptureResponse>() {
            @Override
            public void onResponse(Call<CaptureResponse> call, Response<CaptureResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    captureLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "getCaptureBySlug failed: " + response.code());
                    errorLiveData.setValue("Failed to get 3D capture");
                }
            }

            @Override
            public void onFailure(Call<CaptureResponse> call, Throwable t) {
                Log.e(TAG, "getCaptureBySlug error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void addCartItem(CartItem cartItem,
                            MutableLiveData<Boolean> successLiveData,
                            MutableLiveData<String> errorLiveData) {
        Call<CartItem> call = apiService.addCartItem(cartItem);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    successLiveData.setValue(true);
                } else {
                    Log.e(TAG, "addCartItem failed: " + response.code());
                    errorLiveData.setValue("Failed to add item to cart");
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                Log.e(TAG, "addCartItem error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void logProductView(ProductViewLog productViewLog) {
        Call<Void> call = apiService.logProductView(productViewLog);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "logProductView: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "logProductView error: " + t.getMessage());
            }
        });
    }
}
