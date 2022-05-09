package com.example.wmc.ReviewTag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.MypageFavorite.MypageFavoriteAdapter;
import com.example.wmc.MypageFavorite.MypageFavoriteViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReviewTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReviewTagItem> tag_items;

    public interface OnItemClickEventListener_ReviewTag {
        void onItemClick(View a_view, int a_position);
    }
    private ReviewTagAdapter.OnItemClickEventListener_ReviewTag mItemClcikListener_ReviewTag;

    public ReviewTagAdapter(ArrayList<ReviewTagItem> list){ tag_items = list; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ReviewTagViewHolder(view, mItemClcikListener_ReviewTag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ReviewTagItem item = tag_items.get(position);
        ReviewTagViewHolder viewHolder = (ReviewTagViewHolder) holder;

        viewHolder.tag_but.setText(item.getTagName());

    }

    @Override
    public int getItemCount() {
        return tag_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ReviewTagViewHolder.ReviewTag_VIEW_TYPE;
    }

    public void setOnItemClickListener_ReviewTag(ReviewTagAdapter.OnItemClickEventListener_ReviewTag a_listener) {
        mItemClcikListener_ReviewTag = a_listener;
    }

}
