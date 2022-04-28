package com.example.wmc.MypageMyreview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailFooterViewHolder;

import java.util.ArrayList;

public class MypageMyreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MypageMyreviewItem> review_items;


    public interface OnItemClickEventListener_cafeDetail { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private OnItemClickEventListener_cafeDetail mItemClickListener_cafeDetail;    // 인터페이스 객체 생성

    public MypageMyreviewAdapter(ArrayList<MypageMyreviewItem> list){
        review_items = list;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup a_viewGroup, int a_viewType) {

        View view = LayoutInflater.from(a_viewGroup.getContext()).inflate(a_viewType, a_viewGroup, false);

        final RecyclerView.ViewHolder viewHolder;

        if (a_viewType == CafeDetailFooterViewHolder.MORE_VIEW_TYPE) {
            viewHolder = new CafeDetailFooterViewHolder(view, mItemClickListener_cafeDetail);
        } else {
            viewHolder = new MypageMyreviewViewHolder(view, mItemClickListener_cafeDetail);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        if (a_holder instanceof CafeDetailFooterViewHolder) {
            CafeDetailFooterViewHolder footerViewHolder = (CafeDetailFooterViewHolder) a_holder;
        } else {
            // 기본적으로 header 를 빼고 item 을 구한다.
            final MypageMyreviewItem item = review_items.get(a_position);

            MypageMyreviewViewHolder viewHolder = (MypageMyreviewViewHolder) a_holder;

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
            return MypageMyreviewViewHolder.REVIEW_VIEW_TYPE;
        }
    }

    public void setOnItemClickListener_cafeDetail(OnItemClickEventListener_cafeDetail a_listener) {
        mItemClickListener_cafeDetail = a_listener;
    }
}
