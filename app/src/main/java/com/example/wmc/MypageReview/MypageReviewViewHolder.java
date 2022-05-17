package com.example.wmc.MypageReview;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class MypageReviewViewHolder extends RecyclerView.ViewHolder{

    TextView reviewNickName;
    TextView level_and_location;
    TextView review_comment;
    TextView good_count_textView;
    ImageView reviewProfile_image;
    ImageView reviewImage1;
    ImageView reviewImage2;
    ImageView reviewImage3;

    TextView review_modify;
    TextView review_modifyLine;
    TextView review_delete;
    TextView review_deleteLine;
    CheckBox good_button;
    ImageView good_button_imageView;
    boolean check_user_flag;

    public static int REVIEW_VIEW_TYPE = R.layout.item_more_review;

    public MypageReviewViewHolder(@NonNull View itemView, final MypageReviewAdapter.OnItemClickEventListener_MyPageReview a_itemClickListener) {
        super(itemView);

        reviewNickName = itemView.findViewById(R.id.nickName);
        level_and_location = itemView.findViewById(R.id.level_and_location);
        review_comment = itemView.findViewById(R.id.review_comment);
        good_count_textView = itemView.findViewById(R.id.good_count_textView);
        reviewProfile_image = itemView.findViewById(R.id.reviewProfile_image);
        reviewImage1 = itemView.findViewById(R.id.reviewMore_image1);
        reviewImage2= itemView.findViewById(R.id.reviewMore_image2);
        reviewImage3 = itemView.findViewById(R.id.reviewMore_image3);

        review_modify = itemView.findViewById(R.id.review_modify);  // 마이페이지 리뷰의 수정 버튼
        review_modifyLine = itemView.findViewById(R.id.review_modifyLine);  // 마이페이지 리뷰의 수정 버튼의 밑줄
        review_delete = itemView.findViewById(R.id.review_delete);  // 마이페이지 리뷰의 삭제 버튼
        review_deleteLine = itemView.findViewById(R.id.review_deleteLine);  // 마이페이지 리뷰의 삭제 버튼의 밑줄
        good_button = itemView.findViewById(R.id.good_button);      // 마이페이지 리뷰의 좋아요버튼
        good_button_imageView = itemView.findViewById(R.id.good_button_imageView);  // 마이페이지 리뷰의 좋아요 버튼 이미지

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    a_itemClickListener.onItemClick(a_view, position);
                }
            }
        });
    }

}
