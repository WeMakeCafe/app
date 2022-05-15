package com.example.wmc.CafeDetailImageViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.wmc.R;

import java.util.ArrayList;

public class CafeDetailImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<Integer> imageList;
    ImageView imageView;


    public CafeDetailImageViewPagerAdapter(Context context, ArrayList<Integer> imageList){
        mContext = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_cafe_detail_image1, null);

        imageView = view.findViewById(R.id.cafeDetail_image1);
        imageView.setImageResource(imageList.get(position));

        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
