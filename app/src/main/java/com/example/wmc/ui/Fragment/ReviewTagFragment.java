package com.example.wmc.ui.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wmc.MypageFavorite.MypageFavoriteItem;
import com.example.wmc.R;
import com.example.wmc.ReviewTag.ReviewTagAdapter;
import com.example.wmc.ReviewTag.ReviewTagItem;
import com.example.wmc.databinding.ActivityMainBinding;
import com.example.wmc.databinding.FragmentReviewTagBinding;

import java.util.ArrayList;

public class ReviewTagFragment extends Fragment {

    private FragmentReviewTagBinding binding;
    private static NavController navController;

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

                    Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들
                    bundle.putString("key1",addTag1.getText().toString()); // 번들에 String 데이터를 전달. key1 을 키로 사용
                    bundle.putString("key2",addTag2.getText().toString());
                    bundle.putString("key3",addTag3.getText().toString());
                    navController.navigate(R.id.review_tag_to_review, bundle); // 번들과 함께 전달
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
