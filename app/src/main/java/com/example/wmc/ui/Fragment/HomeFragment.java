package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingViewPagerAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.HomeTag1.HomeTag1Adapter;
import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPagerAdapter;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPagerItem;
import com.example.wmc.HomeTag2.HomeTag2Adapter;
import com.example.wmc.HomeTag2.HomeTag2Item;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static NavController navController;

    TextView favoirte_default_textView;
    ViewPager first_viewPager;
    ViewPager second_viewPager;
    Button first_previousButton;
    Button first_nextButton;

    HomeTag1ViewPagerAdapter tag1Adapter;
    ArrayList<HomeTag1ViewPagerItem> tag1_List;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        favoirte_default_textView = root.findViewById(R.id.favoirte_default_textView);
        first_viewPager = root.findViewById(R.id.first_viewPager);
        second_viewPager = root.findViewById(R.id.second_viewPager);
        first_previousButton = root.findViewById(R.id.first_previousButton);
        first_nextButton = root.findViewById(R.id.first_nextButton);



        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Home 에서 찜한 카페에 대한 리사이클러뷰 작성
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

        if (homeFavoriteItems.size() == 0){
            favoirte_default_textView.setVisibility(View.VISIBLE);
        }

        favoriteAdapter.setOnItemClickListener_HomeFavorite(new HomeFavoriteAdapter.OnItemClickEventListener_HomeFavorite() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final HomeFavoriteItem item = homeFavoriteItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getCafeName());
                navController.navigate(R.id.home_to_cafe_detail, bundle);
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Home에서 1순위 해시태그에 대한 뷰페이저 작성

        first_viewPager.setOffscreenPageLimit(5);
        tag1_List = new ArrayList<>();


        HomeTag1ViewPagerItem firstTag_item1 = new HomeTag1ViewPagerItem("이디야커피 수원대점", "경기도 화성시 와우리 46", "#가성비", "#마카롱", "#디저트",
                "테이블이 매우 협소합니다. \n" + "하지만, 가격이 매우 저렴하고 맛있습니다!\n" + "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, 3);
        HomeTag1ViewPagerItem firstTag_item2 = new HomeTag1ViewPagerItem("할리스커피 수원대점", "경기도 화성시 와우리 41-17", "#다인석", "#회의실", "#힙한",
                "테이블이 협소해서 공부하기는 어렵지만\n" + "노래도 나오고 친구들이랑 같이 이야기하기에는좋아요.", R.drawable.logo, 2);
        HomeTag1ViewPagerItem firstTag_item3 = new HomeTag1ViewPagerItem("메가커피 성균관대점", "경기도 수원시 탑동 801-4", "#맛집", "#스터디", "#조용한",
                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo_v2, 5);
        HomeTag1ViewPagerItem firstTag_item4 = new HomeTag1ViewPagerItem("이디야커피 수원대점", "경기도 화성시 와우리 46", "#가성비", "#마카롱", "#디저트",
                "테이블이 매우 협소합니다. \n" + "하지만, 가격이 매우 저렴하고 맛있습니다!\n" + "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, 3);
        HomeTag1ViewPagerItem firstTag_item5 = new HomeTag1ViewPagerItem("할리스커피 수원대점", "경기도 화성시 와우리 41-17", "#다인석", "#회의실", "#힙한",
                "테이블이 협소해서 공부하기는 어렵지만\n" + "노래도 나오고 친구들이랑 같이 이야기하기에는좋아요.", R.drawable.logo, 2);

        tag1_List.add(firstTag_item1);
        tag1_List.add(firstTag_item2);
        tag1_List.add(firstTag_item3);
        tag1_List.add(firstTag_item4);
        tag1_List.add(firstTag_item5);

        first_viewPager.setAdapter(new HomeTag1ViewPagerAdapter(getContext().getApplicationContext(), tag1_List));
        tag1Adapter = new HomeTag1ViewPagerAdapter(getContext().getApplicationContext(), tag1_List);

        // 첫번째 태그 카페목록의 좌측 버튼 클릭 시, 카페 목록 넘어감
        first_previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNum = first_viewPager.getCurrentItem();
                first_viewPager.setCurrentItem(pageNum - 1, true);
            }
        });


        // 첫번째 태그 카페목록의 우측 버튼 클릭 시, 카페 목록 넘어감
        first_nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNum = first_viewPager.getCurrentItem();
                first_viewPager.setCurrentItem(pageNum + 1, true);
            }
        });

//        ArrayList<HomeTag1Item> homeTag1Items = new ArrayList<>();
//
//        homeTag1Items.add(new HomeTag1Item("이디야커피 수원대점", "경기도 화성시 와우리 46", "#가성비", "#마카롱", "#디저트",
//                "테이블이 매우 협소합니다. \n" + "하지만, 가격이 매우 저렴하고 맛있습니다!\n" + "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, 3));
//        homeTag1Items.add(new HomeTag1Item("할리스커피 수원대점", "경기도 화성시 와우리 41-17", "#다인석", "#회의실", "#힙한",
//                "테이블이 협소해서 공부하기는 어렵지만\n" + "노래도 나오고 친구들이랑 같이 이야기하기에는좋아요.", R.drawable.logo, 2));
//        homeTag1Items.add(new HomeTag1Item("메가커피 성균관대점", "경기도 수원시 탑동 801-4", "#맛집", "#스터디", "#조용한",
//                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo_v2, 5));
//
//
//        // Recycler view
//        RecyclerView tag1RecyclerView = root.findViewById(R.id.first_recyclerView);
//
//        // Adapter 추가
//        HomeTag1Adapter tag1Adapter = new HomeTag1Adapter(homeTag1Items);
//        tag1RecyclerView.setAdapter(tag1Adapter);
//
//        // Layout manager 추가
//        LinearLayoutManager tag1LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        tag1RecyclerView.setLayoutManager(tag1LayoutManager);
//
//        tag1Adapter.setOnItemClickListener_HomeTag1(new HomeTag1Adapter.OnItemClickEventListener_HomeTag1() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final HomeTag1Item item = homeTag1Items.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("cafeName", item.getCafeName());
//                navController.navigate(R.id.home_to_cafe_detail, bundle);
//            }
//        });


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Home에서 2순위 해시태그에 대한 리싸이클러뷰 작성

//        ArrayList<HomeTag2Item> homeTag2Items = new ArrayList<>();
//
//        homeTag2Items.add(new HomeTag2Item("뺵다방 홍대점", "서울시 ~~~~ 167", "#가성비", "#신나는", "#디저트",
//                "테이블이 매우 협소합니다. \n" + "하지만, 가격이 매우 저렴하고 맛있습니다!\n" + "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, 1));
//        homeTag2Items.add(new HomeTag2Item("잇츠커피 수원대점", "경기도 화성시 와우리 42-11", "#화장실", "#애견", "#레트로",
//                "테이블이 협소해서 공부하기는 어렵지만\n" + "노래도 나오고 친구들이랑 같이 이야기하기에는좋아요.", R.drawable.logo, 3));
//        homeTag2Items.add(new HomeTag2Item("스타벅스 수원역점", "경기도 수원시 수원역 ~~~ 84-4", "#분위기", "#스터디", "#감성",
//                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo_v2, 4));
//
//
//        // Recycler view
//        RecyclerView tag2RecyclerView = root.findViewById(R.id.second_recyclerView);
//
//        // Adapter 추가
//        HomeTag2Adapter tag2Adapter = new HomeTag2Adapter(homeTag2Items);
//        tag2RecyclerView.setAdapter(tag2Adapter);
//
//        // Layout manager 추가
//        LinearLayoutManager tag2LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        tag2RecyclerView.setLayoutManager(tag2LayoutManager);
//
//        tag2Adapter.setOnItemClickListener_HomeTag2(new HomeTag2Adapter.OnItemClickEventListener_HomeTag2() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final HomeTag2Item item = homeTag2Items.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("cafeName", item.getCafeName());
//                navController.navigate(R.id.home_to_cafe_detail, bundle);
//            }
//        });


        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}