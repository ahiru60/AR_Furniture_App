package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Repository.CartRepository;
import com.example.ar_furniture_application.Repository.OrderRepository;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.OrderRequest;
import com.example.ar_furniture_application.WebServices.Models.OrderResponse;

import java.util.List;

public class CheckoutViewModel extends ViewModel {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final MutableLiveData<OrderResponse> _orderResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _deleteCartSuccessLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();

    public LiveData<OrderResponse> getOrderResponseLiveData() { return _orderResponseLiveData; }
    public LiveData<Boolean> getDeleteCartSuccessLiveData() { return _deleteCartSuccessLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }

    public CheckoutViewModel() {
        orderRepository = new OrderRepository();
        cartRepository = new CartRepository();
    }

    public void placeOrder(OrderRequest orderRequest) {
        orderRepository.placeOrder(orderRequest, _orderResponseLiveData, _errorLiveData);
    }

    public void deleteCartItems(List<CartItem> items) {
        cartRepository.deleteCartItems(items, _deleteCartSuccessLiveData, _errorLiveData);
    }
}
