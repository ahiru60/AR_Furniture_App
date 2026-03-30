package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Models.CaptureResponse;
import com.example.ar_furniture_application.Repository.ProductRepository;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.ProductViewLog;

public class ProductViewModel extends ViewModel {

    private final ProductRepository productRepository;

    private final MutableLiveData<CaptureResponse> _captureLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _addToCartSuccessLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();

    public LiveData<CaptureResponse> getCaptureLiveData() { return _captureLiveData; }
    public LiveData<Boolean> getAddToCartSuccessLiveData() { return _addToCartSuccessLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }

    public ProductViewModel() {
        productRepository = new ProductRepository();
    }

    public void loadCapture(String slug) {
        productRepository.getCaptureBySlug(slug, _captureLiveData, _errorLiveData);
    }

    public void addToCart(CartItem cartItem) {
        productRepository.addCartItem(cartItem, _addToCartSuccessLiveData, _errorLiveData);
    }

    public void logProductView(ProductViewLog log) {
        productRepository.logProductView(log);
    }
}
