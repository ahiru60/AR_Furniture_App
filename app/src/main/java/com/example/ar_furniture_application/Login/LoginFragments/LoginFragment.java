package com.example.ar_furniture_application.Login.LoginFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.Hashing;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;  // Declare FirebaseAnalytics

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Button loginButton;
    View signUp, close;
    EditText email, password;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize FirebaseAnalytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));
        FragmentManager fragmentManager = getParentFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        close = view.findViewById(R.id.close);
        email = view.findViewById(R.id.loginEditTextEmailAddress);
        password = view.findViewById(R.id.loginEditTextTextPassword);

        loginButton = view.findViewById(R.id.login_button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = fragmentManager.beginTransaction();
                trans.remove(loginFragment);
                trans.commit();
                fragmentManager.popBackStack();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Hashing hasher = new Hashing();
                String passwordHash = hasher.hashPassword(password.getText().toString());
                UserRequestBody loginRequest = new UserRequestBody(email.getText().toString(), passwordHash);
                Call<User> call = apiService.getUserLoginAuth(loginRequest);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            UserSession userSession = new UserSession(getContext());
                            userSession.createSession(response.body());
                            Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();

                            // Firebase Analytics - Log Login Event
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "email"); // Specify login method
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                            FragmentTransaction trans = fragmentManager.beginTransaction();
                            trans.remove(loginFragment);
                            trans.commit();
                            fragmentManager.popBackStack();
                        } else {
                            try {
                                String errorMessage = response.errorBody().string();
                                Toast.makeText(getContext(), "Failed to login: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to get login and parse error body", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        View signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SignUpFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("profile") // Name can be null
                        .commit();
            }
        });
        return view;
    }
}
