package com.example.ar_furniture_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ar_furniture_application.Home_Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    ImageButton homeImgButton,searchImgButton,notifImgButton,profileImgButton,topSearchImageButton;
    View mContentView;
    ConstraintLayout searchSection;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentView = findViewById(R.id.root_layout);
        mContentView.setFitsSystemWindows(true);

        getWindow().setFlags (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        homeImgButton = findViewById(R.id.home_imageBtn);
        searchImgButton = findViewById(R.id.search_imageBtn);
        notifImgButton= findViewById(R.id.notification_imageBtn);
        profileImgButton= findViewById(R.id.profile_imageBtn);
        searchSection = findViewById(R.id.search_section);
        searchEditText = findViewById(R.id.searchEditText);
        topSearchImageButton = findViewById(R.id.topSearchImageButton);

        homeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("home") // Name can be null
                        .commit();
                setSearchSectionColors(false);
            }
        });

        searchImgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SearchFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("search") // Name can be null
                        .commit();

                setSearchSectionColors(true);

            }
        });

        notifImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, NotificationsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("notification") // Name can be null
                        .commit();
                setSearchSectionColors(false);
            }
        });

        profileImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ProfileFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("profile") // Name can be null
                        .commit();
                setSearchSectionColors(false);
            }
        });
    }

    void setSearchSectionColors(boolean state){
        if(state==true){
        searchSection.setBackground(getDrawable(R.drawable.background_transparent_shape));
        searchEditText.setTextColor(getResources().getColor(R.color.black, null));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black, null));
        searchEditText.setBackground(getDrawable(R.drawable.background_round_edged_black_shape));
        topSearchImageButton.setImageResource(R.drawable.ic_search_black);}
        else{
            searchSection.setBackground(getDrawable(R.drawable.background_gradient_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setBackground(getDrawable(R.drawable.background_round_edged_white_shape));
            topSearchImageButton.setImageResource(R.drawable.ic_search_white);}
    }
}