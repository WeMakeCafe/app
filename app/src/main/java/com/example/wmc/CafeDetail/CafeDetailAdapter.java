package com.example.wmc.CafeDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.ui.Fragment.CafeDetailFragment;

import java.util.ArrayList;

public class CafeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CafeDetailItem> review_items;
    private static NavController navController;


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
            viewHolder = new CafeDetailFooterViewHolder(view, mItemClickListener_cafeDetail);
        } else {
            viewHolder = new CafeDetailViewHolder(view, mItemClickListener_cafeDetail);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        if (a_holder instanceof CafeDetailFooterViewHolder) {
            CafeDetailFooterViewHolder footerViewHolder = (CafeDetailFooterViewHolder) a_holder;
        }

        else {
            // 기본적으로 header 를 빼고 item 을 구한다.
            final CafeDetailItem item = review_items.get(a_position);

            CafeDetailViewHolder viewHolder = (CafeDetailViewHolder) a_holder;

            viewHolder.reviewNickName.setText(item.getReviewNickName());
            viewHolder.level_and_location.setText(item.getLevel_and_location());
            viewHolder.review_comment.setText(item.getReview_comment());
            viewHolder.good_count_textView.setText(item.getGood_count_textView());
            viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
            viewHolder.reviewImage.setImageResource(item.getReviewImage());

            // 리뷰에서 수정 버튼 클릭 시,
            viewHolder.reviewModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                    navController = Navigation.findNavController(v);
                    navController.navigate(R.id.cafe_detail_to_review);
                }
            });

            // 리뷰에서 삭제 버튼 클릭 시,
            viewHolder.reviewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                    review_items.remove(item);
                    notifyDataSetChanged();
                }
            });

            // 리뷰에서 좋아요 버튼 클릭 시,
            viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인

                    if(checked) {
                        // 즐겨찾기 항목에 추가함
                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // 즐겨찾기 항목에서 제거됨
                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

