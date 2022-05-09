package com.example.wmc.ReviewSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ListSearch.ListSearchItem;
import com.example.wmc.ListSearch.ListSearchViewHolder;

import java.util.ArrayList;

public class ReviewSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ReviewSearchItem> reviewSearch_items;


    public interface OnItemClickEventListener_reviewSearch { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private ReviewSearchAdapter.OnItemClickEventListener_reviewSearch mItemClickListener_reviewSearch;    // 인터페이스 객체 생성

    public ReviewSearchAdapter(ArrayList<ReviewSearchItem> list){
        reviewSearch_items = list;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ReviewSearchViewHolder(view, mItemClickListener_reviewSearch);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 기본적으로 header 를 빼고 item 을 구한다.
        final ReviewSearchItem item = reviewSearch_items.get(position);
        ReviewSearchViewHolder viewHolder = (ReviewSearchViewHolder) holder;

        viewHolder.cafe_name_textView.setText(item.getSearch_cafeName());
        viewHolder.cafe_address_textView.setText(item.getSearch_cafeAddress());

        // ReviewSearch에서 최근검색 X버튼 클릭 시,
        viewHolder.recentDelete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "최근검색 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                reviewSearch_items.remove(item);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewSearch_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ReviewSearchViewHolder.REVIEWSEARCH_VIEW_TYPE;
    }

    public void setOnItemClickListener_reviewSearch(ReviewSearchAdapter.OnItemClickEventListener_reviewSearch listener) {
        mItemClickListener_reviewSearch = listener;
    }
}
