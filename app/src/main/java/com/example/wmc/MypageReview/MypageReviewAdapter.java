package com.example.wmc.MypageReview;

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

public class MypageReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MypageReviewItem> myPageReview_items;
    private static NavController navController;

    public interface OnItemClickEventListener_MyPageReview { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private MypageReviewAdapter.OnItemClickEventListener_MyPageReview mItemClickListener_MyPageReview;    // 인터페이스 객체 생성

    public MypageReviewAdapter(ArrayList<MypageReviewItem> list){
        myPageReview_items = list;
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
        final MypageReviewItem item = myPageReview_items.get(a_position);
        MypageReviewViewHolder viewHolder = (MypageReviewViewHolder) a_holder;

        viewHolder.reviewNickName.setText(item.getReviewNickName());
        viewHolder.level_and_location.setText(item.getLevel_and_location());
        viewHolder.review_comment.setText(item.getReview_comment());
        viewHolder.good_count_textView.setText(item.getGood_count_textView());
        viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
        viewHolder.reviewImage1.setImageResource(item.getReviewImage1());
        viewHolder.reviewImage2.setImageResource(item.getReviewImage2());
        viewHolder.reviewImage3.setImageResource(item.getReviewImage3());


        // 마이페이지 리뷰의 수정 버튼 클릭 시,
        viewHolder.review_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                navController = Navigation.findNavController(v);
                navController.navigate(R.id.myPage_to_review);
            }
        });


        // 마이페이지 리뷰의 삭제 버튼 클릭 시,
        viewHolder.review_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                myPageReview_items.remove(item);
                notifyDataSetChanged();
            }
        });


        // 마이페이지 리뷰의 좋아요 버튼 클릭 시,
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
    public int getItemCount() { return myPageReview_items.size(); }

    @Override
    public int getItemViewType(int a_position) {
        return MypageReviewViewHolder.REVIEW_VIEW_TYPE;
    }

    public void setOnItemClickListener_MypageReview(MypageReviewAdapter.OnItemClickEventListener_MyPageReview a_listener) {
        mItemClickListener_MyPageReview = a_listener;
    }
}
