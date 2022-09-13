package com.example.wmc.CafeModify;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wmc.R;

import java.util.ArrayList;

public class CafeModifyAdapter extends RecyclerView.Adapter<CafeModifyViewHolder>{

    private ArrayList<Uri> modifyData = null;
    private Context modifyContext = null;

    public CafeModifyAdapter(ArrayList<Uri> list, Context context) {
        modifyData = list ;
        modifyContext = context;
    }


    @NonNull
    @Override
    public CafeModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_add_image, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        CafeModifyViewHolder viewHolder = new CafeModifyViewHolder(view) ;

        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull CafeModifyViewHolder holder, int position) {
        Uri image_uri = modifyData.get(position) ;

        Glide.with(modifyContext)
                .load(image_uri)
                .into(holder.modifyImage);


        // 이미지 삭제 버튼(X) 클릭 시,
        holder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                modifyData.remove(image_uri);
                //이미지 DELETE 코드 추가



                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modifyData.size();
    }

//    private ArrayList<CafeModifyItem> modifyImage_items;
//
//    public interface OnItemClickEventListener_CafeModify { // 클릭 이벤트를 위한 인터페이스
//        void onItemClick(View a_view, int a_position);
//    }
//    private CafeModifyAdapter.OnItemClickEventListener_CafeModify mItemClickListener_CafeModify;    // 인터페이스 객체 생성
//
//    public CafeModifyAdapter(ArrayList<CafeModifyItem> list){
//        modifyImage_items = list;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
//        final RecyclerView.ViewHolder viewHolder;
//        viewHolder = new CafeModifyViewHolder(view, mItemClickListener_CafeModify);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            // 기본적으로 header 를 빼고 item 을 구한다.
//            final CafeModifyItem item = modifyImage_items.get(position);
//            CafeModifyViewHolder viewHolder = (CafeModifyViewHolder) holder;
//
//            viewHolder.add_modify_imageView.setImageResource(item.getModifyImage());
//
//            // 카페 수정에서 이미지 삭제 버튼(X) 클릭 시,
//            viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
//                    modifyImage_items.remove(item);
//                    notifyDataSetChanged();
//                }
//            });
//    }
//
//    @Override
//    public int getItemCount() {
//        return modifyImage_items.size();
//    }
//
//    @Override
//    public int getItemViewType(int a_position) {
//        return CafeModifyViewHolder.CAFEMODIFY_VIEW_TYPE;
//    }
//
//    public void setOnItemClickListener_CafeModify(CafeModifyAdapter.OnItemClickEventListener_CafeModify a_listener) {
//        mItemClickListener_CafeModify = a_listener;
//    }
}
