package com.example.ar_furniture_application.Cart.Cart_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.CartItemListAdapter;
import com.example.ar_furniture_application.LoginActivity;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.ViewModel.CartViewModel;
import com.example.ar_furniture_application.WebServices.Models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartItemListAdapter.CartOnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "CartFragment";

    private String mParam1;
    private String mParam2;
    private CartViewModel viewModel;
    private View view;
    private TextView itemsDeleteBtn;
    private ProgressBar progressBar;
    private ImageButton loginBtn;
    private Button checkoutBtn;
    private TextView noItemsText;
    private LinearLayout itemsLayout;
    private FragmentManager fragmentManager;
    private CartItemListAdapter cartItemListAdapter;
    private List<CartItem> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayout checkoutBtnLayout;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));

        fragmentManager = getActivity().getSupportFragmentManager();
        itemsDeleteBtn = view.findViewById(R.id.itemsDeleteBtn);
        itemsLayout = view.findViewById(R.id.itemsLayout);
        recyclerView = view.findViewById(R.id.orderListRecyclerView);
        checkoutBtnLayout = view.findViewById(R.id.checkoutBtnLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        cartItemListAdapter = new CartItemListAdapter(this);
        noItemsText = view.findViewById(R.id.message_cart);
        progressBar = view.findViewById(R.id.progressBar);
        checkoutBtn = view.findViewById(R.id.checkoutBtn);
        loginBtn = view.findViewById(R.id.loginBtn);

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        viewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            items = cartItems;
            if (items.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                checkoutBtnLayout.setVisibility(View.VISIBLE);
                itemsLayout.setGravity(Gravity.BOTTOM);
                cartItemListAdapter.addData(items);
                progressBar.setVisibility(View.GONE);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(cartItemListAdapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                noItemsText.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getDeleteSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                List<CartItem> checkedItems = cartItemListAdapter.getCheckedItems();
                for (CartItem item : checkedItems) {
                    cartItemListAdapter.notifyItemRemoved(items.indexOf(item));
                    items.remove(item);
                }
                checkedItems.clear();
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Log.d(TAG, "Error: " + errorMessage);
            noItemsText.setText(errorMessage);
            noItemsText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        UserSession session = new UserSession(getContext());
        if (session.getCurrentUser() != null) {
            viewModel.loadCartItems(session.getCurrentUser());
        } else {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noItemsText.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
        }

        itemsDeleteBtn.setOnClickListener(v -> {
            List<CartItem> checkedItems = cartItemListAdapter.getCheckedItems();
            viewModel.deleteCartItems(checkedItems);
        });

        checkoutBtn.setOnClickListener(v -> {
            if (items.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("cart_items", (ArrayList<CartItem>) items);
                CheckoutFragment checkoutFragment = new CheckoutFragment();
                checkoutFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, checkoutFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("CheckoutFragment")
                        .commit();
            } else {
                Toast.makeText(getContext(), "No items in cart", Toast.LENGTH_SHORT).show();
            }
        });

        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean isSelectedMode = cartItemListAdapter.getSelectedMode();
                if (isSelectedMode) {
                    cartItemListAdapter.getCheckedItems().clear();
                    itemsDeleteBtn.setVisibility(View.GONE);
                    cartItemListAdapter.setSelectedMode(false);
                    cartItemListAdapter.notifyDataSetChanged();
                } else {
                    getFragmentManager().popBackStack();
                }
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(callback);

        return view;
    }

    @Override
    public void ItemsOnLongClick() {
        itemsDeleteBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteBtnOnClick() {
    }
}
