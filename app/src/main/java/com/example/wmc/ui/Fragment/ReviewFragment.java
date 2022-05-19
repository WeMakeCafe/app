package com.example.wmc.ui.Fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.databinding.FragmentReviewBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private static NavController navController;

    Button review_search_input;
    Button addTag_cafe_button;
    Button comment_button;
    Button location_button;
    Button finish_button;

    String stag1;
    String stag2;
    String stag3;
    Long cafeNum;

    RatingBar rating_sour;
    RatingBar rating_acerbity;
    RatingBar rating_dessert;
    RatingBar rating_beverage;
    RatingBar rating_twoseat;
    RatingBar rating_fourseat;
    RatingBar rating_manyseat;
    RatingBar rating_toilet;
    RatingBar rating_wifi;
    RatingBar rating_plug;
    RatingBar rating_quiet;
    RatingBar rating_light;

    Float s1;
    Float s2;
    Float s3;
    Float s4;
    Float s5;
    Float s6;
    Float s7;
    Float s8;
    Float s9;
    Float s10;
    Float s11;
    Float s12;

    Long[] k = new Long[36];
    Long mem_num = MainActivity.mem_num;

    ArrayList<Cafe> cafe_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        review_search_input = root.findViewById(R.id.review_search_input);           // 검색창 클릭 시
        addTag_cafe_button = root.findViewById(R.id.addTag_cafe_button);    // 태그의 추가 버튼
        comment_button = root.findViewById(R.id.comment_button);    // 코멘터리 버튼
        location_button = root.findViewById(R.id.location_button);  // 위치인증 버튼
        finish_button = root.findViewById(R.id.finish_button);  // 작성완료 버튼

        rating_sour = root.findViewById(R.id.rating_sour);
        rating_acerbity = root.findViewById(R.id.rating_acerbity);
        rating_dessert = root.findViewById(R.id.rating_dessert);
        rating_beverage = root.findViewById(R.id.rating_beverage);
        rating_twoseat = root.findViewById(R.id.rating_twoseat);
        rating_fourseat = root.findViewById(R.id.rating_fourseat);
        rating_manyseat = root.findViewById(R.id.rating_manyseat);
        rating_toilet = root.findViewById(R.id.rating_toilet);
        rating_wifi = root.findViewById(R.id.rating_wifi);
        rating_plug = root.findViewById(R.id.rating_plug);
        rating_quiet = root.findViewById(R.id.rating_quiet);
        rating_light = root.findViewById(R.id.rating_light);


        // 태그 추가 페이지 (ReviewTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView setTag1 = root.findViewById(R.id.select_tag1); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스1
        TextView setTag2 = root.findViewById(R.id.select_tag2); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스2
        TextView setTag3 = root.findViewById(R.id.select_tag3); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스3

        // 서버연산을 위한 long형 배열 초기화 코드
        for(int i = 0 ; i<=35; i++){
            k[i] = (long) 0;
        }


        // 카페 검색 창 클릭 시,
        review_search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_cafelist);
            }
        });


        // 태그 추가 버튼 클릭 시,
        addTag_cafe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 리뷰 검색에서 리뷰를 작성할 카페를 선택하지 않았을 경우.
                if(review_search_input.getText().toString().equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "리뷰를 작성할 카페를 검색해주세요.", Toast.LENGTH_SHORT).show();
                }

                // 리뷰를 작성할 카페를 선택한 경우
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("cafeName", review_search_input.getText().toString());

                    bundle.putFloat("tastePoint1", rating_sour.getRating());
                    bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                    bundle.putFloat("tastePoint3", rating_dessert.getRating());
                    bundle.putFloat("tastePoint4", rating_beverage.getRating());
                    bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                    bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                    bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                    bundle.putFloat("seatPoint4", rating_toilet.getRating());
                    bundle.putFloat("studyPoint1", rating_wifi.getRating());
                    bundle.putFloat("studyPoint2", rating_plug.getRating());
                    bundle.putFloat("studyPoint3", rating_quiet.getRating());
                    bundle.putFloat("studyPoint4", rating_light.getRating());

                    navController.navigate(R.id.review_to_review_tag, bundle);
                }
            }
        });

        // ReviewTag에서 가져온 태그들 설정 및 카페이름을 기억해두기
        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {

                setTag1.setText(argBundle.getString("key1"));
                setTag2.setText(argBundle.getString("key2"));
                setTag3.setText(argBundle.getString("key3"));
                stag1 = argBundle.getString("key1");
                stag2 = argBundle.getString("key2");
                stag3 = argBundle.getString("key3");

                rating_sour.setRating(argBundle.getFloat("tag_review_tastePoint1"));
                rating_acerbity.setRating(argBundle.getFloat("tag_review_tastePoint2"));
                rating_dessert.setRating(argBundle.getFloat("tag_review_tastePoint3"));
                rating_beverage.setRating(argBundle.getFloat("tag_review_tastePoint4"));
                rating_twoseat.setRating(argBundle.getFloat("tag_review_seatPoint1"));
                rating_fourseat.setRating(argBundle.getFloat("tag_review_seatPoint2"));
                rating_manyseat.setRating(argBundle.getFloat("tag_review_seatPoint3"));
                rating_toilet.setRating(argBundle.getFloat("tag_review_seatPoint4"));
                rating_wifi.setRating(argBundle.getFloat("tag_review_studyPoint1"));
                rating_plug.setRating(argBundle.getFloat("tag_review_studyPoint2"));
                rating_quiet.setRating(argBundle.getFloat("tag_review_studyPoint3"));
                rating_light.setRating(argBundle.getFloat("tag_review_studyPoint4"));
                s1 = argBundle.getFloat("tag_review_tastePoint1");
                s2 = argBundle.getFloat("tag_review_tastePoint2");
                s3 = argBundle.getFloat("tag_review_tastePoint3");
                s4 = argBundle.getFloat("tag_review_tastePoint4");
                s5 = argBundle.getFloat("tag_review_seatPoint1");
                s6 = argBundle.getFloat("tag_review_seatPoint2");
                s7 = argBundle.getFloat("tag_review_seatPoint3");
                s8 = argBundle.getFloat("tag_review_seatPoint4");
                s9 = argBundle.getFloat("tag_review_studyPoint1");
                s10 = argBundle.getFloat("tag_review_studyPoint2");
                s11 = argBundle.getFloat("tag_review_studyPoint3");
                s12 = argBundle.getFloat("tag_review_studyPoint4");

                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // 카페이름 Bold처리
                review_search_input.setGravity(Gravity.CENTER);          // 카페 위치 Center로 변경
                review_search_input.setText(argBundle.getString("review_cafeName"));
            }
        }

        // 위치인증 버튼 클릭 시,
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 위치 인증이 되면 아래 Toast를 띄움
                Toast.makeText(getContext().getApplicationContext(), "위치인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        // 카페 디테일에서 리뷰 작성 플로팅 버튼 클릭 시, 또는 ReviewCafeList에서 선택한 카페 이름 가져옴
        Bundle cafeNameBundle = getArguments();
        if(cafeNameBundle != null) {
            if(cafeNameBundle.getString("cafeName") != null ){
                review_search_input.setText(cafeNameBundle.getString("cafeName"));
                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // 카페이름 Bold처리
                review_search_input.setGravity(Gravity.CENTER);          // 카페 위치 Center로 변경
            }
        }




        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String url = getResources().getString(R.string.url) + "cafe";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                Type listType = new TypeToken<ArrayList<Cafe>>() {
                }.getType();

                cafe_list = gson.fromJson(changeString, listType);


                for(Cafe c : cafe_list) {
                    if(c.getCafeName().equals(review_search_input.getText().toString())) {
                        cafeNum = c.getCafeNum();
                    }
                }

                // 코멘터리 버튼 클릭 시,
                comment_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(rating_sour.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        } else if(rating_acerbity.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_dessert.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_beverage.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_twoseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_fourseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_manyseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_toilet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_wifi.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_plug.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_quiet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_light.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(setTag1.getText().toString().equals("")){
                            Toast.makeText(getContext().getApplicationContext(), "태그를 추가해주세요.", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들
                            bundle.putString("tag1", setTag1.getText().toString());
                            bundle.putString("tag2", setTag2.getText().toString());
                            bundle.putString("tag3", setTag3.getText().toString());
                            bundle.putFloat("tastePoint1", rating_sour.getRating());
                            bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                            bundle.putFloat("tastePoint3", rating_dessert.getRating());
                            bundle.putFloat("tastePoint4", rating_beverage.getRating());
                            bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                            bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                            bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                            bundle.putFloat("seatPoint4", rating_toilet.getRating());
                            bundle.putFloat("studyPoint1", rating_wifi.getRating());
                            bundle.putFloat("studyPoint2", rating_plug.getRating());
                            bundle.putFloat("studyPoint3", rating_quiet.getRating());
                            bundle.putFloat("studyPoint4", rating_light.getRating());
                            bundle.putLong("cafeNum", cafeNum);
                            bundle.putLong("memNum", mem_num);
                            navController.navigate(R.id.review_to_review_comment, bundle);


                        }
                    }
                });


                // 작성완료 버튼 클릭 시,
                finish_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 내가 리뷰를 작성한 CafeDetailFragment로 이동
                        // 만약 별점을 비워둘경우, 별점을 체크하게 Toast메시지를 띄움
                        if(rating_sour.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        } else if(rating_acerbity.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_dessert.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_beverage.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_twoseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_fourseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_manyseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_toilet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_wifi.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_plug.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_quiet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(rating_light.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "별점을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(setTag1.getText().toString().equals("")){
                            Toast.makeText(getContext().getApplicationContext(), "태그를 추가해주세요.", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Map map = new HashMap();
                            map.put("tastePoint1", Integer.valueOf((int)rating_sour.getRating()));
                            map.put("tastePoint2", Integer.valueOf((int)rating_acerbity.getRating()));
                            map.put("tastePoint3", Integer.valueOf((int)rating_dessert.getRating()));
                            map.put("tastePoint4", Integer.valueOf((int)rating_beverage.getRating()));
                            map.put("seatPoint1", Integer.valueOf((int)rating_twoseat.getRating()));
                            map.put("seatPoint2", Integer.valueOf((int)rating_fourseat.getRating()));
                            map.put("seatPoint3", Integer.valueOf((int)rating_manyseat.getRating()));
                            map.put("seatPoint4", Integer.valueOf((int)rating_toilet.getRating()));
                            map.put("studyPoint1", Integer.valueOf((int)rating_wifi.getRating()));
                            map.put("studyPoint2", Integer.valueOf((int)rating_plug.getRating()));
                            map.put("studyPoint3", Integer.valueOf((int)rating_quiet.getRating()));
                            map.put("studyPoint4", Integer.valueOf((int)rating_light.getRating()));
                            map.put("cafeNum", cafeNum);
                            map.put("likeCount", 0);
                            map.put("reviewText", null);
                            map.put("memNum", mem_num);
                            switch (stag1) {
                                case ("#쓴맛"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#신맛"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#짠맛"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#단맛"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#향미"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#바디감"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#콜드브루"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#메뉴多"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#가성비"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#양많음"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#디저트맛집"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#논커피맛집"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#인스타"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#앤티크"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#모던"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#캐주얼"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#이국적"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#일상"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#따뜻한"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#조용한"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#우드톤"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#채광"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#힙한"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#귀여운"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#친절한"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#청결한"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#애견"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#주차장"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#노키즈존"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#교통편의"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#신속한"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#쾌적한"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#회의실"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#규모大"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#규모小"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#편한좌석"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }

                            switch (stag2) {
                                case ("#쓴맛"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#신맛"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#짠맛"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#단맛"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#향미"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#바디감"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#콜드브루"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#메뉴多"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#가성비"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#양많음"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#디저트맛집"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#논커피맛집"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#인스타"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#앤티크"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#모던"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#캐주얼"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#이국적"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#일상"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#따뜻한"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#조용한"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#우드톤"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#채광"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#힙한"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#귀여운"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#친절한"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#청결한"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#애견"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#주차장"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#노키즈존"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#교통편의"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#신속한"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#쾌적한"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#회의실"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#규모大"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#규모小"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#편한좌석"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }

                            switch (stag3) {
                                case ("#쓴맛"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#신맛"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#짠맛"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#단맛"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#향미"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#바디감"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#콜드브루"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#메뉴多"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#가성비"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#양많음"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#디저트맛집"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#논커피맛집"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#인스타"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#앤티크"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#모던"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#캐주얼"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#이국적"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#일상"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#따뜻한"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#조용한"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#우드톤"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#채광"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#힙한"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#귀여운"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#친절한"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#청결한"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#애견"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#주차장"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#노키즈존"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#교통편의"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#신속한"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#쾌적한"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#회의실"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#규모大"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#규모小"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#편한좌석"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }
                            map.put("keyword1", k[0]);
                            map.put("keyword2", k[1]);
                            map.put("keyword3", k[2]);
                            map.put("keyword4", k[3]);
                            map.put("keyword5", k[4]);
                            map.put("keyword6", k[5]);
                            map.put("keyword7", k[6]);
                            map.put("keyword8", k[7]);
                            map.put("keyword9", k[8]);
                            map.put("keyword10", k[9]);
                            map.put("keyword11", k[10]);
                            map.put("keyword12", k[11]);
                            map.put("keyword13", k[12]);
                            map.put("keyword14", k[13]);
                            map.put("keyword15", k[14]);
                            map.put("keyword16", k[15]);
                            map.put("keyword17", k[16]);
                            map.put("keyword18", k[17]);
                            map.put("keyword19", k[18]);
                            map.put("keyword20", k[19]);
                            map.put("keyword21", k[20]);
                            map.put("keyword22", k[21]);
                            map.put("keyword23", k[22]);
                            map.put("keyword24", k[23]);
                            map.put("keyword25", k[24]);
                            map.put("keyword26", k[25]);
                            map.put("keyword27", k[26]);
                            map.put("keyword28", k[27]);
                            map.put("keyword29", k[28]);
                            map.put("keyword30", k[29]);
                            map.put("keyword31", k[30]);
                            map.put("keyword32", k[31]);
                            map.put("keyword33", k[32]);
                            map.put("keyword34", k[33]);
                            map.put("keyword35", k[34]);
                            map.put("keyword36", k[35]);


                            String url2 = getResources().getString(R.string.url) + "review";
                            JSONObject jsonObject = new JSONObject(map);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("test1", error.toString());
                                        }
                                    }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=UTF-8";
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(requireContext());
                            queue.add(objectRequest);

                            // 여기는 cafePut작업해야할 곳
                            for(Cafe c : cafe_list) {
                                if(c.getCafeNum().equals(cafeNum)) {  //bundle에서 가져온 카페아이디값 cafe_name에 넣어서 비교 연산
                                    Map map2 = new HashMap();
                                    map2.put("cafeName", c.getCafeName());
                                    map2.put("cafeAddress", c.getCafeAddress());
                                    map2.put("openTime", c.getOpenTime());
                                    map2.put("closeTime", c.getCloseTime());
                                    map2.put("bookmarkNum", c.getBookmarkNum());
                                    map2.put("scoreNum", c.getScoreNum());
                                    map2.put("keyword1", c.getKeyword1()+k[0]);
                                    map2.put("keyword2", c.getKeyword2()+k[1]);
                                    map2.put("keyword3", c.getKeyword3()+k[2]);
                                    map2.put("keyword4", c.getKeyword4()+k[3]);
                                    map2.put("keyword5", c.getKeyword5()+k[4]);
                                    map2.put("keyword6", c.getKeyword6()+k[5]);
                                    map2.put("keyword7", c.getKeyword7()+k[6]);
                                    map2.put("keyword8", c.getKeyword8()+k[7]);
                                    map2.put("keyword9", c.getKeyword9()+k[8]);
                                    map2.put("keyword10", c.getKeyword10()+k[9]);
                                    map2.put("keyword11", c.getKeyword11()+k[10]);
                                    map2.put("keyword12", c.getKeyword12()+k[11]);
                                    map2.put("keyword13", c.getKeyword13()+k[12]);
                                    map2.put("keyword14", c.getKeyword14()+k[13]);
                                    map2.put("keyword15", c.getKeyword15()+k[14]);
                                    map2.put("keyword16", c.getKeyword16()+k[15]);
                                    map2.put("keyword17", c.getKeyword17()+k[16]);
                                    map2.put("keyword18", c.getKeyword18()+k[17]);
                                    map2.put("keyword19", c.getKeyword19()+k[18]);
                                    map2.put("keyword20", c.getKeyword20()+k[19]);
                                    map2.put("keyword21", c.getKeyword21()+k[20]);
                                    map2.put("keyword22", c.getKeyword22()+k[21]);
                                    map2.put("keyword23", c.getKeyword23()+k[22]);
                                    map2.put("keyword24", c.getKeyword24()+k[23]);
                                    map2.put("keyword25", c.getKeyword25()+k[24]);
                                    map2.put("keyword26", c.getKeyword26()+k[25]);
                                    map2.put("keyword27", c.getKeyword27()+k[26]);
                                    map2.put("keyword28", c.getKeyword28()+k[27]);
                                    map2.put("keyword29", c.getKeyword29()+k[28]);
                                    map2.put("keyword30", c.getKeyword30()+k[29]);
                                    map2.put("keyword31", c.getKeyword31()+k[30]);
                                    map2.put("keyword32", c.getKeyword32()+k[31]);
                                    map2.put("keyword33", c.getKeyword33()+k[32]);
                                    map2.put("keyword34", c.getKeyword34()+k[33]);
                                    map2.put("keyword35", c.getKeyword35()+k[34]);
                                    map2.put("keyword36", c.getKeyword36()+k[35]);
                                    map2.put("tastePoint1", c.getTastePoint1()+Integer.valueOf((int)rating_sour.getRating()));
                                    map2.put("tastePoint2", c.getTastePoint2()+Integer.valueOf((int)rating_acerbity.getRating()));
                                    map2.put("tastePoint3", c.getTastePoint3()+Integer.valueOf((int)rating_dessert.getRating()));
                                    map2.put("tastePoint4", c.getTastePoint4()+Integer.valueOf((int)rating_beverage.getRating()));
                                    map2.put("seatPoint1", c.getSeatPoint1()+Integer.valueOf((int)rating_twoseat.getRating()));
                                    map2.put("seatPoint2", c.getSeatPoint2()+Integer.valueOf((int)rating_fourseat.getRating()));
                                    map2.put("seatPoint3", c.getSeatPoint3()+Integer.valueOf((int)rating_manyseat.getRating()));
                                    map2.put("seatPoint4", c.getSeatPoint4()+Integer.valueOf((int)rating_toilet.getRating()));
                                    map2.put("studyPoint1", c.getStudyPoint1()+Integer.valueOf((int)rating_wifi.getRating()));
                                    map2.put("studyPoint2", c.getStudyPoint2()+Integer.valueOf((int)rating_plug.getRating()));
                                    map2.put("studyPoint3", c.getStudyPoint3()+Integer.valueOf((int)rating_quiet.getRating()));
                                    map2.put("studyPoint4", c.getStudyPoint4()+Integer.valueOf((int)rating_light.getRating()));


                                    JSONObject jsonObject2 = new JSONObject(map2);

                                    String url3 = getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // 해당 카페에만 데이터 삽입하기 위함

                                    JsonObjectRequest objectRequest2 = new JsonObjectRequest(Request.Method.PUT, url3, jsonObject2,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("qwer", error.toString());
                                                }
                                            }) {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    RequestQueue queue2 = Volley.newRequestQueue(requireContext());
                                    queue2.add(objectRequest2);

                                }
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("cafeName",review_search_input.getText().toString());
                            navController.navigate(R.id.review_to_cafe_detail, bundle);
                        }
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                Log.e("test_error", error.toString());
            }
        });
        requestQueue.add(stringRequest);

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