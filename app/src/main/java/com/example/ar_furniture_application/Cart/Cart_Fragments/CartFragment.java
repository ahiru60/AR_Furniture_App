package com.example.ar_furniture_application.Cart.Cart_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.CartItemListAdapter;
import com.example.ar_furniture_application.LoginActivity;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements CartItemListAdapter.CartOnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ApiService apiService;
    private View view;
    private TextView itemsDeleteBtn;
    private ProgressBar progressBar;
    private ImageButton loginBtn;
    private TextView noItemsText;
    private boolean isSelectedMode = false;
    private CartItemListAdapter cartItemListAdapter;
    private static List<CartItem> items = new ArrayList<>();
    private static List<CartItem> checkedItems = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        itemsDeleteBtn = view.findViewById(R.id.itemsDeleteBtn);
        RecyclerView recyclerView = view.findViewById(R.id.orderListRecyclerView);
        LinearLayout checkoutBtnLayout = view.findViewById(R.id.checkoutBtnLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        cartItemListAdapter = new CartItemListAdapter(this);
        noItemsText = view.findViewById(R.id.message_cart);
        progressBar = view.findViewById(R.id.progressBar);
        loginBtn = view.findViewById(R.id.loginBtn);

        UserSession session = new UserSession(getContext());
        if (session.getCurrentUser() != null) {

            Call<List<CartItem>> call;
            User itemRequest = session.getCurrentUser();
            call = apiService.getCartItems(itemRequest);
            call.enqueue(new Callback<List<CartItem>>() {

                @Override
                public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        items = response.body();
                        System.out.println('l');
                        if (items.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            checkoutBtnLayout.setVisibility(View.VISIBLE);
                            cartItemListAdapter.addData(items);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(cartItemListAdapter);
                            Toast.makeText(getContext(), "Items loaded", Toast.LENGTH_SHORT);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            noItemsText.setVisibility(View.VISIBLE);
                        }
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Toast.makeText(getContext(), "Failed to add item :" + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to get add item and parse error body", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<CartItem>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    noItemsText.setText("Faild to load items");
                    noItemsText.setVisibility(View.VISIBLE);
                }
            });
        } else {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noItemsText.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);

        }
        itemsDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items = cartItemListAdapter.getItems();
                checkedItems = cartItemListAdapter.getCheckedItems();

                Call<ResponseBody> call;
                User itemRequest = session.getCurrentUser();
                call = apiService.removeCartItems(checkedItems);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (CartItem item : checkedItems) {
                                cartItemListAdapter.notifyItemRemoved(items.indexOf(item));
                                items.remove(item);

                            }
                            checkedItems.clear();
                            System.out.println(items);
                            Toast.makeText(getContext(), "Items removed", Toast.LENGTH_SHORT);
                        } else {
                            try {
                                // Convert the error body to a string
                                String errorMessage = response.errorBody().string();
                                Toast.makeText(getContext(), "Failed to remove item :" + errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to remove item and parse error body", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                isSelectedMode = cartItemListAdapter.getSelectedMode();
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