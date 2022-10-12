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

}
