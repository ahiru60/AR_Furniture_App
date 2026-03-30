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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.CatalogAdapter;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.ProductFragments.ProductFragment;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.ViewModel.CatalogViewModel;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CatalogFragment extends Fragment implements CatalogAdapter.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "CatalogFragment";

    private String mParam1;
    private String mParam2;
    private TextView message;
    private CatalogAdapter productListsAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String userId;
    private UserSession session;
    private CatalogViewModel viewModel;

    public CatalogFragment() {
        // Required empty public constructor
    }

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
        userId = (session.getCurrentUser() != null) ? session.getCurrentUser().getUserID() : "0";

        progressBar = view.findViewById(R.id.progressBar);
        message = view.findViewById(R.id.message_catalog);
        recyclerView = view.findViewById(R.id.catalog_recyclerView);
        productListsAdapter = new CatalogAdapter(this);
        recyclerView.setAdapter(productListsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        viewModel.getProductsLiveData().observe(getViewLifecycleOwner(), catItems -> {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            productListsAdapter.setCatItems(catItems);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            progressBar.setVisibility(View.GONE);
            message.setText(errorMessage);
            message.setVisibility(View.VISIBLE);
            Log.d(TAG, "Error: " + errorMessage);
        });

        viewModel.getAddToCartSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.loadProducts(userId);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null
                        && layoutManager.findLastCompletelyVisibleItemPosition() == productListsAdapter.getItemCount() - 1) {
                    viewModel.loadProducts(userId);
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClickItem(CatItem item) {
        ProductFragment productFragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        productFragment.setArguments(args);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(item.getFurnitureID()));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_item");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, productFragment)
                .setReorderingAllowed(true)
                .addToBackStack("product")
                .commit();
    }

    @Override
    public void onAddToCartClick(CatItem item) {
        if (session.getCurrentUser() != null) {
            CartItem cartItem = new CartItem(
                    session.getCurrentUser().getCartID(),
                    item.getFurnitureID(), "1", item.getPrice());

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(item.getFurnitureID()));
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "add_to_cart");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            viewModel.addToCart(cartItem);
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_LONG).show();
        }
    }
}
