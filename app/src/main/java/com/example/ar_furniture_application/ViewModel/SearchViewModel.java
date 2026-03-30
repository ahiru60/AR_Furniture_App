package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Repository.ProductRepository;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.Keyword;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final ProductRepository productRepository;

    private final MutableLiveData<List<CatItem>> _searchResultsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Keyword>> _keywordsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _addToCartSuccessLiveData = new MutableLiveData<>();

    public LiveData<List<CatItem>> getSearchResultsLiveData() { return _searchResultsLiveData; }
    public LiveData<List<Keyword>> getKeywordsLiveData() { return _keywordsLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }
    public LiveData<Boolean> getAddToCartSuccessLiveData() { return _addToCartSuccessLiveData; }

    public SearchViewModel() {
        productRepository = new ProductRepository();
    }

    public void searchProducts(String userId, String searchTerm) {
        productRepository.searchProducts(userId, searchTerm, _searchResultsLiveData, _errorLiveData);
    }

    public void searchKeywords(String keyword) {
        productRepository.searchKeywords(keyword, _keywordsLiveData);
    }

    public void addToCart(CartItem cartItem) {
        productRepository.addCartItem(cartItem, _addToCartSuccessLiveData, _errorLiveData);
    }
}
