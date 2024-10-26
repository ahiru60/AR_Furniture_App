package com.example.ar_furniture_application.WebServices;


import com.example.ar_furniture_application.Models.CaptureResponse;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.GetOrderResponse;
import com.example.ar_furniture_application.WebServices.Models.Keyword;
import com.example.ar_furniture_application.WebServices.Models.OrderRequest;
import com.example.ar_furniture_application.WebServices.Models.OrderResponse;
import com.example.ar_furniture_application.WebServices.Models.ProductViewLog;
import com.example.ar_furniture_application.WebServices.Models.UserRequest;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String adhiru = "20252478-2c55-4aca-ac52-ae92a0ec8673-a513462-88fc-4ecb-9fe3-f35ae603ffd4";
    String a_hiru = "790532d0-128e-4e51-bf58-85137b71a1e5-75fd8f8-4971-4cfe-9690-26e0459174d7";
    @POST("auth")
    Call<User> getUserLoginAuth(@Body UserRequestBody loginRequest);

    @GET("users")
    Call<User> getUsers();

    @POST("users")
    Call<User> createUser(@Body UserRequestBody loginRequest);

    @POST("cart/item")
    Call<CartItem> addCartItem(@Body CartItem cartItem);

    @POST("cart/items")
    Call<List<CartItem>> getCartItems(@Body User itemRequest);
    @POST("cart/delete")
    Call<ResponseBody> deleteCartItems(@Body List<CartItem> items);

    @POST("furniture")
    Call <List<CatItem>> getProducts(@Body UserRequestBody productRequest);

    @GET("furniture/{userId}")
    Call <List<CatItem>> getProducts(@Path("userId") String userId);

    @GET("furniture/like-keywords/{keyword}")
    Call <List<Keyword>> searchKeywords(@Path("keyword") String keyword);
    @GET("furniture/like-items/{userId}/{name}")
    Call <List<CatItem>>searchItmes(@Path("userId") String userId,@Path("name") String name);

    @POST("3dgs/capture")
    Call <String> createCapture();
    @Headers("authorization: luma-api-key="+a_hiru)
    @GET("3dgs/get-capture/{slug}")
    Call<CaptureResponse> getCaptureBySlug(
            @Path("slug") String slug
    );

    @POST("cart/place-order")
    Call<OrderResponse> placeOrder(@Body OrderRequest orderRequest);
    @POST("cart/get-orders")
    Call<List<GetOrderResponse>> getOrders(@Body UserRequest userRequest);

    @POST("furniture/log-view")
    Call<Void> logProductView(@Body ProductViewLog productViewLog);

}



