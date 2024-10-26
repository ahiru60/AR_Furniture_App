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
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.ErrorResponse;
import com.example.ar_furniture_application.WebServices.Hashing;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;  // Declare FirebaseAnalytics

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize FirebaseAnalytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        if (getArguments() != null) {
            // Your existing code for argument handling
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));
        FragmentManager fragmentManager = getParentFragmentManager();
        SignUpFragment signUpFragment = new SignUpFragment();
        LoginFragment loginFragment = new LoginFragment();
        Button signUp = view.findViewById(R.id.signupButton);
        ImageButton close = view.findViewById(R.id.close);
        EditText name, phone, addressLine1, addressLine2, addressLine3, email, password, rePassword;
        name = view.findViewById(R.id.editTextName);
        phone = view.findViewById(R.id.editTextPhone);
        addressLine1 = view.findViewById(R.id.editTextAddressLine1);
        addressLine2 = view.findViewById(R.id.editTextAddressLine2);
        addressLine3 = view.findViewById(R.id.editTextAddressLine3);
        email = view.findViewById(R.id.editTextEmailAddress);
        password = view.findViewById(R.id.editTextpassword);
        rePassword = view.findViewById(R.id.editTextRepeat_password);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(rePassword.getText().toString())) {
                    Hashing hasher = new Hashing();
                    String passwordHash = hasher.hashPassword(password.getText().toString());
                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                    String address = addressLine1.getText().toString() + ", " + addressLine2.getText().toString() + ", " + addressLine3.getText().toString() + ".";
                    UserRequestBody loginRequest = new UserRequestBody(name.getText().toString(), phone.getText().toString(), address, email.getText().toString(), passwordHash, "customer");
                    Call<User> call = apiService.createUser(loginRequest);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                UserSession userSession = new UserSession(getContext());
                                userSession.createSession(response.body());
                                // Firebase Analytics - Log Sign Up Event
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.METHOD, "email"); // Specify the sign-up method (e.g., "email")
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                                FragmentTransaction trans = fragmentManager.beginTransaction();
                                trans.remove(signUpFragment);
                                trans.remove(loginFragment);
                                trans.commit();
                                fragmentManager.popBackStack();
                                Toast.makeText(getContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();



                            } else {
                                try {
                                    Gson gson = new Gson();
                                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                                    String errorMessage = errorResponse.getError();
                                    Toast.makeText(getContext(), "Failed to get signup :" + errorMessage, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Failed to get signup and parse error body", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = fragmentManager.beginTransaction();
                trans.remove(signUpFragment);
                trans.commit();
                fragmentManager.popBackStack();
            }
        });

        return view;
    }
}
