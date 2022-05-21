package com.example.wmc.CafeDetail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentHostCallback;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Review;
import com.example.wmc.ui.Fragment.CafeDetailFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CafeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<CafeDetailItem> review_items;
    CafeDetailFragment cafeDetailFragment;

    private static NavController navController;

    ArrayList<Review> review_list;

    Long mem_num = MainActivity.mem_num; // 임시 유저 넘버


    public interface OnItemClickEventListener_cafeDetail { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeDetailAdapter.OnItemClickEventListener_cafeDetail mItemClickListener_cafeDetail;    // 인터페이스 객체 생성

    public CafeDetailAdapter(Context context, ArrayList<CafeDetailItem> list, CafeDetailFragment cafeDetailFragment){
        this.context = context;
        review_items = list;
        this.cafeDetailFragment = cafeDetailFragment;
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
            viewHolder.review_writeTime.setText(item.getReview_writeTime());
            viewHolder.good_count_textView.setText(item.getGood_count_textView());
            viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
            viewHolder.reviewImage.setImageResource(item.getReviewImage());
            viewHolder.check_user_flag = (item.getCheck_user_flag());


            // 리뷰 작성자와 로그인한 사람이 같을 때,
            if(viewHolder.check_user_flag){
                viewHolder.reviewModify.setVisibility(View.VISIBLE);
                viewHolder.reviewModifyLine.setVisibility(View.VISIBLE);
                viewHolder.reviewDelete.setVisibility(View.VISIBLE);
                viewHolder.reviewDeleteLine.setVisibility(View.VISIBLE);
                viewHolder.good_button_image.setVisibility(View.VISIBLE);
                viewHolder.good_button.setVisibility(View.INVISIBLE);

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
            }

            // 리뷰 작성자와 로그인한 사람이 다를 때,
            else{
                viewHolder.reviewModify.setVisibility(View.INVISIBLE);
                viewHolder.reviewModifyLine.setVisibility(View.INVISIBLE);
                viewHolder.reviewDelete.setVisibility(View.INVISIBLE);
                viewHolder.reviewDeleteLine.setVisibility(View.INVISIBLE);
                viewHolder.good_button_image.setVisibility(View.INVISIBLE);
                viewHolder.good_button.setVisibility(View.VISIBLE);

//                // 리뷰에서 좋아요 버튼 클릭 시,
//                viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인
//
//                        // 자신이 쓴 글일 경우 좋아요 버튼 클릭 불가로 변경
//                        if(checked) {
//                            // 좋아요 추가
//                            Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            // 좋아요 취소
//                            Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
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

