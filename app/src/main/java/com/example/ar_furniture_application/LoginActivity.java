package com.example.ar_furniture_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ar_furniture_application.ProfileFragment;
import com.example.ar_furniture_application.R;

public class LoginActivity extends AppCompatActivity {

    private ImageButton close;

    private TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragmentContainerView, ProfileFragment.class, null)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("profile") // Name can be null
//                        .commit();
//            }
//        });
    }
}