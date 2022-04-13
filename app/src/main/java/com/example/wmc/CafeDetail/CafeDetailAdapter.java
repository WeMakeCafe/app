package com.example.wmc.CafeDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CafeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CafeDetailItem> review_items;


    public interface OnItemClickEventListener_cafeDetail { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeDetailAdapter.OnItemClickEventListener_cafeDetail mItemClickListener_cafeDetail;    // 인터페이스 객체 생성

    public CafeDetailAdapter(ArrayList<CafeDetailItem> list){
        review_items = list;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup a_viewGroup, int a_viewType) {

        View view = LayoutInflater.from(a_viewGroup.getContext()).inflate(a_viewType, a_viewGroup, false);

        final RecyclerView.ViewHolder viewHolder;

        if (a_viewType == CafeDetailFooterViewHolder.MORE_VIEW_TYPE) {
            viewHolder = new CafeDetailFooterViewHolder(view);
        } else {
            viewHolder = new CafeDetailViewHolder(view, mItemClickListener_cafeDetail);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        if (a_holder instanceof CafeDetailFooterViewHolder) {
            CafeDetailFooterViewHolder footerViewHolder = (CafeDetailFooterViewHolder) a_holder;
        } else {
            // 기본적으로 header 를 빼고 item 을 구한다.
            final CafeDetailItem item = review_items.get(a_position);

            CafeDetailViewHolder viewHolder = (CafeDetailViewHolder) a_holder;

            viewHolder.reviewNickName.setText(item.getReviewNickName());
            viewHolder.level_and_location.setText(item.getLevel_and_location());
            viewHolder.review_comment.setText(item.getReview_comment());
            viewHolder.good_count_textView.setText(item.getGood_count_textView());
            viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
            viewHolder.reviewImage.setImageResource(item.getReviewImage());
        }
    }

    @Override
    public int getItemCount() {
        return review_items.size() + 1;
    }

    @Override
    public int getItemViewType(int a_position) {

        if (a_position == review_items.size()) {
            return CafeDetailFooterViewHolder.MORE_VIEW_TYPE;
        } else {
            return CafeDetailViewHolder.REVIEW_VIEW_TYPE;
        }
    }

    public void setOnItemClickListener_cafeDetail(CafeDetailAdapter.OnItemClickEventListener_cafeDetail a_listener) {
        mItemClickListener_cafeDetail = a_listener;
    }
}
