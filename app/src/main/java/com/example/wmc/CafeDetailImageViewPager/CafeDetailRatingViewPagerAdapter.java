package com.example.wmc.CafeDetailImageViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.wmc.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CafeDetailRatingViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<CafeDetailRatingItem> ratingList;

    TextView viewPager_seat_textView;
    ImageView viewPager_seat_image;
    TextView viewPager_rating1;
    TextView viewPager_rating2;
    TextView viewPager_rating3;
    TextView viewPager_rating4;

    RatingBar viewPager_rating1_score;
    RatingBar viewPager_rating2_score;
    RatingBar viewPager_rating3_score;
    RatingBar viewPager_rating4_score;


    public CafeDetailRatingViewPagerAdapter(Context context, ArrayList<CafeDetailRatingItem> ratingList){
        mContext = context;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_rating_taste, null);

        viewPager_seat_textView = view.findViewById(R.id.viewPager_tasteTextView);
        viewPager_seat_textView.setText(ratingList.get(position).getRatingName());
        viewPager_seat_image = view.findViewById(R.id.viewPager_taste_image);
        viewPager_seat_image.setImageResource(ratingList.get(position).getRatingImage());

        viewPager_rating1 = view.findViewById(R.id.viewPager_sour);
        viewPager_rating1.setText(ratingList.get(position).rating1);
        viewPager_rating2 = view.findViewById(R.id.viewPager_acerbity);
        viewPager_rating2.setText(ratingList.get(position).rating2);
        viewPager_rating3 = view.findViewById(R.id.viewPager_dessert);
        viewPager_rating3.setText(ratingList.get(position).rating3);
        viewPager_rating4 = view.findViewById(R.id.viewPager_beverage);
        viewPager_rating4.setText(ratingList.get(position).rating4);

        viewPager_rating1_score = view.findViewById(R.id.viewPager_rating_sour);
        viewPager_rating1_score.setRating(Integer.parseInt(ratingList.get(position).rating1_score));
        viewPager_rating2_score = view.findViewById(R.id.viewPager_rating_acerbity);
        viewPager_rating2_score.setRating(Integer.parseInt(ratingList.get(position).rating2_score));
        viewPager_rating3_score = view.findViewById(R.id.viewPager_rating_dessert);
        viewPager_rating3_score.setRating(Integer.parseInt(ratingList.get(position).rating3_score));
        viewPager_rating4_score = view.findViewById(R.id.viewPager_rating_beverage);
        viewPager_rating4_score.setRating(Integer.parseInt(ratingList.get(position).rating4_score));

        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return ratingList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
