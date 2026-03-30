package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.Repository.AuthRepository;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<User> _userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _errorLiveData = new MutableLiveData<>();

    public LiveData<User> getUserLiveData() { return _userLiveData; }
    public LiveData<String> getErrorLiveData() { return _errorLiveData; }

    public LoginViewModel() {
        authRepository = new AuthRepository();
    }

    public void login(UserRequestBody loginRequest) {
        authRepository.login(loginRequest, _userLiveData, _errorLiveData);
    }
}
