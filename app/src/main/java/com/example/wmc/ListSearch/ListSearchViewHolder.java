package com.example.wmc.ListSearch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetailMore.CafeDetailMoreAdapter;
import com.example.wmc.R;

public class ListSearchViewHolder extends RecyclerView.ViewHolder{

    TextView cafe_name_textView;
    TextView cafe_address_textView;

    public static int LISTSEARCH_VIEW_TYPE = R.layout.item_list_recent_searches;

    public ListSearchViewHolder(@NonNull View itemView, final ListSearchAdapter.OnItemClickEventListener_listSearch itemClickListener) {
        super(itemView);

        cafe_name_textView = itemView.findViewById(R.id.cafe_name_textView);
        cafe_address_textView = itemView.findViewById(R.id.cafe_address_textView);

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
