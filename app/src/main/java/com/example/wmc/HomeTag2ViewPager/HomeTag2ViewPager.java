package com.example.wmc.HomeTag2ViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class HomeTag2ViewPager extends ViewPager {

    private OnItemClickListener mOnItemClickListener_second;

    public HomeTag2ViewPager(@NonNull Context context) {
        super(context);
        setup();
    }

    public HomeTag2ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        final GestureDetector tapGestureDetector = new    GestureDetector(getContext(), new TapGestureListener());

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    public void setOnItemClickListener_second(OnItemClickListener onItemClickListener) {
        mOnItemClickListener_second = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mOnItemClickListener_second != null) {
                mOnItemClickListener_second.onItemClick(getCurrentItem());
            }
            return true;
        }
    }
}
