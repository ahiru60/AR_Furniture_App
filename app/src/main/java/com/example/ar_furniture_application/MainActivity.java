package com.example.ar_furniture_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ar_furniture_application.Cart.Cart_Fragments.CartFragment;
import com.example.ar_furniture_application.Home.Home_Fragments.CatalogFragment;
import com.example.ar_furniture_application.ProductFragments.SearchFragment;

public class MainActivity extends AppCompatActivity{
    private ImageButton homeImgButton,searchImgButton, cartImgButton,profileImgButton;
    private View mContentView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getTheme()));

//        mContentView = findViewById(R.id.root_layout);
//        mContentView.setFitsSystemWindows(true);
//
//        getWindow().setFlags (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initViews();
    setupBtnClicks();
    setActiveBtn(homeImgButton);

    }

    private void setupBtnClicks() {
        homeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FragmentManager fragmentManager = getSupportFragmentManager();
                setActiveBtn(homeImgButton);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, CatalogFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("home") // Name can be null
                        .commit();
            }
        });

        searchImgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setActiveBtn(searchImgButton);
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
                setActiveBtn(cartImgButton);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, CartFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("notification") // Name can be null
                        .commit();
            }
        });

        profileImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragmentManager = getSupportFragmentManager();
                setActiveBtn(profileImgButton);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ProfileFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("profile") // Name can be null
                        .commit();
            }
        });
    }

    private void initViews() {
        homeImgButton = findViewById(R.id.home_imageBtn);
        searchImgButton = findViewById(R.id.search_imageBtn);
        cartImgButton = findViewById(R.id.cart_imageBtn);
        profileImgButton= findViewById(R.id.profile_imageBtn);
        fragmentManager = getSupportFragmentManager();
    }

    private void setActiveBtn(ImageButton button){

        //button.setBackground(buttonDrawable);
        ImageView [] imagebuttons = new ImageView[] {homeImgButton,searchImgButton, cartImgButton,profileImgButton};
        for(ImageView imagebutton : imagebuttons){
            if( imagebutton!= button){
                Drawable buttonDrawable = imagebutton.getDrawable();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.white));
            }else {
                Drawable buttonDrawable = imagebutton.getDrawable();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.plain_yellow));
            }
        }
    }
}