package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.CafeRegistration.CafeRegistrationItem;
import com.example.wmc.R;
import com.example.wmc.ReviewComment.ReviewCommentAdapter;
import com.example.wmc.ReviewComment.ReviewCommentItem;
import com.example.wmc.databinding.FragmentReviewCommentBinding;

import java.util.ArrayList;

public class ReviewCommentFragment extends Fragment {
    private FragmentReviewCommentBinding binding;
    private static NavController navController;
    Button reviewComment_add_image_button;
    Button reviewComment_finish_button;
    EditText reviewComment_editText;
    TextView commentCount_textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCommentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        reviewComment_add_image_button = root.findViewById(R.id.reviewComment_add_image_button);
        reviewComment_finish_button = root.findViewById(R.id.reviewComment_finish_button);
        reviewComment_editText = root.findViewById(R.id.reviewComment_editText);
        commentCount_textView = root.findViewById(R.id.commentCount_textView);

        //코멘트의 글자수 출력
        reviewComment_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String comment = reviewComment_editText.getText().toString();
                commentCount_textView.setText(comment.length() + "/200 Bytes");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 이미지 추가 버튼 클릭 시
        reviewComment_add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리 여는 이벤트 추가
            }
        });

        // 작성완료 버튼 클릭 시
        reviewComment_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 작성완료 후, 내가 리뷰를 작성한 카페디테일로 이동
            }
        });

        // 이미지 추가 리싸이클러뷰
        ArrayList<ReviewCommentItem> reviewCommentImageItems = new ArrayList<>();

        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.logo));
        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.logo_v2));
        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade1));
        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade2));
        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade3));

        // Adapter 추가
        RecyclerView reviewCommentRecyclerView = root.findViewById(R.id.reviewCommentImageRecyclerView);

        ReviewCommentAdapter reviewCommentAdapter = new ReviewCommentAdapter(reviewCommentImageItems);
        reviewCommentRecyclerView.setAdapter(reviewCommentAdapter);

        // Layout manager 추가
        LinearLayoutManager reviewCommentLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        reviewCommentRecyclerView.setLayoutManager(reviewCommentLayoutManager);

        reviewCommentAdapter.setOnItemClickListener_ReviewComment(new ReviewCommentAdapter.OnItemClickEventListener_ReviewComment() {
            @Override
            public void onItemClick(View view, int position) {
                final ReviewCommentItem item = reviewCommentImageItems.get(position);
                Toast.makeText(getContext().getApplicationContext(), item.getReviewCommentImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
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
