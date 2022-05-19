package com.example.wmc.ListCafeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        viewHolder.cafeList_cafe_name_textView.setText(item.getCafeList_cafeName());
        viewHolder.cafeList_cafe_address_textView.setText(item.getCafeList_cafeAddress());
        viewHolder.opening_hours.setText(item.getOpenTime());
        viewHolder.cafeList_hashTag1.setText(item.getTag1());
        viewHolder.cafeList_hashTag2.setText(item.getTag2());
        viewHolder.cafeList_cafeImage.setImageResource(item.getCafeList_cafeImage());
        viewHolder.check_user_flag = item.getCheck_user_flag();


        if(viewHolder.check_user_flag){
            viewHolder.favorite_button.setChecked(true);
        }
        else{
            viewHolder.favorite_button.setChecked(false);
        }


        // 즐겨찾기 버튼 클릭 시,
        viewHolder.favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();    // 즐겨찾기가 됐는지 확인

                if(checked) {
                    // 즐겨찾기 항목에 추가함
                    //Bundle bundle = new Bundle();
                    Toast.makeText(v.getContext().getApplicationContext(), "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 즐겨찾기 항목에서 제거됨
                    Toast.makeText(v.getContext().getApplicationContext(), "즐겨찾기 삭제", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
