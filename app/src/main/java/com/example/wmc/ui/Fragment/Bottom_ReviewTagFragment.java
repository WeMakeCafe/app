package com.example.wmc.ui.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;
import com.example.wmc.ReviewTag.ReviewTagAdapter;
import com.example.wmc.ReviewTag.ReviewTagItem;
import com.example.wmc.databinding.FragmentReviewTagBinding;

import java.util.ArrayList;

public class Bottom_ReviewTagFragment extends Fragment {

    private FragmentReviewTagBinding binding;
    private static NavController navController;

    String review_cafe_name = null;   // ReviewFragment에서 가져온 카페이름을 저장할 변수 (밑에도 동일)

    Float s1 = null;
    Float s2 = null;
    Float s3 = null;
    Float s4 = null;
    Float s5 = null;
    Float s6 = null;
    Float s7 = null;
    Float s8 = null;
    Float s9 = null;
    Float s10 = null;
    Float s11 = null;
    Float s12 = null;
    Boolean flag;
    Long reviewNum;
    Integer score1; // 본래의 리뷰에 있던 점수 보관용
    Integer score2;
    Integer score3;
    Integer score4;
    Integer score5;
    Integer score6;
    Integer score7;
    Integer score8;
    Integer score9;
    Integer score10;
    Integer score11;
    Integer score12;
    String comment = "";
    Long[] k2 = new Long[36];

    Boolean floating_flag = false;
    Boolean reviewCafeList_flag = false;
    Boolean cafeDetail_reviewModify_flag = false;
    Boolean moreReview_reviewModify_flag = false;
    Boolean mypage_reviewModify_flag = false;

    Boolean location_flag = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView view1 = root.findViewById(R.id.tasteimage7); // 음료, 맛 버튼의 뒷 배경
        ImageView view2 = root.findViewById(R.id.feelingImage7); // 분위기 버튼의 뒷 배경
        ImageView view3 = root.findViewById(R.id.serviceimage7); // 서비스 버튼의 뒷 배경
        TextView taste = root.findViewById(R.id.tasteTab7); // 음료, 맛 버튼의 글씨
        TextView feeling = root.findViewById(R.id.feelingTab7); // 분위기 버튼의 글씨
        TextView service = root.findViewById(R.id.serviceTab7); // 서비스 버튼의 글씨
        TextView addTag1 = root.findViewById(R.id.addTag1); // 아래 태그 버튼 3개 중 1
        TextView addTag2 = root.findViewById(R.id.addTag2); // 아래 태그 버튼 3개 중 2
        TextView addTag3 = root.findViewById(R.id.addTag3); // 아래 태그 버튼 3개 중 3
        Button tag1_delete_button = root.findViewById(R.id.tag1_delete_button); // 아래 태그 버튼 3개의 X버튼
        Button tag2_delete_button = root.findViewById(R.id.tag2_delete_button); // 아래 태그 버튼 3개의 X버튼
        Button tag3_delete_button = root.findViewById(R.id.tag3_delete_button); // 아래 태그 버튼 3개의 X버튼
        Button addTag_button3 = root.findViewById(R.id.addTag_button3); // 태그 선택 후, 추가하기 버튼

        for(int i = 0; i < k2.length; i++)
            k2[i] = (long) 0;



        if(getArguments().getBoolean("floating_flag")){ // 플로팅에서 온 경우

            floating_flag = getArguments().getBoolean("floating_flag");

            review_cafe_name = getArguments().getString("floating_cafeName");

            s1 = getArguments().getFloat("tastePoint1");
            s2 = getArguments().getFloat("tastePoint2");
            s3 = getArguments().getFloat("tastePoint3");
            s4 = getArguments().getFloat("tastePoint4");

            s5 = getArguments().getFloat("seatPoint1");
            s6 = getArguments().getFloat("seatPoint2");
            s7 = getArguments().getFloat("seatPoint3");
            s8 = getArguments().getFloat("seatPoint4");

            s9 = getArguments().getFloat("studyPoint1");
            s10 = getArguments().getFloat("studyPoint2");
            s11 = getArguments().getFloat("studyPoint3");
            s12 = getArguments().getFloat("studyPoint4");

            location_flag = getArguments().getBoolean("location_flag");

        }

        else if(getArguments().getBoolean("reviewCafeList_flag")){  // 하단바 리뷰 작성을 통해서 온 경우,
            reviewCafeList_flag = getArguments().getBoolean("reviewCafeList_flag");

            review_cafe_name = getArguments().getString("reviewCafeList_cafeName");

            s1 = getArguments().getFloat("tastePoint1");
            s2 = getArguments().getFloat("tastePoint2");
            s3 = getArguments().getFloat("tastePoint3");
            s4 = getArguments().getFloat("tastePoint4");

            s5 = getArguments().getFloat("seatPoint1");
            s6 = getArguments().getFloat("seatPoint2");
            s7 = getArguments().getFloat("seatPoint3");
            s8 = getArguments().getFloat("seatPoint4");

            s9 = getArguments().getFloat("studyPoint1");
            s10 = getArguments().getFloat("studyPoint2");
            s11 = getArguments().getFloat("studyPoint3");
            s12 = getArguments().getFloat("studyPoint4");

            location_flag = getArguments().getBoolean("location_flag");
        }

        else if(getArguments().getBoolean("cafeDetail_reviewModify_flag")) {   // 리뷰 수정을 통해서 온 경우,
            cafeDetail_reviewModify_flag = getArguments().getBoolean("cafeDetail_reviewModify_flag");

            review_cafe_name = getArguments().getString("cafeName");

            s1 = getArguments().getFloat("tastePoint1");
            s2 = getArguments().getFloat("tastePoint2");
            s3 = getArguments().getFloat("tastePoint3");
            s4 = getArguments().getFloat("tastePoint4");

            s5 = getArguments().getFloat("seatPoint1");
            s6 = getArguments().getFloat("seatPoint2");
            s7 = getArguments().getFloat("seatPoint3");
            s8 = getArguments().getFloat("seatPoint4");

            s9 = getArguments().getFloat("studyPoint1");
            s10 = getArguments().getFloat("studyPoint2");
            s11 = getArguments().getFloat("studyPoint3");
            s12 = getArguments().getFloat("studyPoint4");
            flag = getArguments().getBoolean("flag");
            reviewNum = getArguments().getLong("reviewNum");

            score1 = getArguments().getInt("score1");
            score2 = getArguments().getInt("score2");
            score3 = getArguments().getInt("score3");
            score4 = getArguments().getInt("score4");
            score5 = getArguments().getInt("score5");
            score6 = getArguments().getInt("score6");
            score7 = getArguments().getInt("score7");
            score8 = getArguments().getInt("score8");
            score9 = getArguments().getInt("score9");
            score10 = getArguments().getInt("score10");
            score11 = getArguments().getInt("score11");
            score12 = getArguments().getInt("score12");

            k2[0] = getArguments().getLong("k2-1");
            k2[1] = getArguments().getLong("k2-2");
            k2[2] = getArguments().getLong("k2-3");
            k2[3] = getArguments().getLong("k2-4");
            k2[4] = getArguments().getLong("k2-5");
            k2[5] = getArguments().getLong("k2-6");
            k2[6] = getArguments().getLong("k2-7");
            k2[7] = getArguments().getLong("k2-8");
            k2[8] = getArguments().getLong("k2-9");
            k2[9] = getArguments().getLong("k2-10");
            k2[10] = getArguments().getLong("k2-11");
            k2[11] = getArguments().getLong("k2-12");
            k2[12] = getArguments().getLong("k2-13");
            k2[13] = getArguments().getLong("k2-14");
            k2[14] = getArguments().getLong("k2-15");
            k2[15] = getArguments().getLong("k2-16");
            k2[16] = getArguments().getLong("k2-17");
            k2[17] = getArguments().getLong("k2-18");
            k2[18] = getArguments().getLong("k2-19");
            k2[19] = getArguments().getLong("k2-20");
            k2[20] = getArguments().getLong("k2-21");
            k2[21] = getArguments().getLong("k2-22");
            k2[22] = getArguments().getLong("k2-23");
            k2[23] = getArguments().getLong("k2-24");
            k2[24] = getArguments().getLong("k2-25");
            k2[25] = getArguments().getLong("k2-26");
            k2[26] = getArguments().getLong("k2-27");
            k2[27] = getArguments().getLong("k2-28");
            k2[28] = getArguments().getLong("k2-29");
            k2[29] = getArguments().getLong("k2-30");
            k2[30] = getArguments().getLong("k2-31");
            k2[31] = getArguments().getLong("k2-32");
            k2[32] = getArguments().getLong("k2-33");
            k2[33] = getArguments().getLong("k2-34");
            k2[34] = getArguments().getLong("k2-35");
            k2[35] = getArguments().getLong("k2-36");

            comment = getArguments().getString("comment");

            location_flag = getArguments().getBoolean("location_flag");
        }

        else if(getArguments().getBoolean("moreReview_reviewModify_flag")) {   // 리뷰 수정을 통해서 온 경우,
            moreReview_reviewModify_flag = getArguments().getBoolean("moreReview_reviewModify_flag");

            review_cafe_name = getArguments().getString("cafeName");

            s1 = getArguments().getFloat("tastePoint1");
            s2 = getArguments().getFloat("tastePoint2");
            s3 = getArguments().getFloat("tastePoint3");
            s4 = getArguments().getFloat("tastePoint4");

            s5 = getArguments().getFloat("seatPoint1");
            s6 = getArguments().getFloat("seatPoint2");
            s7 = getArguments().getFloat("seatPoint3");
            s8 = getArguments().getFloat("seatPoint4");

            s9 = getArguments().getFloat("studyPoint1");
            s10 = getArguments().getFloat("studyPoint2");
            s11 = getArguments().getFloat("studyPoint3");
            s12 = getArguments().getFloat("studyPoint4");
            flag = getArguments().getBoolean("flag");
            reviewNum = getArguments().getLong("reviewNum");

            score1 = getArguments().getInt("score1");
            score2 = getArguments().getInt("score2");
            score3 = getArguments().getInt("score3");
            score4 = getArguments().getInt("score4");
            score5 = getArguments().getInt("score5");
            score6 = getArguments().getInt("score6");
            score7 = getArguments().getInt("score7");
            score8 = getArguments().getInt("score8");
            score9 = getArguments().getInt("score9");
            score10 = getArguments().getInt("score10");
            score11 = getArguments().getInt("score11");
            score12 = getArguments().getInt("score12");

            k2[0] = getArguments().getLong("k2-1");
            k2[1] = getArguments().getLong("k2-2");
            k2[2] = getArguments().getLong("k2-3");
            k2[3] = getArguments().getLong("k2-4");
            k2[4] = getArguments().getLong("k2-5");
            k2[5] = getArguments().getLong("k2-6");
            k2[6] = getArguments().getLong("k2-7");
            k2[7] = getArguments().getLong("k2-8");
            k2[8] = getArguments().getLong("k2-9");
            k2[9] = getArguments().getLong("k2-10");
            k2[10] = getArguments().getLong("k2-11");
            k2[11] = getArguments().getLong("k2-12");
            k2[12] = getArguments().getLong("k2-13");
            k2[13] = getArguments().getLong("k2-14");
            k2[14] = getArguments().getLong("k2-15");
            k2[15] = getArguments().getLong("k2-16");
            k2[16] = getArguments().getLong("k2-17");
            k2[17] = getArguments().getLong("k2-18");
            k2[18] = getArguments().getLong("k2-19");
            k2[19] = getArguments().getLong("k2-20");
            k2[20] = getArguments().getLong("k2-21");
            k2[21] = getArguments().getLong("k2-22");
            k2[22] = getArguments().getLong("k2-23");
            k2[23] = getArguments().getLong("k2-24");
            k2[24] = getArguments().getLong("k2-25");
            k2[25] = getArguments().getLong("k2-26");
            k2[26] = getArguments().getLong("k2-27");
            k2[27] = getArguments().getLong("k2-28");
            k2[28] = getArguments().getLong("k2-29");
            k2[29] = getArguments().getLong("k2-30");
            k2[30] = getArguments().getLong("k2-31");
            k2[31] = getArguments().getLong("k2-32");
            k2[32] = getArguments().getLong("k2-33");
            k2[33] = getArguments().getLong("k2-34");
            k2[34] = getArguments().getLong("k2-35");
            k2[35] = getArguments().getLong("k2-36");

            comment = getArguments().getString("comment");

            location_flag = getArguments().getBoolean("location_flag");
        }

        else if(getArguments().getBoolean("mypage_reviewModify_flag")) {   // 리뷰 수정을 통해서 온 경우,
            mypage_reviewModify_flag = getArguments().getBoolean("mypage_reviewModify_flag");

            review_cafe_name = getArguments().getString("cafeName");

            s1 = getArguments().getFloat("tastePoint1");
            s2 = getArguments().getFloat("tastePoint2");
            s3 = getArguments().getFloat("tastePoint3");
            s4 = getArguments().getFloat("tastePoint4");

            s5 = getArguments().getFloat("seatPoint1");
            s6 = getArguments().getFloat("seatPoint2");
            s7 = getArguments().getFloat("seatPoint3");
            s8 = getArguments().getFloat("seatPoint4");

            s9 = getArguments().getFloat("studyPoint1");
            s10 = getArguments().getFloat("studyPoint2");
            s11 = getArguments().getFloat("studyPoint3");
            s12 = getArguments().getFloat("studyPoint4");
            flag = getArguments().getBoolean("flag");
            reviewNum = getArguments().getLong("reviewNum");

            score1 = getArguments().getInt("score1");
            score2 = getArguments().getInt("score2");
            score3 = getArguments().getInt("score3");
            score4 = getArguments().getInt("score4");
            score5 = getArguments().getInt("score5");
            score6 = getArguments().getInt("score6");
            score7 = getArguments().getInt("score7");
            score8 = getArguments().getInt("score8");
            score9 = getArguments().getInt("score9");
            score10 = getArguments().getInt("score10");
            score11 = getArguments().getInt("score11");
            score12 = getArguments().getInt("score12");

            k2[0] = getArguments().getLong("k2-1");
            k2[1] = getArguments().getLong("k2-2");
            k2[2] = getArguments().getLong("k2-3");
            k2[3] = getArguments().getLong("k2-4");
            k2[4] = getArguments().getLong("k2-5");
            k2[5] = getArguments().getLong("k2-6");
            k2[6] = getArguments().getLong("k2-7");
            k2[7] = getArguments().getLong("k2-8");
            k2[8] = getArguments().getLong("k2-9");
            k2[9] = getArguments().getLong("k2-10");
            k2[10] = getArguments().getLong("k2-11");
            k2[11] = getArguments().getLong("k2-12");
            k2[12] = getArguments().getLong("k2-13");
            k2[13] = getArguments().getLong("k2-14");
            k2[14] = getArguments().getLong("k2-15");
            k2[15] = getArguments().getLong("k2-16");
            k2[16] = getArguments().getLong("k2-17");
            k2[17] = getArguments().getLong("k2-18");
            k2[18] = getArguments().getLong("k2-19");
            k2[19] = getArguments().getLong("k2-20");
            k2[20] = getArguments().getLong("k2-21");
            k2[21] = getArguments().getLong("k2-22");
            k2[22] = getArguments().getLong("k2-23");
            k2[23] = getArguments().getLong("k2-24");
            k2[24] = getArguments().getLong("k2-25");
            k2[25] = getArguments().getLong("k2-26");
            k2[26] = getArguments().getLong("k2-27");
            k2[27] = getArguments().getLong("k2-28");
            k2[28] = getArguments().getLong("k2-29");
            k2[29] = getArguments().getLong("k2-30");
            k2[30] = getArguments().getLong("k2-31");
            k2[31] = getArguments().getLong("k2-32");
            k2[32] = getArguments().getLong("k2-33");
            k2[33] = getArguments().getLong("k2-34");
            k2[34] = getArguments().getLong("k2-35");
            k2[35] = getArguments().getLong("k2-36");

            comment = getArguments().getString("comment");

            location_flag = getArguments().getBoolean("location_flag");

            Log.d("myReview에서 받은comment", comment);

        }

        ////////////////////////////////////////////////////////////////////////////////////
        // 아래 3개 삭제 버튼 클릭 이벤트 작성

        tag1_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( addTag2.getText().toString().equals("")) {
                    addTag1.setText("");
                }
                else {
                    addTag1.setText(addTag2.getText().toString());

                    if( addTag3.getText().toString().equals(""))
                        addTag2.setText("");
                    else {
                        addTag2.setText(addTag3.getText().toString());
                        addTag3.setText("");
                    }
                }
            }
        });

        tag2_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTag3.getText().toString().equals("")) {
                    addTag2.setText("");
                }
                else {
                    addTag2.setText(addTag3.getText().toString());
                    addTag3.setText("");
                }
            }
        });

        tag3_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag3.setText("");
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        // 태그 추가 페이지의 태그 리사이클러뷰 작성

        ArrayList<ReviewTagItem> reviewTagItems = new ArrayList<>();

        // 더미 데이터 *현재 초기값을 어떻게 처리해야하는지 어려움 있음! 고민해봐야 할 문제 -> 이 더미데이터 삭제 시 초기 아이템 값이 아예 등장하지 않음
        reviewTagItems.add(new ReviewTagItem("#쓴맛"));
        reviewTagItems.add(new ReviewTagItem("#신맛"));
        reviewTagItems.add(new ReviewTagItem("#짠맛"));
        reviewTagItems.add(new ReviewTagItem("#단맛"));
        reviewTagItems.add(new ReviewTagItem("#향미"));
        reviewTagItems.add(new ReviewTagItem("#바디감"));
        reviewTagItems.add(new ReviewTagItem("#콜드브루"));
        reviewTagItems.add(new ReviewTagItem("#메뉴多"));
        reviewTagItems.add(new ReviewTagItem("#가성비"));
        reviewTagItems.add(new ReviewTagItem("#양많음"));
        reviewTagItems.add(new ReviewTagItem("#디저트맛집"));
        reviewTagItems.add(new ReviewTagItem("#논커피맛집"));

        // Recycler view
        RecyclerView reviewTagRecyclerView = root.findViewById(R.id.tag_recycle);

        // Adapter 추가
        ReviewTagAdapter reviewTagAdapter = new ReviewTagAdapter(reviewTagItems);
        reviewTagRecyclerView.setAdapter(reviewTagAdapter);

        // Layout manager 추가
        GridLayoutManager reviewTagLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        reviewTagRecyclerView.setLayoutManager(reviewTagLayoutManager);

        // 태그 클릭 시,
        reviewTagAdapter.setOnItemClickListener_ReviewTag(new ReviewTagAdapter.OnItemClickEventListener_ReviewTag() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final ReviewTagItem item = reviewTagItems.get(a_position);

                String selectedTag = item.getTagName();

                if(addTag1.getText().toString().equals("")){
                    if(addTag2.getText().toString().equals(selectedTag) || addTag3.getText().toString().equals(selectedTag))
                        Toast.makeText(getContext().getApplicationContext(), "이미 선택한 태그입니다.", Toast.LENGTH_SHORT).show();
                    else
                        addTag1.setText(selectedTag);
                }

                else if(addTag2.getText().toString().equals("")){
                    if(addTag1.getText().toString().equals(selectedTag) || addTag3.getText().toString().equals(selectedTag))
                        Toast.makeText(getContext().getApplicationContext(), "이미 선택한 태그입니다.", Toast.LENGTH_SHORT).show();
                    else
                        addTag2.setText(selectedTag);
                }
                else if(addTag3.getText().toString().equals("")){
                    if(addTag1.getText().toString().equals(selectedTag) || addTag2.getText().toString().equals(selectedTag))
                        Toast.makeText(getContext().getApplicationContext(), "이미 선택한 태그입니다.", Toast.LENGTH_SHORT).show();
                    else
                        addTag3.setText(selectedTag);
                }
                else
                    Toast.makeText(getContext().getApplicationContext(), "태그3개를 모두 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 음료, 맛 메뉴 선택했을 때 -> 글씨 색, 배경 색, 리사이클러뷰 내 아이템 초기화 후 갱신
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view1.setImageResource(R.drawable.edge_top_bottom);
                view2.setImageResource(R.color.transper);
                view3.setImageResource(R.color.transper);
                taste.setTextColor(Color.BLACK);
                feeling.setTextColor(Color.WHITE);
                service.setTextColor(Color.WHITE);

                // 더미 데이터
                reviewTagItems.clear();
                reviewTagItems.add(new ReviewTagItem("#쓴맛"));
                reviewTagItems.add(new ReviewTagItem("#신맛"));
                reviewTagItems.add(new ReviewTagItem("#짠맛"));
                reviewTagItems.add(new ReviewTagItem("#단맛"));
                reviewTagItems.add(new ReviewTagItem("#향미"));
                reviewTagItems.add(new ReviewTagItem("#바디감"));
                reviewTagItems.add(new ReviewTagItem("#콜드브루"));
                reviewTagItems.add(new ReviewTagItem("#메뉴多"));
                reviewTagItems.add(new ReviewTagItem("#가성비"));
                reviewTagItems.add(new ReviewTagItem("#양많음"));
                reviewTagItems.add(new ReviewTagItem("#디저트맛집"));
                reviewTagItems.add(new ReviewTagItem("#논커피맛집"));
                reviewTagAdapter.notifyDataSetChanged();
            }
        });

        // 분위기 선택했을 때 -> 글씨 색, 배경 색, 리사이클러뷰 내 아이템 초기화 후 갱신
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view1.setImageResource(R.color.transper);
                view2.setImageResource(R.drawable.edge_top_bottom);
                view3.setImageResource(R.color.transper);
                taste.setTextColor(Color.WHITE);
                feeling.setTextColor(Color.BLACK);
                service.setTextColor(Color.WHITE);

                // 더미 데이터
                reviewTagItems.clear();
                reviewTagItems.add(new ReviewTagItem("#인스타"));
                reviewTagItems.add(new ReviewTagItem("#앤티크"));
                reviewTagItems.add(new ReviewTagItem("#모던"));
                reviewTagItems.add(new ReviewTagItem("#캐주얼"));
                reviewTagItems.add(new ReviewTagItem("#이국적"));
                reviewTagItems.add(new ReviewTagItem("#일상"));
                reviewTagItems.add(new ReviewTagItem("#따뜻한"));
                reviewTagItems.add(new ReviewTagItem("#조용한"));
                reviewTagItems.add(new ReviewTagItem("#우드톤"));
                reviewTagItems.add(new ReviewTagItem("#채광"));
                reviewTagItems.add(new ReviewTagItem("#힙한"));
                reviewTagItems.add(new ReviewTagItem("#귀여운"));
                reviewTagAdapter.notifyDataSetChanged();
            }
        });

        // 서비스 메뉴 선택했을 때 -> 글씨 색, 배경 색, 리사이클러뷰 내 아이템 초기화 후 갱신
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view1.setImageResource(R.color.transper);
                view2.setImageResource(R.color.transper);
                view3.setImageResource(R.drawable.edge_top_bottom);
                taste.setTextColor(Color.WHITE);
                feeling.setTextColor(Color.WHITE);
                service.setTextColor(Color.BLACK);

                // 더미 데이터
                reviewTagItems.clear();
                reviewTagItems.add(new ReviewTagItem("#친절한"));
                reviewTagItems.add(new ReviewTagItem("#청결한"));
                reviewTagItems.add(new ReviewTagItem("#애견"));
                reviewTagItems.add(new ReviewTagItem("#주차장"));
                reviewTagItems.add(new ReviewTagItem("#노키즈존"));
                reviewTagItems.add(new ReviewTagItem("#교통편의"));
                reviewTagItems.add(new ReviewTagItem("#신속한"));
                reviewTagItems.add(new ReviewTagItem("#쾌적한"));
                reviewTagItems.add(new ReviewTagItem("#회의실"));
                reviewTagItems.add(new ReviewTagItem("#규모大"));
                reviewTagItems.add(new ReviewTagItem("#규모小"));
                reviewTagItems.add(new ReviewTagItem("#편한좌석"));
                reviewTagAdapter.notifyDataSetChanged();
            }
        });


        // 태그 모두 선택 후, 추가하기 버튼 클릭 시,
        addTag_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 태그를 하나도 선택하지 않았을 경우
                if(addTag1.getText().toString().equals("") && addTag2.getText().toString().equals("") && addTag3.getText().toString().equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "최소 1개의 태그를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getContext().getApplicationContext(), "선택한 태그 추가하기.", Toast.LENGTH_SHORT).show();
                    // 번들을 이용해 프래그먼트간 데이터 전달

                    if(floating_flag){
                        Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들

                        bundle.putBoolean("return_floating_flag", floating_flag);
                        bundle.putString("review_cafeName", review_cafe_name);

                        bundle.putString("key1",addTag1.getText().toString()); // 번들에 String 데이터를 전달. key1 을 키로 사용
                        bundle.putString("key2",addTag2.getText().toString());
                        bundle.putString("key3",addTag3.getText().toString());

                        bundle.putFloat("tag_review_tastePoint1", s1);
                        bundle.putFloat("tag_review_tastePoint2", s2);
                        bundle.putFloat("tag_review_tastePoint3", s3);
                        bundle.putFloat("tag_review_tastePoint4", s4);
                        bundle.putFloat("tag_review_seatPoint1", s5);
                        bundle.putFloat("tag_review_seatPoint2", s6);
                        bundle.putFloat("tag_review_seatPoint3", s7);
                        bundle.putFloat("tag_review_seatPoint4", s8);
                        bundle.putFloat("tag_review_studyPoint1", s9);
                        bundle.putFloat("tag_review_studyPoint2", s10);
                        bundle.putFloat("tag_review_studyPoint3", s11);
                        bundle.putFloat("tag_review_studyPoint4", s12);

                        bundle.putBoolean("return_location_flag", location_flag);

                        navController.navigate(R.id.bottom_review_tag_to_bottom_review, bundle); // 번들과 함께 전달
                    }

                    else if(reviewCafeList_flag){
                        Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들

                        bundle.putBoolean("return_reviewCafeList_flag", reviewCafeList_flag);
                        bundle.putString("review_cafeName", review_cafe_name);

                        bundle.putString("key1",addTag1.getText().toString()); // 번들에 String 데이터를 전달. key1 을 키로 사용
                        bundle.putString("key2",addTag2.getText().toString());
                        bundle.putString("key3",addTag3.getText().toString());

                        bundle.putFloat("tag_review_tastePoint1", s1);
                        bundle.putFloat("tag_review_tastePoint2", s2);
                        bundle.putFloat("tag_review_tastePoint3", s3);
                        bundle.putFloat("tag_review_tastePoint4", s4);
                        bundle.putFloat("tag_review_seatPoint1", s5);
                        bundle.putFloat("tag_review_seatPoint2", s6);
                        bundle.putFloat("tag_review_seatPoint3", s7);
                        bundle.putFloat("tag_review_seatPoint4", s8);
                        bundle.putFloat("tag_review_studyPoint1", s9);
                        bundle.putFloat("tag_review_studyPoint2", s10);
                        bundle.putFloat("tag_review_studyPoint3", s11);
                        bundle.putFloat("tag_review_studyPoint4", s12);

                        bundle.putBoolean("return_location_flag", location_flag);
                        Log.d("review -> location_flag", location_flag.toString());
                        navController.navigate(R.id.bottom_review_tag_to_bottom_review, bundle); // 번들과 함께 전달
                    }

                    else if(cafeDetail_reviewModify_flag){
                        Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들

                        bundle.putBoolean("return_cafeDetail_reviewModify_flag", cafeDetail_reviewModify_flag);
                        bundle.putString("review_cafeName", review_cafe_name);

                        bundle.putString("key1",addTag1.getText().toString()); // 번들에 String 데이터를 전달. key1 을 키로 사용
                        bundle.putString("key2",addTag2.getText().toString());
                        bundle.putString("key3",addTag3.getText().toString());

                        bundle.putFloat("tag_review_tastePoint1", s1);
                        bundle.putFloat("tag_review_tastePoint2", s2);
                        bundle.putFloat("tag_review_tastePoint3", s3);
                        bundle.putFloat("tag_review_tastePoint4", s4);
                        bundle.putFloat("tag_review_seatPoint1", s5);
                        bundle.putFloat("tag_review_seatPoint2", s6);
                        bundle.putFloat("tag_review_seatPoint3", s7);
                        bundle.putFloat("tag_review_seatPoint4", s8);
                        bundle.putFloat("tag_review_studyPoint1", s9);
                        bundle.putFloat("tag_review_studyPoint2", s10);
                        bundle.putFloat("tag_review_studyPoint3", s11);
                        bundle.putFloat("tag_review_studyPoint4", s12);
                        bundle.putInt("score1", score1);
                        bundle.putInt("score2", score2);
                        bundle.putInt("score3", score3);
                        bundle.putInt("score4", score4);
                        bundle.putInt("score5", score5);
                        bundle.putInt("score6", score6);
                        bundle.putInt("score7", score7);
                        bundle.putInt("score8", score8);
                        bundle.putInt("score9", score9);
                        bundle.putInt("score10", score10);
                        bundle.putInt("score11", score11);
                        bundle.putInt("score12", score12);

                        bundle.putLong("k2-1", (long) k2[0]);
                        bundle.putLong("k2-2", (long) k2[1]);
                        bundle.putLong("k2-3", (long) k2[2]);
                        bundle.putLong("k2-4", (long) k2[3]);
                        bundle.putLong("k2-5", (long) k2[4]);
                        bundle.putLong("k2-6", (long) k2[5]);
                        bundle.putLong("k2-7", (long) k2[6]);
                        bundle.putLong("k2-8", (long) k2[7]);
                        bundle.putLong("k2-9", (long) k2[8]);
                        bundle.putLong("k2-10", (long) k2[9]);
                        bundle.putLong("k2-11", (long) k2[10]);
                        bundle.putLong("k2-12", (long) k2[11]);
                        bundle.putLong("k2-13", (long) k2[12]);
                        bundle.putLong("k2-14", (long) k2[13]);
                        bundle.putLong("k2-15", (long) k2[14]);
                        bundle.putLong("k2-16", (long) k2[15]);
                        bundle.putLong("k2-17", (long) k2[16]);
                        bundle.putLong("k2-18", (long) k2[17]);
                        bundle.putLong("k2-19", (long) k2[18]);
                        bundle.putLong("k2-20", (long) k2[19]);
                        bundle.putLong("k2-21", (long) k2[20]);
                        bundle.putLong("k2-22", (long) k2[21]);
                        bundle.putLong("k2-23", (long) k2[22]);
                        bundle.putLong("k2-24", (long) k2[23]);
                        bundle.putLong("k2-25", (long) k2[24]);
                        bundle.putLong("k2-26", (long) k2[25]);
                        bundle.putLong("k2-27", (long) k2[26]);
                        bundle.putLong("k2-28", (long) k2[27]);
                        bundle.putLong("k2-29", (long) k2[28]);
                        bundle.putLong("k2-30", (long) k2[29]);
                        bundle.putLong("k2-31", (long) k2[30]);
                        bundle.putLong("k2-32", (long) k2[31]);
                        bundle.putLong("k2-33", (long) k2[32]);
                        bundle.putLong("k2-34", (long) k2[33]);
                        bundle.putLong("k2-35", (long) k2[34]);
                        bundle.putLong("k2-36", (long) k2[35]);

                        bundle.putBoolean("flag", flag);
                        bundle.putLong("reviewNum", reviewNum);

                        bundle.putString("comment", comment);
                        Log.d("리뷰로 보내는comment", comment);

                        bundle.putBoolean("return_location_flag", location_flag);

                        navController.navigate(R.id.bottom_review_tag_to_bottom_review, bundle); // 번들과 함께 전달
                    }

                    else if(moreReview_reviewModify_flag){
                        Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들

                        bundle.putBoolean("return_moreReview_reviewModify_flag", moreReview_reviewModify_flag);
                        bundle.putString("review_cafeName", review_cafe_name);

                        bundle.putString("key1",addTag1.getText().toString()); // 번들에 String 데이터를 전달. key1 을 키로 사용
                        bundle.putString("key2",addTag2.getText().toString());
                        bundle.putString("key3",addTag3.getText().toString());

                        bundle.putFloat("tag_review_tastePoint1", s1);
                        bundle.putFloat("tag_review_tastePoint2", s2);
                        bundle.putFloat("tag_review_tastePoint3", s3);
                        bundle.putFloat("tag_review_tastePoint4", s4);
                        bundle.putFloat("tag_review_seatPoint1", s5);
                        bundle.putFloat("tag_review_seatPoint2", s6);
                        bundle.putFloat("tag_review_seatPoint3", s7);
                        bundle.putFloat("tag_review_seatPoint4", s8);
                        bundle.putFloat("tag_review_studyPoint1", s9);
                        bundle.putFloat("tag_review_studyPoint2", s10);
                        bundle.putFloat("tag_review_studyPoint3", s11);
                        bundle.putFloat("tag_review_studyPoint4", s12);
                        bundle.putInt("score1", score1);
                        bundle.putInt("score2", score2);
                        bundle.putInt("score3", score3);
                        bundle.putInt("score4", score4);
                        bundle.putInt("score5", score5);
                        bundle.putInt("score6", score6);
                        bundle.putInt("score7", score7);
                        bundle.putInt("score8", score8);
                        bundle.putInt("score9", score9);
                        bundle.putInt("score10", score10);
                        bundle.putInt("score11", score11);
                        bundle.putInt("score12", score12);

                        bundle.putLong("k2-1", (long) k2[0]);
                        bundle.putLong("k2-2", (long) k2[1]);
                        bundle.putLong("k2-3", (long) k2[2]);
                        bundle.putLong("k2-4", (long) k2[3]);
                        bundle.putLong("k2-5", (long) k2[4]);
                        bundle.putLong("k2-6", (long) k2[5]);
                        bundle.putLong("k2-7", (long) k2[6]);
                        bundle.putLong("k2-8", (long) k2[7]);
                        bundle.putLong("k2-9", (long) k2[8]);
                        bundle.putLong("k2-10", (long) k2[9]);
                        bundle.putLong("k2-11", (long) k2[10]);
                        bundle.putLong("k2-12", (long) k2[11]);
                        bundle.putLong("k2-13", (long) k2[12]);
                        bundle.putLong("k2-14", (long) k2[13]);
                        bundle.putLong("k2-15", (long) k2[14]);
                        bundle.putLong("k2-16", (long) k2[15]);
                        bundle.putLong("k2-17", (long) k2[16]);
                        bundle.putLong("k2-18", (long) k2[17]);
                        bundle.putLong("k2-19", (long) k2[18]);
                        bundle.putLong("k2-20", (long) k2[19]);
                        bundle.putLong("k2-21", (long) k2[20]);
                        bundle.putLong("k2-22", (long) k2[21]);
                        bundle.putLong("k2-23", (long) k2[22]);
                        bundle.putLong("k2-24", (long) k2[23]);
                        bundle.putLong("k2-25", (long) k2[24]);
                        bundle.putLong("k2-26", (long) k2[25]);
                        bundle.putLong("k2-27", (long) k2[26]);
                        bundle.putLong("k2-28", (long) k2[27]);
                        bundle.putLong("k2-29", (long) k2[28]);
                        bundle.putLong("k2-30", (long) k2[29]);
                        bundle.putLong("k2-31", (long) k2[30]);
                        bundle.putLong("k2-32", (long) k2[31]);
                        bundle.putLong("k2-33", (long) k2[32]);
                        bundle.putLong("k2-34", (long) k2[33]);
                        bundle.putLong("k2-35", (long) k2[34]);
                        bundle.putLong("k2-36", (long) k2[35]);

                        bundle.putBoolean("flag", flag);
                        bundle.putLong("reviewNum", reviewNum);

                        bundle.putString("comment", comment);
                        Log.d("리뷰로 보내는comment", comment);

                        bundle.putBoolean("return_location_flag", location_flag);

                        navController.navigate(R.id.bottom_review_tag_to_bottom_review, bundle); // 번들과 함께 전달
                    }

                    else if(mypage_reviewModify_flag){
                        Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들

                        bundle.putBoolean("return_mypage_reviewModify_flag", mypage_reviewModify_flag);
                        bundle.putString("review_cafeName", review_cafe_name);

                        bundle.putString("key1",addTag1.getText().toString()); // 번들에 String 데이터를 전달. key1 을 키로 사용
                        bundle.putString("key2",addTag2.getText().toString());
                        bundle.putString("key3",addTag3.getText().toString());

                        bundle.putFloat("tag_review_tastePoint1", s1);
                        bundle.putFloat("tag_review_tastePoint2", s2);
                        bundle.putFloat("tag_review_tastePoint3", s3);
                        bundle.putFloat("tag_review_tastePoint4", s4);
                        bundle.putFloat("tag_review_seatPoint1", s5);
                        bundle.putFloat("tag_review_seatPoint2", s6);
                        bundle.putFloat("tag_review_seatPoint3", s7);
                        bundle.putFloat("tag_review_seatPoint4", s8);
                        bundle.putFloat("tag_review_studyPoint1", s9);
                        bundle.putFloat("tag_review_studyPoint2", s10);
                        bundle.putFloat("tag_review_studyPoint3", s11);
                        bundle.putFloat("tag_review_studyPoint4", s12);
                        bundle.putInt("score1", score1);
                        bundle.putInt("score2", score2);
                        bundle.putInt("score3", score3);
                        bundle.putInt("score4", score4);
                        bundle.putInt("score5", score5);
                        bundle.putInt("score6", score6);
                        bundle.putInt("score7", score7);
                        bundle.putInt("score8", score8);
                        bundle.putInt("score9", score9);
                        bundle.putInt("score10", score10);
                        bundle.putInt("score11", score11);
                        bundle.putInt("score12", score12);

                        bundle.putLong("k2-1", (long) k2[0]);
                        bundle.putLong("k2-2", (long) k2[1]);
                        bundle.putLong("k2-3", (long) k2[2]);
                        bundle.putLong("k2-4", (long) k2[3]);
                        bundle.putLong("k2-5", (long) k2[4]);
                        bundle.putLong("k2-6", (long) k2[5]);
                        bundle.putLong("k2-7", (long) k2[6]);
                        bundle.putLong("k2-8", (long) k2[7]);
                        bundle.putLong("k2-9", (long) k2[8]);
                        bundle.putLong("k2-10", (long) k2[9]);
                        bundle.putLong("k2-11", (long) k2[10]);
                        bundle.putLong("k2-12", (long) k2[11]);
                        bundle.putLong("k2-13", (long) k2[12]);
                        bundle.putLong("k2-14", (long) k2[13]);
                        bundle.putLong("k2-15", (long) k2[14]);
                        bundle.putLong("k2-16", (long) k2[15]);
                        bundle.putLong("k2-17", (long) k2[16]);
                        bundle.putLong("k2-18", (long) k2[17]);
                        bundle.putLong("k2-19", (long) k2[18]);
                        bundle.putLong("k2-20", (long) k2[19]);
                        bundle.putLong("k2-21", (long) k2[20]);
                        bundle.putLong("k2-22", (long) k2[21]);
                        bundle.putLong("k2-23", (long) k2[22]);
                        bundle.putLong("k2-24", (long) k2[23]);
                        bundle.putLong("k2-25", (long) k2[24]);
                        bundle.putLong("k2-26", (long) k2[25]);
                        bundle.putLong("k2-27", (long) k2[26]);
                        bundle.putLong("k2-28", (long) k2[27]);
                        bundle.putLong("k2-29", (long) k2[28]);
                        bundle.putLong("k2-30", (long) k2[29]);
                        bundle.putLong("k2-31", (long) k2[30]);
                        bundle.putLong("k2-32", (long) k2[31]);
                        bundle.putLong("k2-33", (long) k2[32]);
                        bundle.putLong("k2-34", (long) k2[33]);
                        bundle.putLong("k2-35", (long) k2[34]);
                        bundle.putLong("k2-36", (long) k2[35]);

                        bundle.putBoolean("flag", flag);
                        bundle.putLong("reviewNum", reviewNum);

                        bundle.putString("comment", comment);
                        Log.d("리뷰로 보내는comment", comment);

                        bundle.putBoolean("return_location_flag", location_flag);

                        navController.navigate(R.id.bottom_review_tag_to_bottom_review, bundle); // 번들과 함께 전달
                    }
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
