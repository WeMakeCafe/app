package com.example.wmc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wmc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_app_signup);  //페이지 확인하기

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        BottomNavigationView navView = findViewById(R.id.nav_view_main);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_list, R.id.navigation_review, R.id.navigation_myPage)
//                .build();
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupWithNavController(binding.navViewMain, navController);
    }
}