package com.example.wmc.CafeRegistration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeModify.CafeModifyViewHolder;

import java.util.ArrayList;

public class CafeRegistrationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CafeRegistrationItem> registrationImage_items;

    public interface OnItemClickEventListener_CafeRegistration { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration mItemClickListener_CafeRegistration;    // 인터페이스 객체 생성

    public CafeRegistrationAdapter(ArrayList<CafeRegistrationItem> list){
        registrationImage_items = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new CafeRegistrationViewHolder(view, mItemClickListener_CafeRegistration);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            // 기본적으로 header 를 빼고 item 을 구한다.
        final CafeRegistrationItem item = registrationImage_items.get(position);
        CafeRegistrationViewHolder viewHolder = (CafeRegistrationViewHolder) holder;

        viewHolder.add_modify_imageView.setImageResource(item.getRegistrationImage());


        // 이미지 삭제 버튼(X) 클릭 시,
        viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                registrationImage_items.remove(item);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return registrationImage_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return CafeRegistrationViewHolder.CAFEREGISTRATION_VIEW_TYPE;
    }

    public void setOnItemClickListener_CafeRegistration(CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration a_listener) {
        mItemClickListener_CafeRegistration = a_listener;
    }
}
