package com.example.wmc.CafeDetail;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class CafeDetailViewHolder extends RecyclerView.ViewHolder{

    TextView reviewNickName;
    TextView level_and_location;
    TextView review_comment;
    TextView review_writeTime;
    TextView good_count_textView;
    ImageView reviewProfile_image;
    ImageView reviewImage;

    TextView reviewModify;
    TextView reviewModifyLine;
    TextView reviewDelete;
    TextView reviewDeleteLine;
    CheckBox good_button;
    ImageView good_button_image;

    TextView location_text;
    boolean check_user_flag;
    boolean check_love_flag;

    public static int REVIEW_VIEW_TYPE = R.layout.item_review;

    public CafeDetailViewHolder(@NonNull View itemView, final CafeDetailAdapter.OnItemClickEventListener_cafeDetail a_itemClickListener) {
        super(itemView);

        reviewNickName = itemView.findViewById(R.id.reviewNickName);
        level_and_location = itemView.findViewById(R.id.level_and_location);
        review_comment = itemView.findViewById(R.id.review_comment);
        review_writeTime = itemView.findViewById(R.id.review_writeTime);
        good_count_textView = itemView.findViewById(R.id.good_count_textView);
        reviewProfile_image = itemView.findViewById(R.id.reviewProfile_image);
        reviewImage = itemView.findViewById(R.id.reviewImage);
        reviewModify = itemView.findViewById(R.id.reviewModify);    // 리뷰에서 수정 버튼
        reviewModifyLine = itemView.findViewById(R.id.reviewModifyLine); // 리뷰에서 수정 버튼의 밑줄
        reviewDelete = itemView.findViewById(R.id.reviewDelete);    // 리뷰에서 삭제 버튼
        reviewDeleteLine = itemView.findViewById(R.id.reviewDeleteLine);    // 리뷰에서 삭제 버튼의 밑줄
        good_button = itemView.findViewById(R.id.good_button);      // 리뷰에서 좋아요 버튼
        good_button_image = itemView.findViewById(R.id.good_button_image);
        location_text = itemView.findViewById(R.id.location_text);

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
