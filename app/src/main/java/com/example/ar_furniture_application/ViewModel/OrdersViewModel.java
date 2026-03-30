package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Repository.OrderRepository;
import com.example.ar_furniture_application.WebServices.Models.GetOrderResponse;
import com.example.ar_furniture_application.WebServices.Models.UserRequest;

import java.util.List;

public class OrdersViewModel extends ViewModel {

    private final OrderRepository orderRepository;

    private final MutableLiveData<List<GetOrderResponse>> _ordersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();

    public LiveData<List<GetOrderResponse>> getOrdersLiveData() { return _ordersLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }

    public OrdersViewModel() {
        orderRepository = new OrderRepository();
    }

    public void loadOrders(UserRequest userRequest) {
        orderRepository.getOrders(userRequest, _ordersLiveData, _errorLiveData);
    }
}
