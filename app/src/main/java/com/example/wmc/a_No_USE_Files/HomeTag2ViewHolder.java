package com.example.wmc.a_No_USE_Files;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class HomeTag2ViewHolder extends RecyclerView.ViewHolder{

    TextView cafeName;
    TextView cafeAddress;
    TextView tag1;
    TextView tag2;
    TextView tag3;
    TextView bestReview_example;
    ImageView cafe_image;
    RatingBar rating_all;

    public static int HOMETAG2_VIEW_TYPE = R.layout.item_home_taglist;

    public HomeTag2ViewHolder(@NonNull View itemView, final HomeTag2Adapter.OnItemClickEventListener_HomeTag2 a_itemClickListener) {
        super(itemView);

        cafeName = itemView.findViewById(R.id.cafeName);
        cafeAddress = itemView.findViewById(R.id.cafeAddress);
        tag1 = itemView.findViewById(R.id.tag1);
        tag2 = itemView.findViewById(R.id.tag2);
        tag3 = itemView.findViewById(R.id.tag3);
        bestReview_example = itemView.findViewById(R.id.bestReview_example);
        cafe_image = itemView.findViewById(R.id.cafe_image);
        rating_all = itemView.findViewById(R.id.rating_all);

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
