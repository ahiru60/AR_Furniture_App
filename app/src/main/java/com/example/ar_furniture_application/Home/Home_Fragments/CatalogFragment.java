package com.example.ar_furniture_application.Home.Home_Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.CatalogAdapter;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.ProductFragments.ProductFragment;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.ErrorResponse;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment implements CatalogAdapter.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "CatalogFragment";
    ConstraintLayout productItem;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView message;
    private CatalogAdapter productListsAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String userId;
    private UserSession session;


    public CatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));
        session = new UserSession(getContext());
        if(session.getCurrentUser()!= null){
            userId = session.getCurrentUser().getUserID();
        }else{
            userId = "0";
        }
        progressBar = view.findViewById(R.id.progressBar);
        message = view.findViewById(R.id.message_catalog);
        recyclerView = view.findViewById(R.id.catalog_recyclerView);
        productListsAdapter = new CatalogAdapter(this);
        loadData(new DataCallback() {
            @Override
            public void onDataLoaded(List<CatItem> catItems) {
                // Handle the loaded data here
                // For example, set it to your adapter
                productListsAdapter.setCatItems(catItems);
                recyclerView.setAdapter(productListsAdapter);
            }

            @Override
            public void onDataError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onDataError: " + errorMessage);
                //message.setText(errorMessage);
                recyclerView.setAdapter(null);
            }
        });

        recyclerView.setAdapter(productListsAdapter);
//        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));// Inflate the layout for this fragment
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == productListsAdapter.getItemCount() - 1) {
                    // Load more data
                    loadMoreData();
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClickItem(CatItem item) {
        // Perform the fragment transaction to replace the fragment container with a new fragment
        ProductFragment productFragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);

        // Set the arguments to the fragment
        productFragment.setArguments(args);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(item.getFurnitureID())); // Assuming FurnitureID is the content ID
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_item"); // Set content type, e.g., "furniture"
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContainerView, productFragment)
                .setReorderingAllowed(true)
                .addToBackStack("product") // Name can be null
                .commit();
    }

    @Override
    public void onAddToCartClick(CatItem item){
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
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(item.getFurnitureID())); // Assuming FurnitureID is the content ID
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "add_to_cart"); // Set content type, e.g., "furniture"
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    Toast.makeText(getContext(),"Item added to cart",Toast.LENGTH_SHORT);
                } else {
                    try {
                        // Convert the error body to a string
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getContext(), "Failed to add item :" + errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
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

    private void loadData(DataCallback callback) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<CatItem>> call;
        if (session.getCurrentUser() != null) {
            call = apiService.getProducts(userId);
        } else {

            call = apiService.getProducts(userId);
        }
        call.enqueue(new Callback<List<CatItem>>() {
            @Override
            public void onResponse(Call<List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    userId = "0";
                    callback.onDataLoaded(response.body());
                    System.out.println('k');
                } else {
                    try {
                        progressBar.setVisibility(View.GONE);
                        message.setText("Failed to get products");
                        message.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onResponse: Failed to get products: " + response.errorBody().string());
                        callback.onDataError("Failed to get products: " + response.errorBody().string());
                    } catch (IOException e) {
                        progressBar.setVisibility(View.GONE);
                        message.setText("Failed to get products");
                        message.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                        callback.onDataError("Failed to get products and parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CatItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                message.setText("Failed to get products");
                message.setVisibility(View.VISIBLE);
                callback.onDataError("Error: " + t.getMessage());
            }
        });
    }


    public interface DataCallback {
        void onDataLoaded(List<CatItem> catItems);

        void onDataError(String errorMessage);
    }


    private void loadMoreData() {
        // Assume you have a method to fetch data from the API
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        UserSession session = new UserSession(getContext());
        Call<List<CatItem>> call;
        if (session.getCurrentUser() != null) {
            UserRequestBody loginRequest = new UserRequestBody(session.getCurrentUser().getEmail());
            call = apiService.getProducts(userId);
        } else {
            call = apiService.getProducts(userId);
        }
        call.enqueue(new Callback<List<CatItem>>() {
            @Override
            public void onResponse(Call<List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productListsAdapter.addData(response.body());
                } else {
                    try {
                        // Convert the error body to a string
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        userId = "0";
                        String errorMessage = errorResponse.getError();
                        Toast.makeText(getContext(), "Failed to get signup :" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to get signup and parse error body", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CatItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}