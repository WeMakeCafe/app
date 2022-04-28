package com.example.wmc.CafeModify;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.HomeTag1.HomeTag1Adapter;
import com.example.wmc.R;

public class CafeModifyViewHolder extends RecyclerView.ViewHolder{

    ImageView add_modify_imageView;

    public static int CAFEMODIFY_VIEW_TYPE = R.layout.item_add_image;

    public CafeModifyViewHolder(@NonNull View itemView, final CafeModifyAdapter.OnItemClickEventListener_CafeModify a_itemClickListener) {
        super(itemView);

        add_modify_imageView = itemView.findViewById(R.id.add_modify_imageView);

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
