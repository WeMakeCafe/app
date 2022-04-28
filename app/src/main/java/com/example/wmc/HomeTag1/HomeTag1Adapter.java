package com.example.wmc.HomeTag1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.HomeFavorite.HomeFavoriteViewHolder;

import java.util.ArrayList;

public class HomeTag1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<HomeTag1Item> tag1_items;

    public interface OnItemClickEventListener_HomeTag1 { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private HomeTag1Adapter.OnItemClickEventListener_HomeTag1 mItemClickListener_HomeTag1;    // 인터페이스 객체 생성

    public HomeTag1Adapter(ArrayList<HomeTag1Item> list){
        tag1_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new HomeTag1ViewHolder(view, mItemClickListener_HomeTag1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            // 기본적으로 header 를 빼고 item 을 구한다.
            final HomeTag1Item item = tag1_items.get(position);
        HomeTag1ViewHolder viewHolder = (HomeTag1ViewHolder) holder;

            viewHolder.cafeName.setText(item.getCafeName());
            viewHolder.cafeAddress.setText(item.getCafeAddress());
            viewHolder.tag1.setText(item.getTag1());
            viewHolder.tag2.setText(item.getTag2());
            viewHolder.tag3.setText(item.getTag3());
            viewHolder.bestReview_example.setText(item.getReview());
            viewHolder.cafe_image.setImageResource(item.getCafeImage());
            viewHolder.rating_all.setRating(item.getRating());
    }

    @Override
    public int getItemCount() {
        return tag1_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return HomeTag1ViewHolder.HOMETAG1_VIEW_TYPE;
    }

    public void setOnItemClickListener_HomeTag1(HomeTag1Adapter.OnItemClickEventListener_HomeTag1 a_listener) {
        mItemClickListener_HomeTag1 = a_listener;
    }
}
