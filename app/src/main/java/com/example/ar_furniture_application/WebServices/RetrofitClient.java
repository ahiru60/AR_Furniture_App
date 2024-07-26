package com.example.ar_furniture_application.WebServices;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    //private static final String BASE_URL = "http://192.168.8.118:3000/";
    private static final String BASE_URL = "http://10.0.2.2:3000/";
//    private static final String BASE_URL = "https://ar-app-bkend.netlify.app/.netlify/functions/api/";
    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

