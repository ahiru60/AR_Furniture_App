package com.example.ar_furniture_application.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private final ApiService apiService;

    public AuthRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void login(UserRequestBody loginRequest,
                      MutableLiveData<User> userLiveData,
                      MutableLiveData<String> errorLiveData) {
        Call<User> call = apiService.getUserLoginAuth(loginRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "login failed: " + response.code());
                    errorLiveData.setValue("Invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "login error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void createUser(UserRequestBody registerRequest,
                           MutableLiveData<User> userLiveData,
                           MutableLiveData<String> errorLiveData) {
        Call<User> call = apiService.createUser(registerRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "createUser failed: " + response.code());
                    errorLiveData.setValue("Failed to sign up");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "createUser error: " + t.getMessage());
                errorLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }
}
