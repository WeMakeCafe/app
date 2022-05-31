package com.example.wmc.CafeDetailImageViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.wmc.R;
import com.example.wmc.ui.Fragment.CafeDetailFragment;

import java.util.ArrayList;

public class CafeDetailImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> imageList;
    ImageView imageView;
    CafeDetailFragment cafeDetailFragment;


    public CafeDetailImageViewPagerAdapter(Context context, ArrayList<String> imageList, CafeDetailFragment cafeDetailFragment){
        mContext = context;
        this.imageList = imageList;
        this.cafeDetailFragment = cafeDetailFragment;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_cafe_detail_image1, null);

        imageView = view.findViewById(R.id.cafeDetail_image1);
        //imageView.setImageResource(imageList.get(position));

        String url = imageList.get(position);

        Glide.with(cafeDetailFragment.getActivity()).load(url).into(imageView);

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
