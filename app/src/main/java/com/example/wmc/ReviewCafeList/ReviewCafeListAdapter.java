package com.example.wmc.ReviewCafeList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ListCafeList.ListCafeListItem;
import com.example.wmc.ListCafeList.ListCafeListViewHolder;

import java.util.ArrayList;

public class ReviewCafeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ReviewCafeListItem> reviewCafeList_items;

    public interface OnItemClickEventListener_ReviewCafeList { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList mItemClickListener_ReviewCafeList;    // 인터페이스 객체 생성

    public ReviewCafeListAdapter(ArrayList<ReviewCafeListItem> list){
        reviewCafeList_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ReivewCafeListViewHolder(view, mItemClickListener_ReviewCafeList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ReviewCafeListItem item = reviewCafeList_items.get(position);
        ReivewCafeListViewHolder viewHolder = (ReivewCafeListViewHolder) holder;

        viewHolder.cafeList_cafe_name_textView.setText(item.getCafeList_cafeName());
        viewHolder.cafeList_cafe_address_textView.setText(item.getCafeList_cafeAddress());
        viewHolder.opening_hours.setText(item.getOpenTime());
        viewHolder.cafeList_hashTag1.setText(item.getTag1());
        viewHolder.cafeList_hashTag2.setText(item.getTag2());
        viewHolder.cafeList_cafeImage.setImageResource(item.getCafeList_cafeImage());
    }

    @Override
    public int getItemCount() {
        return reviewCafeList_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ReivewCafeListViewHolder.REVIEWCAFELIST_VIEW_TYPE;
    }

    public void setOnItemClickListener_ReviewCafeList(ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList listener) {
        mItemClickListener_ReviewCafeList = listener;
    }
}
