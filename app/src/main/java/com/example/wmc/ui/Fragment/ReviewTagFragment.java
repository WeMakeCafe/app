package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import com.example.wmc.R;
import com.example.wmc.databinding.ActivityMainBinding;
import com.example.wmc.databinding.FragmentReviewTagBinding;

public class ReviewTagFragment extends Fragment {
    private FragmentReviewTagBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
//    private ActivityMainBinding binding;
//    ConstraintLayout cons1;
//    ConstraintLayout cons2;
//    ConstraintLayout cons3;
//    Button but1 = findViewById(R.id.addTag1);
//    Button but2 = findViewById(R.id.tag4);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        but2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                but1.setText(but2.getText().toString());
//            }
//        });
//
//        cons1 = (ConstraintLayout) findViewById(R.id.constrant_taste);
//        cons2 = (ConstraintLayout) findViewById(R.id.constrant_feel);
//        cons3 = (ConstraintLayout) findViewById(R.id.constrant_service);
//        ImageView view1 = findViewById(R.id.tasteimage8);
//        ImageView view2 = findViewById(R.id.feelingImage8);
//        ImageView view3 = findViewById(R.id.serviceimage8);
//
//        ImageView view4 = findViewById(R.id.tasteimage7);
//        ImageView view5 = findViewById(R.id.feelingImage7);
//        ImageView view6 = findViewById(R.id.serviceimage7);
//
//        ImageView view7 = findViewById(R.id.tasteimage6);
//        ImageView view8 = findViewById(R.id.feelingimage6);
//        ImageView view9 = findViewById(R.id.serviceImage6);
//
//        view1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.VISIBLE);
//                cons2.setVisibility(View.INVISIBLE);
//                cons3.setVisibility(View.INVISIBLE);
//            }
//        });
//        view4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.VISIBLE);
//                cons2.setVisibility(View.INVISIBLE);
//                cons3.setVisibility(View.INVISIBLE);
//            }
//        });
//        view7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.VISIBLE);
//                cons2.setVisibility(View.INVISIBLE);
//                cons3.setVisibility(View.INVISIBLE);
//            }
//        });
//        view2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.INVISIBLE);
//                cons2.setVisibility(View.VISIBLE);
//                cons3.setVisibility(View.INVISIBLE);
//            }
//        });
//        view5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.INVISIBLE);
//                cons2.setVisibility(View.VISIBLE);
//                cons3.setVisibility(View.INVISIBLE);
//            }
//        });
//        view8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.INVISIBLE);
//                cons2.setVisibility(View.VISIBLE);
//                cons3.setVisibility(View.INVISIBLE);
//            }
//        });
//        view3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.INVISIBLE);
//                cons2.setVisibility(View.INVISIBLE);
//                cons3.setVisibility(View.VISIBLE);
//            }
//        });
//        view6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.INVISIBLE);
//                cons2.setVisibility(View.INVISIBLE);
//                cons3.setVisibility(View.VISIBLE);
//            }
//        });
//        view9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cons1.setVisibility(View.INVISIBLE);
//                cons2.setVisibility(View.INVISIBLE);
//                cons3.setVisibility(View.VISIBLE);
//            }
//        });
////        bindList(); // fragment_list_search 페이지 리사이클러뷰 푸터 실험용 코드
