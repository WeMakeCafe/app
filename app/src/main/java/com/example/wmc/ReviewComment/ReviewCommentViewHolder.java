package com.example.wmc.ReviewComment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.R;

public class ReviewCommentViewHolder extends RecyclerView.ViewHolder{

    ImageView reviewComment_imageView;
    Button imageDeleteButton;

    ReviewCommentViewHolder(View itemView) {
        super(itemView) ;

        // 뷰 객체에 대한 참조.
        reviewComment_imageView = itemView.findViewById(R.id.add_modify_imageView);
        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
    }

//    public static int REVIEWCOMMENT_VIEW_TYPE = R.layout.item_add_image;
//
//    public ReviewCommentViewHolder(@NonNull View itemView, final ReviewCommentAdapter.OnItemClickEventListener_ReviewComment itemClickListener) {
//        super(itemView);
//
//        reviewComment_imageView = itemView.findViewById(R.id.add_modify_imageView);
//        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View a_view) {
//                final int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    itemClickListener.onItemClick(a_view, position);
//                }
//            }
//        });
//    }
}
