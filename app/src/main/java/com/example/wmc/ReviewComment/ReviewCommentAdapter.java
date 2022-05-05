package com.example.wmc.ReviewComment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeRegistration.CafeRegistrationItem;
import com.example.wmc.CafeRegistration.CafeRegistrationViewHolder;

import java.util.ArrayList;

public class ReviewCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ReviewCommentItem> reviewCommentImage_items;

    public interface OnItemClickEventListener_ReviewComment { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View view, int position);
    }
    private ReviewCommentAdapter.OnItemClickEventListener_ReviewComment mItemClickListener_ReviewComment;    // 인터페이스 객체 생성

    public ReviewCommentAdapter(ArrayList<ReviewCommentItem> list){
        reviewCommentImage_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ReviewCommentViewHolder(view, mItemClickListener_ReviewComment);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ReviewCommentItem item = reviewCommentImage_items.get(position);
        ReviewCommentViewHolder viewHolder = (ReviewCommentViewHolder) holder;

        viewHolder.reviewComment_imageView.setImageResource(item.getReviewCommentImage());


        // 리뷰 코멘토리의 이미지 삭제 버튼(X) 클릭 시,
        viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                reviewCommentImage_items.remove(item);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewCommentImage_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ReviewCommentViewHolder.REVIEWCOMMENT_VIEW_TYPE;
    }

    public void setOnItemClickListener_ReviewComment(ReviewCommentAdapter.OnItemClickEventListener_ReviewComment listener) {
        mItemClickListener_ReviewComment = listener;
    }
}
