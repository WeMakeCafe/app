package com.example.wmc.MypageFavorite;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class MypageFavoriteViewHolder extends RecyclerView.ViewHolder{

    TextView favorite_but;

    public static int MYPAGEFAVORITE_VIEW_TYPE = R.layout.item_mypage_bookmark;

    public MypageFavoriteViewHolder(@NonNull View itemView, final MypageFavoriteAdapter.OnItemClickEventListener_MypageFavorite a_itemClickListener) {
        super(itemView);

        favorite_but = itemView.findViewById(R.id.favorite_cafe);

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
