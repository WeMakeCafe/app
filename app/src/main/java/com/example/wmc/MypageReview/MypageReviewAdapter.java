package com.example.wmc.MypageReview;

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

import com.example.wmc.R;
import com.example.wmc.ui.Fragment.MyPageFragment;

import java.util.ArrayList;

public class MypageReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<MypageReviewItem> myPageReview_items;
    MyPageFragment myPageFragment;
    private static NavController navController;

    public interface OnItemClickEventListener_MyPageReview { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private MypageReviewAdapter.OnItemClickEventListener_MyPageReview mItemClickListener_MyPageReview;    // 인터페이스 객체 생성

    public MypageReviewAdapter(Context context, ArrayList<MypageReviewItem> list, MyPageFragment myPageFragment){
        this.context = context;
        myPageReview_items = list;
        this.myPageFragment = myPageFragment;
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

        final MypageReviewItem item = myPageReview_items.get(a_position);
        MypageReviewViewHolder viewHolder = (MypageReviewViewHolder) a_holder;

        viewHolder.mypageReview_CafeName.setText(item.getMypageReview_CafeName());
        viewHolder.mypageReview_writeTime.setText(item.getMypageReview_writeTime());
        viewHolder.myPageReview_comment.setText(item.getReview_comment());
        viewHolder.good_count_textView.setText(item.getGood_count_textView());
        viewHolder.myPageReview_image1.setImageResource(item.getReviewImage1());
        viewHolder.myPageReview_image2.setImageResource(item.getReviewImage2());
        viewHolder.myPageReview_image3.setImageResource(item.getReviewImage3());

        viewHolder.check_user_flag = (item.getCheck_user_flag());   // 작성자와 로그인한 유저가 같은지 확인


        // 리뷰 작성자와 로그인한 유저가 같을 경우,
        if(viewHolder.check_user_flag){

            viewHolder.myPageReview_modify.setVisibility(View.VISIBLE);
            viewHolder.myPageReview_modifyLine.setVisibility(View.VISIBLE);
            viewHolder.myPageReview_delete.setVisibility(View.VISIBLE);
            viewHolder.myPageReview_deleteLine.setVisibility(View.VISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.VISIBLE);
            viewHolder.good_button.setVisibility(View.INVISIBLE);

            // 마이페이지 리뷰의 수정 버튼 클릭 시,
            viewHolder.myPageReview_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                    navController = Navigation.findNavController(v);

                    Bundle bundle = new Bundle();
                    bundle.putLong("cafeNum", item.getGet_cafe_num());
                    bundle.putLong("memNum", item.getMem_num());
                    bundle.putBoolean("mypage_reviewModify_flag", true);

                    navController.navigate(R.id.myPage_to_review, bundle);

                }
            });


            // 마이페이지 리뷰의 삭제 버튼 클릭 시,
            viewHolder.myPageReview_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(myPageFragment.getActivity());
                    builder.setTitle("리뷰 삭제").setMessage("리뷰를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(v.getContext().getApplicationContext(), "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                             DB에서 테이블 삭제 하는 코드 추가하기
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

        // 리뷰 작성자와 로그인한 유저가 다를 경우,
        else {
            viewHolder.myPageReview_modify.setVisibility(View.INVISIBLE);
            viewHolder.myPageReview_modifyLine.setVisibility(View.INVISIBLE);
            viewHolder.myPageReview_delete.setVisibility(View.INVISIBLE);
            viewHolder.myPageReview_deleteLine.setVisibility(View.INVISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.INVISIBLE);
            viewHolder.good_button.setVisibility(View.VISIBLE);

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
        return MypageReviewViewHolder.MYPAGE_REVIEW_VIEW_TYPE;
    }

    public void setOnItemClickListener_MypageReview(MypageReviewAdapter.OnItemClickEventListener_MyPageReview a_listener) {
        mItemClickListener_MyPageReview = a_listener;
    }
}
