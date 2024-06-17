package com.example.ar_furniture_application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface SignInCallback {
    void onSignInResult(Task<GoogleSignInAccount> task);
}
