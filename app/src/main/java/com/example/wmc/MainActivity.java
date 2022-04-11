package com.example.wmc;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.wmc.ui.Fragment.ListSearchAdapter;
import com.example.wmc.ui.Fragment.ListSearchItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_list_search);  //페이지 확인하기

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

//    private void bindList() {         // fragment_list_search 페이지 리사이클러뷰 푸터 실험용 코드
//        // List item 생성
//        ArrayList<ListSearchItem> recentCafe = new ArrayList<>();
//
//        recentCafe.add(new ListSearchItem("이디야커피 수원대점", "경기 화성시 봉담읍 와우안길 16"));
//        recentCafe.add(new ListSearchItem("와우당", "경기 화성시 봉담읍 와우안길 10"));
//        recentCafe.add(new ListSearchItem("-", "서울특별시 강남구 역삼동 논현로 539"));
//        recentCafe.add(new ListSearchItem("옹느세자메", "서울특별시 용산구 이태원로54길 51"));
//        recentCafe.add(new ListSearchItem("이디야커피 수원대점", "경기 화성시 봉담읍 와우안길 16"));
//        recentCafe.add(new ListSearchItem("와우당", "경기 화성시 봉담읍 와우안길 10"));
//        recentCafe.add(new ListSearchItem("-", "서울특별시 강남구 역삼동 논현로 539"));
//        recentCafe.add(new ListSearchItem("옹느세자메", "서울특별시 용산구 이태원로54길 51"));
//
//        // Recycler view
//        RecyclerView recyclerView = findViewById(R.id.recent_searches_recyclerView);
//
//        // Adapter 추가
//        ListSearchAdapter adapter = new ListSearchAdapter(recentCafe);
//        recyclerView.setAdapter(adapter);
//
//        // Layout manager 추가
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//
//
//
//        adapter.setOnItemClickListener(new ListSearchAdapter.OnItemClickEventListener() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//
//                final ListSearchItem item = recentCafe.get(a_position);
//                Toast.makeText(MainActivity.this, item.getCafeName() + ", " + item.getCafeAddress() + " 클릭됨", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}