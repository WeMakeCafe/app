package com.example.wmc.CafeDetailMore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

import java.util.ArrayList;

public class CafeDetailMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CafeDetailMoreItem> reviewMore_items;
    private static NavController navController;


    public interface OnItemClickEventListener_cafeDetailMore { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore mItemClickListener_cafeDetailMore;    // 인터페이스 객체 생성

    public CafeDetailMoreAdapter(ArrayList<CafeDetailMoreItem> list){
        reviewMore_items = list;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new CafeDetailMoreViewHolder(view, mItemClickListener_cafeDetailMore);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 기본적으로 header 를 빼고 item 을 구한다.
        final CafeDetailMoreItem item = reviewMore_items.get(position);
        CafeDetailMoreViewHolder viewHolder = (CafeDetailMoreViewHolder) holder;

//        viewHolder.cafeName.setText(item.getCafeName());
        viewHolder.nickName.setText(item.getReviewNickName());
        viewHolder.level_and_location.setText(item.getLevel_and_location());
        viewHolder.review_comment.setText(item.getReview_comment());
        viewHolder.good_count_textView.setText(item.getGood_count_textView());

        viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
        viewHolder.reviewMore_image1.setImageResource(item.getReviewImage1());
        viewHolder.reviewMore_image2.setImageResource(item.getReviewImage2());
        viewHolder.reviewMore_image3.setImageResource(item.getReviewImage3());


        // 리뷰 더보기의 수정 버튼 클릭 시,
        viewHolder.review_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                navController = Navigation.findNavController(v);
                navController.navigate(R.id.cafe_detail_more_to_review);
            }
        });


        // 리뷰 더보기의 삭제 버튼 클릭 시,
        viewHolder.review_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                reviewMore_items.remove(item);
                notifyDataSetChanged();
            }
        });


        // 리뷰 더보기의 좋아요 버튼 클릭 시,
        viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인

                // 자신이 쓴 글일 경우 좋아요 버튼 클릭 불가로 변경
                if(checked) {
                    // 좋아요 추가
                    Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 좋아요 취소
                    Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewMore_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return CafeDetailMoreViewHolder.REVIEWMORE_VIEW_TYPE;
    }

    public void setOnItemClickListener_cafeDetailMore(CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore a_listener) {
        mItemClickListener_cafeDetailMore = a_listener;
    }
}
