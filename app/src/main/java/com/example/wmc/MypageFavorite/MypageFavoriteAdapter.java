package com.example.wmc.MypageFavorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MypageFavoriteAdapterItem> favorite_items;

    public interface OnItemClickEventListener_HomeFavorite { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private OnItemClickEventListener_HomeFavorite mItemClickListener_HomeFavorite;    // 인터페이스 객체 생성

    public MypageFavoriteAdapter(ArrayList<MypageFavoriteAdapterItem> list){
        favorite_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new MypageFavoriteViewHolder(view, mItemClickListener_HomeFavorite);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            // 기본적으로 header 를 빼고 item 을 구한다.
            final MypageFavoriteAdapterItem item = favorite_items.get(position);
            MypageFavoriteViewHolder viewHolder = (MypageFavoriteViewHolder) holder;

            viewHolder.cafeName_textView.setText(item.getCafeName());
            viewHolder.cafeTag1_textView.setText(item.getTag1());
            viewHolder.cafeTag2_textView.setText(item.getTag2());
            viewHolder.cafe_image.setImageResource(item.getCafeImage());
    }

    @Override
    public int getItemCount() {
        return favorite_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return MypageFavoriteViewHolder.HOMEFAVORITE_VIEW_TYPE;
    }

    public void setOnItemClickListener_HomeFavorite(OnItemClickEventListener_HomeFavorite a_listener) {
        mItemClickListener_HomeFavorite = a_listener;
    }
}
