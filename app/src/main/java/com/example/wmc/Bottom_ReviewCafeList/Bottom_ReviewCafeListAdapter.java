package com.example.wmc.Bottom_ReviewCafeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wmc.ListCafeList.ListCafeListItem;
import com.example.wmc.ui.Fragment.Bottom_ReviewCafeListFragment;
import com.example.wmc.ui.Fragment.ListCafelistFragment;

import java.util.ArrayList;

public class Bottom_ReviewCafeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Bottom_ReviewCafeListItem> bottom_reviewCafeListItems;
    private Context context;
    Bottom_ReviewCafeListFragment bottom_reviewCafeListFragment;

    public interface OnItemClickEventListener_ReviewCafeList { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private Bottom_ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList mItemClickListener_ReviewCafeList;    // 인터페이스 객체 생성

    public Bottom_ReviewCafeListAdapter(Context context, ArrayList<Bottom_ReviewCafeListItem> list, Bottom_ReviewCafeListFragment bottom_reviewCafeListFragment)
    {
        this.context = context;
        bottom_reviewCafeListItems = list;
        this.bottom_reviewCafeListFragment = bottom_reviewCafeListFragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new Bottom_ReviewCafeListViewHolder(view, mItemClickListener_ReviewCafeList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Bottom_ReviewCafeListItem item = bottom_reviewCafeListItems.get(position);
        Bottom_ReviewCafeListViewHolder viewHolder = (Bottom_ReviewCafeListViewHolder) holder;

        viewHolder.cafeList_cafe_name_textView.setText(item.getCafeList_cafeName());
        viewHolder.cafeList_cafe_address_textView.setText(item.getCafeList_cafeAddress());
        viewHolder.opening_hours.setText(item.getOpenTime());
        viewHolder.cafeList_hashTag1.setText(item.getTag1());
        viewHolder.cafeList_hashTag2.setText(item.getTag2());

        Glide.with(bottom_reviewCafeListFragment.getActivity()).load(item.getCafeList_cafeImage()).into(viewHolder.cafeList_cafeImage);

        viewHolder.check_user_flag = item.getCheck_user_flag();

        if(viewHolder.check_user_flag){
            viewHolder.favorite_button.setChecked(true);
        }
        else{
            viewHolder.favorite_button.setChecked(false);
        }

        // 즐겨찾기 버튼 클릭 시,
        viewHolder.favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();    // 즐겨찾기가 됐는지 확인

                if(checked) {
                    // 즐겨찾기 항목에 추가함
                    Toast.makeText(view.getContext().getApplicationContext(), "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 즐겨찾기 항목에서 제거됨
                    Toast.makeText(view.getContext().getApplicationContext(), "즐겨찾기 삭제", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bottom_reviewCafeListItems.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return Bottom_ReviewCafeListViewHolder.REVIEWCAFELIST_VIEW_TYPE;
    }

    public void setOnItemClickListener_ReviewCafeList(Bottom_ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList listener) {
        mItemClickListener_ReviewCafeList = listener;
    }
}
