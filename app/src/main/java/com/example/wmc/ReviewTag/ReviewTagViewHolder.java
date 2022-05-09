package com.example.wmc.ReviewTag;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class ReviewTagViewHolder extends RecyclerView.ViewHolder{

    TextView tag_but;

    public static int ReviewTag_VIEW_TYPE = R.layout.item_review_tag;

    public ReviewTagViewHolder(@NonNull View itemView, final ReviewTagAdapter.OnItemClickEventListener_ReviewTag a_itemClickListener) {
        super(itemView);

        tag_but = itemView.findViewById(R.id.favorite_cafe);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    a_itemClickListener.onItemClick(a_view, position);
                }
            }
        });
    }
}
