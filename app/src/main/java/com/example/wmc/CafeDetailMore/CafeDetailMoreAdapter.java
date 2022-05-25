package com.example.wmc.CafeDetailMore;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Review;
import com.example.wmc.ui.Fragment.CafeDetailMoreFragment;

import java.util.ArrayList;

public class CafeDetailMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<CafeDetailMoreItem> reviewMore_items;
    CafeDetailMoreFragment cafeDetailMoreFragment;
    private static NavController navController;

    ArrayList<Review> review_list;

    Long mem_num = MainActivity.mem_num; // 임시 유저 넘버


    public interface OnItemClickEventListener_cafeDetailMore { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore mItemClickListener_cafeDetailMore;    // 인터페이스 객체 생성

    public CafeDetailMoreAdapter(Context context, ArrayList<CafeDetailMoreItem> list, CafeDetailMoreFragment cafeDetailMoreFragment){
        this.context = context;
        reviewMore_items = list;
        this.cafeDetailMoreFragment = cafeDetailMoreFragment;
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

        final CafeDetailMoreItem item = reviewMore_items.get(position);
        CafeDetailMoreViewHolder viewHolder = (CafeDetailMoreViewHolder) holder;

//        viewHolder.cafeName.setText(item.getCafeName());
        viewHolder.nickName.setText(item.getReviewNickName());
        viewHolder.level_and_location.setText(item.getLevel_and_location());
        viewHolder.review_comment.setText(item.getReview_comment());
        viewHolder.reviewMore_writeTime.setText(item.getReviewMore_writeTime());// 작성자가 리뷰를 작성한 시간
        viewHolder.good_count_textView.setText(item.getGood_count_textView());

        viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
        viewHolder.reviewMore_image1.setImageResource(item.getReviewImage1());
        viewHolder.reviewMore_image2.setImageResource(item.getReviewImage2());
        viewHolder.reviewMore_image3.setImageResource(item.getReviewImage3());

        viewHolder.check_user_flag = (item.getCheck_user_flag());   // 작성자와 로그인한 유저가 같은지 확인



        // 리뷰 작성자와 로그인한 사람이 같을 때,
        if(viewHolder.check_user_flag){
            viewHolder.review_modify.setVisibility(View.VISIBLE);
            viewHolder.review_modifyLine.setVisibility(View.VISIBLE);
            viewHolder.review_modify.setVisibility(View.VISIBLE);
            viewHolder.review_modify.setVisibility(View.VISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.VISIBLE);
            viewHolder.good_button.setVisibility(View.INVISIBLE);


            // 리뷰 더보기의 수정 버튼 클릭 시,
            viewHolder.review_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();

                    navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putLong("cafeNum", item.getGet_cafe_num());
                    bundle.putLong("memNum", item.getMem_num());
                    bundle.putBoolean("moreReview_reviewModify_flag", true);
                    navController.navigate(R.id.cafe_detail_more_to_review, bundle);

                }
            });


            // 리뷰 더보기의 삭제 버튼 클릭 시,
            viewHolder.review_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(cafeDetailMoreFragment.getActivity());
                    builder.setTitle("리뷰 삭제").setMessage("리뷰를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(v.getContext().getApplicationContext(), "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            // DB에서 테이블 삭제 하는 코드 추가하기
//                            review_items.remove(item);    // 리사이클러뷰에서도 아이템 삭제
//                            notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }


        // 리뷰 작성자와 로그인한 사람이 다를를 때,
       else{
            viewHolder.review_modify.setVisibility(View.INVISIBLE);
            viewHolder.review_modifyLine.setVisibility(View.INVISIBLE);
            viewHolder.review_delete.setVisibility(View.INVISIBLE);
            viewHolder.review_deleteLine.setVisibility(View.INVISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.INVISIBLE);
            viewHolder.good_button.setVisibility(View.VISIBLE);

//            // 리뷰 더보기의 좋아요 버튼 클릭 시,
//            viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인
//
//                    // 자신이 쓴 글일 경우 좋아요 버튼 클릭 불가로 변경
//                    if(checked) {
//                        // 좋아요 추가
//                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        // 좋아요 취소
//                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }


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
