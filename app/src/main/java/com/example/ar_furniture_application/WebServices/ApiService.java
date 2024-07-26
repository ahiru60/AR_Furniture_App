package com.example.ar_furniture_application.WebServices;


import com.example.ar_furniture_application.Login.LoginFragments.User;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.Keyword;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth")
    Call<User> getUserLoginAuth(@Body UserRequestBody loginRequest);

    @GET("users")
    Call<User> getUsers();

    @POST("users")
    Call<User> createUser(@Body UserRequestBody loginRequest);

    @POST("furniture")
    Call <List<CatItem>> getProducts(UserRequestBody productRequest);

    @GET("furniture")
    Call <List<CatItem>> getProducts();

    @GET("furniture/like-keywords/{keyword}")
    Call <List<Keyword>> searchKeywords(@Path("keyword") String keyword);
    @GET("furniture/like-items/{name}")
    Call <List<CatItem>> searchItmes(@Path("name") String name);

    @GET("3dgs")
    Call <String> getWebView();

}



