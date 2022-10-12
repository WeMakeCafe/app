package com.example.wmc.HomeTag1ViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.wmc.R;
import com.example.wmc.ui.Fragment.HomeFragment;

import java.util.ArrayList;

public class HomeTag1ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<HomeTag1ViewPagerItem> tag1_List;
    HomeFragment homeFragment;

    TextView cafeName;
    TextView cafeAddress;
    TextView tag1;
    TextView tag2;
    TextView tag3;
    TextView bestReview_example;
    ImageView cafe_image;
    RatingBar rating_all;


    public HomeTag1ViewPagerAdapter(Context context, ArrayList<HomeTag1ViewPagerItem> tag1_List, HomeFragment homeFragment){
        mContext = context;
        this.tag1_List = tag1_List;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_home_taglist, null);

        cafeName = view.findViewById(R.id.cafeName);
        cafeName.setText(tag1_List.get(position).getCafeName());
        cafeAddress = view.findViewById(R.id.cafeAddress);
        cafeAddress.setText(tag1_List.get(position).getCafeAddress());

        tag1 = view.findViewById(R.id.tag1);
        tag1.setText(tag1_List.get(position).getTag1());
        tag2 = view.findViewById(R.id.tag2);
        tag2.setText(tag1_List.get(position).getTag2());
        tag3 = view.findViewById(R.id.tag3);
        tag3.setText(tag1_List.get(position).getTag3());

        bestReview_example = view.findViewById(R.id.bestReview_example);
        bestReview_example.setText(tag1_List.get(position).getReview());

        cafe_image = view.findViewById(R.id.cafe_image);
        Glide.with(homeFragment.getActivity()).load(tag1_List.get(position).getCafeImage()).into(cafe_image);

        rating_all = view.findViewById(R.id.rating_all);
        rating_all.setRating(tag1_List.get(position).getRating());

        container.addView(view);

        return view;
    }


    @Override
    public int getCount() {
        return tag1_List.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    //java.lang.UnsupportedOperationException 에러 해결용 코드 - 송상화
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        ((ViewPager) container).removeView((View) object);
    }
}
