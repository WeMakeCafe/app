package com.example.wmc.CafeModify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.HomeTag1.HomeTag1ViewHolder;

import java.util.ArrayList;

public class CafeModifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CafeModifyItem> modifyImage_items;

    public interface OnItemClickEventListener_CafeModify { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeModifyAdapter.OnItemClickEventListener_CafeModify mItemClickListener_CafeModify;    // 인터페이스 객체 생성

    public CafeModifyAdapter(ArrayList<CafeModifyItem> list){
        modifyImage_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new CafeModifyViewHolder(view, mItemClickListener_CafeModify);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            // 기본적으로 header 를 빼고 item 을 구한다.
            final CafeModifyItem item = modifyImage_items.get(position);
            CafeModifyViewHolder viewHolder = (CafeModifyViewHolder) holder;

            viewHolder.add_modify_imageView.setImageResource(item.getModifyImage());
    }

    @Override
    public int getItemCount() {
        return modifyImage_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return CafeModifyViewHolder.CAFEMODIFY_VIEW_TYPE;
    }

    public void setOnItemClickListener_CafeModify(CafeModifyAdapter.OnItemClickEventListener_CafeModify a_listener) {
        mItemClickListener_CafeModify = a_listener;
    }
}
