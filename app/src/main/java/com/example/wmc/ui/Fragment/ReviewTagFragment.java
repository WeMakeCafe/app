package com.example.wmc.ui.Fragment;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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

        // 더미 데이터
        reviewTagItems.add(new ReviewTagItem("#가"));
        reviewTagItems.add(new ReviewTagItem("#나"));
        reviewTagItems.add(new ReviewTagItem("#다"));
        reviewTagItems.add(new ReviewTagItem("#라"));
        reviewTagItems.add(new ReviewTagItem("#마"));
        reviewTagItems.add(new ReviewTagItem("#바"));
        reviewTagItems.add(new ReviewTagItem("#사"));
        reviewTagItems.add(new ReviewTagItem("#아"));
        reviewTagItems.add(new ReviewTagItem("#자"));
        reviewTagItems.add(new ReviewTagItem("#차"));
        reviewTagItems.add(new ReviewTagItem("#카"));
        reviewTagItems.add(new ReviewTagItem("#타"));
        reviewTagItems.add(new ReviewTagItem("#파"));
        reviewTagItems.add(new ReviewTagItem("#하"));
        reviewTagItems.add(new ReviewTagItem("#까"));
        reviewTagItems.add(new ReviewTagItem("#따"));

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

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view1.setImageResource(R.drawable.edge_top_bottom);
                view2.setImageResource(R.drawable.edge_top_back_brown);
                view3.setImageResource(R.drawable.edge_top_back_brown);
                taste.setTextColor(Color.BLACK);
                feeling.setTextColor(Color.WHITE);
                service.setTextColor(Color.WHITE);

                // 더미 데이터
                reviewTagItems.add(new ReviewTagItem("#가"));
                reviewTagItems.add(new ReviewTagItem("#나"));
                reviewTagItems.add(new ReviewTagItem("#다"));
                reviewTagItems.add(new ReviewTagItem("#라"));
                reviewTagItems.add(new ReviewTagItem("#마"));
                reviewTagItems.add(new ReviewTagItem("#바"));
                reviewTagItems.add(new ReviewTagItem("#사"));
                reviewTagItems.add(new ReviewTagItem("#아"));
                reviewTagItems.add(new ReviewTagItem("#자"));
                reviewTagItems.add(new ReviewTagItem("#차"));
                reviewTagItems.add(new ReviewTagItem("#카"));
                reviewTagItems.add(new ReviewTagItem("#타"));
                reviewTagItems.add(new ReviewTagItem("#파"));
                reviewTagItems.add(new ReviewTagItem("#하"));
                reviewTagItems.add(new ReviewTagItem("#까"));
                reviewTagItems.add(new ReviewTagItem("#따"));
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view1.setImageResource(R.drawable.edge_top_back_brown);
                view2.setImageResource(R.drawable.edge_top_bottom);
                view3.setImageResource(R.drawable.edge_top_back_brown);
                taste.setTextColor(Color.WHITE);
                feeling.setTextColor(Color.BLACK);
                service.setTextColor(Color.WHITE);

                // 더미 데이터
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
                reviewTagItems.add(new ReviewTagItem("#분위기"));
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view1.setImageResource(R.drawable.edge_top_back_brown);
                view2.setImageResource(R.drawable.edge_top_back_brown);
                view3.setImageResource(R.drawable.edge_top_bottom);
                taste.setTextColor(Color.WHITE);
                feeling.setTextColor(Color.WHITE);
                service.setTextColor(Color.BLACK);

                // 더미 데이터
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
                reviewTagItems.add(new ReviewTagItem("#서비스"));
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
