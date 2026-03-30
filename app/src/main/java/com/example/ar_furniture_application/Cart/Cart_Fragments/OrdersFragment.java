package com.example.ar_furniture_application.Cart.Cart_Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.OrdersAdapter;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.ViewModel.OrdersViewModel;
import com.example.ar_furniture_application.WebServices.Models.UserRequest;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private OrdersViewModel viewModel;
    private static final String TAG = "OrdersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = view.findViewById(R.id.ordersProgressBar);
        errorTextView = view.findViewById(R.id.ordersErrorTextView);

        viewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
            progressBar.setVisibility(View.GONE);
            ordersRecyclerView.setAdapter(new OrdersAdapter(orders));
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Log.d(TAG, "Error: " + errorMessage);
            progressBar.setVisibility(View.GONE);
            errorTextView.setText(errorMessage);
            errorTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        UserSession userSession = new UserSession(getActivity());
        int userID = Integer.parseInt(userSession.getCurrentUser().getUserID());
        viewModel.loadOrders(new UserRequest(userID));

        return view;
    }
}
