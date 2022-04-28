package com.example.wmc.CafeDetailMore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailFooterViewHolder;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetail.CafeDetailViewHolder;
import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.HomeTag1.HomeTag1ViewHolder;

import java.util.ArrayList;

public class CafeDetailMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CafeDetailMoreItem> reviewMore_items;


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
