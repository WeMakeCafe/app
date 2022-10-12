package com.example.wmc.ui.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailImageViewPagerAdapter;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingViewPagerAdapter;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.CafeImage;
import com.example.wmc.database.Love;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.database.ReviewImage;
import com.example.wmc.databinding.FragmentCafeDetailBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CafeDetailFragment extends Fragment {

    private FragmentCafeDetailBinding binding;
    private static NavController navController;
    Button cafe_modify_button;
    FloatingActionButton review_floatingButton;
    CheckBox favorite_checkbox;
    ViewPager cafeImageViewPager;
    ViewPager cafeRatingViewPager;
    Button cafeDetail_favorite_previousButton;
    Button cafeDetail_favorite_nextButton;
    TextView moreReview2; // 최상단 카페 이름 ID
    TextView moreReview3; // 사진 아래 카페 이름 ID
    TextView moreReview4; // 사진 아래 카페 주소 ID
    TextView moreReview10; // 사진 아래 운영 시간 첫번째
    TextView moreReview8; // 사진 아래 운영 시간 두번째
    TextView moreReview5; // 카페 키워드
    TextView moreReview6; // 카페 태그 1
    TextView moreReview7; // 카페 태그 2
    RecyclerView recyclerView;

    ArrayList<String> imageList;   // 카페 이미지 5장을 저장하는 ArrayList
    ArrayList<CafeDetailRatingItem> ratingList; // 카페 평점을 저장하는 ArrayList

    ArrayList<Cafe> cafe_list;
    ArrayList<Review> review_list;
    ArrayList<Personal> personal_list;
    ArrayList<Bookmark> bookmark_list;
    ArrayList<Love> love_list;
    ArrayList<ReviewImage> reviewImage_list;
    ArrayList<CafeImage> CafeImage_list;

    Long mem_num = MainActivity.mem_num;

    String cafe_name; // Bundle을 통해 받아온 cafe_name을 임시로 저장함
    String create_date;    // 리뷰 등록 시간
    Long get_cafe_num; // cafe_num을 임시로 저장함.
    Long get_bookmark_num; // bookmark_num을 임시로 저장함.

    Long[] get_keyword = new Long[36]; // 카페 태그 임시 저장
    String reviewImage;

    // 카페에 저장된 리뷰 점수 평균 저장
    int[] get_seat_point = new int[4];
    int[] get_study_point = new int[4];
    int[] get_taste_point = new int[4];

    // 카페 리뷰 점수 총점을 구해 카페 분류(스터디, 맛)
    int get_seat_point_total = 0;
    int get_study_point_total = 0;
    int get_taste_point_total = 0;

    int point_counter = 0; // 리뷰가 몇 번 작성되었는지 count하여 카페 리뷰 점수 평균 구하기
    boolean love_flag = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cafe_modify_button = root.findViewById(R.id.cafe_modify_button);
        moreReview3 = root.findViewById(R.id.moreReview3);
        moreReview4 = root.findViewById(R.id.moreReview4);
        review_floatingButton = root.findViewById(R.id.review_floatingButton);
        favorite_checkbox = root.findViewById(R.id.favorite_checkbox);
        cafeDetail_favorite_previousButton = root.findViewById(R.id.cafeDetail_favorite_previousButton);
        cafeDetail_favorite_nextButton = root.findViewById(R.id.cafeDetail_favorite_nextButton);
        moreReview2 = root.findViewById(R.id.moreReview2); // 최상단 카페 이름 ID
        moreReview3 = root.findViewById(R.id.moreReview3); // 사진 아래 카페 이름 ID
        moreReview4 = root.findViewById(R.id.moreReview4); // 사진 아래 카페 주소 ID
        moreReview10 = root.findViewById(R.id.moreReview10); // 사진 아래 운영 시간 첫번째
        moreReview8 = root.findViewById(R.id.moreReview8); // 사진 아래 운영 시간 두번째
        moreReview5 = root.findViewById(R.id.moreReview5); // 카페 키워드
        moreReview6 = root.findViewById(R.id.moreReview6); // 카페 태그 1
        moreReview7 = root.findViewById(R.id.moreReview7); // 카페 태그 2
        recyclerView = root.findViewById(R.id.cafeDetailReviewRecyclerView);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        // cafeDetail 외부에서 Bundle로 이름 받아오기
        Bundle cafeNameBundle = getArguments();
        if(cafeNameBundle != null){
            if(cafeNameBundle.getString("cafeName") != null){
                cafe_name = cafeNameBundle.getString("cafeName");
            }
        }


        //서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        String get_cafe_url = getResources().getString(R.string.url) + "cafe";

        StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
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


                ///////////////////////////////////////////////////////////////////////////////////////////
                // cafeDetail 정보 세팅
                for(Cafe c : cafe_list){
                    if(c.getCafeName().equals(cafe_name)){
                        get_cafe_num = c.getCafeNum();
                        Log.d("check_cafe_num", get_cafe_num.toString()); // 카페 넘버가 제대로 들어오는지 확인

                        String get_cafeImage_url = getResources().getString(R.string.url) + "cafeImage";

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

                                CafeImage_list = gson.fromJson(changeString, listType);

                                for(CafeImage ci : CafeImage_list){
                                    if(ci.getCafeNum().equals(get_cafe_num)){
                                        Log.d("cafeImage URL", ci.getFileUrl());
                                        imageList.add(ci.getFileUrl());
                                    }
                                }

                                cafeImageViewPager.setAdapter(new CafeDetailImageViewPagerAdapter(getContext().getApplicationContext(), imageList, CafeDetailFragment.this));

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("cafeImage_stringRequest_error",error.toString());
                            }
                        });
                        requestQueue.add(cafeImage_stringRequest);

                        moreReview2.setText(c.getCafeName());
                        moreReview3.setText(c.getCafeName());
                        moreReview4.setText(c.getCafeAddress());
                        moreReview10.setText(c.getOpenTime().substring(0,2)+":"+c.getOpenTime().substring(2,4));
                        moreReview8.setText(c.getCloseTime().substring(0,2)+":"+c.getCloseTime().substring(2,4));

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
                        for(int i = 1; i < 36; i++){
                            secondMax = get_keyword[i];
                            if(Max <= secondMax){
                                secondMax = Max;
                                counter_second = counter_max;

                                Max = get_keyword[i];
                                counter_max = i;
                            }
                        }

                        // 태그 검색이 되는지 확인용
                        Log.d("show_Max_and_secondMax", Max.toString() + ", " + secondMax.toString());

                        // 태그 1 세팅
                        switch (counter_max){
                            case 0:
                                moreReview6.setText("#쓴맛");
                                break;
                            case 1:
                                moreReview6.setText("#신맛");
                                break;
                            case 2:
                                moreReview6.setText("#짠맛");
                                break;
                            case 3:
                                moreReview6.setText("#단맛");
                                break;
                            case 4:
                                moreReview6.setText("#향미");
                                break;
                            case 5:
                                moreReview6.setText("#바디감");
                                break;
                            case 6:
                                moreReview6.setText("#콜드브루");
                                break;
                            case 7:
                                moreReview6.setText("#메뉴多");
                                break;
                            case 8:
                                moreReview6.setText("#가성비");
                                break;
                            case 9:
                                moreReview6.setText("#양많음");
                                break;
                            case 10:
                                moreReview6.setText("#디저트맛집");
                                break;
                            case 11:
                                moreReview6.setText("#논커피맛집");
                                break;
                            case 12:
                                moreReview6.setText("#인스타");
                                break;
                            case 13:
                                moreReview6.setText("#앤티크");
                                break;
                            case 14:
                                moreReview6.setText("#모던");
                                break;
                            case 15:
                                moreReview6.setText("#캐주얼");
                                break;
                            case 16:
                                moreReview6.setText("#이국적");
                                break;
                            case 17:
                                moreReview6.setText("#일상");
                                break;
                            case 18:
                                moreReview6.setText("#따뜻한");
                                break;
                            case 19:
                                moreReview6.setText("#조용한");
                                break;
                            case 20:
                                moreReview6.setText("#우드톤");
                                break;
                            case 21:
                                moreReview6.setText("#채광");
                                break;
                            case 22:
                                moreReview6.setText("#힙한");
                                break;
                            case 23:
                                moreReview6.setText("#귀여운");
                                break;
                            case 24:
                                moreReview6.setText("#친절한");
                                break;
                            case 25:
                                moreReview6.setText("#청결한");
                                break;
                            case 26:
                                moreReview6.setText("#애견");
                                break;
                            case 27:
                                moreReview6.setText("#주차장");
                                break;
                            case 28:
                                moreReview6.setText("#노키즈존");
                                break;
                            case 29:
                                moreReview6.setText("#교통편의");
                                break;
                            case 30:
                                moreReview6.setText("#신속한");
                                break;
                            case 31:
                                moreReview6.setText("#쾌적한");
                                break;
                            case 32:
                                moreReview6.setText("#회의실");
                                break;
                            case 33:
                                moreReview6.setText("#규모大");
                                break;
                            case 34:
                                moreReview6.setText("#규모小");
                                break;
                            case 35:
                                moreReview6.setText("#편한좌석");
                                break;

                        }

                        //태그 2 세팅
                        switch (counter_second){
                            case 0:
                                moreReview7.setText("#쓴맛");
                                break;
                            case 1:
                                moreReview7.setText("#신맛");
                                break;
                            case 2:
                                moreReview7.setText("#짠맛");
                                break;
                            case 3:
                                moreReview7.setText("#단맛");
                                break;
                            case 4:
                                moreReview7.setText("#향미");
                                break;
                            case 5:
                                moreReview7.setText("#바디감");
                                break;
                            case 6:
                                moreReview7.setText("#콜드브루");
                                break;
                            case 7:
                                moreReview7.setText("#메뉴多");
                                break;
                            case 8:
                                moreReview7.setText("#가성비");
                                break;
                            case 9:
                                moreReview7.setText("#양많음");
                                break;
                            case 10:
                                moreReview7.setText("#디저트맛집");
                                break;
                            case 11:
                                moreReview7.setText("#논커피맛집");
                                break;
                            case 12:
                                moreReview7.setText("#인스타");
                                break;
                            case 13:
                                moreReview7.setText("#앤티크");
                                break;
                            case 14:
                                moreReview7.setText("#모던");
                                break;
                            case 15:
                                moreReview7.setText("#캐주얼");
                                break;
                            case 16:
                                moreReview7.setText("#이국적");
                                break;
                            case 17:
                                moreReview7.setText("#일상");
                                break;
                            case 18:
                                moreReview7.setText("#따뜻한");
                                break;
                            case 19:
                                moreReview7.setText("#조용한");
                                break;
                            case 20:
                                moreReview7.setText("#우드톤");
                                break;
                            case 21:
                                moreReview7.setText("#채광");
                                break;
                            case 22:
                                moreReview7.setText("#힙한");
                                break;
                            case 23:
                                moreReview7.setText("#귀여운");
                                break;
                            case 24:
                                moreReview7.setText("#친절한");
                                break;
                            case 25:
                                moreReview7.setText("#청결한");
                                break;
                            case 26:
                                moreReview7.setText("#애견");
                                break;
                            case 27:
                                moreReview7.setText("#주차장");
                                break;
                            case 28:
                                moreReview7.setText("#노키즈존");
                                break;
                            case 29:
                                moreReview7.setText("#교통편의");
                                break;
                            case 30:
                                moreReview7.setText("#신속한");
                                break;
                            case 31:
                                moreReview7.setText("#쾌적한");
                                break;
                            case 32:
                                moreReview7.setText("#회의실");
                                break;
                            case 33:
                                moreReview7.setText("#규모大");
                                break;
                            case 34:
                                moreReview7.setText("#규모小");
                                break;
                            case 35:
                                moreReview7.setText("#편한좌석");
                                break;

                        }

                        // 카페 리뷰 점수
                        get_seat_point[0] = c.getSeatPoint1();
                        get_seat_point[1] = c.getSeatPoint2();
                        get_seat_point[2] = c.getSeatPoint3();
                        get_seat_point[3] = c.getSeatPoint4();

                        get_study_point[0] = c.getStudyPoint1();
                        get_study_point[1] = c.getStudyPoint2();
                        get_study_point[2] = c.getStudyPoint3();
                        get_study_point[3] = c.getStudyPoint4();

                        get_taste_point[0] = c.getTastePoint1();
                        get_taste_point[1] = c.getTastePoint2();
                        get_taste_point[2] = c.getTastePoint3();
                        get_taste_point[3] = c.getTastePoint4();
                    }
                }

                String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";
                // 카페 북마크 여부 확인
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

                        for(Bookmark b : bookmark_list){
                            // "북마크의 mem_num과 사용자의 mem_num이 일치 && 북마크의 cafe_num과 cafeDetail의 cafe_num이 일치"할 경우
                            if(b.getMemNum().equals(mem_num) && b.getCafeNum().equals(get_cafe_num)){
                                get_bookmark_num = b.getBookmarkNum(); // bookmark_num 일시 저장
                                favorite_checkbox.setChecked(true); // 즐겨찾기 버튼 true 세팅
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("bookmark_stringRequest_error",error.toString());
                    }
                });
                requestQueue.add(bookmark_stringRequest);




                // 즐겨찾기 버튼(별) 클릭 시,
                favorite_checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean checked = ((CheckBox) view).isChecked();    // 즐겨찾기가 됐는지 확인


                        // 카페 북마크 여부 확인 및 등록, 삭제
                        String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";
                        // 카페 북마크 여부 확인
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

                                for(Bookmark b : bookmark_list){
                                    // "북마크의 mem_num과 사용자의 mem_num이 일치 && 북마크의 cafe_num과 cafeDetail의 cafe_num이 일치"할 경우
                                    if(b.getMemNum().equals(mem_num) && b.getCafeNum().equals(get_cafe_num)){
                                        get_bookmark_num = b.getBookmarkNum(); // bookmark_num 일시 저장
//                                        favorite_checkbox.setChecked(true); // 즐겨찾기 버튼 true 세팅
                                    }
                                }

                                if(checked) {   // 불이 꺼져있을 때 누르는 경우,
                                    // 즐겨찾기 항목에 추가함

                                    Map map = new HashMap();
                                    map.put("cafeNum", get_cafe_num);
                                    map.put("memNum", mem_num);

                                    JSONObject bookmark_jsonObject = new JSONObject(map);
                                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, get_bookmark_url, bookmark_jsonObject,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // 북마크 추가 성공, 토스트 띄우기.
                                                    Toast.makeText(getContext().getApplicationContext(), "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("bookmark_jsonObject_error", error.toString());
                                                }
                                            }) {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    Log.d("json", bookmark_jsonObject.toString());
                                    RequestQueue queue = Volley.newRequestQueue(requireContext());
                                    queue.add(objectRequest);
                                }


                                else {  // 불이 켜져있을 때 누르는 경우
                                    // 즐겨찾기 항목에서 제거됨
                                    String bookmark_delete_url = getResources().getString(R.string.url) + "bookmark/" + get_bookmark_num.toString();
                                    Log.e("bookmark_num", get_bookmark_num.toString());
                                    StringRequest bookmark_delete_stringRequest = new StringRequest(Request.Method.DELETE, bookmark_delete_url, new Response.Listener<String>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onResponse(String response) {
                                            // 북마크 제거 성공, 토스트 띄우기.
                                            Toast.makeText(getContext().getApplicationContext(), "즐겨찾기 삭제", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("bookmark_delete_stringRequest_error",error.toString());
                                        }
                                    });
                                    requestQueue.add(bookmark_delete_stringRequest);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("bookmark_stringRequest_error",error.toString());
                            }
                        });
                        requestQueue.add(bookmark_stringRequest);
                    }
                });

                ///////////////////////////////////////////////////////////////////////////////////////////////////


                ///////////////////////////////////////////////////////////////////////////////////////////
                // review 채우기
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


                        ////////////////////////////////////////////////////////////////////////////////////////////
                        //리뷰 작성자의 정보 가져오기
                        String get_personal_url = getResources().getString(R.string.url) + "personal";

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


                                String get_love_url = getResources().getString(R.string.url) + "love";

                                StringRequest love_stringRequest = new StringRequest(Request.Method.GET, get_love_url, new Response.Listener<String>() {
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
                                        Type listType = new TypeToken<ArrayList<Love>>(){}.getType();

                                        love_list = gson.fromJson(changeString, listType);


                                        //review 리사이클러뷰
                                        ArrayList<CafeDetailItem> cafeDetailReviewItem = new ArrayList<>();


                                        ///////////////////////////////////////////////////////////////////////////////////////////
                                        // 리뷰 작성자를 비교해서
                                        // 1. 어플 사용자가 해당 카페에 대한 리뷰를 작성한 경우, 리사이클러뷰 가장 처음에 나오도록 설정
                                        // 2. 리뷰 작성자들의 닉네임, 회원 등급을 포함한 리뷰 Item 작성
                                        // 3. 카페 디테일에서는 가장 최근 리뷰 3개만 나오도록 설정
                                        for(Review r : review_list){
                                            if(r.getCafeNum().equals(get_cafe_num)) {
                                                point_counter = point_counter + 1;
                                                love_flag = false;

                                                // DB에서 받아온 리뷰 생성 시간을 변경하기 위한 코드
                                                String creatTime = r.getCreateTime();
                                                SimpleDateFormat old_format =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                                old_format.setTimeZone(TimeZone.getTimeZone("KST"));
                                                SimpleDateFormat new_format = new SimpleDateFormat("yyyy/MM/dd   HH:mm");

                                                try {
                                                    Date old_date =  old_format.parse(creatTime);
                                                    create_date = new_format.format(old_date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                // 리뷰 이미지
                                                String get_reviewImage_url = getResources().getString(R.string.url) + "reviewImage";

                                                StringRequest reviewImage_stringRequest = new StringRequest(Request.Method.GET, get_reviewImage_url, new Response.Listener<String>() {
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
                                                        Type listType = new TypeToken<ArrayList<ReviewImage>>(){}.getType();

                                                        reviewImage_list = gson.fromJson(changeString, listType);

                                                        reviewImage = "";

                                                        // 리뷰 이미지가 있을때
                                                        for(ReviewImage ri : reviewImage_list) {
                                                            if(ri.getReviewNum().equals(r.getReviewNum())){
                                                                reviewImage = ri.getFileUrl();
                                                                Log.d("reviewImage", ri.getFileUrl());
                                                                break;
                                                            }
                                                        }

                                                        // 리뷰 이미지가 없을때
                                                        if(reviewImage == "")
                                                            //(나중에 로고 올리고 바꾸기)
                                                            reviewImage = getString(R.string.default_Review_Caefimage);

                                                        // 프로필 이미지
                                                        for (Personal p : personal_list) {

                                                            String personImage = "";

//                                                            Log.d("profileUrl", p.getProfileImageUrl());
                                                            // 프로필 사진을 설정했는지 확인하는 곳(설정 X시, 기본 프로필 사진으로 설정)
                                                            if(p.getProfileimageurl() == null)
                                                                // 기본 이미지 URL입력하면 됨(현재 뚱이사진 예시)
                                                                personImage = getString(R.string.default_Profileimage);
                                                            else
                                                                personImage = p.getProfileimageurl();

                                                            // 1. 어플 사용자가 해당 카페에 대한 리뷰를 작성한 경우, 리사이클러뷰 가장 처음에 나오도록 설정
                                                            if (r.getMemNum().equals(mem_num) && p.getMemNum().equals(mem_num)) {
                                                                cafeDetailReviewItem.add( 0, new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                        r.getReviewText(), create_date, personImage, reviewImage, r.getLikeCount().toString(), true, false, mem_num, get_cafe_num, -1L, r.getReviewNum(), r.getLocationCheck()));
                                                                Log.d("review_check", r.getReviewNum().toString());
                                                            } // 2. 리뷰 작성자들의 닉네임, 회원 등급을 포함한 리뷰 Item 작성
                                                            else if (r.getMemNum().equals(p.getMemNum())) {
                                                                if(!love_list.isEmpty()) { // love_list가 비어있지 않은 경우
                                                                    for (Love l : love_list) {
                                                                        // love 테이블에 reviewNum이 같은 경우 && love 테이블에 사용자의 memNum이 같은 경우
                                                                        if (l.getReviewNum().equals(r.getReviewNum()) && l.getMemNum().equals(mem_num)){
                                                                            Log.d("love_for_if_test", "love_for_if_test");
                                                                            love_flag = true;
                                                                            cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                                    r.getReviewText(), create_date, personImage, reviewImage, r.getLikeCount().toString(), false, true, mem_num, get_cafe_num, l.getLoveNum(), r.getReviewNum(), r.getLocationCheck()));
                                                                        }
                                                                    }
                                                                }else{
                                                                    Log.d("love_not_test", "love_not_test");
                                                                    cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                            r.getReviewText(), create_date, personImage, reviewImage, r.getLikeCount().toString(), false, false, mem_num, get_cafe_num, -1L, r.getReviewNum(), r.getLocationCheck()));
                                                                }
                                                                if(!love_flag){
                                                                    Log.d("love_not_test", "love_not_test");
                                                                    cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                            r.getReviewText(), create_date, personImage, reviewImage, r.getLikeCount().toString(), false, false, mem_num, get_cafe_num, -1L, r.getReviewNum(), r.getLocationCheck()));
                                                                }
                                                            }
                                                        }

                                                        while(cafeDetailReviewItem.size() > 3) {
                                                            cafeDetailReviewItem.remove(cafeDetailReviewItem.size() - 1);   // 리뷰가 3개가 될 때까지 마지막 리뷰 지우기
                                                        }


                                                        // Recycler view
                                                        // Adapter 추가
                                                        CafeDetailAdapter adapter = new CafeDetailAdapter(getContext(), cafeDetailReviewItem, CafeDetailFragment.this);
                                                        recyclerView.setAdapter(adapter);

                                                        // Layout manager 추가
                                                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                                        recyclerView.setLayoutManager(layoutManager);

                                                        adapter.setOnItemClickListener_cafeDetail(new CafeDetailAdapter.OnItemClickEventListener_cafeDetail() {
                                                            @Override
                                                            public void onItemClick(View view, int position) {

                                                                if(cafeDetailReviewItem.size() == 0){
                                                                    Toast.makeText(getContext().getApplicationContext(), "작성된 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    // 리뷰 더보기 클릭 시,
                                                                    if(position == cafeDetailReviewItem.size()){
//                                                                        Toast.makeText(getContext().getApplicationContext(), "리뷰 더보기 클릭", Toast.LENGTH_SHORT).show();
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString("cafeNum", get_cafe_num.toString());
                                                                        //bundle.putString("name",moreReview3.getText().toString());
                                                                        navController.navigate(R.id.cafe_detail_to_cafe_detail_more, bundle);
                                                                    }

                                                                    // 리뷰 클릭 시,
                                                                    else {
                                                                        final CafeDetailItem item = cafeDetailReviewItem.get(position);
//                                                                        Toast.makeText(getContext().getApplicationContext(), item.getReviewNickName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        });

                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("reviewImage_stringRequest_error",error.toString());
                                                    }
                                                });
                                                
                                                requestQueue.add(reviewImage_stringRequest);





                                            }
                                        }


                                        ////////////////////////////////////////////////////////////////////////////////////////
                                        // 카페 리뷰 점수 구하기.

                                        for(int i = 0; i < 4; i++){
                                            get_taste_point_total += get_taste_point[i]; // 맛 점수 총점 구하기
                                            if(get_taste_point[i] == 0 || point_counter == 0){
                                                get_taste_point[i] = 0;
                                            }else {
                                                get_taste_point[i] = get_taste_point[i] / point_counter; // 맛 점수 별 평균 구하기
                                            }

                                            get_seat_point_total += get_seat_point[i]; // 좌석 점수 총점 구하기
                                            if(get_seat_point[i] == 0 || point_counter == 0){
                                                get_seat_point[i] = 0;
                                            }else {
                                                get_seat_point[i] = get_seat_point[i] / point_counter; // 좌석 점수 별 평균 구하기
                                            }

                                            get_study_point_total += get_study_point[i]; // 스터디 점수 총점 구하기
                                            if(get_study_point[i] == 0 || point_counter == 0){
                                                get_study_point[i] = 0;
                                            }else {
                                                get_study_point[i] = get_study_point[i] / point_counter; // 스터디 점수 별 평균 구하기
                                            }
                                        }

                                        // 점수 viewPager에 점수 넣어주기
                                        cafeRatingViewPager = root.findViewById(R.id.ratingViewPager);
                                        cafeRatingViewPager.setOffscreenPageLimit(3);
                                        ratingList = new ArrayList<>();

                                        CafeDetailRatingItem taste = new CafeDetailRatingItem("맛", "산미", "쓴맛", "디저트", "기타음료", R.drawable.taste_score, String.valueOf(get_taste_point[0]), String.valueOf(get_taste_point[1]), String.valueOf(get_taste_point[2]), String.valueOf(get_taste_point[3]));
                                        CafeDetailRatingItem seat = new CafeDetailRatingItem("좌석", "2인좌석", "4인좌석", "화장실", "다인좌석", R.drawable.sit_score, String.valueOf(get_seat_point[0]), String.valueOf(get_seat_point[1]), String.valueOf(get_seat_point[2]), String.valueOf(get_seat_point[3]));
                                        CafeDetailRatingItem study = new CafeDetailRatingItem("스터디", "와이파이", "콘센트", "조명", "조용함", R.drawable.study_score, String.valueOf(get_study_point[0]), String.valueOf(get_study_point[1]), String.valueOf(get_study_point[2]), String.valueOf(get_study_point[3]));

                                        ratingList.add(taste);
                                        ratingList.add(seat);
                                        ratingList.add(study);

                                        cafeRatingViewPager.setAdapter(new CafeDetailRatingViewPagerAdapter(getContext().getApplicationContext(), ratingList));


                                        // 별점에서 좌측 버튼 클릭 시, 별점 페이지 넘어감
                                        cafeDetail_favorite_previousButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = cafeRatingViewPager.getCurrentItem();
                                                cafeRatingViewPager.setCurrentItem(pageNum - 1, true);
                                            }
                                        });


                                        // 별점에서 우측 버튼 클릭 시, 별점 페이지 넘어감
                                        cafeDetail_favorite_nextButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = cafeRatingViewPager.getCurrentItem();
                                                cafeRatingViewPager.setCurrentItem(pageNum + 1, true);
                                            }
                                        });


                                        ////////////////////////////////////////////////////////////////////////////////////////////
                                        // 카페 키워드 설정
                                        if(get_taste_point_total > get_study_point_total) { // 맛 점수가 더 높은 경우
                                            moreReview5.setText("#맛");
                                        }else if(get_taste_point_total < get_study_point_total){ // 스터디 점수가 더 높은 경우
                                            moreReview5.setText("#스터디");
                                        }else{
                                            // 맛 점수와 스터디 점수가 같은 경우, 사용자의 1순위에 따라 작성.
                                            for(Personal p : personal_list){
                                                if(p.getMemNum().equals(mem_num)){
                                                    if(p.getFavorite1().equals("맛")){
                                                        moreReview5.setText("#맛");
                                                    }else{
                                                        moreReview5.setText("#스터디");
                                                    }
                                                }
                                            }
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("love_stringRequest_error",error.toString());
                                    }
                                });


                                requestQueue.add(love_stringRequest);






                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("cafeDetail_personal_stringRequest_error",error.toString());
                            }
                        });


                        requestQueue.add(personal_stringRequest);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("cafeDetail_review_stringRequest_error",error.toString());
                    }
                });


                requestQueue.add(review_stringRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cafeDetail_cafe_stringRequest_error",error.toString());
            }
        });

        requestQueue.add(cafe_stringRequest);



        // 카페 운영 시간 가져오기
//        Bundle cafeModifyBundle = getArguments();
//        if(cafeModifyBundle != null){
//            if(cafeModifyBundle.getString("time_open_Modi") != null ){
//                moreReview10.setText(cafeModifyBundle.getString("time_open_Modi"));
//                moreReview8.setText(cafeModifyBundle.getString("time_close_Modi"));
//            }
//        }

        // 카페 수정(연필) 버튼 클릭 시,
        cafe_modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name",moreReview3.getText().toString());
                bundle.putString("address",moreReview4.getText().toString());
                bundle.putString("time_open",moreReview10.getText().toString());
                bundle.putString("time_close",moreReview8.getText().toString());
                navController.navigate(R.id.cafe_detail_to_cafe_modify, bundle);
            }
        });


        // 리뷰 플로팅 버튼 클릭 시,
        review_floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("floating_flag", true);
                bundle.putString("floating_cafeName", moreReview2.getText().toString());
                navController.navigate(R.id.cafe_detail_to_review, bundle);
            }
        });


        cafeImageViewPager = root.findViewById(R.id.cafeImageViewPager);
        cafeImageViewPager.setOffscreenPageLimit(5);
        imageList = new ArrayList<>();



//        CafeDetailImagePagerAdapter cafeImageAdapter = new CafeDetailImagePagerAdapter(getActivity().getSupportFragmentManager());
//
//        CafeDetailImageViewPager1 page1 = new CafeDetailImageViewPager1();
//        cafeImageAdapter.cafeImageAddItem(page1);
//        CafeDetailImageViewPager2 page2 = new CafeDetailImageViewPager2();
//        cafeImageAdapter.cafeImageAddItem(page2);
//        CafeDetailImageViewPager3 page3 = new CafeDetailImageViewPager3();
//        cafeImageAdapter.cafeImageAddItem(page3);
//        CafeDetailImageViewPager4 page4 = new CafeDetailImageViewPager4();
//        cafeImageAdapter.cafeImageAddItem(page4);
//        CafeDetailImageViewPager5 page5 = new CafeDetailImageViewPager5();
//        cafeImageAdapter.cafeImageAddItem(page5);



        // 카페디테일에 해당하는 카페별점 보여주기


        return root;
    }

    // 카페디테일 카페이미지 뷰페이저
//    class CafeDetailImagePagerAdapter extends FragmentStatePagerAdapter {
//
//        ArrayList<Fragment> imageItems = new ArrayList<>();
//        public CafeDetailImagePagerAdapter(FragmentManager fm){
//            super(fm);
//        }
//
//        public void cafeImageAddItem(Fragment item){
//            imageItems.add(item);   // Fragment 추가
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return imageItems.get(position);    // 프래그먼트 가져오기
//        }
//
//        @Override
//        public int getCount() {
//            return imageItems.size();   // 프래그먼트 개수반환
//        }
//    }


    // 카페디테일 별점 뷰페이저
//    class CafeDetailRatingPagerAdapter extends FragmentStatePagerAdapter {
//        ArrayList<Fragment> ratingItems = new ArrayList<>();
//        public CafeDetailRatingPagerAdapter(FragmentManager ratingFm) {
//            super(ratingFm);
//        }
//
//        public void cafeRatingAddItem(Fragment item){
//            ratingItems.add(item);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return ratingItems.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return ratingItems.size();
//        }
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        this.getArguments().clear();
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        point_counter = 0;
        return;
    }
}
