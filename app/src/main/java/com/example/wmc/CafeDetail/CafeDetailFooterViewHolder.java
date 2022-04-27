package com.example.wmc.CafeDetail;

import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wmc.R;

public class CafeDetailFooterViewHolder extends RecyclerView.ViewHolder{

    public static int MORE_VIEW_TYPE = R.layout.recyclerview_cafe_detail_footer;

    public CafeDetailFooterViewHolder(@NonNull View itemView, final CafeDetailAdapter.OnItemClickEventListener_cafeDetail a_itemClickListener) {
        super(itemView);

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
