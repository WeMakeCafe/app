package com.example.wmc.ui.Fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.databinding.FragmentReviewBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private static NavController navController;

    Button review_search_input;
    Button addTag_cafe_button;
    Button comment_button;
    Button location_button;
    Button finish_button;

    String comment ="";
    Long cafeNum;
    Long reviewNum;
    Integer likeCount = 0;

    Integer score1; // ????????? ????????? ?????? ?????? ?????????
    Integer score2;
    Integer score3;
    Integer score4;
    Integer score5;
    Integer score6;
    Integer score7;
    Integer score8;
    Integer score9;
    Integer score10;
    Integer score11;
    Integer score12;


    RatingBar rating_sour;
    RatingBar rating_acerbity;
    RatingBar rating_dessert;
    RatingBar rating_beverage;
    RatingBar rating_twoseat;
    RatingBar rating_fourseat;
    RatingBar rating_manyseat;
    RatingBar rating_toilet;
    RatingBar rating_wifi;
    RatingBar rating_plug;
    RatingBar rating_quiet;
    RatingBar rating_light;

    Float s1;
    Float s2;
    Float s3;
    Float s4;
    Float s5;
    Float s6;
    Float s7;
    Float s8;
    Float s9;
    Float s10;
    Float s11;
    Float s12;

    Long[] k = new Long[36];
    Long[] k2 = new Long[36];
    int t1, t2, t3;
    Long mem_num = MainActivity.mem_num;

    ArrayList<Cafe> cafe_list;
    ArrayList<Review> review_list;

    Boolean flag = false;

    Boolean floating_flag = false;
    Boolean reviewCafeList_flag = false;
    Boolean cafeDetail_reviewModify_flag = false;
    Boolean moreReview_reviewModify_flag = false;
    Boolean mypage_reviewModify_flag = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        review_search_input = root.findViewById(R.id.review_search_input);           // ????????? ?????? ???
        addTag_cafe_button = root.findViewById(R.id.addTag_cafe_button);    // ????????? ?????? ??????
        comment_button = root.findViewById(R.id.comment_button);    // ???????????? ??????
        location_button = root.findViewById(R.id.location_button);  // ???????????? ??????
        finish_button = root.findViewById(R.id.finish_button);  // ???????????? ??????

        rating_sour = root.findViewById(R.id.rating_sour);
        rating_acerbity = root.findViewById(R.id.rating_acerbity);
        rating_dessert = root.findViewById(R.id.rating_dessert);
        rating_beverage = root.findViewById(R.id.rating_beverage);
        rating_twoseat = root.findViewById(R.id.rating_twoseat);
        rating_fourseat = root.findViewById(R.id.rating_fourseat);
        rating_manyseat = root.findViewById(R.id.rating_manyseat);
        rating_toilet = root.findViewById(R.id.rating_toilet);
        rating_wifi = root.findViewById(R.id.rating_wifi);
        rating_plug = root.findViewById(R.id.rating_plug);
        rating_quiet = root.findViewById(R.id.rating_quiet);
        rating_light = root.findViewById(R.id.rating_light);


        // ?????? ?????? ????????? (ReviewTagFragment) ?????? ????????? ????????? ?????? ?????? ?????? ??????
        TextView setTag1 = root.findViewById(R.id.select_tag1); // ?????? ?????? ?????? ??? ????????? ?????? ?????? ???????????? ?????? ??????1
        TextView setTag2 = root.findViewById(R.id.select_tag2); // ?????? ?????? ?????? ??? ????????? ?????? ?????? ???????????? ?????? ??????2
        TextView setTag3 = root.findViewById(R.id.select_tag3); // ?????? ?????? ?????? ??? ????????? ?????? ?????? ???????????? ?????? ??????3

        // ??????????????? ?????? long??? ?????? ????????? ??????
        for(int i = 0 ; i<=35; i++){
            k[i] = (long) 0;
        }

        for(int i = 0 ; i<=35; i++){
            k2[i] = (long) 0;
        }


        // ?????? ?????? ??? ?????? ???,
        review_search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.review_to_review_cafelist);
            }
        });


        // ReviewCafeList?????? ????????? ?????? ?????? ????????????
        Bundle reviewCafeList_Bundle = getArguments();
        if(reviewCafeList_Bundle != null) {
            if (reviewCafeList_Bundle.getBoolean("reviewCafeList_flag")) {
                review_search_input.setText(reviewCafeList_Bundle.getString("reviewCafeList_flag_cafeName"));
                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // ???????????? Bold??????
                review_search_input.setGravity(Gravity.CENTER);          // ?????? ?????? Center??? ??????

                reviewCafeList_flag = reviewCafeList_Bundle.getBoolean("reviewCafeList_flag");
            }
        }


        // ?????? ?????? ?????? ?????? ???,
        addTag_cafe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ?????? ???????????? ????????? ????????? ????????? ???????????? ????????? ??????.
                if(review_search_input.getText().toString().equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }

                // ????????? ????????? ????????? ????????? ??????
                else{

                    if(floating_flag){  // ????????? ????????? Review??? ????????? ??????
                        Bundle bundle = new Bundle();
                        bundle.putString("floating_cafeName", review_search_input.getText().toString());
                        bundle.putBoolean("floating_flag", floating_flag);

                        bundle.putFloat("tastePoint1", rating_sour.getRating());
                        bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                        bundle.putFloat("tastePoint3", rating_dessert.getRating());
                        bundle.putFloat("tastePoint4", rating_beverage.getRating());
                        bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                        bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                        bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                        bundle.putFloat("seatPoint4", rating_toilet.getRating());
                        bundle.putFloat("studyPoint1", rating_wifi.getRating());
                        bundle.putFloat("studyPoint2", rating_plug.getRating());
                        bundle.putFloat("studyPoint3", rating_quiet.getRating());
                        bundle.putFloat("studyPoint4", rating_light.getRating());

                        navController.navigate(R.id.review_to_review_tag, bundle);
                    }

                    else if(reviewCafeList_flag){   // ???????????? ?????????????????? Review??? ????????? ??????
                        Bundle bundle = new Bundle();
                        bundle.putString("reviewCafeList_cafeName", review_search_input.getText().toString());
                        bundle.putBoolean("reviewCafeList_flag", reviewCafeList_flag);

                        bundle.putFloat("tastePoint1", rating_sour.getRating());
                        bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                        bundle.putFloat("tastePoint3", rating_dessert.getRating());
                        bundle.putFloat("tastePoint4", rating_beverage.getRating());
                        bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                        bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                        bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                        bundle.putFloat("seatPoint4", rating_toilet.getRating());
                        bundle.putFloat("studyPoint1", rating_wifi.getRating());
                        bundle.putFloat("studyPoint2", rating_plug.getRating());
                        bundle.putFloat("studyPoint3", rating_quiet.getRating());
                        bundle.putFloat("studyPoint4", rating_light.getRating());

                        navController.navigate(R.id.review_to_review_tag, bundle);
                    }

                    else if(cafeDetail_reviewModify_flag){ // ?????? ????????? ???????????? Review??? ????????? ??????
                        Bundle bundle = new Bundle();

                        bundle.putBoolean("cafeDetail_reviewModify_flag", cafeDetail_reviewModify_flag);

                        bundle.putString("cafeName", review_search_input.getText().toString());
                        bundle.putFloat("tastePoint1", rating_sour.getRating());
                        bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                        bundle.putFloat("tastePoint3", rating_dessert.getRating());
                        bundle.putFloat("tastePoint4", rating_beverage.getRating());
                        bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                        bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                        bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                        bundle.putFloat("seatPoint4", rating_toilet.getRating());
                        bundle.putFloat("studyPoint1", rating_wifi.getRating());
                        bundle.putFloat("studyPoint2", rating_plug.getRating());
                        bundle.putFloat("studyPoint3", rating_quiet.getRating());
                        bundle.putFloat("studyPoint4", rating_light.getRating());
                        bundle.putLong("k2-1", (long) k2[0]);
                        bundle.putLong("k2-2", (long) k2[1]);
                        bundle.putLong("k2-3", (long) k2[2]);
                        bundle.putLong("k2-4", (long) k2[3]);
                        bundle.putLong("k2-5", (long) k2[4]);
                        bundle.putLong("k2-6", (long) k2[5]);
                        bundle.putLong("k2-7", (long) k2[6]);
                        bundle.putLong("k2-8", (long) k2[7]);
                        bundle.putLong("k2-9", (long) k2[8]);
                        bundle.putLong("k2-10",(long)  k2[9]);
                        bundle.putLong("k2-11",(long)  k2[10]);
                        bundle.putLong("k2-12",(long)  k2[11]);
                        bundle.putLong("k2-13", (long) k2[12]);
                        bundle.putLong("k2-14", (long) k2[13]);
                        bundle.putLong("k2-15", (long) k2[14]);
                        bundle.putLong("k2-16", (long) k2[15]);
                        bundle.putLong("k2-17", (long) k2[16]);
                        bundle.putLong("k2-18", (long) k2[17]);
                        bundle.putLong("k2-19", (long) k2[18]);
                        bundle.putLong("k2-20", (long) k2[19]);
                        bundle.putLong("k2-21", (long) k2[20]);
                        bundle.putLong("k2-22", (long) k2[21]);
                        bundle.putLong("k2-23", (long) k2[22]);
                        bundle.putLong("k2-24", (long) k2[23]);
                        bundle.putLong("k2-25", (long) k2[24]);
                        bundle.putLong("k2-26", (long) k2[25]);
                        bundle.putLong("k2-27", (long) k2[26]);
                        bundle.putLong("k2-28", (long) k2[27]);
                        bundle.putLong("k2-29", (long) k2[28]);
                        bundle.putLong("k2-30", (long) k2[29]);
                        bundle.putLong("k2-31", (long) k2[30]);
                        bundle.putLong("k2-32", (long) k2[31]);
                        bundle.putLong("k2-33", (long) k2[32]);
                        bundle.putLong("k2-34", (long) k2[33]);
                        bundle.putLong("k2-35", (long) k2[34]);
                        bundle.putLong("k2-36", (long) k2[35]);
                        bundle.putInt("score1", score1.intValue());
                        bundle.putInt("score2", score2.intValue());
                        bundle.putInt("score3", score3.intValue());
                        bundle.putInt("score4", score4.intValue());
                        bundle.putInt("score5", score5.intValue());
                        bundle.putInt("score6", score6.intValue());
                        bundle.putInt("score7", score7.intValue());
                        bundle.putInt("score8", score8.intValue());
                        bundle.putInt("score9", score9.intValue());
                        bundle.putInt("score10", score10.intValue());
                        bundle.putInt("score11", score11.intValue());
                        bundle.putInt("score12", score12.intValue());

                        bundle.putString("comment", comment);

                        bundle.putBoolean("flag", flag);
                        bundle.putLong("reviewNum", reviewNum);

                        navController.navigate(R.id.review_to_review_tag, bundle);
                    }

                    else if(moreReview_reviewModify_flag){
                        Bundle bundle = new Bundle();

                        bundle.putBoolean("moreReview_reviewModify_flag", moreReview_reviewModify_flag);

                        bundle.putString("cafeName", review_search_input.getText().toString());
                        bundle.putFloat("tastePoint1", rating_sour.getRating());
                        bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                        bundle.putFloat("tastePoint3", rating_dessert.getRating());
                        bundle.putFloat("tastePoint4", rating_beverage.getRating());
                        bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                        bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                        bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                        bundle.putFloat("seatPoint4", rating_toilet.getRating());
                        bundle.putFloat("studyPoint1", rating_wifi.getRating());
                        bundle.putFloat("studyPoint2", rating_plug.getRating());
                        bundle.putFloat("studyPoint3", rating_quiet.getRating());
                        bundle.putFloat("studyPoint4", rating_light.getRating());

                        bundle.putLong("k2-1", (long) k2[0]);
                        bundle.putLong("k2-2", (long) k2[1]);
                        bundle.putLong("k2-3", (long) k2[2]);
                        bundle.putLong("k2-4", (long) k2[3]);
                        bundle.putLong("k2-5", (long) k2[4]);
                        bundle.putLong("k2-6", (long) k2[5]);
                        bundle.putLong("k2-7", (long) k2[6]);
                        bundle.putLong("k2-8", (long) k2[7]);
                        bundle.putLong("k2-9", (long) k2[8]);
                        bundle.putLong("k2-10",(long)  k2[9]);
                        bundle.putLong("k2-11",(long)  k2[10]);
                        bundle.putLong("k2-12",(long)  k2[11]);
                        bundle.putLong("k2-13", (long) k2[12]);
                        bundle.putLong("k2-14", (long) k2[13]);
                        bundle.putLong("k2-15", (long) k2[14]);
                        bundle.putLong("k2-16", (long) k2[15]);
                        bundle.putLong("k2-17", (long) k2[16]);
                        bundle.putLong("k2-18", (long) k2[17]);
                        bundle.putLong("k2-19", (long) k2[18]);
                        bundle.putLong("k2-20", (long) k2[19]);
                        bundle.putLong("k2-21", (long) k2[20]);
                        bundle.putLong("k2-22", (long) k2[21]);
                        bundle.putLong("k2-23", (long) k2[22]);
                        bundle.putLong("k2-24", (long) k2[23]);
                        bundle.putLong("k2-25", (long) k2[24]);
                        bundle.putLong("k2-26", (long) k2[25]);
                        bundle.putLong("k2-27", (long) k2[26]);
                        bundle.putLong("k2-28", (long) k2[27]);
                        bundle.putLong("k2-29", (long) k2[28]);
                        bundle.putLong("k2-30", (long) k2[29]);
                        bundle.putLong("k2-31", (long) k2[30]);
                        bundle.putLong("k2-32", (long) k2[31]);
                        bundle.putLong("k2-33", (long) k2[32]);
                        bundle.putLong("k2-34", (long) k2[33]);
                        bundle.putLong("k2-35", (long) k2[34]);
                        bundle.putLong("k2-36", (long) k2[35]);
                        bundle.putInt("score1", score1.intValue());
                        bundle.putInt("score2", score2.intValue());
                        bundle.putInt("score3", score3.intValue());
                        bundle.putInt("score4", score4.intValue());
                        bundle.putInt("score5", score5.intValue());
                        bundle.putInt("score6", score6.intValue());
                        bundle.putInt("score7", score7.intValue());
                        bundle.putInt("score8", score8.intValue());
                        bundle.putInt("score9", score9.intValue());
                        bundle.putInt("score10", score10.intValue());
                        bundle.putInt("score11", score11.intValue());
                        bundle.putInt("score12", score12.intValue());

                        bundle.putString("comment", comment);

                        bundle.putBoolean("flag", flag);
                        bundle.putLong("reviewNum", reviewNum);

                        Log.d("moreReview_to_tag_review4", String.valueOf(k2[3]));
                        Log.d("moreReview_to_tag_review5", String.valueOf(k2[4]));
                        Log.d("moreReview_to_tag_review6", String.valueOf(k2[5]));
                        Log.d("moreReview_to_tag_review7", String.valueOf(k2[6]));
                        Log.d("moreReview_to_tag_review8", String.valueOf(k2[7]));
                        Log.d("moreReview_to_tag_review9", String.valueOf(k2[8]));
                        navController.navigate(R.id.review_to_review_tag, bundle);
                    }

                    else if (mypage_reviewModify_flag){
                        Bundle bundle = new Bundle();

                        bundle.putBoolean("mypage_reviewModify_flag", mypage_reviewModify_flag);

                        bundle.putString("cafeName", review_search_input.getText().toString());
                        bundle.putFloat("tastePoint1", rating_sour.getRating());
                        bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                        bundle.putFloat("tastePoint3", rating_dessert.getRating());
                        bundle.putFloat("tastePoint4", rating_beverage.getRating());
                        bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                        bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                        bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                        bundle.putFloat("seatPoint4", rating_toilet.getRating());
                        bundle.putFloat("studyPoint1", rating_wifi.getRating());
                        bundle.putFloat("studyPoint2", rating_plug.getRating());
                        bundle.putFloat("studyPoint3", rating_quiet.getRating());
                        bundle.putFloat("studyPoint4", rating_light.getRating());

                        bundle.putLong("k2-1", (long) k2[0]);
                        bundle.putLong("k2-2", (long) k2[1]);
                        bundle.putLong("k2-3", (long) k2[2]);
                        bundle.putLong("k2-4", (long) k2[3]);
                        bundle.putLong("k2-5", (long) k2[4]);
                        bundle.putLong("k2-6", (long) k2[5]);
                        bundle.putLong("k2-7", (long) k2[6]);
                        bundle.putLong("k2-8", (long) k2[7]);
                        bundle.putLong("k2-9", (long) k2[8]);
                        bundle.putLong("k2-10",(long)  k2[9]);
                        bundle.putLong("k2-11",(long)  k2[10]);
                        bundle.putLong("k2-12",(long)  k2[11]);
                        bundle.putLong("k2-13", (long) k2[12]);
                        bundle.putLong("k2-14", (long) k2[13]);
                        bundle.putLong("k2-15", (long) k2[14]);
                        bundle.putLong("k2-16", (long) k2[15]);
                        bundle.putLong("k2-17", (long) k2[16]);
                        bundle.putLong("k2-18", (long) k2[17]);
                        bundle.putLong("k2-19", (long) k2[18]);
                        bundle.putLong("k2-20", (long) k2[19]);
                        bundle.putLong("k2-21", (long) k2[20]);
                        bundle.putLong("k2-22", (long) k2[21]);
                        bundle.putLong("k2-23", (long) k2[22]);
                        bundle.putLong("k2-24", (long) k2[23]);
                        bundle.putLong("k2-25", (long) k2[24]);
                        bundle.putLong("k2-26", (long) k2[25]);
                        bundle.putLong("k2-27", (long) k2[26]);
                        bundle.putLong("k2-28", (long) k2[27]);
                        bundle.putLong("k2-29", (long) k2[28]);
                        bundle.putLong("k2-30", (long) k2[29]);
                        bundle.putLong("k2-31", (long) k2[30]);
                        bundle.putLong("k2-32", (long) k2[31]);
                        bundle.putLong("k2-33", (long) k2[32]);
                        bundle.putLong("k2-34", (long) k2[33]);
                        bundle.putLong("k2-35", (long) k2[34]);
                        bundle.putLong("k2-36", (long) k2[35]);

                        bundle.putInt("score1", score1.intValue());
                        bundle.putInt("score2", score2.intValue());
                        bundle.putInt("score3", score3.intValue());
                        bundle.putInt("score4", score4.intValue());
                        bundle.putInt("score5", score5.intValue());
                        bundle.putInt("score6", score6.intValue());
                        bundle.putInt("score7", score7.intValue());
                        bundle.putInt("score8", score8.intValue());
                        bundle.putInt("score9", score9.intValue());
                        bundle.putInt("score10", score10.intValue());
                        bundle.putInt("score11", score11.intValue());
                        bundle.putInt("score12", score12.intValue());

                        bundle.putString("comment", comment);

                        bundle.putBoolean("flag", flag);
                        bundle.putLong("reviewNum", reviewNum);

                        Log.d("myReview_comment", comment);

                        navController.navigate(R.id.review_to_review_tag, bundle);
                    }
                }
            }
        });


        // ReviewTag?????? ????????? ????????? ?????? ??? ??????????????? ???????????????
        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getBoolean("return_reviewCafeList_flag") || argBundle.getBoolean("return_floating_flag")) {

                review_search_input.setText(argBundle.getString("review_cafeName"));

                setTag1.setText(argBundle.getString("key1"));
                setTag2.setText(argBundle.getString("key2"));
                setTag3.setText(argBundle.getString("key3"));

                rating_sour.setRating(argBundle.getFloat("tag_review_tastePoint1"));
                rating_acerbity.setRating(argBundle.getFloat("tag_review_tastePoint2"));
                rating_dessert.setRating(argBundle.getFloat("tag_review_tastePoint3"));
                rating_beverage.setRating(argBundle.getFloat("tag_review_tastePoint4"));
                rating_twoseat.setRating(argBundle.getFloat("tag_review_seatPoint1"));
                rating_fourseat.setRating(argBundle.getFloat("tag_review_seatPoint2"));
                rating_manyseat.setRating(argBundle.getFloat("tag_review_seatPoint3"));
                rating_toilet.setRating(argBundle.getFloat("tag_review_seatPoint4"));
                rating_wifi.setRating(argBundle.getFloat("tag_review_studyPoint1"));
                rating_plug.setRating(argBundle.getFloat("tag_review_studyPoint2"));
                rating_quiet.setRating(argBundle.getFloat("tag_review_studyPoint3"));
                rating_light.setRating(argBundle.getFloat("tag_review_studyPoint4"));
                s1 = argBundle.getFloat("tag_review_tastePoint1");
                s2 = argBundle.getFloat("tag_review_tastePoint2");
                s3 = argBundle.getFloat("tag_review_tastePoint3");
                s4 = argBundle.getFloat("tag_review_tastePoint4");
                s5 = argBundle.getFloat("tag_review_seatPoint1");
                s6 = argBundle.getFloat("tag_review_seatPoint2");
                s7 = argBundle.getFloat("tag_review_seatPoint3");
                s8 = argBundle.getFloat("tag_review_seatPoint4");
                s9 = argBundle.getFloat("tag_review_studyPoint1");
                s10 = argBundle.getFloat("tag_review_studyPoint2");
                s11 = argBundle.getFloat("tag_review_studyPoint3");
                s12 = argBundle.getFloat("tag_review_studyPoint4");

                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // ???????????? Bold??????
                review_search_input.setGravity(Gravity.CENTER);          // ?????? ?????? Center??? ??????
            }

            else if(argBundle.getBoolean("return_cafeDetail_reviewModify_flag")
                    || argBundle.getBoolean("return_moreReview_reviewModify_flag")
                    || argBundle.getBoolean("return_mypage_reviewModify_flag")) {

                review_search_input.setText(argBundle.getString("review_cafeName"));

                setTag1.setText(argBundle.getString("key1"));
                setTag2.setText(argBundle.getString("key2"));
                setTag3.setText(argBundle.getString("key3"));

                rating_sour.setRating(argBundle.getFloat("tag_review_tastePoint1"));
                rating_acerbity.setRating(argBundle.getFloat("tag_review_tastePoint2"));
                rating_dessert.setRating(argBundle.getFloat("tag_review_tastePoint3"));
                rating_beverage.setRating(argBundle.getFloat("tag_review_tastePoint4"));
                rating_twoseat.setRating(argBundle.getFloat("tag_review_seatPoint1"));
                rating_fourseat.setRating(argBundle.getFloat("tag_review_seatPoint2"));
                rating_manyseat.setRating(argBundle.getFloat("tag_review_seatPoint3"));
                rating_toilet.setRating(argBundle.getFloat("tag_review_seatPoint4"));
                rating_wifi.setRating(argBundle.getFloat("tag_review_studyPoint1"));
                rating_plug.setRating(argBundle.getFloat("tag_review_studyPoint2"));
                rating_quiet.setRating(argBundle.getFloat("tag_review_studyPoint3"));
                rating_light.setRating(argBundle.getFloat("tag_review_studyPoint4"));
                s1 = argBundle.getFloat("tag_review_tastePoint1");
                s2 = argBundle.getFloat("tag_review_tastePoint2");
                s3 = argBundle.getFloat("tag_review_tastePoint3");
                s4 = argBundle.getFloat("tag_review_tastePoint4");
                s5 = argBundle.getFloat("tag_review_seatPoint1");
                s6 = argBundle.getFloat("tag_review_seatPoint2");
                s7 = argBundle.getFloat("tag_review_seatPoint3");
                s8 = argBundle.getFloat("tag_review_seatPoint4");
                s9 = argBundle.getFloat("tag_review_studyPoint1");
                s10 = argBundle.getFloat("tag_review_studyPoint2");
                s11 = argBundle.getFloat("tag_review_studyPoint3");
                s12 = argBundle.getFloat("tag_review_studyPoint4");
                flag = argBundle.getBoolean("flag");
                reviewNum = argBundle.getLong("reviewNum");

                score1 = Integer.valueOf(argBundle.getInt("score1"));
                score2 = Integer.valueOf(argBundle.getInt("score2"));
                score3 = Integer.valueOf(argBundle.getInt("score3"));
                score4 = Integer.valueOf(argBundle.getInt("score4"));
                score5 = Integer.valueOf(argBundle.getInt("score5"));
                score6 = Integer.valueOf(argBundle.getInt("score6"));
                score7 = Integer.valueOf(argBundle.getInt("score7"));
                score8 = Integer.valueOf(argBundle.getInt("score8"));
                score9 = Integer.valueOf(argBundle.getInt("score9"));
                score10 = Integer.valueOf(argBundle.getInt("score10"));
                score11 = Integer.valueOf(argBundle.getInt("score11"));
                score12 = Integer.valueOf(argBundle.getInt("score12"));

                k2[0] = argBundle.getLong("k2-1");
                k2[1] = argBundle.getLong("k2-2");
                k2[2] = argBundle.getLong("k2-3");
                k2[3] = argBundle.getLong("k2-4");
                k2[4] = argBundle.getLong("k2-5");
                k2[5] = argBundle.getLong("k2-6");
                k2[6] = argBundle.getLong("k2-7");
                k2[7] = argBundle.getLong("k2-8");
                k2[8] = argBundle.getLong("k2-9");
                k2[9] = argBundle.getLong("k2-10");
                k2[10] = argBundle.getLong("k2-11");
                k2[11] = argBundle.getLong("k2-12");
                k2[12] = argBundle.getLong("k2-13");
                k2[13] = argBundle.getLong("k2-14");
                k2[14] = argBundle.getLong("k2-15");
                k2[15] = argBundle.getLong("k2-16");
                k2[16] = argBundle.getLong("k2-17");
                k2[17] = argBundle.getLong("k2-18");
                k2[18] = argBundle.getLong("k2-19");
                k2[19] = argBundle.getLong("k2-20");
                k2[20] = argBundle.getLong("k2-21");
                k2[21] = argBundle.getLong("k2-22");
                k2[22] = argBundle.getLong("k2-23");
                k2[23] = argBundle.getLong("k2-24");
                k2[24] = argBundle.getLong("k2-25");
                k2[25] = argBundle.getLong("k2-26");
                k2[26] = argBundle.getLong("k2-27");
                k2[27] = argBundle.getLong("k2-28");
                k2[28] = argBundle.getLong("k2-29");
                k2[29] = argBundle.getLong("k2-30");
                k2[30] = argBundle.getLong("k2-31");
                k2[31] = argBundle.getLong("k2-32");
                k2[32] = argBundle.getLong("k2-33");
                k2[33] = argBundle.getLong("k2-34");
                k2[34] = argBundle.getLong("k2-35");
                k2[35] = argBundle.getLong("k2-36");

                comment = argBundle.getString("comment");
                Log.d("?????? ????????? ?????? comment", comment);
                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // ???????????? Bold??????
                review_search_input.setGravity(Gravity.CENTER);          // ?????? ?????? Center??? ??????
            }
        }


        // ?????? ?????????????????? ?????????????????? ?????? ??????
        Bundle argBundle2 = getArguments(); // ?????? ?????????????????? ??????, ?????? ?????????????????? ??????, ???????????????????????? ???????????? ???????????? ?????? ?????????
        if( argBundle2 != null ) {
            if (argBundle2.getBoolean("cafeDetail_reviewModify_flag") || argBundle2.getBoolean("moreReview_reviewModify_flag")
                                                                            || argBundle2.getBoolean("mypage_reviewModify_flag")) {

                if (argBundle2.getBoolean("cafeDetail_reviewModify_flag")){
                    cafeDetail_reviewModify_flag = argBundle2.getBoolean("cafeDetail_reviewModify_flag");
                }

                else if (argBundle2.getBoolean("moreReview_reviewModify_flag")){
                    moreReview_reviewModify_flag = argBundle2.getBoolean("moreReview_reviewModify_flag");
                }

                else if (argBundle2.getBoolean("mypage_reviewModify_flag")){
                    mypage_reviewModify_flag = argBundle2.getBoolean("mypage_reviewModify_flag");
                }
                flag = true;

                cafeNum = argBundle2.getLong("cafeNum");
                mem_num = argBundle2.getLong("memNum");
                Log.d("qwer1", cafeNum.toString());
                Log.d("qwer2", mem_num.toString());

                RequestQueue requestQueue;
                Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                Network network = new BasicNetwork(new HurlStack());
                requestQueue = new RequestQueue(cache, network);
                requestQueue.start();
                String url = getResources().getString(R.string.url) + "review";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // ???????????? ?????? ??????
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<Review>>() {
                        }.getType();

                        review_list = gson.fromJson(changeString, listType);


                        for(Review r : review_list){
                            if((r.getCafeNum()==cafeNum) && (r.getMemNum() == mem_num)) {

                                reviewNum = r.getReviewNum();
                                Log.d("reviewNum", reviewNum.toString());
                                comment = r.getReviewText();
                                likeCount = r.getLikeCount();
                                //????????? ??????
                                rating_sour.setRating(r.getTastePoint1());
                                rating_acerbity.setRating(r.getTastePoint2());
                                rating_dessert.setRating(r.getTastePoint3());
                                rating_beverage.setRating(r.getTastePoint4());
                                rating_twoseat.setRating(r.getSeatPoint1());
                                rating_fourseat.setRating(r.getSeatPoint2());
                                rating_manyseat.setRating(r.getSeatPoint3());
                                rating_toilet.setRating(r.getSeatPoint4());
                                rating_wifi.setRating(r.getStudyPoint1());
                                rating_plug.setRating(r.getStudyPoint2());
                                rating_quiet.setRating(r.getStudyPoint3());
                                rating_light.setRating(r.getStudyPoint4());

                                score1 = r.getTastePoint1();
                                score2 = r.getTastePoint2();
                                score3 = r.getTastePoint3();
                                score4 = r.getTastePoint4();
                                score5 = r.getSeatPoint1();
                                score6 = r.getSeatPoint2();
                                score7 = r.getSeatPoint3();
                                score8 = r.getSeatPoint4();
                                score9 = r.getStudyPoint1();
                                score10 = r.getStudyPoint2();
                                score11 = r.getStudyPoint3();
                                score12 = r.getStudyPoint4();

                                k2[0] = r.getKeyword1();
                                k2[1] = r.getKeyword2();
                                k2[2] = r.getKeyword3();
                                k2[3] = r.getKeyword4();
                                k2[4] = r.getKeyword5();
                                k2[5] = r.getKeyword6();
                                k2[6] = r.getKeyword7();
                                k2[7] = r.getKeyword8();
                                k2[8] = r.getKeyword9();
                                k2[9] = r.getKeyword10();
                                k2[10] = r.getKeyword11();
                                k2[11] = r.getKeyword12();
                                k2[12] = r.getKeyword13();
                                k2[13] = r.getKeyword14();
                                k2[14] = r.getKeyword15();
                                k2[15] = r.getKeyword16();
                                k2[16] = r.getKeyword17();
                                k2[17] = r.getKeyword18();
                                k2[18] = r.getKeyword19();
                                k2[19] = r.getKeyword20();
                                k2[20] = r.getKeyword21();
                                k2[21] = r.getKeyword22();
                                k2[22] = r.getKeyword23();
                                k2[23] = r.getKeyword24();
                                k2[24] = r.getKeyword25();
                                k2[25] = r.getKeyword26();
                                k2[26] = r.getKeyword27();
                                k2[27] = r.getKeyword28();
                                k2[28] = r.getKeyword29();
                                k2[29] = r.getKeyword30();
                                k2[30] = r.getKeyword31();
                                k2[31] = r.getKeyword32();
                                k2[32] = r.getKeyword33();
                                k2[33] = r.getKeyword34();
                                k2[34] = r.getKeyword35();
                                k2[35] = r.getKeyword36();

                                for(int i=0;i<35;i++) {
                                    if(k2[i]==(long)1) {
                                        t1 = i;
                                        break;
                                    }
                                }
                                for(int i=t1+1;i<35;i++) {
                                    if(k2[i]==(long)1) {
                                        t2 = i;
                                        break;
                                    }
                                }
                                for(int i=t2+1;i<35;i++) {
                                    if(k2[i]==(long)1) {
                                        t3 = i;
                                        break;
                                    }
                                }
                                switch (t1) {
                                    case (0):
                                        setTag1.setText("#??????");
                                        break;
                                    case (1):
                                        setTag1.setText("#??????");
                                        break;
                                    case (2):
                                        setTag1.setText("#??????");
                                        break;
                                    case (3):
                                        setTag1.setText("#??????");
                                        break;
                                    case (4):
                                        setTag1.setText("#??????");
                                        break;
                                    case (5):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (6):
                                        setTag1.setText("#????????????");
                                        break;
                                    case (7):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (8):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (9):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (10):
                                        setTag1.setText("#???????????????");
                                        break;
                                    case (11):
                                        setTag1.setText("#???????????????");
                                        break;
                                    case (12):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (13):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (14):
                                        setTag1.setText("#??????");
                                        break;
                                    case (15):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (16):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (17):
                                        setTag1.setText("#??????");
                                        break;
                                    case (18):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (19):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (20):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (21):
                                        setTag1.setText("#??????");
                                        break;
                                    case (22):
                                        setTag1.setText("#??????");
                                        break;
                                    case (23):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (24):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (25):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (26):
                                        setTag1.setText("#??????");
                                        break;
                                    case (27):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (28):
                                        setTag1.setText("#????????????");
                                        break;
                                    case (29):
                                        setTag1.setText("#????????????");
                                        break;
                                    case (30):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (31):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (32):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (33):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (34):
                                        setTag1.setText("#?????????");
                                        break;
                                    case (35):
                                        setTag1.setText("#????????????");
                                        break;
                                }
                                switch (t2) {
                                    case (0):
                                        setTag2.setText("#??????");
                                        break;
                                    case (1):
                                        setTag2.setText("#??????");
                                        break;
                                    case (2):
                                        setTag2.setText("#??????");
                                        break;
                                    case (3):
                                        setTag2.setText("#??????");
                                        break;
                                    case (4):
                                        setTag2.setText("#??????");
                                        break;
                                    case (5):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (6):
                                        setTag2.setText("#????????????");
                                        break;
                                    case (7):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (8):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (9):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (10):
                                        setTag2.setText("#???????????????");
                                        break;
                                    case (11):
                                        setTag2.setText("#???????????????");
                                        break;
                                    case (12):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (13):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (14):
                                        setTag2.setText("#??????");
                                        break;
                                    case (15):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (16):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (17):
                                        setTag2.setText("#??????");
                                        break;
                                    case (18):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (19):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (20):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (21):
                                        setTag2.setText("#??????");
                                        break;
                                    case (22):
                                        setTag2.setText("#??????");
                                        break;
                                    case (23):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (24):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (25):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (26):
                                        setTag2.setText("#??????");
                                        break;
                                    case (27):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (28):
                                        setTag2.setText("#????????????");
                                        break;
                                    case (29):
                                        setTag2.setText("#????????????");
                                        break;
                                    case (30):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (31):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (32):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (33):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (34):
                                        setTag2.setText("#?????????");
                                        break;
                                    case (35):
                                        setTag2.setText("#????????????");
                                        break;
                                }
                                switch (t3) {
                                    case (0):
                                        setTag3.setText("#??????");
                                        break;
                                    case (1):
                                        setTag3.setText("#??????");
                                        break;
                                    case (2):
                                        setTag3.setText("#??????");
                                        break;
                                    case (3):
                                        setTag3.setText("#??????");
                                        break;
                                    case (4):
                                        setTag3.setText("#??????");
                                        break;
                                    case (5):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (6):
                                        setTag3.setText("#????????????");
                                        break;
                                    case (7):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (8):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (9):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (10):
                                        setTag3.setText("#???????????????");
                                        break;
                                    case (11):
                                        setTag3.setText("#???????????????");
                                        break;
                                    case (12):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (13):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (14):
                                        setTag3.setText("#??????");
                                        break;
                                    case (15):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (16):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (17):
                                        setTag3.setText("#??????");
                                        break;
                                    case (18):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (19):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (20):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (21):
                                        setTag3.setText("#??????");
                                        break;
                                    case (22):
                                        setTag3.setText("#??????");
                                        break;
                                    case (23):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (24):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (25):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (26):
                                        setTag3.setText("#??????");
                                        break;
                                    case (27):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (28):
                                        setTag3.setText("#????????????");
                                        break;
                                    case (29):
                                        setTag3.setText("#????????????");
                                        break;
                                    case (30):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (31):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (32):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (33):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (34):
                                        setTag3.setText("#?????????");
                                        break;
                                    case (35):
                                        setTag3.setText("#????????????");
                                        break;
                                }

                                String url = getResources().getString(R.string.url) + "cafe";

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // ???????????? ?????? ??????
                                        String changeString = new String();
                                        try {
                                            changeString = new String(response.getBytes("8859_1"), "utf-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                        Type listType = new TypeToken<ArrayList<Cafe>>() {
                                        }.getType();

                                        cafe_list = gson.fromJson(changeString, listType);


                                        for(Cafe c : cafe_list){
                                            if(c.getCafeNum()==cafeNum) {
                                                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // ???????????? Bold??????
                                                review_search_input.setGravity(Gravity.CENTER);          // ?????? ?????? Center??? ??????
                                                review_search_input.setText(c.getCafeName());
                                            }
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // ????????? ????????? ??? ????????? ????????? ???????????? ??????
                                        Log.e("test_error", error.toString());
                                    }
                                });
                                requestQueue.add(stringRequest);

                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // ????????? ????????? ??? ????????? ????????? ???????????? ??????
                        Log.e("test_error", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            }
        }


        // ???????????? ?????? ?????? ???,
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????? ????????? ?????? ?????? Toast??? ??????
                Toast.makeText(getContext().getApplicationContext(), "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });


        // ?????? ??????????????? ?????? ??????  ??????????????? ?????? ???, ????????? ?????? ?????? ?????????
        Bundle cafeNameBundle = getArguments();
        if(cafeNameBundle != null) {
            if(cafeNameBundle.getBoolean("floating_flag")){
                floating_flag = cafeNameBundle.getBoolean("floating_flag");

                review_search_input.setText(cafeNameBundle.getString("floating_cafeName"));
                review_search_input.setTypeface(Typeface.DEFAULT_BOLD);  // ???????????? Bold??????
                review_search_input.setGravity(Gravity.CENTER);          // ?????? ?????? Center??? ??????

                setTag1.setText("");
                setTag2.setText("");
                setTag3.setText("");

                rating_sour.setRating(0);
                rating_acerbity.setRating(0);
                rating_dessert.setRating(0);
                rating_beverage.setRating(0);

                rating_twoseat.setRating(0);
                rating_fourseat.setRating(0);
                rating_manyseat.setRating(0);
                rating_toilet.setRating(0);

                rating_wifi.setRating(0);
                rating_plug.setRating(0);
                rating_quiet.setRating(0);
                rating_light.setRating(0);
            }
        }




        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String url = getResources().getString(R.string.url) + "cafe";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // ???????????? ?????? ??????
                String changeString = new String();
                try {
                    changeString = new String(response.getBytes("8859_1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<ArrayList<Cafe>>() {
                }.getType();

                cafe_list = gson.fromJson(changeString, listType);


                for(Cafe c : cafe_list) {
                    if(c.getCafeName().equals(review_search_input.getText().toString())) {
                        cafeNum = c.getCafeNum();
                    }
                }

                // ???????????? ?????? ?????? ???,
                comment_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(rating_sour.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        } else if(rating_acerbity.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_dessert.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_beverage.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_twoseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_fourseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_manyseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_toilet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_wifi.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_plug.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_quiet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_light.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(setTag1.getText().toString().equals("")){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            // ?????? ?????? ?????? ???, ??????????????? ????????? ??????
                            if(flag == false) {
                                Bundle bundle = new Bundle(); // ??????????????? ??? ????????? ?????? ?????? ??????
                                bundle.putString("cafeName", review_search_input.getText().toString());

                                bundle.putString("tag1", setTag1.getText().toString());
                                bundle.putString("tag2", setTag2.getText().toString());
                                bundle.putString("tag3", setTag3.getText().toString());

                                bundle.putFloat("tastePoint1", rating_sour.getRating());
                                bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                                bundle.putFloat("tastePoint3", rating_dessert.getRating());
                                bundle.putFloat("tastePoint4", rating_beverage.getRating());
                                bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                                bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                                bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                                bundle.putFloat("seatPoint4", rating_toilet.getRating());
                                bundle.putFloat("studyPoint1", rating_wifi.getRating());
                                bundle.putFloat("studyPoint2", rating_plug.getRating());
                                bundle.putFloat("studyPoint3", rating_quiet.getRating());
                                bundle.putFloat("studyPoint4", rating_light.getRating());

                                bundle.putLong("cafeNum", cafeNum);
                                bundle.putLong("memNum", mem_num);

                                bundle.putBoolean("flag", flag);
                                //?????????
                                navController.navigate(R.id.review_to_review_comment, bundle);
                            }

                            // ?????? ???????????? ??? ??????,
                            else if(flag == true) {
                                Bundle bundle = new Bundle(); // ??????????????? ??? ????????? ?????? ?????? ??????
                                bundle.putString("cafeName", review_search_input.getText().toString());

                                bundle.putString("tag1", setTag1.getText().toString());
                                bundle.putString("tag2", setTag2.getText().toString());
                                bundle.putString("tag3", setTag3.getText().toString());

                                bundle.putFloat("tastePoint1", rating_sour.getRating());
                                bundle.putFloat("tastePoint2", rating_acerbity.getRating());
                                bundle.putFloat("tastePoint3", rating_dessert.getRating());
                                bundle.putFloat("tastePoint4", rating_beverage.getRating());
                                bundle.putFloat("seatPoint1", rating_twoseat.getRating());
                                bundle.putFloat("seatPoint2", rating_fourseat.getRating());
                                bundle.putFloat("seatPoint3", rating_manyseat.getRating());
                                bundle.putFloat("seatPoint4", rating_toilet.getRating());
                                bundle.putFloat("studyPoint1", rating_wifi.getRating());
                                bundle.putFloat("studyPoint2", rating_plug.getRating());
                                bundle.putFloat("studyPoint3", rating_quiet.getRating());
                                bundle.putFloat("studyPoint4", rating_light.getRating());

                                bundle.putLong("k-1", (long) k[0]);
                                bundle.putLong("k-2", (long) k[1]);
                                bundle.putLong("k-3", (long) k[2]);
                                bundle.putLong("k-4", (long) k[3]);
                                bundle.putLong("k-5", (long) k[4]);
                                bundle.putLong("k-6", (long) k[5]);
                                bundle.putLong("k-7", (long) k[6]);
                                bundle.putLong("k-8", (long) k[7]);
                                bundle.putLong("k-9", (long) k[8]);
                                bundle.putLong("k-10",(long)  k[9]);
                                bundle.putLong("k-11",(long)  k[10]);
                                bundle.putLong("k-12",(long)  k[11]);
                                bundle.putLong("k-13", (long) k[12]);
                                bundle.putLong("k-14", (long) k[13]);
                                bundle.putLong("k-15", (long) k[14]);
                                bundle.putLong("k-16", (long) k[15]);
                                bundle.putLong("k-17", (long) k[16]);
                                bundle.putLong("k-18", (long) k[17]);
                                bundle.putLong("k-19", (long) k[18]);
                                bundle.putLong("k-20", (long) k[19]);
                                bundle.putLong("k-21", (long) k[20]);
                                bundle.putLong("k-22", (long) k[21]);
                                bundle.putLong("k-23", (long) k[22]);
                                bundle.putLong("k-24", (long) k[23]);
                                bundle.putLong("k-25", (long) k[24]);
                                bundle.putLong("k-26", (long) k[25]);
                                bundle.putLong("k-27", (long) k[26]);
                                bundle.putLong("k-28", (long) k[27]);
                                bundle.putLong("k-29", (long) k[28]);
                                bundle.putLong("k-30", (long) k[29]);
                                bundle.putLong("k-31", (long) k[30]);
                                bundle.putLong("k-32", (long) k[31]);
                                bundle.putLong("k-33", (long) k[32]);
                                bundle.putLong("k-34", (long) k[33]);
                                bundle.putLong("k-35", (long) k[34]);
                                bundle.putLong("k-36", (long) k[35]);

                                bundle.putLong("k2-1", (long) k2[0]);
                                bundle.putLong("k2-2", (long) k2[1]);
                                bundle.putLong("k2-3", (long) k2[2]);
                                bundle.putLong("k2-4", (long) k2[3]);
                                bundle.putLong("k2-5", (long) k2[4]);
                                bundle.putLong("k2-6", (long) k2[5]);
                                bundle.putLong("k2-7", (long) k2[6]);
                                bundle.putLong("k2-8", (long) k2[7]);
                                bundle.putLong("k2-9", (long) k2[8]);
                                bundle.putLong("k2-10",(long)  k2[9]);
                                bundle.putLong("k2-11",(long)  k2[10]);
                                bundle.putLong("k2-12",(long)  k2[11]);
                                bundle.putLong("k2-13", (long) k2[12]);
                                bundle.putLong("k2-14", (long) k2[13]);
                                bundle.putLong("k2-15", (long) k2[14]);
                                bundle.putLong("k2-16", (long) k2[15]);
                                bundle.putLong("k2-17", (long) k2[16]);
                                bundle.putLong("k2-18", (long) k2[17]);
                                bundle.putLong("k2-19", (long) k2[18]);
                                bundle.putLong("k2-20", (long) k2[19]);
                                bundle.putLong("k2-21", (long) k2[20]);
                                bundle.putLong("k2-22", (long) k2[21]);
                                bundle.putLong("k2-23", (long) k2[22]);
                                bundle.putLong("k2-24", (long) k2[23]);
                                bundle.putLong("k2-25", (long) k2[24]);
                                bundle.putLong("k2-26", (long) k2[25]);
                                bundle.putLong("k2-27", (long) k2[26]);
                                bundle.putLong("k2-28", (long) k2[27]);
                                bundle.putLong("k2-29", (long) k2[28]);
                                bundle.putLong("k2-30", (long) k2[29]);
                                bundle.putLong("k2-31", (long) k2[30]);
                                bundle.putLong("k2-32", (long) k2[31]);
                                bundle.putLong("k2-33", (long) k2[32]);
                                bundle.putLong("k2-34", (long) k2[33]);
                                bundle.putLong("k2-35", (long) k2[34]);
                                bundle.putLong("k2-36", (long) k2[35]);

                                bundle.putInt("score1", score1.intValue());
                                bundle.putInt("score2", score2.intValue());
                                bundle.putInt("score3", score3.intValue());
                                bundle.putInt("score4", score4.intValue());
                                bundle.putInt("score5", score5.intValue());
                                bundle.putInt("score6", score6.intValue());
                                bundle.putInt("score7", score7.intValue());
                                bundle.putInt("score8", score8.intValue());
                                bundle.putInt("score9", score9.intValue());
                                bundle.putInt("score10", score10.intValue());
                                bundle.putInt("score11", score11.intValue());
                                bundle.putInt("score12", score12.intValue());

                                bundle.putLong("cafeNum", cafeNum);
                                bundle.putLong("memNum", mem_num);
                                bundle.putLong("reviewNum", reviewNum);

                                bundle.putString("comment", comment);

                                bundle.putInt("likeCount", likeCount.intValue()); // likecount??? integer??????
                                bundle.putBoolean("flag", flag);

                                Log.d("??????_to_comment -> flag", String.valueOf(flag));
                                Log.d("??????_to_comment -> keyWord4", String.valueOf(k2[3]));
                                Log.d("??????_to_comment -> keyWord5", String.valueOf(k2[4]));
                                Log.d("??????_to_comment -> keyWord6", String.valueOf(k2[5]));
                                Log.d("??????_to_comment -> keyWord7", String.valueOf(k2[6]));
                                Log.d("??????_to_comment -> keyWord8", String.valueOf(k2[7]));
                                Log.d("??????_to_comment -> keyWord9", String.valueOf(k2[8]));
                                //?????????
                                navController.navigate(R.id.review_to_review_comment, bundle);
                            }
                        }
                    }
                });

                Log.d("qwer4", mem_num.toString());

                // ???????????? ?????? ?????? ???,
                finish_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ?????? ????????? ????????? CafeDetailFragment??? ??????
                        // ?????? ????????? ???????????????, ????????? ???????????? Toast???????????? ??????
                        if(rating_sour.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        } else if(rating_acerbity.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_dessert.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_beverage.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_twoseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_fourseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_manyseat.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_toilet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_wifi.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_plug.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_quiet.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(rating_light.getRating() == 0){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }else if(setTag1.getText().toString().equals("")){
                            Toast.makeText(getContext().getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            // ?????? ?????? ?????? ???,(?????? X)
                            if(flag == false) {
                                Map map = new HashMap();
                                map.put("tastePoint1", Integer.valueOf((int) rating_sour.getRating()));
                                map.put("tastePoint2", Integer.valueOf((int) rating_acerbity.getRating()));
                                map.put("tastePoint3", Integer.valueOf((int) rating_dessert.getRating()));
                                map.put("tastePoint4", Integer.valueOf((int) rating_beverage.getRating()));
                                map.put("seatPoint1", Integer.valueOf((int) rating_twoseat.getRating()));
                                map.put("seatPoint2", Integer.valueOf((int) rating_fourseat.getRating()));
                                map.put("seatPoint3", Integer.valueOf((int) rating_manyseat.getRating()));
                                map.put("seatPoint4", Integer.valueOf((int) rating_toilet.getRating()));
                                map.put("studyPoint1", Integer.valueOf((int) rating_wifi.getRating()));
                                map.put("studyPoint2", Integer.valueOf((int) rating_plug.getRating()));
                                map.put("studyPoint3", Integer.valueOf((int) rating_quiet.getRating()));
                                map.put("studyPoint4", Integer.valueOf((int) rating_light.getRating()));
                                map.put("cafeNum", cafeNum);
                                map.put("likeCount", 0);
                                map.put("reviewText", null);
                                map.put("memNum", mem_num);
                                Log.d("qwer3", mem_num.toString());
                                switch (setTag1.getText().toString()) {
                                    case ("#??????"):
                                        k[0] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[1] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[2] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[3] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[4] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[5] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[6] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[7] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[8] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[9] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[10] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[11] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[12] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[13] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[14] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[15] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[16] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[17] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[18] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[19] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[20] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[21] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[22] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[23] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[24] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[25] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[26] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[27] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[28] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[29] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[30] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[31] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[32] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[33] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[34] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[35] = Long.valueOf(1);
                                        break;
                                }

                                switch (setTag2.getText().toString()) {
                                    case ("#??????"):
                                        k[0] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[1] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[2] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[3] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[4] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[5] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[6] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[7] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[8] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[9] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[10] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[11] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[12] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[13] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[14] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[15] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[16] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[17] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[18] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[19] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[20] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[21] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[22] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[23] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[24] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[25] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[26] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[27] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[28] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[29] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[30] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[31] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[32] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[33] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[34] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[35] = Long.valueOf(1);
                                        break;
                                }

                                switch (setTag3.getText().toString()) {
                                    case ("#??????"):
                                        k[0] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[1] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[2] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[3] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[4] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[5] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[6] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[7] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[8] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[9] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[10] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[11] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[12] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[13] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[14] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[15] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[16] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[17] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[18] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[19] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[20] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[21] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[22] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[23] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[24] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[25] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[26] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[27] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[28] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[29] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[30] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[31] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[32] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[33] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[34] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[35] = Long.valueOf(1);
                                        break;
                                }
                                map.put("keyword1", k[0]);
                                map.put("keyword2", k[1]);
                                map.put("keyword3", k[2]);
                                map.put("keyword4", k[3]);
                                map.put("keyword5", k[4]);
                                map.put("keyword6", k[5]);
                                map.put("keyword7", k[6]);
                                map.put("keyword8", k[7]);
                                map.put("keyword9", k[8]);
                                map.put("keyword10", k[9]);
                                map.put("keyword11", k[10]);
                                map.put("keyword12", k[11]);
                                map.put("keyword13", k[12]);
                                map.put("keyword14", k[13]);
                                map.put("keyword15", k[14]);
                                map.put("keyword16", k[15]);
                                map.put("keyword17", k[16]);
                                map.put("keyword18", k[17]);
                                map.put("keyword19", k[18]);
                                map.put("keyword20", k[19]);
                                map.put("keyword21", k[20]);
                                map.put("keyword22", k[21]);
                                map.put("keyword23", k[22]);
                                map.put("keyword24", k[23]);
                                map.put("keyword25", k[24]);
                                map.put("keyword26", k[25]);
                                map.put("keyword27", k[26]);
                                map.put("keyword28", k[27]);
                                map.put("keyword29", k[28]);
                                map.put("keyword30", k[29]);
                                map.put("keyword31", k[30]);
                                map.put("keyword32", k[31]);
                                map.put("keyword33", k[32]);
                                map.put("keyword34", k[33]);
                                map.put("keyword35", k[34]);
                                map.put("keyword36", k[35]);


                                String url2 = getResources().getString(R.string.url) + "review";
                                JSONObject jsonObject = new JSONObject(map);
                                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("test1", error.toString());
                                            }
                                        }) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=UTF-8";
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(requireContext());
                                queue.add(objectRequest);

                                // ????????? cafePut??????????????? ???
                                for (Cafe c : cafe_list) {
                                    if (c.getCafeNum().equals(cafeNum)) {  //bundle?????? ????????? ?????????????????? cafe_name??? ????????? ?????? ??????
                                        Map map2 = new HashMap();
                                        map2.put("cafeName", c.getCafeName());
                                        map2.put("cafeAddress", c.getCafeAddress());
                                        map2.put("openTime", c.getOpenTime());
                                        map2.put("closeTime", c.getCloseTime());
                                        map2.put("scoreNum", c.getScoreNum());
                                        map2.put("keyword1", c.getKeyword1() + k[0]);
                                        map2.put("keyword2", c.getKeyword2() + k[1]);
                                        map2.put("keyword3", c.getKeyword3() + k[2]);
                                        map2.put("keyword4", c.getKeyword4() + k[3]);
                                        map2.put("keyword5", c.getKeyword5() + k[4]);
                                        map2.put("keyword6", c.getKeyword6() + k[5]);
                                        map2.put("keyword7", c.getKeyword7() + k[6]);
                                        map2.put("keyword8", c.getKeyword8() + k[7]);
                                        map2.put("keyword9", c.getKeyword9() + k[8]);
                                        map2.put("keyword10", c.getKeyword10() + k[9]);
                                        map2.put("keyword11", c.getKeyword11() + k[10]);
                                        map2.put("keyword12", c.getKeyword12() + k[11]);
                                        map2.put("keyword13", c.getKeyword13() + k[12]);
                                        map2.put("keyword14", c.getKeyword14() + k[13]);
                                        map2.put("keyword15", c.getKeyword15() + k[14]);
                                        map2.put("keyword16", c.getKeyword16() + k[15]);
                                        map2.put("keyword17", c.getKeyword17() + k[16]);
                                        map2.put("keyword18", c.getKeyword18() + k[17]);
                                        map2.put("keyword19", c.getKeyword19() + k[18]);
                                        map2.put("keyword20", c.getKeyword20() + k[19]);
                                        map2.put("keyword21", c.getKeyword21() + k[20]);
                                        map2.put("keyword22", c.getKeyword22() + k[21]);
                                        map2.put("keyword23", c.getKeyword23() + k[22]);
                                        map2.put("keyword24", c.getKeyword24() + k[23]);
                                        map2.put("keyword25", c.getKeyword25() + k[24]);
                                        map2.put("keyword26", c.getKeyword26() + k[25]);
                                        map2.put("keyword27", c.getKeyword27() + k[26]);
                                        map2.put("keyword28", c.getKeyword28() + k[27]);
                                        map2.put("keyword29", c.getKeyword29() + k[28]);
                                        map2.put("keyword30", c.getKeyword30() + k[29]);
                                        map2.put("keyword31", c.getKeyword31() + k[30]);
                                        map2.put("keyword32", c.getKeyword32() + k[31]);
                                        map2.put("keyword33", c.getKeyword33() + k[32]);
                                        map2.put("keyword34", c.getKeyword34() + k[33]);
                                        map2.put("keyword35", c.getKeyword35() + k[34]);
                                        map2.put("keyword36", c.getKeyword36() + k[35]);
                                        map2.put("tastePoint1", c.getTastePoint1() + Integer.valueOf((int) rating_sour.getRating()));
                                        map2.put("tastePoint2", c.getTastePoint2() + Integer.valueOf((int) rating_acerbity.getRating()));
                                        map2.put("tastePoint3", c.getTastePoint3() + Integer.valueOf((int) rating_dessert.getRating()));
                                        map2.put("tastePoint4", c.getTastePoint4() + Integer.valueOf((int) rating_beverage.getRating()));
                                        map2.put("seatPoint1", c.getSeatPoint1() + Integer.valueOf((int) rating_twoseat.getRating()));
                                        map2.put("seatPoint2", c.getSeatPoint2() + Integer.valueOf((int) rating_fourseat.getRating()));
                                        map2.put("seatPoint3", c.getSeatPoint3() + Integer.valueOf((int) rating_manyseat.getRating()));
                                        map2.put("seatPoint4", c.getSeatPoint4() + Integer.valueOf((int) rating_toilet.getRating()));
                                        map2.put("studyPoint1", c.getStudyPoint1() + Integer.valueOf((int) rating_wifi.getRating()));
                                        map2.put("studyPoint2", c.getStudyPoint2() + Integer.valueOf((int) rating_plug.getRating()));
                                        map2.put("studyPoint3", c.getStudyPoint3() + Integer.valueOf((int) rating_quiet.getRating()));
                                        map2.put("studyPoint4", c.getStudyPoint4() + Integer.valueOf((int) rating_light.getRating()));


                                        JSONObject jsonObject2 = new JSONObject(map2);

                                        String url3 = getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // ?????? ???????????? ????????? ???????????? ??????

                                        JsonObjectRequest objectRequest2 = new JsonObjectRequest(Request.Method.PUT, url3, jsonObject2,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("error", error.toString());
                                                    }
                                                }) {
                                            @Override
                                            public String getBodyContentType() {
                                                return "application/json; charset=UTF-8";
                                            }
                                        };
                                        RequestQueue queue2 = Volley.newRequestQueue(requireContext());
                                        queue2.add(objectRequest2);

                                    }
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("cafeName", review_search_input.getText().toString());
                                navController.navigate(R.id.review_to_cafe_detail, bundle);

                            }
                            else if (flag == true) {
                                Map map = new HashMap();
                                map.put("tastePoint1", Integer.valueOf((int) rating_sour.getRating()));
                                map.put("tastePoint2", Integer.valueOf((int) rating_acerbity.getRating()));
                                map.put("tastePoint3", Integer.valueOf((int) rating_dessert.getRating()));
                                map.put("tastePoint4", Integer.valueOf((int) rating_beverage.getRating()));
                                map.put("seatPoint1", Integer.valueOf((int) rating_twoseat.getRating()));
                                map.put("seatPoint2", Integer.valueOf((int) rating_fourseat.getRating()));
                                map.put("seatPoint3", Integer.valueOf((int) rating_manyseat.getRating()));
                                map.put("seatPoint4", Integer.valueOf((int) rating_toilet.getRating()));
                                map.put("studyPoint1", Integer.valueOf((int) rating_wifi.getRating()));
                                map.put("studyPoint2", Integer.valueOf((int) rating_plug.getRating()));
                                map.put("studyPoint3", Integer.valueOf((int) rating_quiet.getRating()));
                                map.put("studyPoint4", Integer.valueOf((int) rating_light.getRating()));
                                map.put("cafeNum", cafeNum);
                                map.put("likeCount", likeCount);
                                map.put("memNum", mem_num);
                                switch (setTag1.getText().toString()) {
                                    case ("#??????"):
                                        k[0] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[1] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[2] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[3] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[4] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[5] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[6] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[7] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[8] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[9] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[10] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[11] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[12] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[13] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[14] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[15] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[16] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[17] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[18] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[19] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[20] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[21] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[22] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[23] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[24] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[25] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[26] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[27] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[28] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[29] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[30] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[31] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[32] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[33] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[34] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[35] = Long.valueOf(1);
                                        break;
                                }

                                switch (setTag2.getText().toString()) {
                                    case ("#??????"):
                                        k[0] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[1] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[2] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[3] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[4] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[5] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[6] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[7] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[8] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[9] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[10] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[11] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[12] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[13] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[14] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[15] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[16] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[17] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[18] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[19] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[20] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[21] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[22] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[23] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[24] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[25] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[26] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[27] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[28] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[29] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[30] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[31] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[32] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[33] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[34] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[35] = Long.valueOf(1);
                                        break;
                                }

                                switch (setTag3.getText().toString()) {
                                    case ("#??????"):
                                        k[0] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[1] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[2] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[3] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[4] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[5] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[6] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[7] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[8] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[9] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[10] = Long.valueOf(1);
                                        break;
                                    case ("#???????????????"):
                                        k[11] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[12] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[13] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[14] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[15] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[16] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[17] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[18] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[19] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[20] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[21] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[22] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[23] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[24] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[25] = Long.valueOf(1);
                                        break;
                                    case ("#??????"):
                                        k[26] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[27] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[28] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[29] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[30] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[31] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[32] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[33] = Long.valueOf(1);
                                        break;
                                    case ("#?????????"):
                                        k[34] = Long.valueOf(1);
                                        break;
                                    case ("#????????????"):
                                        k[35] = Long.valueOf(1);
                                        break;
                                }
                                map.put("keyword1", k[0]);
                                map.put("keyword2", k[1]);
                                map.put("keyword3", k[2]);
                                map.put("keyword4", k[3]);
                                map.put("keyword5", k[4]);
                                map.put("keyword6", k[5]);
                                map.put("keyword7", k[6]);
                                map.put("keyword8", k[7]);
                                map.put("keyword9", k[8]);
                                map.put("keyword10", k[9]);
                                map.put("keyword11", k[10]);
                                map.put("keyword12", k[11]);
                                map.put("keyword13", k[12]);
                                map.put("keyword14", k[13]);
                                map.put("keyword15", k[14]);
                                map.put("keyword16", k[15]);
                                map.put("keyword17", k[16]);
                                map.put("keyword18", k[17]);
                                map.put("keyword19", k[18]);
                                map.put("keyword20", k[19]);
                                map.put("keyword21", k[20]);
                                map.put("keyword22", k[21]);
                                map.put("keyword23", k[22]);
                                map.put("keyword24", k[23]);
                                map.put("keyword25", k[24]);
                                map.put("keyword26", k[25]);
                                map.put("keyword27", k[26]);
                                map.put("keyword28", k[27]);
                                map.put("keyword29", k[28]);
                                map.put("keyword30", k[29]);
                                map.put("keyword31", k[30]);
                                map.put("keyword32", k[31]);
                                map.put("keyword33", k[32]);
                                map.put("keyword34", k[33]);
                                map.put("keyword35", k[34]);
                                map.put("keyword36", k[35]);

                                String url2 = getResources().getString(R.string.url) + "review/"+ reviewNum.toString();
                                JSONObject jsonObject = new JSONObject(map);
                                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url2, jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("test3", error.toString());
                                            }
                                        }) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=UTF-8";
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(requireContext());
                                queue.add(objectRequest);

                                // ????????? cafePut??????????????? ???
                                for (Cafe c : cafe_list) {
                                    if (c.getCafeNum().equals(cafeNum)) {  //bundle?????? ????????? ?????????????????? cafe_name??? ????????? ?????? ??????
                                        Map map2 = new HashMap();
                                        map2.put("cafeName", c.getCafeName());
                                        map2.put("cafeAddress", c.getCafeAddress());
                                        map2.put("openTime", c.getOpenTime());
                                        map2.put("closeTime", c.getCloseTime());
                                        map2.put("scoreNum", c.getScoreNum());
                                        Log.d("asdf1", String.valueOf(k[0]));
                                        Log.d("asdf2", String.valueOf(k2[0]));
                                        map2.put("keyword1", c.getKeyword1() + k[0]-k2[0]);
                                        map2.put("keyword2", c.getKeyword2() + k[1]-k2[1]);
                                        map2.put("keyword3", c.getKeyword3() + k[2]-k2[2]);
                                        map2.put("keyword4", c.getKeyword4() + k[3]-k2[3]);
                                        map2.put("keyword5", c.getKeyword5() + k[4]-k2[4]);
                                        map2.put("keyword6", c.getKeyword6() + k[5]-k2[5]);
                                        map2.put("keyword7", c.getKeyword7() + k[6]-k2[6]);
                                        map2.put("keyword8", c.getKeyword8() + k[7]-k2[7]);
                                        map2.put("keyword9", c.getKeyword9() + k[8]-k2[8]);
                                        map2.put("keyword10", c.getKeyword10() + k[9]-k2[9]);
                                        map2.put("keyword11", c.getKeyword11() + k[10]-k2[10]);
                                        map2.put("keyword12", c.getKeyword12() + k[11]-k2[11]);
                                        map2.put("keyword13", c.getKeyword13() + k[12]-k2[12]);
                                        map2.put("keyword14", c.getKeyword14() + k[13]-k2[13]);
                                        map2.put("keyword15", c.getKeyword15() + k[14]-k2[14]);
                                        map2.put("keyword16", c.getKeyword16() + k[15]-k2[15]);
                                        map2.put("keyword17", c.getKeyword17() + k[16]-k2[16]);
                                        map2.put("keyword18", c.getKeyword18() + k[17]-k2[17]);
                                        map2.put("keyword19", c.getKeyword19() + k[18]-k2[18]);
                                        map2.put("keyword20", c.getKeyword20() + k[19]-k2[19]);
                                        map2.put("keyword21", c.getKeyword21() + k[20]-k2[20]);
                                        map2.put("keyword22", c.getKeyword22() + k[21]-k2[21]);
                                        map2.put("keyword23", c.getKeyword23() + k[22]-k2[22]);
                                        map2.put("keyword24", c.getKeyword24() + k[23]-k2[23]);
                                        map2.put("keyword25", c.getKeyword25() + k[24]-k2[24]);
                                        map2.put("keyword26", c.getKeyword26() + k[25]-k2[25]);
                                        map2.put("keyword27", c.getKeyword27() + k[26]-k2[26]);
                                        map2.put("keyword28", c.getKeyword28() + k[27]-k2[27]);
                                        map2.put("keyword29", c.getKeyword29() + k[28]-k2[28]);
                                        map2.put("keyword30", c.getKeyword30() + k[29]-k2[29]);
                                        map2.put("keyword31", c.getKeyword31() + k[30]-k2[30]);
                                        map2.put("keyword32", c.getKeyword32() + k[31]-k2[31]);
                                        map2.put("keyword33", c.getKeyword33() + k[32]-k2[32]);
                                        map2.put("keyword34", c.getKeyword34() + k[33]-k2[33]);
                                        map2.put("keyword35", c.getKeyword35() + k[34]-k2[34]);
                                        map2.put("keyword36", c.getKeyword36() + k[35]-k2[35]);
                                        map2.put("tastePoint1", c.getTastePoint1() + Integer.valueOf((int) rating_sour.getRating())-score1);
                                        map2.put("tastePoint2", c.getTastePoint2() + Integer.valueOf((int) rating_acerbity.getRating())-score2);
                                        map2.put("tastePoint3", c.getTastePoint3() + Integer.valueOf((int) rating_dessert.getRating())-score3);
                                        map2.put("tastePoint4", c.getTastePoint4() + Integer.valueOf((int) rating_beverage.getRating())-score4);
                                        map2.put("seatPoint1", c.getSeatPoint1() + Integer.valueOf((int) rating_twoseat.getRating())-score5);
                                        map2.put("seatPoint2", c.getSeatPoint2() + Integer.valueOf((int) rating_fourseat.getRating())-score6);
                                        map2.put("seatPoint3", c.getSeatPoint3() + Integer.valueOf((int) rating_manyseat.getRating())-score7);
                                        map2.put("seatPoint4", c.getSeatPoint4() + Integer.valueOf((int) rating_toilet.getRating())-score8);
                                        map2.put("studyPoint1", c.getStudyPoint1() + Integer.valueOf((int) rating_wifi.getRating())-score9);
                                        map2.put("studyPoint2", c.getStudyPoint2() + Integer.valueOf((int) rating_plug.getRating())-score10);
                                        map2.put("studyPoint3", c.getStudyPoint3() + Integer.valueOf((int) rating_quiet.getRating())-score11);
                                        map2.put("studyPoint4", c.getStudyPoint4() + Integer.valueOf((int) rating_light.getRating())-score12);


                                        JSONObject jsonObject2 = new JSONObject(map2);

                                        String url3 = getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // ?????? ???????????? ????????? ???????????? ??????

                                        JsonObjectRequest objectRequest2 = new JsonObjectRequest(Request.Method.PUT, url3, jsonObject2,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("error", error.toString());
                                                    }
                                                }) {
                                            @Override
                                            public String getBodyContentType() {
                                                return "application/json; charset=UTF-8";
                                            }
                                        };
                                        RequestQueue queue2 = Volley.newRequestQueue(requireContext());
                                        queue2.add(objectRequest2);

                                    }
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("cafeName", review_search_input.getText().toString());
                                navController.navigate(R.id.review_to_cafe_detail, bundle);
                            }
                        }
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // ????????? ????????? ??? ????????? ????????? ???????????? ??????
                Log.e("test_error", error.toString());
            }
        });
        requestQueue.add(stringRequest);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}