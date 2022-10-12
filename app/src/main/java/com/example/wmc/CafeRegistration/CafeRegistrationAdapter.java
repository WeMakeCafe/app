package com.example.wmc.CafeRegistration;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeModify.CafeModifyViewHolder;
import com.example.wmc.R;

import java.util.ArrayList;

public class CafeRegistrationAdapter extends RecyclerView.Adapter<CafeRegistrationViewHolder>{

    private ArrayList<Uri> mData = null;
    private Context mContext = null;

    public CafeRegistrationAdapter(ArrayList<Uri> list, Context context) {
        mData = list ;
        mContext = context;
    }


    @NonNull
    @Override
    public CafeRegistrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_add_image, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        CafeRegistrationViewHolder vh = new CafeRegistrationViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull CafeRegistrationViewHolder holder, int position) {

        Uri image_uri = mData.get(position) ;

        Glide.with(mContext)
                .load(image_uri)
                .into(holder.image);


        // 이미지 삭제 버튼(X) 클릭 시,
        holder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(image_uri);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
