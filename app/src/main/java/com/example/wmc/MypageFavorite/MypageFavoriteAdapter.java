package com.example.wmc.MypageFavorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MypageFavoriteItem> favorite_items;

    public interface OnItemClickEventListener_MypageFavorite { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private MypageFavoriteAdapter.OnItemClickEventListener_MypageFavorite mItemClickListener_MypageFavorite;    // 인터페이스 객체 생성

    public MypageFavoriteAdapter(ArrayList<MypageFavoriteItem> list){ favorite_items = list; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new MypageFavoriteViewHolder(view, mItemClickListener_MypageFavorite);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            // 기본적으로 header 를 빼고 item 을 구한다.
            final MypageFavoriteItem item = favorite_items.get(position);
            MypageFavoriteViewHolder viewHolder = (MypageFavoriteViewHolder) holder;

            viewHolder.favorite_but.setText(item.getCafeName());
    }

    @Override
    public int getItemCount() {
        return favorite_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return MypageFavoriteViewHolder.MYPAGEFAVORITE_VIEW_TYPE;
    }

    public void setOnItemClickListener_MypageFavorite(MypageFavoriteAdapter.OnItemClickEventListener_MypageFavorite a_listener) {
        mItemClickListener_MypageFavorite = a_listener;
    }
}
