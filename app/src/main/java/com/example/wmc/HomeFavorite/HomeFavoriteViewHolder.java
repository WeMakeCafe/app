package com.example.wmc.HomeFavorite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.R;

public class HomeFavoriteViewHolder extends RecyclerView.ViewHolder{

    TextView cafeName_textView;
    TextView cafeTag1_textView;
    TextView cafeTag2_textView;
    ImageView cafe_image;

    public static int HOMEFAVORITE_VIEW_TYPE = R.layout.item_home_favoritelist;

    public HomeFavoriteViewHolder(@NonNull View itemView, final HomeFavoriteAdapter.OnItemClickEventListener_HomeFavorite a_itemClickListener) {
        super(itemView);

        cafeName_textView = itemView.findViewById(R.id.cafeName_textView);
        cafeTag1_textView = itemView.findViewById(R.id.cafeTag1_textView);
        cafeTag2_textView = itemView.findViewById(R.id.cafeTag2_textView);
        cafe_image = itemView.findViewById(R.id.cafe_image);

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
