package com.example.wmc.HomeTag2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.HomeTag1.HomeTag1Item;
import com.example.wmc.HomeTag1.HomeTag1ViewHolder;

import java.util.ArrayList;

public class HomeTag2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<HomeTag2Item> tag2_items;

    public interface OnItemClickEventListener_HomeTag2 { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private HomeTag2Adapter.OnItemClickEventListener_HomeTag2 mItemClickListener_HomeTag2;    // 인터페이스 객체 생성

    public HomeTag2Adapter(ArrayList<HomeTag2Item> list){
        tag2_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new HomeTag2ViewHolder(view, mItemClickListener_HomeTag2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            // 기본적으로 header 를 빼고 item 을 구한다.
            final HomeTag2Item item = tag2_items.get(position);
        HomeTag2ViewHolder viewHolder = (HomeTag2ViewHolder) holder;

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
        return tag2_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return HomeTag2ViewHolder.HOMETAG2_VIEW_TYPE;
    }

    public void setOnItemClickListener_HomeTag2(HomeTag2Adapter.OnItemClickEventListener_HomeTag2 a_listener) {
        mItemClickListener_HomeTag2 = a_listener;
    }
}
