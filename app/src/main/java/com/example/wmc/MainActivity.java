package com.example.wmc;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.wmc.databinding.ActivityMainBinding;
import com.example.wmc.ui.Fragment.Bottom_ReviewFragment;
import com.example.wmc.ui.Fragment.HomeFragment;
import com.example.wmc.ui.Fragment.ListCafelistFragment;
import com.example.wmc.ui.Fragment.MyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static long mem_num = 1;

    HomeFragment homeFragment;
    ListCafelistFragment listCafelistFragment;
    Bottom_ReviewFragment bottom_reviewFragment;
    MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeFragment = new HomeFragment();
        listCafelistFragment = new ListCafelistFragment();
        bottom_reviewFragment = new Bottom_ReviewFragment();
        myPageFragment = new MyPageFragment();

//        setContentView(R.layout.fragment_image_add_test); //페이지 확인하기

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_list_cafelist, R.id.navigation_bottom_review, R.id.navigation_myPage)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.navViewMain, navController);

//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.navigation_home:
//                        Toast.makeText(getApplicationContext(), "홈 탭", Toast.LENGTH_SHORT).show();
//
////                        int stack_count = getSupportFragmentManager().getBackStackEntryCount();
////                        for(int i = stack_count; i > 0; i--){
////                            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(i-1)).commit();
////                        }
//                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
//                        return true;
//
//                    case R.id.navigation_list_cafelist:
//                        Toast.makeText(getApplicationContext(), "카페목록 탭", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, listCafelistFragment).commit();
//                        return true;
//
//                    case R.id.navigation_bottom_review:
//                        Toast.makeText(getApplicationContext(), "리뷰 탭", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, bottom_reviewFragment).commit();
//                        return true;
//
//                    case R.id.navigation_myPage:
//                        Toast.makeText(getApplicationContext(), "마이페이지 탭", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myPageFragment).commit();
//                        return true;
//                }
//
//                return false;
//            }
//        });
    }

        // fragment_Cafe_detail_more의 리싸이클러뷰 푸터 + fragment_review_tag의 메인코드는
        // 너무 길어서 구글 드라이브 졸업프로젝트 -> 디자인에 넣어두었습니다.
}