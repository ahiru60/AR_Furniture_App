package com.example.ar_furniture_application.Login.LoginFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Roles.Role;
import com.example.ar_furniture_application.Sessions.UserSession;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.example.ar_furniture_application.WebServices.UserDoa;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        LinearLayout loginGoogle = view.findViewById(R.id.login_google);
        TextView signup = view.findViewById(R.id.signup);
        TextView forgotPwd = view.findViewById(R.id.forgot_password);

        LoginManager loginManager = new LoginManager(getString(R.string.web_id),getActivity());

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                Call<List<UserDoa>> call = apiService.getUserByEmail("someUserId");
                call.enqueue(new Callback <List<UserDoa>>() {
                    @Override
                    public void onResponse(Call<List<UserDoa>> call, Response<List<UserDoa>> response) {
                        if (response.isSuccessful()) {
                            List<UserDoa> users = response.body();
                            // Handle the response
                            if(users.size() == 1){
                                loginManager.signIn();
                                new UserSession(getContext()).createSession(users.get(0));
                            }

                        } else {
                            // Handle the error
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserDoa>> call, Throwable t) {
                        // Handle the failure
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView3, signUpFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPwdFragment forgotPwdFragment = new ForgotPwdFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView3, forgotPwdFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }




}
