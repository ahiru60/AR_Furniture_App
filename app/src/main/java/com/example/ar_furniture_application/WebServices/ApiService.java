package com.example.ar_furniture_application.WebServices;

import com.example.ar_furniture_application.Roles.Role;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("users/email")
    Call<List<UserDoa>> getUserByEmail(String email);

    @POST("users")
    Call<UserDoa> createUser(@Body Role user);
}
