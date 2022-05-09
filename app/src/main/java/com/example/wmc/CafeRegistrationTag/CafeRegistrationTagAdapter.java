package com.example.wmc.CafeRegistrationTag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ReviewTag.ReviewTagItem;
import com.example.wmc.ReviewTag.ReviewTagViewHolder;

import java.util.ArrayList;

public class CafeRegistrationTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CafeRegistrationTagItem> cafeRegistrationTagItems;

    public interface OnItemClickEventListener_CafeRegistrationTag {
        void onItemClick(View a_view, int a_position);
    }
    private CafeRegistrationTagAdapter.OnItemClickEventListener_CafeRegistrationTag mItemClcikListener_CafeRegistrationTag;

    public CafeRegistrationTagAdapter(ArrayList<CafeRegistrationTagItem> list){ cafeRegistrationTagItems = list; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new CafeRegistrationTagViewHolder(view, mItemClcikListener_CafeRegistrationTag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final CafeRegistrationTagItem item = cafeRegistrationTagItems.get(position);
        CafeRegistrationTagViewHolder viewHolder = (CafeRegistrationTagViewHolder) holder;

        viewHolder.tag_but.setText(item.getTagName());
    }

    @Override
    public int getItemCount() {
        return cafeRegistrationTagItems.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return CafeRegistrationTagViewHolder.CAFEREGISTRATIONTAG_VIEW_TYPE;
    }

    public void setOnItemClickListener_ReviewTag(CafeRegistrationTagAdapter.OnItemClickEventListener_CafeRegistrationTag listener) {
        mItemClcikListener_CafeRegistrationTag = listener;
    }

}
