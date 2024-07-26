package com.example.ar_furniture_application.Login.LoginFragments;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Sessions.UserSession;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
