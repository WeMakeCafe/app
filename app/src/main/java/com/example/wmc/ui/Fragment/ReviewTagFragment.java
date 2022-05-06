package com.example.wmc.ui.Fragment;

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


import com.example.wmc.R;
import com.example.wmc.databinding.ActivityMainBinding;
import com.example.wmc.databinding.FragmentReviewTagBinding;

public class ReviewTagFragment extends Fragment {

    private FragmentReviewTagBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ConstraintLayout cons1 = root.findViewById(R.id.constrant_taste);
        ConstraintLayout cons2 = root.findViewById(R.id.constrant_feel);
        ConstraintLayout cons3 = root.findViewById(R.id.constrant_service);

        ImageView view1 = root.findViewById(R.id.tasteimage8);
        ImageView view2 = root.findViewById(R.id.feelingImage8);
        ImageView view3 = root.findViewById(R.id.serviceimage8);

        ImageView view4 = root.findViewById(R.id.tasteimage7);
        ImageView view5 = root.findViewById(R.id.feelingImage7);
        ImageView view6 = root.findViewById(R.id.serviceimage7);

        ImageView view7 = root.findViewById(R.id.tasteimage6);
        ImageView view8 = root.findViewById(R.id.feelingimage6);
        ImageView view9 = root.findViewById(R.id.serviceImage6);

        TextView addTag1 = root.findViewById(R.id.addTag1); // 선택한 태그1
        TextView addTag2 = root.findViewById(R.id.addTag2); // 선택한 태그2
        TextView addTag3 = root.findViewById(R.id.addTag3); // 선택한 태그3
        Button tag1_delete_button = root.findViewById(R.id.tag1_delete_button); // 선택한 태그1의 X버튼
        Button tag2_delete_button = root.findViewById(R.id.tag2_delete_button); // 선택한 태그2의 X버튼
        Button tag3_delete_button = root.findViewById(R.id.tag3_delete_button); // 선택한 태그3의 X버튼
        Button tag1 = root.findViewById(R.id.tag1);
        Button tag2 = root.findViewById(R.id.tag2);
        Button tag3 = root.findViewById(R.id.tag3);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.VISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.VISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.VISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.VISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.VISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.VISIBLE);
                cons3.setVisibility(View.INVISIBLE);
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.VISIBLE);
            }
        });
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.VISIBLE);
            }
        });
        view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons1.setVisibility(View.INVISIBLE);
                cons2.setVisibility(View.INVISIBLE);
                cons3.setVisibility(View.VISIBLE);
            }
        });


        tag1_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag1.setText("");
            }
        });

        tag2_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag2.setText("");
            }
        });

        tag3_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag3.setText("");
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTag = tag1.getText().toString();

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

        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTag = tag2.getText().toString();

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

        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTag = tag3.getText().toString();

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
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
