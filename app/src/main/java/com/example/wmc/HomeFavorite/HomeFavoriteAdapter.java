package com.example.wmc.HomeFavorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailFooterViewHolder;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetail.CafeDetailViewHolder;
import com.example.wmc.ListCafeList.ListCafeListItem;
import com.example.wmc.ui.Fragment.HomeFragment;
import com.example.wmc.ui.Fragment.ListCafelistFragment;

import java.util.ArrayList;

public class HomeFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<HomeFavoriteItem> favorite_items;
    private Context context;
    HomeFragment homeFragment;

    public interface OnItemClickEventListener_HomeFavorite { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private HomeFavoriteAdapter.OnItemClickEventListener_HomeFavorite mItemClickListener_HomeFavorite;    // 인터페이스 객체 생성

    public HomeFavoriteAdapter(Context context, ArrayList<HomeFavoriteItem> list, HomeFragment homeFragment){
        this.context = context;
        favorite_items = list;
        this.homeFragment = homeFragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new HomeFavoriteViewHolder(view, mItemClickListener_HomeFavorite);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 기본적으로 header 를 빼고 item 을 구한다.
        final HomeFavoriteItem item = favorite_items.get(position);
        HomeFavoriteViewHolder viewHolder = (HomeFavoriteViewHolder) holder;

        viewHolder.cafeName_textView.setText(item.getCafeName());
        viewHolder.cafeTag1_textView.setText(item.getTag1());
        viewHolder.cafeTag2_textView.setText(item.getTag2());

        Glide.with(homeFragment.getActivity()).load(item.getCafeImage()).into(viewHolder.cafe_image);

    }

    @Override
    public int getItemCount() {
        return favorite_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return HomeFavoriteViewHolder.HOMEFAVORITE_VIEW_TYPE;
    }

    public void setOnItemClickListener_HomeFavorite(HomeFavoriteAdapter.OnItemClickEventListener_HomeFavorite a_listener) {
        mItemClickListener_HomeFavorite = a_listener;
    }
}
