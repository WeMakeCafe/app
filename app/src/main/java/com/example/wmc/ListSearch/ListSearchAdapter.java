package com.example.wmc.ListSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetailMore.CafeDetailMoreItem;
import com.example.wmc.CafeDetailMore.CafeDetailMoreViewHolder;

import java.util.ArrayList;

public class ListSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ListSearchItem> listSearch_items;


    public interface OnItemClickEventListener_listSearch { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private ListSearchAdapter.OnItemClickEventListener_listSearch mItemClickListener_listSearch;    // 인터페이스 객체 생성

    public ListSearchAdapter(ArrayList<ListSearchItem> list){
        listSearch_items = list;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ListSearchViewHolder(view, mItemClickListener_listSearch);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 기본적으로 header 를 빼고 item 을 구한다.
        final ListSearchItem item = listSearch_items.get(position);
        ListSearchViewHolder viewHolder = (ListSearchViewHolder) holder;

//        viewHolder.cafeName.setText(item.getCafeName());
    }

    @Override
    public int getItemCount() {
        return listSearch_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ListSearchViewHolder.LISTSEARCH_VIEW_TYPE;
    }

    public void setOnItemClickListener_listSearch(ListSearchAdapter.OnItemClickEventListener_listSearch listener) {
        mItemClickListener_listSearch = listener;
    }
}
