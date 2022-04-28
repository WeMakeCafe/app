package com.example.wmc.MypageMyreview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class MypageMyreviewViewHolder extends RecyclerView.ViewHolder{

    TextView reviewNickName;
    TextView level_and_location;
    TextView review_comment;
    TextView good_count_textView;
    ImageView reviewProfile_image;
    ImageView reviewImage;

    public static int REVIEW_VIEW_TYPE = R.layout.item_review;

    public MypageMyreviewViewHolder(@NonNull View itemView, final MypageMyreviewAdapter.OnItemClickEventListener_cafeDetail a_itemClickListener) {
        super(itemView);

        reviewNickName = itemView.findViewById(R.id.reviewNickName);
        level_and_location = itemView.findViewById(R.id.level_and_location);
        review_comment = itemView.findViewById(R.id.review_comment);
        good_count_textView = itemView.findViewById(R.id.good_count_textView);
        reviewProfile_image = itemView.findViewById(R.id.reviewProfile_image);
        reviewImage = itemView.findViewById(R.id.reviewImage);

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
