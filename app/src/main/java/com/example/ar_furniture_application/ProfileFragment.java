package com.example.ar_furniture_application;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button login,logout;
    private TextView username,email,biography;
    private UserSession userSession;
    private View view;
    private FragmentManager fragmentManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View order;
    private ConstraintLayout profile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.deep_dark_blue, getActivity().getTheme()));
       setupViews();
       setupBtnClicks();
        return view;
    }

    private void setupBtnClicks() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, LoginFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("login") // Name can be null
                        .commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.logout();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, CatalogFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("home") // Name can be null
                        .commit();

            }
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
        if(user!= null){
            username.setText(CapitalCaseUtils.toCapitalCase(user.getName()));
            email.setText(user.getEmail());
            logout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }
        else {
            profile.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, OrdersFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("orders") // Name can be null
                        .commit();
            }
        });
    }

}
