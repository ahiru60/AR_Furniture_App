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

import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.OrderItem;
import com.example.ar_furniture_application.WebServices.Models.OrderRequest;
import com.example.ar_furniture_application.WebServices.Models.OrderResponse;
import com.example.ar_furniture_application.WebServices.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment {

    public static final String TAG = "CheckoutFragment";
    private List<CartItem> cartItems; // List to hold the cart items
    private LinearLayout itemsContainer;
    private TextView totalPriceTextView;
    private Button payButton;
    private ApiService apiService;
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

        // Retrieve the cart items from the arguments (if available)
        if (getArguments() != null) {
            cartItems = (List<CartItem>) getArguments().getSerializable("cart_items");
        }

        // Initialize the API service
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.deep_dark_blue, getActivity().getTheme()));
        // Initialize the views
        fragmentManager = getActivity().getSupportFragmentManager();
        itemsContainer = view.findViewById(R.id.itemsContainer);
        totalPriceTextView = view.findViewById(R.id.totalPrice);
        payButton = view.findViewById(R.id.payBtn);
        Button cardBtn = view.findViewById(R.id.cardBtn);
        Button cashBtn = view.findViewById(R.id.cashBtn);
        Button pointsBtn = view.findViewById(R.id.pointsBtn);
        cashBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
        selectedPaymentMethod = "Cash";


        // Populate the items and calculate the total price
        if (cartItems != null && !cartItems.isEmpty()) {


            for (CartItem item : cartItems) {
                // Create a horizontal LinearLayout for each item
                LinearLayout itemLayout = new LinearLayout(getContext());
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                itemLayout.setPadding(0, 10, 0, 10);

                // Create a TextView for the item name (aligned left)
                TextView itemNameTextView = new TextView(getContext());
                itemNameTextView.setText(item.getName());
                itemNameTextView.setTextColor(getResources().getColor(R.color.white));
                itemNameTextView.setTextSize(18);
                itemNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1)); // Weight 1 to take up space

                // Create a TextView for the item price (aligned right)
                TextView itemPriceTextView = new TextView(getContext());
                itemPriceTextView.setText("$" + String.format("%.2f", Double.valueOf(item.getPrice())));
                itemPriceTextView.setTextColor(getResources().getColor(R.color.white));
                itemPriceTextView.setTextSize(18);
                itemPriceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Add the name and price TextViews to the itemLayout
                itemLayout.addView(itemNameTextView);
                itemLayout.addView(itemPriceTextView);

                // Add the itemLayout to the itemsContainer
                itemsContainer.addView(itemLayout);

                // Add the item's price to the total
                totalPrice += Double.valueOf(item.getPrice());
            }

            // Update the total price TextView
            totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
        }
        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentMethod = "Card";
                pointsBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
                cashBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
                cardBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
            }
        });

        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentMethod = "Cash";
                pointsBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
                cashBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
                cardBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
            }
        });

        pointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentMethod = "Points";
                pointsBtn.setBackground(getResources().getDrawable(R.drawable.background_orange_red_gradient_round_shape));
                cashBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
                cardBtn.setBackground(getResources().getDrawable(R.drawable.background_blue_gradient_round_shape));
            }
        });
        // Set up the pay button to delete the cart items
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a list of order items
                List<OrderItem> items = new ArrayList<>();
                for(CartItem cartItem : cartItems) {
                    items.add(new OrderItem(Integer.valueOf(cartItem.getFurnitureID()), Integer.valueOf(cartItem.getQuantity()), Double.valueOf(cartItem.getPrice())));
                }
                UserSession userSession = new UserSession(getContext());
                User user = userSession.getCurrentUser();
                // Create an OrderRequest object
                OrderRequest orderRequest = new OrderRequest(Integer.parseInt(user.getUserID()), totalPrice, user.getAddress(), selectedPaymentMethod, items);

                Call<OrderResponse> call = apiService.placeOrder(orderRequest);

                call.enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Handle successful response
                            OrderResponse orderResponse = response.body();
                            Log.d("Order Success", "Order ID: " + orderResponse.getOrderID() + ", Message: " + orderResponse.getMessage());
                            Toast.makeText(getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                            // After the order is successfully placed, delete the cart items
                            deleteCartItems();
                        } else {
                            // Handle failure in response
                            try {
                                Log.e("Order Failure", "Failed to place order"+response.errorBody().string());
                                Toast.makeText(getContext(), "Failed to place order", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        // Handle request failure (e.g., network error)
                        Log.e("API Error", t.getMessage());
                        Toast.makeText(getContext(), "Error occurred while placing order", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        return view;
    }

    private void deleteCartItems() {
        if (cartItems != null && !cartItems.isEmpty()) {
            // Prepare the cart items to be deleted
            Call<ResponseBody> call = apiService.deleteCartItems(cartItems);

            // Call the delete API
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Handle successful deletion
                        //Toast.makeText(getContext(), "Items deleted successfully", Toast.LENGTH_SHORT).show();
                        cartItems.clear(); // Clear the local cart items list
                        itemsContainer.removeAllViews(); // Clear the UI
                        totalPriceTextView.setText("Total: $0.00");
                        fragmentManager.beginTransaction()
                                .remove(CheckoutFragment.this);
                        fragmentManager.popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete items", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error occurred while deleting items", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
