package com.example.wmc.CafeModify;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.R;

public class CafeModifyViewHolder extends RecyclerView.ViewHolder{

    ImageView modifyImage;
    Button imageDeleteButton;

    CafeModifyViewHolder(View itemView) {
        super(itemView) ;

        // 뷰 객체에 대한 참조.
        modifyImage = itemView.findViewById(R.id.add_modify_imageView);
        imageDeleteButton = itemView.findViewById(R.id.imageDeleteButton);
    }

}
