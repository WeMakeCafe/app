package com.example.wmc.ListSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ui.Fragment.FooterViewHolder;

import java.util.ArrayList;

public class ListSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ListSearchItem> items;

    public interface OnItemClickEventListener { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private OnItemClickEventListener mItemClickListener;    // 인터페이스 객체 생성


    public ListSearchAdapter(ArrayList<ListSearchItem> list){
        items = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup a_viewGroup, int a_viewType) {

        View view = LayoutInflater.from(a_viewGroup.getContext()).inflate(a_viewType, a_viewGroup, false);

        final RecyclerView.ViewHolder viewHolder;

        if (a_viewType == FooterViewHolder.VIEW_TYPE) {
            viewHolder = new FooterViewHolder(view);
        } else {
            viewHolder = new ListSearchViewHolder(view, mItemClickListener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        if (a_holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) a_holder;
        } else {
            // 기본적으로 header 를 빼고 item 을 구한다.
            final ListSearchItem item = items.get(a_position);

            ListSearchViewHolder viewHolder = (ListSearchViewHolder) a_holder;

            viewHolder.cafeNameTextView.setText(item.getCafeName());
            viewHolder.cafeAddressTextView.setText(item.getCafeAddress());
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int a_position) {

        if (a_position == items.size()) {
            return FooterViewHolder.VIEW_TYPE;
        } else {
            return ListSearchViewHolder.VIEW_TYPE;
        }
    }

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }
}
