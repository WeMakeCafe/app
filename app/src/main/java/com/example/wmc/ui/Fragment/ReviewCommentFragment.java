package com.example.wmc.ui.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    RecyclerView reviewCommentImageRecyclerView;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ReviewCommentAdapter reviewCommentAdapter;
    private static final int REQUEST_CODE = 3333;
    private static final String TAG = "ReviewCommentFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCommentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        reviewComment_add_image_button = root.findViewById(R.id.reviewComment_add_image_button);
        reviewComment_finish_button = root.findViewById(R.id.reviewComment_finish_button);
        reviewComment_editText = root.findViewById(R.id.reviewComment_editText);
        commentCount_textView = root.findViewById(R.id.commentCount_textView);
        reviewCommentImageRecyclerView = root.findViewById(R.id.reviewCommentImageRecyclerView);

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
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // 다중 이미지를 가져올 수 있도록 세팅
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        // 작성완료 버튼 클릭 시
        reviewComment_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내가 리뷰를 작성한 카페의 카페디테일로 이동
                navController.navigate(R.id.review_comment_to_cafe_detail);
            }
        });

        
//        // 이미지 추가 리싸이클러뷰
//        ArrayList<ReviewCommentItem> reviewCommentImageItems = new ArrayList<>();
//
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.logo));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.logo_v2));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade1));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade2));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade3));
//
//        // Adapter 추가
//        RecyclerView reviewCommentRecyclerView = root.findViewById(R.id.reviewCommentImageRecyclerView);
//
//        ReviewCommentAdapter reviewCommentAdapter = new ReviewCommentAdapter(reviewCommentImageItems);
//        reviewCommentRecyclerView.setAdapter(reviewCommentAdapter);
//
//        // Layout manager 추가
//        LinearLayoutManager reviewCommentLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        reviewCommentRecyclerView.setLayoutManager(reviewCommentLayoutManager);
//
//        // 이미지 아이템 클릭 시,
//        reviewCommentAdapter.setOnItemClickListener_ReviewComment(new ReviewCommentAdapter.OnItemClickEventListener_ReviewComment() {
//            @Override
//            public void onItemClick(View view, int position) {
//                final ReviewCommentItem item = reviewCommentImageItems.get(position);
//                Toast.makeText(getContext().getApplicationContext(), item.getReviewCommentImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
//            }
//        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getContext().getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }

        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                if(uriList.size() >= 3) {
                    Toast.makeText(getContext().getApplicationContext(), "이미지 3개를 모두 선택하셨습니다.", Toast.LENGTH_LONG).show();
                }

                else{
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);
                }

                reviewCommentAdapter = new ReviewCommentAdapter(uriList, getContext().getApplicationContext());
                reviewCommentImageRecyclerView.setAdapter(reviewCommentAdapter);
                reviewCommentImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 3){   // 선택한 이미지가 4장 이상인 경우
                    Toast.makeText(getContext().getApplicationContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 5장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){

                        if(uriList.size() <= 2){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                            try {
                                uriList.add(imageUri);  //uri를 list에 담는다.

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                    reviewCommentAdapter = new ReviewCommentAdapter(uriList, getContext().getApplicationContext());
                    reviewCommentImageRecyclerView.setAdapter(reviewCommentAdapter);   // 리사이클러뷰에 어댑터 세팅
                    reviewCommentImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
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
