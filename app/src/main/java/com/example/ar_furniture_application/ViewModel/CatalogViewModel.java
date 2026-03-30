package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Repository.ProductRepository;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.ProductViewLog;

import java.util.List;

public class CatalogViewModel extends ViewModel {

    private final ProductRepository productRepository;

    private final MutableLiveData<List<CatItem>> _productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _addToCartSuccessLiveData = new MutableLiveData<>();

    public LiveData<List<CatItem>> getProductsLiveData() { return _productsLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }
    public LiveData<Boolean> getAddToCartSuccessLiveData() { return _addToCartSuccessLiveData; }

    public CatalogViewModel() {
        productRepository = new ProductRepository();
    }

    public void loadProducts(String userId) {
        productRepository.getProducts(userId, _productsLiveData, _errorLiveData);
    }

    public void addToCart(CartItem cartItem) {
        productRepository.addCartItem(cartItem, _addToCartSuccessLiveData, _errorLiveData);
    }

    public void logProductView(ProductViewLog log) {
        productRepository.logProductView(log);
    }
}
