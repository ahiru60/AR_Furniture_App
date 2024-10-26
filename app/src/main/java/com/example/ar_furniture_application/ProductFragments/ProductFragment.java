package com.example.ar_furniture_application.ProductFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ar_furniture_application.Models.CaptureResponse;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.ProductViewLog;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.example.ar_furniture_application.customizations.CustomWebView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CustomWebView webView;
    private CatItem item;
    private LinearLayout linearLayout;
    private ImageView image1,image2,image3;
    private TextView itemName,itemPrice,itemDescription,creatorname;
    public RatingBar ratingBar;
    private Bundle args;
    private CaptureResponse captureResponse;
    private ApiService apiService;

    private Button ARButton,addToCart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2, Serializable item) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable("item",item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        UserSession userSession = new UserSession(getContext());
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.deep_dark_blue, getActivity().getTheme()));
            args = getArguments();
            item = (CatItem) args.getSerializable("item");
            if (item != null) {

                apiService = RetrofitClient.getClient().create(ApiService.class);
                    Call<CaptureResponse> call1 = apiService.getCaptureBySlug(item.getSlug());
                log(userSession, apiService, item,"Viewed product: "+item.getFurnitureID());
                call1.enqueue(new Callback<CaptureResponse>() {
                    @Override
                    public void onResponse(Call<CaptureResponse> call, Response<CaptureResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            captureResponse = response.body();
                            webView = (CustomWebView) view.findViewById(R.id.webView);

                            webView.setInitialScale(110);
                            WebSettings webSettings = webView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setAllowFileAccess(true);
                            webSettings.setDomStorageEnabled(true);
                            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                            webSettings.setUseWideViewPort(true);
                            webSettings.setLoadWithOverviewMode(true);
                            webSettings.setAllowContentAccess(true);
                            webSettings.setGeolocationEnabled(true);
                            webSettings.setDefaultTextEncodingName("utf-8");
                            webView.setWebViewClient(new WebViewClient());
                            if(captureResponse.getEditUrl() != null){
                                webView.loadUrl(captureResponse.getEditUrl());
                            }else{

                            }


                        } else {
                            try {
                                Toast.makeText(getContext(),"Failed to get products: " + response.errorBody().string(),Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onResponse: "+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),"Failed to get products and parse error body",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CaptureResponse> call, Throwable t) {
                        Toast.makeText(getContext(),"Error: " + t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                // Use the item data as needed
                String name = item.getName();
                float rating = item.getRating();
                String price = item.getPrice();
                String stockQuantity = item.getStockQuantity();

                itemName = view.findViewById(R.id.itemName);
                itemPrice = view.findViewById(R.id.itemPrice);
                ratingBar = view.findViewById(R.id.ratingBar);
                itemDescription = view.findViewById(R.id.itemDescription);
                creatorname = view.findViewById(R.id.creatorname);



                linearLayout = view.findViewById(R.id.imageLayout);
                image1 = view.findViewById(R.id.image1);
                image2 = view.findViewById(R.id.image2);
                image3 = view.findViewById(R.id.image3);
                ImageView [] imageViews = { image1,image2,image3};
                int i = 0;
                for(String imageURL : item.getImageURLs()){
                    ImageView imageView = new ImageView(getContext());
                    Picasso.get().load(imageURL)
                            .into(imageViews[i]);
                    linearLayout.addView(imageView);
                    i++;
                    if(i==3){
                        break;
                    }
                }

                itemName.setText(item.getName());

                itemPrice.setText(item.getPrice()+"$");
                itemDescription.setText(item.getDescription());
                creatorname.setText("By "+item.getUsername());
            }


            ARButton = view.findViewById(R.id.ARbutton);
            addToCart = view.findViewById(R.id.addToCart);
            ARButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //webView.loadUrl("");
                    //webView.setVisibility(View.GONE);
                    log(userSession, apiService, item,"Viewed in AR product: "+item.getFurnitureID());
                    Intent intent = new Intent(getActivity(), ARSessionActivity.class);
                    intent.putExtra("furnitureName", item.getName());
                    intent.putExtra("modelURL", captureResponse.getLatestRun().getArtifacts().get(6).getUrl());
                    intent.putExtra("scaleToWorld", captureResponse.getLatestRun().getArtifacts().get(6).getScale_to_world());
                    startActivity(intent);
                }
            });
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                    UserSession session = new UserSession(getContext());
                    Call<CartItem> call;
                    UserSession userSession = new UserSession(getContext());
                    if(userSession.getCurrentUser() != null){
                        CartItem cartItem = new CartItem(userSession.getCurrentUser().getCartID(),item.getFurnitureID(),"1", item.getPrice());
                        call = apiService.addCartItem(cartItem);
                        call.enqueue(new Callback<CartItem>() {
                            @Override
                            public void onResponse(Call<CartItem> call, Response<CartItem> response) {

                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(getContext(),"Item added to cart",Toast.LENGTH_SHORT);
                                } else {
                                    try {
                                        // Convert the error body to a string
                                        String errorMessage = response.errorBody().string();
                                        Toast.makeText(getContext(), "Failed to add item :" + errorMessage, Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "Failed to get add item and parse error body", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), "Failed to get add item and parse error body", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CartItem> call, Throwable t) {
                                Toast.makeText(getContext(),"Error: " + t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });}
                    else {

                        Toast.makeText(getContext(),"user not logged in",Toast.LENGTH_LONG).show();
                    }
                }
            });
        return view;
    }
    private void log(UserSession userSession, ApiService apiService, CatItem item,String message) {
        // Create the request body
        ProductViewLog productViewLog = new ProductViewLog(userSession.getCurrentUser().getUserID(),message);

        // Make the Retrofit call
        Call<Void> call = apiService.logProductView(productViewLog);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "Product view logged successfully");
                } else {
                    Log.e("Retrofit", "Failed to log product view. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error logging product view: " + t.getMessage());
            }
        });
    }
}