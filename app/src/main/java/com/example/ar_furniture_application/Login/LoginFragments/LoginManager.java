package com.example.ar_furniture_application.Login.LoginFragments;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class LoginManager {
    private Activity activity;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginFragment";
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;

    public LoginManager() {

    }

    public void signIn() {


    }
}
