package com.example.wmc.ui.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wmc.R;
import com.example.wmc.databinding.FragmentReviewBinding;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private static NavController navController;
    Button searchText;
    Button addTag_cafe_button;
    Button comment_button;
    Button location_button;
    Button finish_button;

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
        searchText = root.findViewById(R.id.review_search_input);
        addTag_cafe_button = root.findViewById(R.id.addTag_cafe_button);
        comment_button = root.findViewById(R.id.comment_button);
        location_button = root.findViewById(R.id.location_button);
        finish_button = root.findViewById(R.id.finish_button);

        // 카페 검색 창 클릭 시,
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_search);
            }
        });


        // 태그 추가 버튼 클릭 시,
        addTag_cafe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_tag);
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

        // 태그 추가 페이지 (ReviewTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView setTag1 = root.findViewById(R.id.select_tag1); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스1
        TextView setTag2 = root.findViewById(R.id.select_tag2); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스2
        TextView setTag3 = root.findViewById(R.id.select_tag3); // 태그 추가 완료 시 반영할 리뷰 작성 페이지의 태그 박스3

        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {
                setTag1.setText(argBundle.getString("key1"));
                setTag2.setText(argBundle.getString("key2"));
                setTag3.setText(argBundle.getString("key3"));
            }
        }

        // 카페 디테일에서 리뷰 작성 플로팅 버튼 클릭 시, 카페 이름 가져옴
        Bundle cafeNameBundle = getArguments();
        if(cafeNameBundle != null) {
            if(cafeNameBundle.getString("cafeName") != null ){
                searchText.setText(cafeNameBundle.getString("cafeName"));
                searchText.setTypeface(Typeface.DEFAULT_BOLD);  // 카페이름 Bold처리
                searchText.setGravity(Gravity.CENTER);          // 카페 위치 Center로 변경
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