package com.example.wmc.ListCafeList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.HomeTag1.HomeTag1ViewHolder;

import java.util.ArrayList;

public class ListCafeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ListCafeListItem> listCafeList_items;

    public interface OnItemClickEventListener_ListCafeList { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private ListCafeListAdapter.OnItemClickEventListener_ListCafeList mItemClickListener_ListCafeList;    // 인터페이스 객체 생성

    public ListCafeListAdapter(ArrayList<ListCafeListItem> list){
        listCafeList_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ListCafeListViewHolder(view, mItemClickListener_ListCafeList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ListCafeListItem item = listCafeList_items.get(position);
        ListCafeListViewHolder viewHolder = (ListCafeListViewHolder) holder;

//        viewHolder.cafeName.setText(item.getCafeName());
        viewHolder.cafeList_cafe_name_textView.setText(item.getCafeList_cafeName());
        viewHolder.cafeList_cafe_address_textView.setText(item.getCafeList_cafeAddress());
        viewHolder.opening_hours.setText(item.getOpenTime());
        viewHolder.cafeList_hashTag1.setText(item.getTag1());
        viewHolder.cafeList_hashTag2.setText(item.getTag2());
        viewHolder.cafeList_cafeImage.setImageResource(item.getCafeList_cafeImage());
    }

    @Override
    public int getItemCount() {
        return listCafeList_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ListCafeListViewHolder.LISTCAFELIST_VIEW_TYPE;
    }

    public void setOnItemClickListener_ListCafeList(ListCafeListAdapter.OnItemClickEventListener_ListCafeList a_listener) {
        mItemClickListener_ListCafeList = a_listener;
    }
}
