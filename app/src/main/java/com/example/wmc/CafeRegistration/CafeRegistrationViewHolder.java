package com.example.wmc.CafeRegistration;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.R;

public class CafeRegistrationViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    Button imageDeleteButton;

    CafeRegistrationViewHolder(View itemView) {
        super(itemView) ;

        // 뷰 객체에 대한 참조.
        image = itemView.findViewById(R.id.add_modify_imageView);
        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
    }

//    ImageView add_modify_imageView;
//    Button imageDeleteButton;
//
//    public static int CAFEREGISTRATION_VIEW_TYPE = R.layout.item_add_image;
//
//    public CafeRegistrationViewHolder(@NonNull View itemView, final CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration a_itemClickListener) {
//        super(itemView);
//
//        add_modify_imageView = itemView.findViewById(R.id.add_modify_imageView);
//        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View a_view) {
//                final int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    a_itemClickListener.onItemClick(a_view, position);
//                }
//            }
//        });
//    }
}
