package com.example.ar_furniture_application.Login.LoginFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.ViewModel.LoginViewModel;
import com.example.ar_furniture_application.WebServices.Hashing;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    private LoginViewModel viewModel;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));

        FragmentManager fragmentManager = getParentFragmentManager();
        close = view.findViewById(R.id.close);
        email = view.findViewById(R.id.loginEditTextEmailAddress);
        password = view.findViewById(R.id.loginEditTextTextPassword);
        loginButton = view.findViewById(R.id.login_button);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            UserSession userSession = new UserSession(getContext());
            userSession.createSession(user);
            Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "email");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.remove(LoginFragment.this);
            trans.commit();
            fragmentManager.popBackStack();
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getContext(), "Failed to login: " + errorMessage, Toast.LENGTH_SHORT).show()
        );

        close.setOnClickListener(v -> {
            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.remove(LoginFragment.this);
            trans.commit();
            fragmentManager.popBackStack();
        });

        loginButton.setOnClickListener(v -> {
            Hashing hasher = new Hashing();
            String passwordHash = hasher.hashPassword(password.getText().toString());
            UserRequestBody loginRequest = new UserRequestBody(email.getText().toString(), passwordHash);
            viewModel.login(loginRequest);
        });

        View signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, SignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("profile")
                    .commit();
        });

        return view;
    }
}
