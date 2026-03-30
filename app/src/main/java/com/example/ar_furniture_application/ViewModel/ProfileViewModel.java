package com.example.ar_furniture_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_furniture_application.Models.User;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<User> _userLiveData = new MutableLiveData<>();

    public LiveData<User> getUserLiveData() { return _userLiveData; }

    public void loadUser(User user) {
        _userLiveData.setValue(user);
    }
}
