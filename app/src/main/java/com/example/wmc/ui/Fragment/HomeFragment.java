package com.example.wmc.ui.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.example.wmc.HomeFavorite.HomeFavoriteAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPager;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPagerAdapter;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPagerItem;
import com.example.wmc.HomeTag2ViewPager.HomeTag2ViewPager;
import com.example.wmc.HomeTag2ViewPager.HomeTag2ViewPagerAdapter;
import com.example.wmc.HomeTag2ViewPager.HomeTag2ViewPagerItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.CafeImage;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static NavController navController;

    TextView favoirte_default_textView;
    HomeTag1ViewPager first_viewPager;
    HomeTag2ViewPager second_viewPager;
    Button first_previousButton;
    Button first_nextButton;
    Button second_previousButton;
    Button second_nextButton;
    TextView first_hashtag_textview;
    TextView second_hashtag_textview;
    TextView first_hastage_cafelist_textview;
    TextView second_hastage_cafelist_textview;
    EditText cafe_search_input;

    HomeTag1ViewPagerAdapter tag1Adapter;
    HomeTag2ViewPagerAdapter tag2Adapter;
    ArrayList<HomeTag1ViewPagerItem> tag1_List;
    ArrayList<HomeTag2ViewPagerItem> tag2_List;

    // 서버 데이터 받기
    ArrayList<Personal> personal_list;
    ArrayList<Cafe> cafe_list;
    ArrayList<Bookmark> bookmark_list;
    ArrayList<Review> review_list;
    ArrayList<CafeImage> cafeImage_list = new ArrayList<>();

    Long mem_num = MainActivity.mem_num; // 임시 멤버 넘버
    String get_user_fav;
    String get_user_address;
    Long[] get_keyword = new Long[36];

    int get_study_point_total = 0;
    int get_taste_point_total = 0;

    String tag1;
    String tag2;
    String represent_cafeImage_URL = "";
    String get_review = "";

    List<Address> list = new ArrayList<>();
    Geocoder g;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        favoirte_default_textView = root.findViewById(R.id.favoirte_default_textView);
        first_viewPager = root.findViewById(R.id.first_viewPager);
        second_viewPager = root.findViewById(R.id.second_viewPager);
        first_previousButton = root.findViewById(R.id.first_previousButton);
        first_nextButton = root.findViewById(R.id.first_nextButton);
        second_previousButton = root.findViewById(R.id.second_previousButton);
        second_nextButton = root.findViewById(R.id.second_nextButton);
        first_hashtag_textview = root.findViewById(R.id.first_hashtag_textView);
        second_hashtag_textview = root.findViewById(R.id.second_hashtag_textView);
        first_hastage_cafelist_textview = root.findViewById(R.id.first_hashtag_cafeList_textView);
        second_hastage_cafelist_textview = root.findViewById(R.id.second_hashtag_cafeList_textView);
        cafe_search_input = root.findViewById(R.id.cafe_search_input);

        g = new Geocoder(getContext());

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


        String get_personal_url = getResources().getString(R.string.url) + "personal";
        String get_cafe_url = getResources().getString(R.string.url) + "cafe";
        String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";
        String get_cafeImage_url = getResources().getString(R.string.url) + "cafeImage";


        // 현재 내 위치 정보 가져오기(동까지)
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
                double longitude = location.getLongitude(); // 위도
                double latitude = location.getLatitude(); // 경도

                try {
                    list = g.getFromLocation(latitude, longitude,10);
                    cafe_search_input.setText(list.get(0).getAddressLine(0).substring(5));
                } catch (IOException e) {
                    Toast.makeText(getContext().getApplicationContext(), "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            } public void onProviderEnabled(String provider) {

            } public void onProviderDisabled(String provider) {

            }
        };


        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( getActivity(), new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }

        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                try {
                    list = g.getFromLocation(latitude, longitude,10);

                    Log.d("cafeList", list.get(5).getAddressLine(0).toString());

                    cafe_search_input.setText(list.get(0).getAddressLine(0).substring(5));
                    get_user_address = list.get(5).getAddressLine(0).substring(5);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext().getApplicationContext(), "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
                }
            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }
        // 여기까지가 현재 내 위치 정보 가져오기(동까지)


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

                /////////////////////////////////////////////////////////////////////////////////////////////////
                // 사용자의 선호 해시태그에 맞춰 Home화면 설정.
                for(Personal p : personal_list){
                    if(p.getMemNum().equals(mem_num)){
                        first_hashtag_textview.setText("#" + p.getFavorite1()); // 사용자의 선호 1순위
                        second_hashtag_textview.setText("#" + p.getFavorite2()); // 사용자의 선호 2순위

                        // #맛에 따라 텍스트 변경
                        if(p.getFavorite1().equals("맛")) {
                            first_hastage_cafelist_textview.setText("이 1순위인 당신을 위한 카페!");
                        }
                        else{
                            second_hastage_cafelist_textview.setText("이 2순위인 당신을 위한 카페!");
                        }
                    }
                }

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
                                                /////////////////////////////////////////////////////////////////////////////////////////
                                                // 카페 태그 1, 2 키워드 받기
                                                get_keyword[0] = c.getKeyword1();
                                                get_keyword[1] = c.getKeyword2();
                                                get_keyword[2] = c.getKeyword3();
                                                get_keyword[3] = c.getKeyword4();
                                                get_keyword[4] = c.getKeyword5();
                                                get_keyword[5] = c.getKeyword6();
                                                get_keyword[6] = c.getKeyword7();
                                                get_keyword[7] = c.getKeyword8();
                                                get_keyword[8] = c.getKeyword9();
                                                get_keyword[9] = c.getKeyword10();
                                                get_keyword[10] = c.getKeyword11();
                                                get_keyword[11] = c.getKeyword12();
                                                get_keyword[12] = c.getKeyword13();
                                                get_keyword[13] = c.getKeyword14();
                                                get_keyword[14] = c.getKeyword15();
                                                get_keyword[15] = c.getKeyword16();
                                                get_keyword[16] = c.getKeyword17();
                                                get_keyword[17] = c.getKeyword18();
                                                get_keyword[18] = c.getKeyword19();
                                                get_keyword[19] = c.getKeyword20();
                                                get_keyword[20] = c.getKeyword21();
                                                get_keyword[21] = c.getKeyword22();
                                                get_keyword[22] = c.getKeyword23();
                                                get_keyword[23] = c.getKeyword24();
                                                get_keyword[24] = c.getKeyword25();
                                                get_keyword[25] = c.getKeyword26();
                                                get_keyword[26] = c.getKeyword27();
                                                get_keyword[27] = c.getKeyword28();
                                                get_keyword[28] = c.getKeyword29();
                                                get_keyword[29] = c.getKeyword30();
                                                get_keyword[30] = c.getKeyword31();
                                                get_keyword[31] = c.getKeyword32();
                                                get_keyword[32] = c.getKeyword33();
                                                get_keyword[33] = c.getKeyword34();
                                                get_keyword[34] = c.getKeyword35();
                                                get_keyword[35] = c.getKeyword36();


                                                // keyword 중에서 가장 많이 count된 태그 1, 2 구하기.
                                                Long Max = get_keyword[0];
                                                Long secondMax = 0L;
                                                int counter_max = 0;
                                                int counter_second = 1;
                                                for (int i = 1; i < 36; i++) {
                                                    secondMax = get_keyword[i];
                                                    if (Max <= secondMax) {
                                                        secondMax = Max;
                                                        counter_second = counter_max;

                                                        Max = get_keyword[i];
                                                        counter_max = i;
                                                    }
                                                }


                                                // 태그 검색이 되는지 확인용
                                                Log.d("show_Max_and_secondMax", Max.toString() + ", " + secondMax.toString());

                                                tag1 = "쓴맛";
                                                tag2 = "신맛";

                                                // 태그 1 세팅
                                                switch (counter_max) {
                                                    case 0:
                                                        tag1 = "#쓴맛";
                                                        break;
                                                    case 1:
                                                        tag1 = "#신맛";
                                                        break;
                                                    case 2:
                                                        tag1 = "#짠맛";
                                                        break;
                                                    case 3:
                                                        tag1 = "#단맛";
                                                        break;
                                                    case 4:
                                                        tag1 = "#향미";
                                                        break;
                                                    case 5:
                                                        tag1 = "#바디감";
                                                        break;
                                                    case 6:
                                                        tag1 = "#콜드브루";
                                                        break;
                                                    case 7:
                                                        tag1 = "#메뉴多";
                                                        break;
                                                    case 8:
                                                        tag1 = "#가성비";
                                                        break;
                                                    case 9:
                                                        tag1 = "#양많음";
                                                        break;
                                                    case 10:
                                                        tag1 = "#디저트맛집";
                                                        break;
                                                    case 11:
                                                        tag1 = "#논커피맛집";
                                                        break;
                                                    case 12:
                                                        tag1 = "#인스타";
                                                        break;
                                                    case 13:
                                                        tag1 = "#앤티크";
                                                        break;
                                                    case 14:
                                                        tag1 = "#모던";
                                                        break;
                                                    case 15:
                                                        tag1 = "#캐주얼";
                                                        break;
                                                    case 16:
                                                        tag1 = "#이국적";
                                                        break;
                                                    case 17:
                                                        tag1 = "#일상";
                                                        break;
                                                    case 18:
                                                        tag1 = "#따뜻한";
                                                        break;
                                                    case 19:
                                                        tag1 = "#조용한";
                                                        break;
                                                    case 20:
                                                        tag1 = "#우드론";
                                                        break;
                                                    case 21:
                                                        tag1 = "#채광";
                                                        break;
                                                    case 22:
                                                        tag1 = "#힙한";
                                                        break;
                                                    case 23:
                                                        tag1 = "#귀여운";
                                                        break;
                                                    case 24:
                                                        tag1 = "#친절한";
                                                        break;
                                                    case 25:
                                                        tag1 = "#청결한";
                                                        break;
                                                    case 26:
                                                        tag1 = "#애견";
                                                        break;
                                                    case 27:
                                                        tag1 = "#주차장";
                                                        break;
                                                    case 28:
                                                        tag1 = "#노키즈존";
                                                        break;
                                                    case 29:
                                                        tag1 = "#교통편의";
                                                        break;
                                                    case 30:
                                                        tag1 = "#신속한";
                                                        break;
                                                    case 31:
                                                        tag1 = "#쾌적한";
                                                        break;
                                                    case 32:
                                                        tag1 = "#회의실";
                                                        break;
                                                    case 33:
                                                        tag1 = "#규모大";
                                                        break;
                                                    case 34:
                                                        tag1 = "#규모小";
                                                        break;
                                                    case 35:
                                                        tag1 = "#편한좌석";
                                                        break;

                                                }

                                                //태그 2 세팅
                                                switch (counter_second) {
                                                    case 0:
                                                        tag2 = "#쓴맛";
                                                        break;
                                                    case 1:
                                                        tag2 = "#신맛";
                                                        break;
                                                    case 2:
                                                        tag2 = "#짠맛";
                                                        break;
                                                    case 3:
                                                        tag2 = "#단맛";
                                                        break;
                                                    case 4:
                                                        tag2 = "#향미";
                                                        break;
                                                    case 5:
                                                        tag2 = "#바디감";
                                                        break;
                                                    case 6:
                                                        tag2 = "#콜드브루";
                                                        break;
                                                    case 7:
                                                        tag2 = "#메뉴多";
                                                        break;
                                                    case 8:
                                                        tag2 = "#가성비";
                                                        break;
                                                    case 9:
                                                        tag2 = "#양많음";
                                                        break;
                                                    case 10:
                                                        tag2 = "#디저트맛집";
                                                        break;
                                                    case 11:
                                                        tag2 = "#논커피맛집";
                                                        break;
                                                    case 12:
                                                        tag2 = "#인스타";
                                                        break;
                                                    case 13:
                                                        tag2 = "#앤티크";
                                                        break;
                                                    case 14:
                                                        tag2 = "#모던";
                                                        break;
                                                    case 15:
                                                        tag2 = "#캐주얼";
                                                        break;
                                                    case 16:
                                                        tag2 = "#이국적";
                                                        break;
                                                    case 17:
                                                        tag2 = "#일상";
                                                        break;
                                                    case 18:
                                                        tag2 = "#따뜻한";
                                                        break;
                                                    case 19:
                                                        tag2 = "#조용한";
                                                        break;
                                                    case 20:
                                                        tag2 = "#우드론";
                                                        break;
                                                    case 21:
                                                        tag2 = "#채광";
                                                        break;
                                                    case 22:
                                                        tag2 = "#힙한";
                                                        break;
                                                    case 23:
                                                        tag2 = "#귀여운";
                                                        break;
                                                    case 24:
                                                        tag2 = "#친절한";
                                                        break;
                                                    case 25:
                                                        tag2 = "#청결한";
                                                        break;
                                                    case 26:
                                                        tag2 = "#애견";
                                                        break;
                                                    case 27:
                                                        tag2 = "#주차장";
                                                        break;
                                                    case 28:
                                                        tag2 = "#노키즈존";
                                                        break;
                                                    case 29:
                                                        tag2 = "#교통편의";
                                                        break;
                                                    case 30:
                                                        tag2 = "#신속한";
                                                        break;
                                                    case 31:
                                                        tag2 = "#쾌적한";
                                                        break;
                                                    case 32:
                                                        tag2 = "#회의실";
                                                        break;
                                                    case 33:
                                                        tag2 = "#규모大";
                                                        break;
                                                    case 34:
                                                        tag2 = "#규모小";
                                                        break;
                                                    case 35:
                                                        tag2 = "#편한좌석";
                                                        break;

                                                }
                                                StringRequest cafeImage_stringRequest = new StringRequest(Request.Method.GET, get_cafeImage_url, new Response.Listener<String>() {
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
                                                        Type listType = new TypeToken<ArrayList<CafeImage>>(){}.getType();

                                                        cafeImage_list = gson.fromJson(changeString, listType);

                                                        // 다른페이지 갔다왔을때, URL 초기화 시켜야해서 추가해둠
                                                        represent_cafeImage_URL = "";

                                                        for(CafeImage ci : cafeImage_list){
                                                            if(ci.getCafeNum().equals(c.getCafeNum())){
                                                                Log.d("cafeNum", ci.getCafeNum() + ", " + c.getCafeNum());
                                                                Log.d("cafeImage URL", ci.getFileUrl());
                                                                represent_cafeImage_URL = ci.getFileUrl();
                                                                break;
                                                            }
                                                        }

                                                        if(represent_cafeImage_URL.equals(""))
                                                            represent_cafeImage_URL = "https://w.namu.la/s/0c6301df01fc4f180ec65717bad3d0254258abf0be33299e55df7c261040f517518eb9008a1a2cd3d7b8b7777d70182c185bc891b1054dc57b11cc46fd29130a3474f1b75b816024dfdc16b692a0c77c";


                                                        // 찜한 카페 recycler view에 추가하기
                                                        homeFavoriteItems.add(new HomeFavoriteItem(c.getCafeName(), tag1, tag2 ,represent_cafeImage_URL));

                                                        // Recycler view
                                                        RecyclerView homeFavoriteRecyclerView = root.findViewById(R.id.favorite_recyclerView);

                                                        // Adapter 추가
                                                        HomeFavoriteAdapter favoriteAdapter = new HomeFavoriteAdapter(getContext() ,homeFavoriteItems, HomeFragment.this);
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
                                                    }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("cafeImage_stringRequest_error",error.toString());
                                            }
                                        });
                                        requestQueue.add(cafeImage_stringRequest);
                                        // 여기까지 카페 대표 이미지 URL 가져오기
                                            }
                                        }
                                    }
                                }

                                String get_review_url = getResources().getString(R.string.url) + "review";

                                StringRequest review_stringRequest = new StringRequest(Request.Method.GET, get_review_url, new Response.Listener<String>() {
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
                                        Type listType = new TypeToken<ArrayList<Review>>(){}.getType();

                                        review_list = gson.fromJson(changeString, listType);



                                        // for문 돌려서 fav1을 만족하는 카페 찾기
                                        for (Personal p : personal_list){
                                            if(p.getMemNum().equals(mem_num)){
                                                get_user_fav = p.getFavorite1();
                                            }
                                        }

                                        first_viewPager.setOffscreenPageLimit(5);
                                        tag1_List = new ArrayList<>();


                                        second_viewPager.setOffscreenPageLimit(5);
                                        tag2_List = new ArrayList<>();


                                        for(Cafe c : cafe_list) {
                                            get_taste_point_total = 0;
                                            get_study_point_total = 0;

                                            if (c.getCafeAddress().contains(get_user_address)) {
                                                Log.d("getCafeAddress", c.getCafeAddress().toString());
                                                Log.d("cafeNumber", c.getCafeNum().toString());



                                                get_taste_point_total = c.getTastePoint1() + c.getTastePoint2() + c.getTastePoint3() + c.getTastePoint4();
                                                Log.d("get_taste_point_total_checking", String.valueOf(get_taste_point_total));

                                                get_study_point_total = c.getStudyPoint1() + c.getStudyPoint2() + c.getStudyPoint3() + c.getStudyPoint4();
                                                Log.d("get_study_point_total_checking", String.valueOf(get_study_point_total));


                                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                                // Home에서 1순위 해시태그에 대한 뷰페이저 작성

                                                int review_counter = 0;

                                                int like_counter_max = 0;

                                                for (Review r : review_list) {
                                                    if (r.getCafeNum().equals(c.getCafeNum())) {
                                                        review_counter = review_counter + 1;
                                                        if (like_counter_max <= r.getLikeCount()) {
                                                            like_counter_max = r.getLikeCount();
                                                            get_review = r.getReviewText();
                                                        }
                                                    }
                                                }

                                                /////////////////////////////////////////////////////////////////////////////////////////
                                                // 카페 태그 1, 2 키워드 받기
                                                get_keyword[0] = c.getKeyword1();
                                                get_keyword[1] = c.getKeyword2();
                                                get_keyword[2] = c.getKeyword3();
                                                get_keyword[3] = c.getKeyword4();
                                                get_keyword[4] = c.getKeyword5();
                                                get_keyword[5] = c.getKeyword6();
                                                get_keyword[6] = c.getKeyword7();
                                                get_keyword[7] = c.getKeyword8();
                                                get_keyword[8] = c.getKeyword9();
                                                get_keyword[9] = c.getKeyword10();
                                                get_keyword[10] = c.getKeyword11();
                                                get_keyword[11] = c.getKeyword12();
                                                get_keyword[12] = c.getKeyword13();
                                                get_keyword[13] = c.getKeyword14();
                                                get_keyword[14] = c.getKeyword15();
                                                get_keyword[15] = c.getKeyword16();
                                                get_keyword[16] = c.getKeyword17();
                                                get_keyword[17] = c.getKeyword18();
                                                get_keyword[18] = c.getKeyword19();
                                                get_keyword[19] = c.getKeyword20();
                                                get_keyword[20] = c.getKeyword21();
                                                get_keyword[21] = c.getKeyword22();
                                                get_keyword[22] = c.getKeyword23();
                                                get_keyword[23] = c.getKeyword24();
                                                get_keyword[24] = c.getKeyword25();
                                                get_keyword[25] = c.getKeyword26();
                                                get_keyword[26] = c.getKeyword27();
                                                get_keyword[27] = c.getKeyword28();
                                                get_keyword[28] = c.getKeyword29();
                                                get_keyword[29] = c.getKeyword30();
                                                get_keyword[30] = c.getKeyword31();
                                                get_keyword[31] = c.getKeyword32();
                                                get_keyword[32] = c.getKeyword33();
                                                get_keyword[33] = c.getKeyword34();
                                                get_keyword[34] = c.getKeyword35();
                                                get_keyword[35] = c.getKeyword36();


                                                // keyword 중에서 가장 많이 count된 태그 1, 2 구하기.
                                                Long Max = get_keyword[0];
                                                Long secondMax = 0L;
                                                int counter_max = 0;
                                                int counter_second = 1;
                                                for (int i = 1; i < 36; i++) {
                                                    secondMax = get_keyword[i];
                                                    if (Max <= secondMax) {
                                                        secondMax = Max;
                                                        counter_second = counter_max;

                                                        Max = get_keyword[i];
                                                        counter_max = i;
                                                    }
                                                }


                                                // 태그 검색이 되는지 확인용
                                                Log.d("show_Max_and_secondMax", Max.toString() + ", " + secondMax.toString());

                                                tag1 = "쓴맛";
                                                tag2 = "신맛";

                                                // 태그 1 세팅
                                                switch (counter_max) {
                                                    case 0:
                                                        tag1 = "#쓴맛";
                                                        break;
                                                    case 1:
                                                        tag1 = "#신맛";
                                                        break;
                                                    case 2:
                                                        tag1 = "#짠맛";
                                                        break;
                                                    case 3:
                                                        tag1 = "#단맛";
                                                        break;
                                                    case 4:
                                                        tag1 = "#향미";
                                                        break;
                                                    case 5:
                                                        tag1 = "#바디감";
                                                        break;
                                                    case 6:
                                                        tag1 = "#콜드브루";
                                                        break;
                                                    case 7:
                                                        tag1 = "#메뉴多";
                                                        break;
                                                    case 8:
                                                        tag1 = "#가성비";
                                                        break;
                                                    case 9:
                                                        tag1 = "#양많음";
                                                        break;
                                                    case 10:
                                                        tag1 = "#디저트맛집";
                                                        break;
                                                    case 11:
                                                        tag1 = "#논커피맛집";
                                                        break;
                                                    case 12:
                                                        tag1 = "#인스타";
                                                        break;
                                                    case 13:
                                                        tag1 = "#앤티크";
                                                        break;
                                                    case 14:
                                                        tag1 = "#모던";
                                                        break;
                                                    case 15:
                                                        tag1 = "#캐주얼";
                                                        break;
                                                    case 16:
                                                        tag1 = "#이국적";
                                                        break;
                                                    case 17:
                                                        tag1 = "#일상";
                                                        break;
                                                    case 18:
                                                        tag1 = "#따뜻한";
                                                        break;
                                                    case 19:
                                                        tag1 = "#조용한";
                                                        break;
                                                    case 20:
                                                        tag1 = "#우드론";
                                                        break;
                                                    case 21:
                                                        tag1 = "#채광";
                                                        break;
                                                    case 22:
                                                        tag1 = "#힙한";
                                                        break;
                                                    case 23:
                                                        tag1 = "#귀여운";
                                                        break;
                                                    case 24:
                                                        tag1 = "#친절한";
                                                        break;
                                                    case 25:
                                                        tag1 = "#청결한";
                                                        break;
                                                    case 26:
                                                        tag1 = "#애견";
                                                        break;
                                                    case 27:
                                                        tag1 = "#주차장";
                                                        break;
                                                    case 28:
                                                        tag1 = "#노키즈존";
                                                        break;
                                                    case 29:
                                                        tag1 = "#교통편의";
                                                        break;
                                                    case 30:
                                                        tag1 = "#신속한";
                                                        break;
                                                    case 31:
                                                        tag1 = "#쾌적한";
                                                        break;
                                                    case 32:
                                                        tag1 = "#회의실";
                                                        break;
                                                    case 33:
                                                        tag1 = "#규모大";
                                                        break;
                                                    case 34:
                                                        tag1 = "#규모小";
                                                        break;
                                                    case 35:
                                                        tag1 = "#편한좌석";
                                                        break;

                                                }

                                                //태그 2 세팅
                                                switch (counter_second) {
                                                    case 0:
                                                        tag2 = "#쓴맛";
                                                        break;
                                                    case 1:
                                                        tag2 = "#신맛";
                                                        break;
                                                    case 2:
                                                        tag2 = "#짠맛";
                                                        break;
                                                    case 3:
                                                        tag2 = "#단맛";
                                                        break;
                                                    case 4:
                                                        tag2 = "#향미";
                                                        break;
                                                    case 5:
                                                        tag2 = "#바디감";
                                                        break;
                                                    case 6:
                                                        tag2 = "#콜드브루";
                                                        break;
                                                    case 7:
                                                        tag2 = "#메뉴多";
                                                        break;
                                                    case 8:
                                                        tag2 = "#가성비";
                                                        break;
                                                    case 9:
                                                        tag2 = "#양많음";
                                                        break;
                                                    case 10:
                                                        tag2 = "#디저트맛집";
                                                        break;
                                                    case 11:
                                                        tag2 = "#논커피맛집";
                                                        break;
                                                    case 12:
                                                        tag2 = "#인스타";
                                                        break;
                                                    case 13:
                                                        tag2 = "#앤티크";
                                                        break;
                                                    case 14:
                                                        tag2 = "#모던";
                                                        break;
                                                    case 15:
                                                        tag2 = "#캐주얼";
                                                        break;
                                                    case 16:
                                                        tag2 = "#이국적";
                                                        break;
                                                    case 17:
                                                        tag2 = "#일상";
                                                        break;
                                                    case 18:
                                                        tag2 = "#따뜻한";
                                                        break;
                                                    case 19:
                                                        tag2 = "#조용한";
                                                        break;
                                                    case 20:
                                                        tag2 = "#우드론";
                                                        break;
                                                    case 21:
                                                        tag2 = "#채광";
                                                        break;
                                                    case 22:
                                                        tag2 = "#힙한";
                                                        break;
                                                    case 23:
                                                        tag2 = "#귀여운";
                                                        break;
                                                    case 24:
                                                        tag2 = "#친절한";
                                                        break;
                                                    case 25:
                                                        tag2 = "#청결한";
                                                        break;
                                                    case 26:
                                                        tag2 = "#애견";
                                                        break;
                                                    case 27:
                                                        tag2 = "#주차장";
                                                        break;
                                                    case 28:
                                                        tag2 = "#노키즈존";
                                                        break;
                                                    case 29:
                                                        tag2 = "#교통편의";
                                                        break;
                                                    case 30:
                                                        tag2 = "#신속한";
                                                        break;
                                                    case 31:
                                                        tag2 = "#쾌적한";
                                                        break;
                                                    case 32:
                                                        tag2 = "#회의실";
                                                        break;
                                                    case 33:
                                                        tag2 = "#규모大";
                                                        break;
                                                    case 34:
                                                        tag2 = "#규모小";
                                                        break;
                                                    case 35:
                                                        tag2 = "#편한좌석";
                                                        break;

                                                }

                                                int get_study_point_avg;
                                                int get_taste_point_avg;

                                                if((c.getStudyPoint1() + c.getStudyPoint2() + c.getStudyPoint3() + c.getStudyPoint4()) == 0 || review_counter == 0){
                                                    get_study_point_avg = 0;
                                                }
                                                else
                                                    get_study_point_avg = (c.getStudyPoint1() + c.getStudyPoint2() + c.getStudyPoint3() + c.getStudyPoint4()) / review_counter / 4;

                                                if((c.getTastePoint1() + c.getTastePoint2() + c.getTastePoint3() + c.getTastePoint4()) == 0 || review_counter == 0){
                                                    get_taste_point_avg = 0;
                                                }
                                                else
                                                    get_taste_point_avg = (c.getTastePoint1() + c.getTastePoint2() + c.getTastePoint3() + c.getTastePoint4()) / review_counter / 4;


                                                Log.d("getTastePoint_AVG", String.valueOf(get_taste_point_avg));
                                                Log.d("getstudyPoint_AVG", String.valueOf(get_study_point_avg));
                                                String get_cafeImage_url = getResources().getString(R.string.url) + "cafeImage";

                                                StringRequest cafeImage_stringRequest1 = new StringRequest(Request.Method.GET, get_cafeImage_url, new Response.Listener<String>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // 한글깨짐 해결 코드
                                                        String changeString = new String();
                                                        try {
                                                            changeString = new String(response.getBytes("8859_1"), "utf-8");
                                                        } catch (UnsupportedEncodingException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                                        Type listType = new TypeToken<ArrayList<CafeImage>>() {
                                                        }.getType();

                                                        cafeImage_list = gson.fromJson(changeString, listType);


                                                        // 다른페이지 갔다왔을때, URL 초기화 시켜야해서 추가해둠
                                                        represent_cafeImage_URL = "";

                                                        for (CafeImage ci : cafeImage_list) {
                                                            Log.d("ci.getCafeNum", ci.getCafeNum().toString());
                                                            Log.d("c.getCafeNumcheck", c.getCafeNum().toString());
                                                            if (ci.getCafeNum().equals(c.getCafeNum())) {
                                                                Log.d("Checking_equal", "True");
                                                                represent_cafeImage_URL = ci.getFileUrl();
                                                                break;
                                                            }
                                                        }

                                                        if (represent_cafeImage_URL.equals(""))
                                                            represent_cafeImage_URL = "https://w.namu.la/s/0c6301df01fc4f180ec65717bad3d0254258abf0be33299e55df7c261040f517518eb9008a1a2cd3d7b8b7777d70182c185bc891b1054dc57b11cc46fd29130a3474f1b75b816024dfdc16b692a0c77c";


                                                        if (get_user_fav.equals("스터디")) {
                                                            if ((c.getStudyPoint1() + c.getStudyPoint2() + c.getStudyPoint3() + c.getStudyPoint4()) >= (c.getTastePoint1() + c.getTastePoint2() + c.getTastePoint3() + c.getTastePoint4())) {

                                                                HomeTag1ViewPagerItem firstTag_item = new HomeTag1ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#스터디", tag1, tag2,
                                                                        get_review, represent_cafeImage_URL, get_study_point_avg);
                                                                tag1_List.add(firstTag_item);
                                                            } else {
                                                                HomeTag2ViewPagerItem secondTag_item = new HomeTag2ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#맛", tag1, tag2,
                                                                        get_review, represent_cafeImage_URL, get_taste_point_avg);
                                                                tag2_List.add(secondTag_item);
                                                            }
                                                        } else {
                                                            if ((c.getStudyPoint1() + c.getStudyPoint2() + c.getStudyPoint3() + c.getStudyPoint4()) <= (c.getTastePoint1() + c.getTastePoint2() + c.getTastePoint3() + c.getTastePoint4())) {

                                                                HomeTag1ViewPagerItem firstTag_item = new HomeTag1ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#맛", tag1, tag2,
                                                                        get_review, represent_cafeImage_URL, get_taste_point_avg);
                                                                tag1_List.add(firstTag_item);
                                                            } else {
                                                                Log.d("get_study_point_total", String.valueOf(get_study_point_total));
                                                                Log.d("get_taste_point_total", String.valueOf(get_taste_point_total));


                                                                HomeTag2ViewPagerItem secondTag_item = new HomeTag2ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#스터디", tag1, tag2,
                                                                        get_review, represent_cafeImage_URL, get_study_point_avg);
                                                                tag2_List.add(secondTag_item);
                                                            }
                                                        }

                                                        // tag1_List 별점 높은순으로 정렬
                                                        Collections.sort(tag1_List, new tag1_ratingComparator());
                                                        first_viewPager.setAdapter(new HomeTag1ViewPagerAdapter(getContext().getApplicationContext(), tag1_List, HomeFragment.this));
                                                        tag1Adapter = new HomeTag1ViewPagerAdapter(getContext().getApplicationContext(), tag1_List, HomeFragment.this);


                                                        first_viewPager.setOnItemClickListener(new HomeTag1ViewPager.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(int position) {
                                                                final HomeTag1ViewPagerItem item = tag1_List.get(position);
                                                                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("cafeName", item.getCafeName());
                                                                navController.navigate(R.id.home_to_cafe_detail, bundle);
                                                            }
                                                        });

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


                                                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                                        // Home에서 2순위 해시태그에 대한 뷰페이저 작성

                                                        // tag2_List 별점 높은순으로 정렬
                                                        Collections.sort(tag2_List, new tag2_ratingComparator());
                                                        second_viewPager.setAdapter(new HomeTag2ViewPagerAdapter(getContext().getApplicationContext(), tag2_List, HomeFragment.this));
                                                        tag2Adapter = new HomeTag2ViewPagerAdapter(getContext().getApplicationContext(), tag2_List, HomeFragment.this);


                                                        second_viewPager.setOnItemClickListener_second(new HomeTag2ViewPager.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(int position) {
                                                                final HomeTag2ViewPagerItem item = tag2_List.get(position);
                                                                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("cafeName", item.getCafeName());
                                                                navController.navigate(R.id.home_to_cafe_detail, bundle);
                                                            }
                                                        });

                                                        // 두번째 태그 카페목록의 좌측 버튼 클릭 시, 카페 목록 넘어감
                                                        second_previousButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                int pageNum = second_viewPager.getCurrentItem();
                                                                second_viewPager.setCurrentItem(pageNum - 1, true);
                                                            }
                                                        });


                                                        // 두번째 태그 카페목록의 우측 버튼 클릭 시, 카페 목록 넘어감
                                                        second_nextButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                int pageNum = second_viewPager.getCurrentItem();
                                                                second_viewPager.setCurrentItem(pageNum + 1, true);
                                                            }
                                                        });




                                                    }

                                                }, new Response.ErrorListener()

                                                    {
                                                        @Override
                                                        public void onErrorResponse (VolleyError
                                                        error){
                                                        Log.e("cafeImage_stringRequest_error", error.toString());
                                                    }
                                                });

                                                requestQueue.add(cafeImage_stringRequest1);




                                            }
                                        }






                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("home_review_stringRequest_error",error.toString());
                                    }
                                });


                                requestQueue.add(review_stringRequest);






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

    class tag1_ratingComparator implements Comparator<HomeTag1ViewPagerItem> {

        @Override
        public int compare(HomeTag1ViewPagerItem o1, HomeTag1ViewPagerItem o2) {
            if(o1.getRating() > o2.getRating())
                return 1;
            else if (o1.getRating() < o2.getRating())
                return -1;

            return 0;
        }
    }

    class tag2_ratingComparator implements Comparator<HomeTag2ViewPagerItem> {

        @Override
        public int compare(HomeTag2ViewPagerItem o1, HomeTag2ViewPagerItem o2) {
            if(o1.getRating() > o2.getRating())
                return 1;
            else if (o1.getRating() < o2.getRating())
                return -1;

            return 0;
        }
    }
}