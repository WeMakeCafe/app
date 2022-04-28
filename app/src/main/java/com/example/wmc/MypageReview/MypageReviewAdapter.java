package com.example.wmc.MypageReview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MypageReviewItem> review_items;

    public interface OnItemClickEventListener_MyPageReview { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private MypageReviewAdapter.OnItemClickEventListener_MyPageReview mItemClickListener_MyPageReview;    // 인터페이스 객체 생성

    public MypageReviewAdapter(ArrayList<MypageReviewItem> list){
        review_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new MypageReviewViewHolder(view, mItemClickListener_MyPageReview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        // 기본적으로 header 를 빼고 item 을 구한다.
        final MypageReviewItem item = review_items.get(a_position);
        MypageReviewViewHolder viewHolder = (MypageReviewViewHolder) a_holder;

        viewHolder.reviewNickName.setText(item.getReviewNickName());
        viewHolder.level_and_location.setText(item.getLevel_and_location());
        viewHolder.review_comment.setText(item.getReview_comment());
        viewHolder.good_count_textView.setText(item.getGood_count_textView());
        viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
        viewHolder.reviewImage1.setImageResource(item.getReviewImage1());
        viewHolder.reviewImage2.setImageResource(item.getReviewImage2());
        viewHolder.reviewImage3.setImageResource(item.getReviewImage3());
    }

    @Override
    public int getItemCount() { return review_items.size(); }

    @Override
    public int getItemViewType(int a_position) {
        return MypageReviewViewHolder.REVIEW_VIEW_TYPE;
    }

    public void setOnItemClickListener_MypageReview(MypageReviewAdapter.OnItemClickEventListener_MyPageReview a_listener) {
        mItemClickListener_MyPageReview = a_listener;
    }
}
