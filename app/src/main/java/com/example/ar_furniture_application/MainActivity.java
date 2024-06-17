package com.example.ar_furniture_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ar_furniture_application.Home.Home_Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    ImageButton homeImgButton,searchImgButton, cartImgButton,profileImgButton;
    View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentView = findViewById(R.id.root_layout);
        mContentView.setFitsSystemWindows(true);

        getWindow().setFlags (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        homeImgButton = findViewById(R.id.home_imageBtn);
        searchImgButton = findViewById(R.id.search_imageBtn);
        cartImgButton = findViewById(R.id.cart_imageBtn);
        profileImgButton= findViewById(R.id.profile_imageBtn);

        FragmentManager fragmentManager = getSupportFragmentManager();
        homeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("home") // Name can be null
                        .commit();
            }
        });

        searchImgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SearchFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("search") // Name can be null
                        .commit();
            }
        });

        cartImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, NotificationsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("notification") // Name can be null
                        .commit();
            }
        });

        profileImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ProfileFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("profile") // Name can be null
                        .commit();
            }
        });
    }

}