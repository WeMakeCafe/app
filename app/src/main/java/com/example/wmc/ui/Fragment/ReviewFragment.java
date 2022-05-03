package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchText = root.findViewById(R.id.review_search_input);
        addTag_cafe_button = root.findViewById(R.id.addTag_cafe_button);
        comment_button = root.findViewById(R.id.comment_button);
        location_button = root.findViewById(R.id.location_button);
        finish_button = root.findViewById(R.id.finish_button);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_search);
            }
        });

        addTag_cafe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_tag);
            }
        });

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_comment);
            }
        });

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 위치 인증이 되면 아래 Toast를 띄움
                //Toast.makeText(getContext().getApplicationContext(), "위치인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내가 리뷰를 작성한 CafeDetailFragment로 이동
                //navController.navigate(R.id.);
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