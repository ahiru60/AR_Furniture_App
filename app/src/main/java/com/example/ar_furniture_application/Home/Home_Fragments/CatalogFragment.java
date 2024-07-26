package com.example.ar_furniture_application.Home.Home_Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ar_furniture_application.ProductFragments.ProductFragment;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Sessions.UserSession;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.ErrorResponse;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
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
public class CatalogFragment extends Fragment implements CatalogAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ConstraintLayout productItem;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CatalogAdapter productListsAdapter;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        TextView message = view.findViewById(R.id.message);
        productListsAdapter = new CatalogAdapter(this);
        RecyclerView recyclerView = view.findViewById(R.id.search_product_list_recyclerView);
        loadData(new DataCallback() {
            @Override
            public void onDataLoaded(List<CatItem> catItems) {
                // Handle the loaded data here
                // For example, set it to your adapter
                productListsAdapter.setCatItems(catItems);
                RecyclerView recyclerView = view.findViewById(R.id.search_product_list_recyclerView);
                recyclerView.setAdapter(productListsAdapter);
            }

            @Override
            public void onDataError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                message.setText(errorMessage);
                RecyclerView recyclerView = view.findViewById(R.id.search_product_list_recyclerView);
                recyclerView.setAdapter(null);
            }
        });

        recyclerView.setAdapter(productListsAdapter);
//        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));// Inflate the layout for this fragment
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
    public void onItemClick(CatItem item) {
        // Perform the fragment transaction to replace the fragment container with a new fragment
        ProductFragment productFragment = new ProductFragment();

        // Pass data to the new fragment if needed
//        Bundle bundle = new Bundle();
//        bundle.putString("productName", product.getName());
//        diagReportFragment.setArguments(bundle);

        Bundle args = new Bundle();
        args.putSerializable("product",item);

// Set the arguments to the fragment
        productFragment.setArguments(args);

        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, productFragment)
                .setReorderingAllowed(true)
                .addToBackStack("product") // Name can be null
                .commit();
    }

    private void loadData(DataCallback callback) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        UserSession session = new UserSession(getContext());
        Call<List<CatItem>> call;
        if(session.getCurrentUser() != null){
            UserRequestBody loginRequest = new UserRequestBody(session.getCurrentUser().getEmail());
            call = apiService.getProducts(loginRequest);
        }
        else{
            call = apiService.getProducts();
        }
        call.enqueue(new Callback<List<CatItem>>() {
            @Override
            public void onResponse(Call<List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onDataLoaded(response.body());
                } else {
                    try {
                        callback.onDataError("Failed to get products: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onDataError("Failed to get products and parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CatItem>> call, Throwable t) {
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
        if(session.getCurrentUser() != null){
            UserRequestBody loginRequest = new UserRequestBody(session.getCurrentUser().getEmail());
            call = apiService.getProducts(loginRequest);
        }
        else{
            call = apiService.getProducts();
        }
        call.enqueue(new Callback <List<CatItem>>() {
            @Override
            public void onResponse(Call <List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                        productListsAdapter.addData(response.body());
                } else {
                    try {
                        // Convert the error body to a string
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
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