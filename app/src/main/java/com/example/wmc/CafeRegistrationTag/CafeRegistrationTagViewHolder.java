package com.example.wmc.CafeRegistrationTag;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;
import com.example.wmc.ReviewTag.ReviewTagAdapter;

public class CafeRegistrationTagViewHolder extends RecyclerView.ViewHolder{

    TextView tag_but;

    public static int CAFEREGISTRATIONTAG_VIEW_TYPE = R.layout.item_review_tag;

    public CafeRegistrationTagViewHolder(@NonNull View itemView, final CafeRegistrationTagAdapter.OnItemClickEventListener_CafeRegistrationTag itemClickListener) {
        super(itemView);

        tag_but = itemView.findViewById(R.id.favorite_cafe);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(a_view, position);
                }
            }
        });
    }
}
