package com.example.wmc.ListCafeList;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.HomeTag1.HomeTag1Adapter;
import com.example.wmc.R;

public class ListCafeListViewHolder extends RecyclerView.ViewHolder{

    TextView cafeList_cafe_name_textView;
    TextView cafeList_cafe_address_textView;
    TextView opening_hours;
    TextView cafeList_hashTag1;
    TextView cafeList_hashTag2;
    ImageView cafeList_cafeImage;
    CheckBox favorite_button;

    public static int LISTCAFELIST_VIEW_TYPE = R.layout.item_list_cafelist;

    public ListCafeListViewHolder(@NonNull View itemView, final ListCafeListAdapter.OnItemClickEventListener_ListCafeList itemClickListener_cafeList) {
        super(itemView);

        cafeList_cafe_name_textView = itemView.findViewById(R.id.cafeList_cafe_name_textView);
        cafeList_cafe_address_textView = itemView.findViewById(R.id.cafeList_cafe_address_textView);
        opening_hours = itemView.findViewById(R.id.opening_hours);
        cafeList_hashTag1 = itemView.findViewById(R.id.cafeList_hashTag1);
        cafeList_hashTag2 = itemView.findViewById(R.id.cafeList_hashTag2);
        cafeList_cafeImage = itemView.findViewById(R.id.cafeList_cafeImage);
        favorite_button = itemView.findViewById(R.id.favorite_button);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener_cafeList.onItemClick(a_view, position);
                }
            }
        });
    }
}
