package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.HomeFavorite.HomeFavoriteAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.HomeTag1.HomeTag1Adapter;
import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<HomeFavoriteItem> homeFavoriteItems = new ArrayList<>();

        homeFavoriteItems.add(new HomeFavoriteItem("이디야커피 수원대점", "#가성비", "#마카롱", R.drawable.logo_v2));
        homeFavoriteItems.add(new HomeFavoriteItem("할리스커피 수원대점", "#버블티", "#스터디", R.drawable.logo));
        homeFavoriteItems.add(new HomeFavoriteItem("메가커피 탑동점", "#맛집", "#분위기", R.drawable.home));
        homeFavoriteItems.add(new HomeFavoriteItem("스타벅스 홍대점", "#스터디", "#조용함", R.drawable.logo_v2));
        homeFavoriteItems.add(new HomeFavoriteItem("백다방 성균관대점", "#회의실", "#다인석", R.drawable.logo));
        homeFavoriteItems.add(new HomeFavoriteItem("잇츠커피 수원대점", "#다채로운", "#감성", R.drawable.review));

        // Recycler view
        RecyclerView homeFavoriteRecyclerView = root.findViewById(R.id.favorite_recyclerView);

        // Adapter 추가
        HomeFavoriteAdapter favoriteAdapter = new HomeFavoriteAdapter(homeFavoriteItems);
        homeFavoriteRecyclerView.setAdapter(favoriteAdapter);

        // Layout manager 추가
        LinearLayoutManager favoriteLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        homeFavoriteRecyclerView.setLayoutManager(favoriteLayoutManager);

        favoriteAdapter.setOnItemClickListener_HomeFavorite(new HomeFavoriteAdapter.OnItemClickEventListener_HomeFavorite() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final HomeFavoriteItem item = homeFavoriteItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayList<HomeTag1Item> homeTag1Items = new ArrayList<>();

        homeTag1Items.add(new HomeTag1Item("이디야커피 수원대점", "경기도 화성시 와우리 46", "#가성비", "#마카롱", "#디저트",
                "테이블이 매우 협소합니다. \n" + "하지만, 가격이 매우 저렴하고 맛있습니다!\n" + "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, 3));
        homeTag1Items.add(new HomeTag1Item("할리스커피 수원대점", "경기도 화성시 와우리 41-17", "#다인석", "#회의실", "#힙한",
                "테이블이 협소해서 공부하기는 어렵지만\n" + "노래도 나오고 친구들이랑 같이 이야기하기에는좋아요.", R.drawable.logo, 2));
        homeTag1Items.add(new HomeTag1Item("메가커피 성균관대점", "경기도 수원시 탑동 801-4", "#맛집", "#스터디", "#조용한",
                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo_v2, 5));


        // Recycler view
        RecyclerView tag1RecyclerView = root.findViewById(R.id.first_recyclerView);

        // Adapter 추가
        HomeTag1Adapter tag1Adapter = new HomeTag1Adapter(homeTag1Items);
        tag1RecyclerView.setAdapter(tag1Adapter);

        // Layout manager 추가
        LinearLayoutManager tag1LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        tag1RecyclerView.setLayoutManager(tag1LayoutManager);

        tag1Adapter.setOnItemClickListener_HomeTag1(new HomeTag1Adapter.OnItemClickEventListener_HomeTag1() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final HomeTag1Item item = homeTag1Items.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}