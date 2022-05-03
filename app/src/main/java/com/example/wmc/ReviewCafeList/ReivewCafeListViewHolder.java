package com.example.wmc.ReviewCafeList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ListCafeList.ListCafeListAdapter;
import com.example.wmc.R;

public class ReivewCafeListViewHolder extends RecyclerView.ViewHolder{

    TextView cafeList_cafe_name_textView;
    TextView cafeList_cafe_address_textView;
    TextView opening_hours;
    TextView cafeList_hashTag1;
    TextView cafeList_hashTag2;
    ImageView cafeList_cafeImage;

    public static int REVIEWCAFELIST_VIEW_TYPE = R.layout.item_list_cafelist;

    public ReivewCafeListViewHolder(@NonNull View itemView, final ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList itemClickListener_reviewcafeList) {
        super(itemView);

        cafeList_cafe_name_textView = itemView.findViewById(R.id.cafeList_cafe_name_textView);
        cafeList_cafe_address_textView = itemView.findViewById(R.id.cafeList_cafe_address_textView);
        opening_hours = itemView.findViewById(R.id.opening_hours);
        cafeList_hashTag1 = itemView.findViewById(R.id.cafeList_hashTag1);
        cafeList_hashTag2 = itemView.findViewById(R.id.cafeList_hashTag2);
        cafeList_cafeImage = itemView.findViewById(R.id.cafeList_cafeImage);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener_reviewcafeList.onItemClick(a_view, position);
                }
            }
        });
    }
}
