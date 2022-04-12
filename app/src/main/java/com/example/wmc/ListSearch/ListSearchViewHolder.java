package com.example.wmc.ListSearch;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class ListSearchViewHolder extends RecyclerView.ViewHolder {

    TextView cafeNameTextView;
    TextView cafeAddressTextView;
    Button recentDelete_Button;

    public static int VIEW_TYPE = R.layout.item_list_recent_searches;

    public ListSearchViewHolder(@NonNull View itemView, final ListSearchAdapter.OnItemClickEventListener a_itemClickListener) {
        super(itemView);

        cafeNameTextView = itemView.findViewById(R.id.cafe_name_textView);
        cafeAddressTextView = itemView.findViewById(R.id.cafe_address_textView);

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
