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

//    private ArrayList<CafeRegistrationItem> registrationImage_items;
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
                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                mData.remove(image_uri);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


//    public interface OnItemClickEventListener_CafeRegistration { // 클릭 이벤트를 위한 인터페이스
//        void onItemClick(View a_view, int a_position);
//    }
//    private CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration mItemClickListener_CafeRegistration;    // 인터페이스 객체 생성
//
//    // 생성자
//    public CafeRegistrationAdapter(ArrayList<CafeRegistrationItem> list){
//        registrationImage_items = list;
//    }

//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
//        final RecyclerView.ViewHolder viewHolder;
//        viewHolder = new CafeRegistrationViewHolder(view, mItemClickListener_CafeRegistration);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            // 기본적으로 header 를 빼고 item 을 구한다.
//        final CafeRegistrationItem item = registrationImage_items.get(position);
//        CafeRegistrationViewHolder viewHolder = (CafeRegistrationViewHolder) holder;
//
//        viewHolder.add_modify_imageView.setImageResource(item.getRegistrationImage());
//
//        // 이미지 삭제 버튼(X) 클릭 시,
//        viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
//                registrationImage_items.remove(item);
//                notifyDataSetChanged();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return registrationImage_items.size();
//    }
//
//    @Override
//    public int getItemViewType(int a_position) {
//        return CafeRegistrationViewHolder.CAFEREGISTRATION_VIEW_TYPE;
//    }
//
//    public void setOnItemClickListener_CafeRegistration(CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration a_listener) {
//        mItemClickListener_CafeRegistration = a_listener;
//    }
}
