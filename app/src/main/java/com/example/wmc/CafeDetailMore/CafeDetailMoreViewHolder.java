package com.example.wmc.CafeDetailMore;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.R;

public class CafeDetailMoreViewHolder extends RecyclerView.ViewHolder{

    TextView nickName;
    TextView level_and_location;
    TextView review_comment;
    ImageView reviewProfile_image;
    ImageView reviewMore_image1;
    ImageView reviewMore_image2;
    ImageView reviewMore_image3;
    TextView good_count_textView;

    TextView review_modify;
    TextView review_modifyLine;
    TextView review_delete;
    TextView review_deleteLine;
    CheckBox good_button;
    boolean check_user_flag;

    public static int REVIEWMORE_VIEW_TYPE = R.layout.item_more_review;

    public CafeDetailMoreViewHolder(@NonNull View itemView, final CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore a_itemClickListener) {
        super(itemView);

        nickName = itemView.findViewById(R.id.nickName);
        level_and_location = itemView.findViewById(R.id.level_and_location);
        review_comment = itemView.findViewById(R.id.review_comment);
        reviewProfile_image = itemView.findViewById(R.id.reviewProfile_image);
        reviewMore_image1 = itemView.findViewById(R.id.reviewMore_image1);
        reviewMore_image2 = itemView.findViewById(R.id.reviewMore_image2);
        reviewMore_image3 = itemView.findViewById(R.id.reviewMore_image3);
        good_count_textView = itemView.findViewById(R.id.good_count_textView);
        review_modify = itemView.findViewById(R.id.review_modify);  // 리뷰 더보기의 수정 버튼
        review_modifyLine = itemView.findViewById(R.id.review_modifyLine); // 리뷰 더보기의 수정 버튼의 밑줄
        review_delete = itemView.findViewById(R.id.review_delete);  // 리뷰 더보기의 삭제 버튼
        review_deleteLine = itemView.findViewById(R.id.review_deleteLine);  // 리뷰 더보기의 삭제 버튼의 밑줄
        good_button = itemView.findViewById(R.id.good_button);      // 리뷰 더보기의 좋아요버튼

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
