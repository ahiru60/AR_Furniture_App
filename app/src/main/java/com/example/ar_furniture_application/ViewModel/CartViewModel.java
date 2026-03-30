package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.Repository.CartRepository;
import com.example.ar_furniture_application.WebServices.Models.CartItem;

import java.util.List;

public class CartViewModel extends ViewModel {

    private final CartRepository cartRepository;

    private final MutableLiveData<List<CartItem>> _cartItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _deleteSuccessLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();

    public LiveData<List<CartItem>> getCartItemsLiveData() { return _cartItemsLiveData; }
    public LiveData<Boolean> getDeleteSuccessLiveData() { return _deleteSuccessLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }

    public CartViewModel() {
        cartRepository = new CartRepository();
    }

    public void loadCartItems(User user) {
        cartRepository.getCartItems(user, _cartItemsLiveData, _errorLiveData);
    }

    public void deleteCartItems(List<CartItem> items) {
        cartRepository.deleteCartItems(items, _deleteSuccessLiveData, _errorLiveData);
    }
}
