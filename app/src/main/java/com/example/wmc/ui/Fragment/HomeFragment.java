package com.example.wmc.ui.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.HomeFavorite.HomeFavoriteAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.HomeTag1.HomeTag1Adapter;
import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.HomeTag2.HomeTag2Adapter;
import com.example.wmc.HomeTag2.HomeTag2Item;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Personal;
import com.example.wmc.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static NavController navController;
    TextView favoirte_default_textView;

    // 서버 데이터 받기
    ArrayList<Personal> personal_list;
    ArrayList<Cafe> cafe_list;
    ArrayList<Bookmark> bookmark_list;

    Long mem_num = MainActivity.mem_num; // 임시 멤버 넘버


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        favoirte_default_textView = root.findViewById(R.id.favoirte_default_textView);


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Home 에서 찜한 카페에 대한 리사이클러뷰 작성
        ArrayList<HomeFavoriteItem> homeFavoriteItems = new ArrayList<>();

        // 사용자가 찜한 카페들을 찾기 위한 서버 연결
        // 서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        String get_personal_url = "http://54.221.33.199:8080/personal";
        String get_cafe_url = "http://54.221.33.199:8080/cafe";
        String get_bookmark_url = "http://54.221.33.199:8080/bookmark";

        // Personal 접근
        StringRequest personal_stringRequest = new StringRequest(Request.Method.GET, get_personal_url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                // 한글깨짐 해결 코드
                String changeString = new String();
                try {
                    changeString = new String(response.getBytes("8859_1"),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<ArrayList<Personal>>(){}.getType();

                personal_list = gson.fromJson(changeString, listType);

                // Bookmark 접근 -> 찜한 카페 찾기
                StringRequest bookmark_stringRequest = new StringRequest(Request.Method.GET, get_bookmark_url, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        // 한글깨짐 해결 코드
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<Bookmark>>(){}.getType();

                        bookmark_list = gson.fromJson(changeString, listType);

                        // Cafe 접근 -> Bookmark의 cafeNum과 Cafe의 cafeNum을 비교하여 찜한 카페 찾기
                        StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(String response) {
                                // 한글깨짐 해결 코드
                                String changeString = new String();
                                try {
                                    changeString = new String(response.getBytes("8859_1"),"utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                Type listType = new TypeToken<ArrayList<Cafe>>(){}.getType();

                                cafe_list = gson.fromJson(changeString, listType);

                                for(Bookmark b : bookmark_list) {
                                    if (b.getMemNum().equals(mem_num)) { // bookmark_list에서 사용자의 mem_num과 일치하는 튜플 찾기
                                        for(Cafe c : cafe_list){
                                            if(c.getCafeNum().equals(b.getCafeNum())){ // cafe_list에서 사용자가 bookmark한 카페 찾기
                                                // 찜한 카페 recycler view에 추가하기
                                                homeFavoriteItems.add(new HomeFavoriteItem(c.getCafeName(), "#가성비", "#마카롱" ,R.drawable.logo_v2));
                                            }
                                        }
                                    }
                                }

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


                                // for문 돌려서 fav1을 만족하는 카페 찾기
                                // for문 돌려서 fav2를 만족하는 카페 찾기



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error_cafe",error.toString()); // cafe_list를 받지 못할 경우 확인용
                            }
                        });

                        requestQueue.add(cafe_stringRequest);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_bookmark",error.toString());
                    }
                });
                requestQueue.add(bookmark_stringRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error_personal",error.toString());
            }
        });

        requestQueue.add(personal_stringRequest);


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Home에서 1순위 해시태그에 대한 리싸이클러뷰 작성

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

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getCafeName());
                navController.navigate(R.id.home_to_cafe_detail, bundle);
            }
        });


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Home에서 2순위 해시태그에 대한 리싸이클러뷰 작성

        ArrayList<HomeTag2Item> homeTag2Items = new ArrayList<>();

        homeTag2Items.add(new HomeTag2Item("뺵다방 홍대점", "서울시 ~~~~ 167", "#가성비", "#신나는", "#디저트",
                "테이블이 매우 협소합니다. \n" + "하지만, 가격이 매우 저렴하고 맛있습니다!\n" + "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, 1));
        homeTag2Items.add(new HomeTag2Item("잇츠커피 수원대점", "경기도 화성시 와우리 42-11", "#화장실", "#애견", "#레트로",
                "테이블이 협소해서 공부하기는 어렵지만\n" + "노래도 나오고 친구들이랑 같이 이야기하기에는좋아요.", R.drawable.logo, 3));
        homeTag2Items.add(new HomeTag2Item("스타벅스 수원역점", "경기도 수원시 수원역 ~~~ 84-4", "#분위기", "#스터디", "#감성",
                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo_v2, 4));


        // Recycler view
        RecyclerView tag2RecyclerView = root.findViewById(R.id.second_recyclerView);

        // Adapter 추가
        HomeTag2Adapter tag2Adapter = new HomeTag2Adapter(homeTag2Items);
        tag2RecyclerView.setAdapter(tag2Adapter);

        // Layout manager 추가
        LinearLayoutManager tag2LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        tag2RecyclerView.setLayoutManager(tag2LayoutManager);

        tag2Adapter.setOnItemClickListener_HomeTag2(new HomeTag2Adapter.OnItemClickEventListener_HomeTag2() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final HomeTag2Item item = homeTag2Items.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getCafeName());
                navController.navigate(R.id.home_to_cafe_detail, bundle);
            }
        });
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