package com.example.wmc.HomeTag1ViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingItem;
import com.example.wmc.HomeTag1.HomeTag1Adapter;
import com.example.wmc.R;

import java.util.ArrayList;

public class HomeTag1ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<HomeTag1ViewPagerItem> tag1_List;

    TextView cafeName;
    TextView cafeAddress;
    TextView tag1;
    TextView tag2;
    TextView tag3;
    TextView bestReview_example;
    ImageView cafe_image;
    RatingBar rating_all;


    public HomeTag1ViewPagerAdapter(Context context, ArrayList<HomeTag1ViewPagerItem> tag1_List){
        mContext = context;
        this.tag1_List = tag1_List;
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
        cafe_image.setImageResource(tag1_List.get(position).getCafeImage());

        rating_all = view.findViewById(R.id.rating_all);
        rating_all.setRating(tag1_List.get(position).getRating());

        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(view.getContext().getApplicationContext(), tag1_List.get(position).getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
            }
        });

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
}
