package com.example.wmc.MypageReviewComment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wmc.R;
import com.example.wmc.ReviewComment.ReviewCommentViewHolder;
import com.example.wmc.ui.Fragment.Mypage_ReviewCommentFragment;
import com.example.wmc.ui.Fragment.ReviewCommentFragment;

import java.util.ArrayList;

public class Mypage_ReviewCommentAdapter extends RecyclerView.Adapter<Mypage_ReviewCommentViewHolder>{

    private Context reviewContext = null;
    private ArrayList<Uri> reviewData = null;
    Mypage_ReviewCommentFragment mypage_reviewCommentFragment;

    public Mypage_ReviewCommentAdapter(Context context, ArrayList<Uri> list, Mypage_ReviewCommentFragment mypage_reviewCommentFragment) {
        reviewContext = context;
        reviewData = list ;
        this.mypage_reviewCommentFragment = mypage_reviewCommentFragment;
    }

    @NonNull
    @Override
    public Mypage_ReviewCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_add_image, viewGroup, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        Mypage_ReviewCommentViewHolder vh = new Mypage_ReviewCommentViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull Mypage_ReviewCommentViewHolder viewHolder, int position) {
        Uri image_uri = reviewData.get(position) ;

        Glide.with(reviewContext)
                .load(image_uri)
                .into(viewHolder.reviewComment_imageView);


        // 이미지 삭제 버튼(X) 클릭 시,
        viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                reviewData.remove(image_uri);

                // 이미지 DELETE코드
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewData.size();
    }


//    private ArrayList<ReviewCommentItem> reviewCommentImage_items;
//
//    public interface OnItemClickEventListener_ReviewComment { // 클릭 이벤트를 위한 인터페이스
//        void onItemClick(View view, int position);
//    }
//    private ReviewCommentAdapter.OnItemClickEventListener_ReviewComment mItemClickListener_ReviewComment;    // 인터페이스 객체 생성
//
//    public ReviewCommentAdapter(ArrayList<ReviewCommentItem> list){
//        reviewCommentImage_items = list;
//    }

//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
//        final RecyclerView.ViewHolder viewHolder;
//        viewHolder = new ReviewCommentViewHolder(view, mItemClickListener_ReviewComment);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        final ReviewCommentItem item = reviewCommentImage_items.get(position);
//        ReviewCommentViewHolder viewHolder = (ReviewCommentViewHolder) holder;
//
//        viewHolder.reviewComment_imageView.setImageResource(item.getReviewCommentImage());
//
//
//        // 리뷰 코멘토리의 이미지 삭제 버튼(X) 클릭 시,
//        viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
//                reviewCommentImage_items.remove(item);
//                notifyDataSetChanged();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return reviewCommentImage_items.size();
//    }
//
//    @Override
//    public int getItemViewType(int a_position) {
//        return ReviewCommentViewHolder.REVIEWCOMMENT_VIEW_TYPE;
//    }
//
//    public void setOnItemClickListener_ReviewComment(ReviewCommentAdapter.OnItemClickEventListener_ReviewComment listener) {
//        mItemClickListener_ReviewComment = listener;
//    }
}
