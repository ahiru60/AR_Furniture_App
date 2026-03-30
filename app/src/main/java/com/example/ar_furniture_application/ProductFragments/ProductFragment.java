package com.example.ar_furniture_application.ProductFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.ViewModel.ProductViewModel;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.ProductViewLog;
import com.example.ar_furniture_application.customizations.CustomWebView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CustomWebView webView;
    private CatItem item;
    private LinearLayout linearLayout;
    private ImageView image1, image2, image3;
    private TextView itemName, itemPrice, itemDescription, creatorname;
    public RatingBar ratingBar;
    private ProductViewModel viewModel;
    private Button ARButton, addToCart;

    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String param1, String param2, Serializable item) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable("item", item);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        UserSession userSession = new UserSession(getContext());
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.deep_dark_blue, getActivity().getTheme()));

        Bundle args = getArguments();
        item = (CatItem) args.getSerializable("item");

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        viewModel.getCaptureLiveData().observe(getViewLifecycleOwner(), captureResponse -> {
            webView = view.findViewById(R.id.webView);
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
            if (captureResponse.getEditUrl() != null) {
                webView.loadUrl(captureResponse.getEditUrl());
            }

            ARButton.setOnClickListener(v -> {
                if (userSession.getCurrentUser() != null) {
                    viewModel.logProductView(new ProductViewLog(
                            userSession.getCurrentUser().getUserID(),
                            "Viewed in AR product: " + item.getFurnitureID()));
                }
                Intent intent = new Intent(getActivity(), ARSessionActivity.class);
                intent.putExtra("furnitureName", item.getName());
                intent.putExtra("modelURL", captureResponse.getLatestRun().getArtifacts().get(6).getUrl());
                intent.putExtra("scaleToWorld", captureResponse.getLatestRun().getArtifacts().get(6).getScale_to_world());
                startActivity(intent);
            });
        });

        viewModel.getAddToCartSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Log.e("ProductFragment", "Error: " + errorMessage);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        if (item != null) {
            viewModel.loadCapture(item.getSlug());
            if (userSession.getCurrentUser() != null) {
                viewModel.logProductView(new ProductViewLog(
                        userSession.getCurrentUser().getUserID(),
                        "Viewed product: " + item.getFurnitureID()));
            }

            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            ratingBar = view.findViewById(R.id.ratingBar);
            itemDescription = view.findViewById(R.id.itemDescription);
            creatorname = view.findViewById(R.id.creatorname);
            linearLayout = view.findViewById(R.id.imageLayout);
            image1 = view.findViewById(R.id.image1);
            image2 = view.findViewById(R.id.image2);
            image3 = view.findViewById(R.id.image3);
            ImageView[] imageViews = {image1, image2, image3};

            int i = 0;
            for (String imageURL : item.getImageURLs()) {
                Picasso.get().load(imageURL).into(imageViews[i]);
                i++;
                if (i == 3) break;
            }

            itemName.setText(item.getName());
            itemPrice.setText(item.getPrice() + "$");
            itemDescription.setText(item.getDescription());
            creatorname.setText("By " + item.getUsername());
        }

        ARButton = view.findViewById(R.id.ARbutton);
        addToCart = view.findViewById(R.id.addToCart);

        addToCart.setOnClickListener(v -> {
            if (userSession.getCurrentUser() != null) {
                CartItem cartItem = new CartItem(
                        userSession.getCurrentUser().getCartID(),
                        item.getFurnitureID(), "1", item.getPrice());
                viewModel.addToCart(cartItem);
            } else {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
