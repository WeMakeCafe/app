package com.example.wmc;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ConstraintLayout cons1;
    ConstraintLayout cons2;
    ConstraintLayout cons3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_review_tag);  //페이지 확인하기

        Button but1 = findViewById(R.id.addTag1);
        Button but2 = findViewById(R.id.tag4);

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but1.setText(but2.getText().toString());
            }
        });

        cons1 = (ConstraintLayout)findViewById(R.id.constrant_taste);
        cons2 = (ConstraintLayout)findViewById(R.id.constrant_feel);
        cons3 = (ConstraintLayout)findViewById(R.id.constrant_service);
        ImageView view1 = findViewById(R.id.tasteimage8);
        ImageView view2 = findViewById(R.id.feelingImage8);
        ImageView view3 = findViewById(R.id.serviceimage8);

        ImageView view4 = findViewById(R.id.tasteimage7);
        ImageView view5 = findViewById(R.id.feelingImage7);
        ImageView view6 = findViewById(R.id.serviceimage7);

        ImageView view7 = findViewById(R.id.tasteimage6);
        ImageView view8 = findViewById(R.id.feelingimage6);
        ImageView view9 = findViewById(R.id.serviceImage6);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.VISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.VISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.VISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.VISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.VISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.VISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.VISIBLE);
            }
        });
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.VISIBLE);
            }
        });
        view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.VISIBLE);
            }
        });
//        bindList(); // fragment_list_search 페이지 리사이클러뷰 푸터 실험용 코드

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

//    public void tasteClicked(View a){ //review tag 벝느 이벤트 만들며 해본 시행착오
//        cons1.setVisibility(View.VISIBLE);
//        cons2.setVisibility(View.INVISIBLE);
//        cons3.setVisibility(View.INVISIBLE);
//    }
  
    private void bindList_cafeDetail() {         // fragment_list_search 페이지 리사이클러뷰 푸터 실험용 코드
        // List item 생성
        ArrayList<CafeDetailItem> cafeDetailReview = new ArrayList<>();

        cafeDetailReview.add(new CafeDetailItem("아이유", "Lv.3",
                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo, R.drawable.logo_v2, "7"));
        cafeDetailReview.add(new CafeDetailItem("지코", "Lv.3",
                "테이블이 매우 협소합니다. \n" +
                        "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                        "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, R.drawable.logo_v2, "5"));
        cafeDetailReview.add(new CafeDetailItem("애쉬", "Lv.1(위치 인증 완료)",
                "테이블이 협소해서 공부하기는 어렵지만\n" +
                        "노래도 나오고 친구들이랑 같이 이야기하기에는\n" +
                        "좋아요.", R.drawable.logo, R.drawable.logo_v2, "1"));

        // Recycler view
        RecyclerView recyclerView = findViewById(R.id.cafeDetailReviewRecyclerView);

        // Adapter 추가
        CafeDetailAdapter adapter = new CafeDetailAdapter(cafeDetailReview);
        recyclerView.setAdapter(adapter);

        // Layout manager 추가
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        adapter.setOnItemClickListener_cafeDetail(new CafeDetailAdapter.OnItemClickEventListener_cafeDetail() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final CafeDetailItem item = cafeDetailReview.get(a_position);
                Toast.makeText(MainActivity.this, item.getReviewNickName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}