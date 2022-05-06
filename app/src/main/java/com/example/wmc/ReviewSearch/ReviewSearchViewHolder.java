package com.example.wmc.ReviewSearch;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ListSearch.ListSearchAdapter;
import com.example.wmc.R;

public class ReviewSearchViewHolder extends RecyclerView.ViewHolder{

    TextView cafe_name_textView;
    TextView cafe_address_textView;
    Button recentDelete_Button; // 최근검색의 X 버튼(ReviewSearch)

    public static int REVIEWSEARCH_VIEW_TYPE = R.layout.item_list_recent_searches;

    public ReviewSearchViewHolder(@NonNull View itemView, final ReviewSearchAdapter.OnItemClickEventListener_reviewSearch reivewItemClickListener) {
        super(itemView);

        cafe_name_textView = itemView.findViewById(R.id.cafe_name_textView);
        cafe_address_textView = itemView.findViewById(R.id.cafe_address_textView);
        recentDelete_Button = itemView.findViewById(R.id.recentDelete_Button); // 최근검색의 X버튼(ReviewSearch)

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    reivewItemClickListener.onItemClick(a_view, position);
                }
            }
        });
    }

}
