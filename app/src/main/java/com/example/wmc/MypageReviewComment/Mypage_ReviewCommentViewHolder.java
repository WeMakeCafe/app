package com.example.wmc.MypageReviewComment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class Mypage_ReviewCommentViewHolder extends RecyclerView.ViewHolder{

    ImageView reviewComment_imageView;
    Button imageDeleteButton;

    Mypage_ReviewCommentViewHolder(View itemView) {
        super(itemView) ;

        // 뷰 객체에 대한 참조.
        reviewComment_imageView = itemView.findViewById(R.id.add_modify_imageView);
        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
    }
}
