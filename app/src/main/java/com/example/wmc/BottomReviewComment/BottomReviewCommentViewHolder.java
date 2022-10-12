package com.example.wmc.BottomReviewComment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class BottomReviewCommentViewHolder extends RecyclerView.ViewHolder{

    ImageView reviewComment_imageView;
    Button imageDeleteButton;

    BottomReviewCommentViewHolder(View itemView) {
        super(itemView) ;

        // 뷰 객체에 대한 참조.
        reviewComment_imageView = itemView.findViewById(R.id.add_modify_imageView);
        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
    }

}
