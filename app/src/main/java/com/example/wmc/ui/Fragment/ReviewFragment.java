package com.example.wmc.ui.Fragment;

import android.graphics.Typeface;
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
import com.android.volley.toolbox.StringRequest;
import com.example.wmc.R;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.databinding.FragmentReviewBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private static NavController navController;

    Button review_search_input;
    Button addTag_cafe_button;
    Button comment_button;
    Button location_button;
    Button finish_button;
    TextView tag1;  /// 태그 선언
    TextView tag2;
    TextView tag3;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        review_search_input = root.findViewById(R.id.review_search_input);           // 검색창 클릭 시
        addTag_cafe_button = root.findViewById(R.id.addTag_cafe_button);    // 태그의 추가 버튼
        comment_button = root.findViewById(R.id.comment_button);    // 코멘터리 버튼
        location_button = root.findViewById(R.id.location_button);  // 위치인증 버튼
        finish_button = root.findViewById(R.id.finish_button);  // 작성완료 버튼
        tag1 = root.findViewById(R.id.select_tag1);    // 태그 연결
        tag2 = root.findViewById(R.id.select_tag2);
        tag3 = root.findViewById(R.id.select_tag3);


        // 태그 추가 페이지 (ReviewTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView setTag1 = root.findViewById(R.id.select_tag1); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스1
        TextView setTag2 = root.findViewById(R.id.select_tag2); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스2
        TextView setTag3 = root.findViewById(R.id.select_tag3); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스3


        // ReviewTag에서 가져온 태그들 설정 및 카페이름을 기억해두기
        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {
                setTag1.setText(argBundle.getString("key1"));
                setTag2.setText(argBundle.getString("key2"));
                setTag3.setText(argBundle.getString("key3"));
                review_search_input.setText(argBundle.getString("review_cafeName"));
                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // 카페이름 Bold처리
                review_search_input.setGravity(Gravity.CENTER);          // 카페 위치 Center로 변경
            }
        }

        ///////서버 호출








        ////// 서버 엔드


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

                    navController.navigate(R.id.review_to_review_tag, bundle);
                }
            }
        });

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_comment);
            }
        });

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
                    navController.navigate(R.id.review_to_review_comment);
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
                    navController.navigate(R.id.review_to_cafe_detail);
                }
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