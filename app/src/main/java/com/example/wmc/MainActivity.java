package com.example.wmc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.wmc.databinding.ActivityMainBinding;
import com.example.wmc.ui.Fragment.Bottom_ReviewCafeListFragment;
import com.example.wmc.ui.Fragment.Bottom_ReviewFragment;
import com.example.wmc.ui.Fragment.CafeDetailFragment;
import com.example.wmc.ui.Fragment.HomeFragment;
import com.example.wmc.ui.Fragment.ListCafelistFragment;
import com.example.wmc.ui.Fragment.MyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static long mem_num = 1;

    HomeFragment homeFragment;
    ListCafelistFragment listCafelistFragment;
    Bottom_ReviewFragment bottom_reviewFragment;
    MyPageFragment myPageFragment;

    NavController navController;

    long backpressedTime = 0;
    int recentFragment = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeFragment = new HomeFragment();
        listCafelistFragment = new ListCafelistFragment();
        bottom_reviewFragment = new Bottom_ReviewFragment();
        myPageFragment = new MyPageFragment();
//        setContentView(R.layout.fragment_image_add_test); //페이지 확인하기

        Intent intent = getIntent();
        String text = intent.getStringExtra("mem_num");
        Long numLong = Long.parseLong(text);
        mem_num = numLong;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_list_cafelist, R.id.navigation_bottom_review, R.id.navigation_myPage)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.navViewMain, navController);


//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
//                        Toast.makeText(getApplicationContext(), "홈 탭", Toast.LENGTH_SHORT).show();
//                        // 홈으로 오기 직전 프래그먼트를 알려쥼
//                        Log.d("recentFragment", navController.getCurrentDestination().toString());

                        if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCafeListFragment"))
                            navController.navigate(R.id.bottom_review_cafelist_to_home);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCommentFragment"))
                            navController.navigate(R.id.bottom_review_comment_to_home);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewFragment"))
                            navController.navigate(R.id.bottom_review_to_home);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewTagFragment"))
                            navController.navigate(R.id.bottom_review_tag_to_home);
                        else if(navController.getCurrentDestination().toString().contains("CafeDeleteFragment"))
                            navController.navigate(R.id.cafe_delete_to_home);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailFragment"))
                            navController.navigate(R.id.cafe_detail_to_home);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailMoreFragment"))
                            navController.navigate(R.id.cafe_detail_more_to_home);
                        else if(navController.getCurrentDestination().toString().contains("CafeModifyFragment"))
                            navController.navigate(R.id.cafe_modify_to_home);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationFragment"))
                            navController.navigate(R.id.cafe_registration_to_home);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationTagFragment"))
                            navController.navigate(R.id.cafe_registration_tag_to_home);
                        else if(navController.getCurrentDestination().toString().contains("HomeFragment"))
                            navController.navigate(R.id.home_to_home);
                        else if(navController.getCurrentDestination().toString().contains("ListCafelistFragment"))
                            navController.navigate(R.id.list_cafelist_to_home);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewCommentFragment"))
                            navController.navigate(R.id.mypage_review_comment_to_home);
                        else if(navController.getCurrentDestination().toString().contains("MyPage_ReviewFragment"))
                            navController.navigate(R.id.mypage_review_to_home);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewTagFragment"))
                            navController.navigate(R.id.mypage_review_tag_to_home);
                        else if(navController.getCurrentDestination().toString().contains("MyPageFragment"))
                            navController.navigate(R.id.mypage_to_home);
                        else if(navController.getCurrentDestination().toString().contains("MypageGradeFragment"))
                            navController.navigate(R.id.myPage_grade_to_home);
                        else if(navController.getCurrentDestination().toString().contains("ReviewCommentFragment"))
                            navController.navigate(R.id.review_comment_to_home);
                        else if(navController.getCurrentDestination().toString().contains("ReviewFragment"))
                            navController.navigate(R.id.review_to_home);
                        else if(navController.getCurrentDestination().toString().contains("ReviewTagFragment"))
                            navController.navigate(R.id.review_tag_to_home);
                        else
                            navController.navigate(R.id.navigation_home);
                        break;


                    case R.id.navigation_list_cafelist:
//                        Toast.makeText(getApplicationContext(), "카페목록 탭", Toast.LENGTH_SHORT).show();

                        if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCafeListFragment"))
                            navController.navigate(R.id.bottom_review_cafelist_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCommentFragment"))
                            navController.navigate(R.id.bottom_review_comment_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewFragment"))
                            navController.navigate(R.id.bottom_review_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewTagFragment"))
                            navController.navigate(R.id.bottom_review_tag_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("CafeDeleteFragment"))
                            navController.navigate(R.id.cafe_delete_to_list_cafeList);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailFragment"))
                            navController.navigate(R.id.cafe_detail_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailMoreFragment"))
                            navController.navigate(R.id.cafe_detail_more_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("CafeModifyFragment"))
                            navController.navigate(R.id.cafe_modify_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationFragment"))
                            navController.navigate(R.id.cafe_registration_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationTagFragment"))
                            navController.navigate(R.id.cafe_registration_tag_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("HomeFragment"))
                            navController.navigate(R.id.home_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("ListCafelistFragment"))
                            navController.navigate(R.id.list_cafelist_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewCommentFragment"))
                            navController.navigate(R.id.mypage_review_comment_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("MyPage_ReviewFragment"))
                            navController.navigate(R.id.mypage_review_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewTagFragment"))
                            navController.navigate(R.id.mypage_review_tag_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("MyPageFragment"))
                            navController.navigate(R.id.mypage_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("MypageGradeFragment"))
                            navController.navigate(R.id.myPage_grade_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("ReviewCommentFragment"))
                            navController.navigate(R.id.review_comment_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("ReviewFragment"))
                            navController.navigate(R.id.review_to_list_cafelist);
                        else if(navController.getCurrentDestination().toString().contains("ReviewTagFragment"))
                            navController.navigate(R.id.review_tag_to_list_cafelist);
                        else
                            navController.navigate(R.id.navigation_list_cafelist);
                        break;


                    case R.id.navigation_bottom_review:
//                        Toast.makeText(getApplicationContext(), "리뷰 탭", Toast.LENGTH_SHORT).show();

                        if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCafeListFragment"))
                            navController.navigate(R.id.bottom_review_cafelist_to_bottom_review);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCommentFragment"))
                            navController.navigate(R.id.bottom_review_comment_to_bottom_review);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewFragment"))
                            navController.navigate(R.id.bottom_review_to_bottom_review);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewTagFragment"))
                            navController.navigate(R.id.bottom_review_tag_to_bottom_review);
                        else if(navController.getCurrentDestination().toString().contains("CafeDeleteFragment"))
                            navController.navigate(R.id.cafe_delete_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailFragment"))
                            navController.navigate(R.id.cafe_detail_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailMoreFragment"))
                            navController.navigate(R.id.cafe_detail_more_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("CafeModifyFragment"))
                            navController.navigate(R.id.cafe_modify_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationFragment"))
                            navController.navigate(R.id.cafe_registration_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationTagFragment"))
                            navController.navigate(R.id.cafe_registration_tag_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("HomeFragment"))
                            navController.navigate(R.id.home_to_review);
                        else if(navController.getCurrentDestination().toString().contains("ListCafelistFragment"))
                            navController.navigate(R.id.list_cafelist_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewCommentFragment"))
                            navController.navigate(R.id.mypage_review_comment_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("MyPage_ReviewFragment"))
                            navController.navigate(R.id.mypage_review_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewTagFragment"))
                            navController.navigate(R.id.mypage_review_tag_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("MyPageFragment"))
                            navController.navigate(R.id.mypage_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("MypageGradeFragment"))
                            navController.navigate(R.id.myPage_grade_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("ReviewCommentFragment"))
                            navController.navigate(R.id.review_comment_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("ReviewFragment"))
                            navController.navigate(R.id.review_to_bottomReview);
                        else if(navController.getCurrentDestination().toString().contains("ReviewTagFragment"))
                            navController.navigate(R.id.review_tag_to_bottomReview);
                        else
                            navController.navigate(R.id.navigation_bottom_review);
                        break;

                    case R.id.navigation_myPage:
//                        Toast.makeText(getApplicationContext(), "마이페이지 탭", Toast.LENGTH_SHORT).show();
//                        Log.d("currentFragment", navController.getCurrentDestination().toString());

                        if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCafeListFragment"))
                            navController.navigate(R.id.bottom_review_cafelist_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewCommentFragment"))
                            navController.navigate(R.id.bottom_review_comment_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewFragment"))
                            navController.navigate(R.id.bottom_review_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("Bottom_ReviewTagFragment"))
                            navController.navigate(R.id.bottom_review_tag_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("CafeDeleteFragment"))
                            navController.navigate(R.id.cafe_delete_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailFragment"))
                            navController.navigate(R.id.cafe_detail_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("CafeDetailMoreFragment"))
                            navController.navigate(R.id.cafe_detail_more_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("CafeModifyFragment"))
                            navController.navigate(R.id.cafe_modify_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationFragment"))
                            navController.navigate(R.id.cafe_registration_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("CafeRegistrationTagFragment"))
                            navController.navigate(R.id.cafe_registration_tag_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("HomeFragment"))
                            navController.navigate(R.id.home_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("ListCafelistFragment"))
                            navController.navigate(R.id.list_cafelist_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewCommentFragment"))
                            navController.navigate(R.id.mypage_review_comment_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("MyPage_ReviewFragment"))
                            navController.navigate(R.id.mypage_review_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("Mypage_ReviewTagFragment"))
                            navController.navigate(R.id.mypage_review_tag_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("MyPageFragment"))
                            navController.navigate(R.id.mypage_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("MypageGradeFragment"))
                            navController.navigate(R.id.myPage_grade_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("ReviewCommentFragment"))
                            navController.navigate(R.id.review_comment_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("ReviewFragment"))
                            navController.navigate(R.id.review_to_mypage);
                        else if(navController.getCurrentDestination().toString().contains("ReviewTagFragment"))
                            navController.navigate(R.id.review_tag_to_mypage);
                        else
                            navController.navigate(R.id.navigation_myPage);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    // 뒤로 버튼 2번 눌러야 앱 종료
    @Override
    public void onBackPressed() {
//        NavDestination currentDestination = navController.getCurrentDestination();
//        Log.d("currentDestination", navController.getCurrentDestination().toString());

        // 마지막 HomeFragment일때,
//        if(!navController.popBackStack()) // 이렇게 할 경우에도 HomeFragment에 닿는 순간 여태 있던 모든 스택이 사라짐(아래 코드와 같은 효과)
        if(navController.getCurrentDestination().toString().contains("HomeFragment")) {

            // 1번만 누른경우 뒤로가기 안내(2000밀리초 = 2초)
            if (System.currentTimeMillis() > backpressedTime + 2000) {
                backpressedTime = System.currentTimeMillis();
                Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2번 연속으로 눌렀을 때, 앱 종료
            else if (System.currentTimeMillis() <= backpressedTime + 2000) {
                finish();
            }
        }

        // 스택이 있을 경우에는 그냥 뒤로가기
        super.onBackPressed();
    }

        // fragment_Cafe_detail_more의 리싸이클러뷰 푸터 + fragment_review_tag의 메인코드는
        // 너무 길어서 구글 드라이브 졸업프로젝트 -> 디자인에 넣어두었습니다.
}