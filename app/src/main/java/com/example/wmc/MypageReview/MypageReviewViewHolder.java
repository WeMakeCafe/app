package com.example.wmc.MypageReview;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class MypageReviewViewHolder extends RecyclerView.ViewHolder{

    TextView mypageReview_CafeName;
    TextView mypageReview_writeTime;
    TextView myPageReview_comment;
    TextView good_count_textView;
    ImageView myPageReview_image1;
    ImageView myPageReview_image2;
    ImageView myPageReview_image3;

    TextView myPageReview_modify;
    TextView myPageReview_modifyLine;
    TextView myPageReview_delete;
    TextView myPageReview_deleteLine;
    CheckBox good_button;
    ImageView good_button_imageView;
    boolean check_user_flag;

    public static int MYPAGE_REVIEW_VIEW_TYPE = R.layout.item_mypage_review;

    public MypageReviewViewHolder(@NonNull View itemView, final MypageReviewAdapter.OnItemClickEventListener_MyPageReview a_itemClickListener) {
        super(itemView);

        mypageReview_CafeName = itemView.findViewById(R.id.mypageReview_CafeName);
        mypageReview_writeTime = itemView.findViewById(R.id.mypageReview_writeTime);
        myPageReview_comment = itemView.findViewById(R.id.myPageReview_comment);
        good_count_textView = itemView.findViewById(R.id.good_count_textView);
        myPageReview_image1 = itemView.findViewById(R.id.myPageReview_image1);
        myPageReview_image2= itemView.findViewById(R.id.myPageReview_image2);
        myPageReview_image3 = itemView.findViewById(R.id.myPageReview_image3);

        myPageReview_modify = itemView.findViewById(R.id.myPageReview_modify);  // 마이페이지 리뷰의 수정 버튼
        myPageReview_modifyLine = itemView.findViewById(R.id.myPageReview_modifyLine);  // 마이페이지 리뷰의 수정 버튼의 밑줄
        myPageReview_delete = itemView.findViewById(R.id.myPageReview_delete);  // 마이페이지 리뷰의 삭제 버튼
        myPageReview_deleteLine = itemView.findViewById(R.id.myPageReview_deleteLine);  // 마이페이지 리뷰의 삭제 버튼의 밑줄
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
