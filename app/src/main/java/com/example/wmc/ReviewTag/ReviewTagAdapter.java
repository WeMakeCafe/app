package com.example.wmc.ReviewTag;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReviewTagItem> tag_items;
    public interface OnItemClickEventListener_ReviewTag {
        void onItemClick(View a_view, int a_position);
    }
    private ReviewTagAdapter.OnItemClickEventListner_

    public interface OnItemClickEventListner_
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}