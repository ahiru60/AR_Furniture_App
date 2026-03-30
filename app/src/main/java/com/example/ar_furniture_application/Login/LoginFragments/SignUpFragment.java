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
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.ViewModel.SignUpViewModel;
import com.example.ar_furniture_application.WebServices.Hashing;
import com.example.ar_furniture_application.WebServices.Models.UserRequestBody;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SignUpFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    private SignUpViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));

        FragmentManager fragmentManager = getParentFragmentManager();

        Button signUp = view.findViewById(R.id.signupButton);
        ImageButton close = view.findViewById(R.id.close);
        EditText name = view.findViewById(R.id.editTextName);
        EditText phone = view.findViewById(R.id.editTextPhone);
        EditText addressLine1 = view.findViewById(R.id.editTextAddressLine1);
        EditText addressLine2 = view.findViewById(R.id.editTextAddressLine2);
        EditText addressLine3 = view.findViewById(R.id.editTextAddressLine3);
        EditText email = view.findViewById(R.id.editTextEmailAddress);
        EditText password = view.findViewById(R.id.editTextpassword);
        EditText rePassword = view.findViewById(R.id.editTextRepeat_password);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            UserSession userSession = new UserSession(getContext());
            userSession.createSession(user);

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "email");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.remove(SignUpFragment.this);
            trans.commit();
            fragmentManager.popBackStack();
            Toast.makeText(getContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getContext(), "Failed to sign up: " + errorMessage, Toast.LENGTH_SHORT).show()
        );

        signUp.setOnClickListener(v -> {
            if (password.getText().toString().equals(rePassword.getText().toString())) {
                Hashing hasher = new Hashing();
                String passwordHash = hasher.hashPassword(password.getText().toString());
                String address = addressLine1.getText().toString() + ", "
                        + addressLine2.getText().toString() + ", "
                        + addressLine3.getText().toString() + ".";
                UserRequestBody registerRequest = new UserRequestBody(
                        name.getText().toString(),
                        phone.getText().toString(),
                        address,
                        email.getText().toString(),
                        passwordHash,
                        "customer");
                viewModel.signUp(registerRequest);
            } else {
                Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            }
        });

        close.setOnClickListener(v -> {
            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.remove(SignUpFragment.this);
            trans.commit();
            fragmentManager.popBackStack();
        });

        return view;
    }
}
