package com.example.ar_furniture_application.Cart.Cart_Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.ViewModel.CheckoutViewModel;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.OrderItem;
import com.example.ar_furniture_application.WebServices.Models.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {

    public static final String TAG = "CheckoutFragment";
    private List<CartItem> cartItems;
    private LinearLayout itemsContainer;
    private TextView totalPriceTextView;
    private Button payButton;
    private CheckoutViewModel viewModel;
    private String selectedPaymentMethod;
    private Button cardBtn;
    private Button cashBtn;
    private Button pointsBtn;
    private FragmentManager fragmentManager;
    private double totalPrice = 0.0;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cartItems = (List<CartItem>) getArguments().getSerializable("cart_items");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.deep_dark_blue, getActivity().getTheme()));

        fragmentManager = getActivity().getSupportFragmentManager();
        itemsContainer = view.findViewById(R.id.itemsContainer);
        totalPriceTextView = view.findViewById(R.id.totalPrice);
        payButton = view.findViewById(R.id.payBtn);
        cardBtn = view.findViewById(R.id.cardBtn);
        cashBtn = view.findViewById(R.id.cashBtn);
        pointsBtn = view.findViewById(R.id.pointsBtn);
        cashBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
        selectedPaymentMethod = "Cash";

        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);

        viewModel.getOrderResponseLiveData().observe(getViewLifecycleOwner(), orderResponse -> {
            Log.d(TAG, "Order placed: " + orderResponse.getOrderID());
            Toast.makeText(getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
            viewModel.deleteCartItems(cartItems);
        });

        viewModel.getDeleteCartSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                cartItems.clear();
                itemsContainer.removeAllViews();
                totalPriceTextView.setText("Total: $0.00");
                fragmentManager.beginTransaction().remove(CheckoutFragment.this);
                fragmentManager.popBackStack();
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Log.e(TAG, "Error: " + errorMessage);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                LinearLayout itemLayout = new LinearLayout(getContext());
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                itemLayout.setPadding(0, 10, 0, 10);

                TextView itemNameTextView = new TextView(getContext());
                itemNameTextView.setText(item.getName());
                itemNameTextView.setTextColor(getResources().getColor(R.color.white));
                itemNameTextView.setTextSize(18);
                itemNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                TextView itemPriceTextView = new TextView(getContext());
                itemPriceTextView.setText("$" + String.format("%.2f", Double.valueOf(item.getPrice())));
                itemPriceTextView.setTextColor(getResources().getColor(R.color.white));
                itemPriceTextView.setTextSize(18);
                itemPriceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                itemLayout.addView(itemNameTextView);
                itemLayout.addView(itemPriceTextView);
                itemsContainer.addView(itemLayout);

                totalPrice += Double.valueOf(item.getPrice());
            }
            totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
        }

        cardBtn.setOnClickListener(v -> {
            selectedPaymentMethod = "Card";
            pointsBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
            cashBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
            cardBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
        });

        cashBtn.setOnClickListener(v -> {
            selectedPaymentMethod = "Cash";
            pointsBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
            cashBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
            cardBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
        });

        pointsBtn.setOnClickListener(v -> {
            selectedPaymentMethod = "Points";
            pointsBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
            cashBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
            cardBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
        });

        payButton.setOnClickListener(v -> {
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                orderItems.add(new OrderItem(
                        Integer.valueOf(cartItem.getFurnitureID()),
                        Integer.valueOf(cartItem.getQuantity()),
                        Double.valueOf(cartItem.getPrice())));
            }
            UserSession userSession = new UserSession(getContext());
            User user = userSession.getCurrentUser();
            OrderRequest orderRequest = new OrderRequest(
                    Integer.parseInt(user.getUserID()),
                    totalPrice,
                    user.getAddress(),
                    selectedPaymentMethod,
                    orderItems);
            viewModel.placeOrder(orderRequest);
        });

        return view;
    }
}
