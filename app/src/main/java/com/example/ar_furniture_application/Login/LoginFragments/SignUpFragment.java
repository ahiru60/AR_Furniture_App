package com.example.ar_furniture_application.Login.LoginFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Sessions.UserSession;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.ErrorResponse;
import com.example.ar_furniture_application.WebServices.Hashing;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Button signUp = view.findViewById(R.id.signupButton);
        EditText name,email,password,rePassword;
        name = view.findViewById(R.id.editTextName);
        email = view.findViewById(R.id.editTextEmailAddress);
        password = view.findViewById(R.id.editTextpassword);
        rePassword = view.findViewById(R.id.editTextRepeat_password);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().equals(rePassword.getText().toString())){
                    Hashing hasher = new Hashing();
                    String passwordHash = hasher.hashPassword(password.getText().toString());
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                UserRequestBody loginRequest = new UserRequestBody(name.getText().toString(), email.getText().toString(), passwordHash, "customer");
                Call<User> call = apiService.createUser(loginRequest);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            UserSession userSession = new UserSession(getContext());
                            userSession.createSession(response.body());
                            Toast.makeText(getContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            try {
                                // Convert the error body to a string
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

            }
                else{
                    Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
        }
        }

        );

        return view;
    }
}