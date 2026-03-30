package com.example.ar_furniture_application;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ar_furniture_application.Cart.Cart_Fragments.OrdersFragment;
import com.example.ar_furniture_application.Home.Home_Fragments.CatalogFragment;
import com.example.ar_furniture_application.Login.LoginFragments.LoginFragment;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.Models.User;
import com.example.ar_furniture_application.Utills.CapitalCaseUtils;
import com.example.ar_furniture_application.ViewModel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button login, logout;
    private TextView username, email;
    private UserSession userSession;
    private View view;
    private FragmentManager fragmentManager;

    private String mParam1;
    private String mParam2;
    private View order;
    private ConstraintLayout profile;
    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.deep_dark_blue, getActivity().getTheme()));

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupViews();
        setupBtnClicks();

        return view;
    }

    private void setupBtnClicks() {
        login.setOnClickListener(v -> fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, LoginFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("login")
                .commit());

        logout.setOnClickListener(v -> {
            userSession.logout();
            viewModel.loadUser(null);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CatalogFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("home")
                    .commit();
        });
    }

    private void setupViews() {
        fragmentManager = getParentFragmentManager();
        profile = view.findViewById(R.id.profile_header);
        order = view.findViewById(R.id.orders);
        login = view.findViewById(R.id.login);
        logout = view.findViewById(R.id.logout);
        username = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        userSession = new UserSession(getContext());
        User user = userSession.getCurrentUser();

        viewModel.loadUser(user);

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), currentUser -> {
            if (currentUser != null) {
                username.setText(CapitalCaseUtils.toCapitalCase(currentUser.getName()));
                email.setText(currentUser.getEmail());
                profile.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
            } else {
                profile.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            }
        });

        order.setOnClickListener(v -> fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, OrdersFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("orders")
                .commit());
    }
}
