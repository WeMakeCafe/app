package com.example.wmc.ui.Fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.bumptech.glide.Glide;
import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailImageViewPagerAdapter;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingViewPagerAdapter;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.CafeImage;
import com.example.wmc.database.Category;
import com.example.wmc.database.Love;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.databinding.FragmentCafeDetailBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CafeDetailFragment extends Fragment {

    private FragmentCafeDetailBinding binding;
    private static NavController navController;
    Button cafe_modify_button;
    FloatingActionButton review_floatingButton;
    CheckBox favorite_checkbox;
    ViewPager cafeImageViewPager;
    ViewPager cafeRatingViewPager;
    Button cafeDetail_favorite_previousButton;
    Button cafeDetail_favorite_nextButton;
    TextView moreReview2; // ????????? ?????? ?????? ID
    TextView moreReview3; // ?????? ?????? ?????? ?????? ID
    TextView moreReview4; // ?????? ?????? ?????? ?????? ID
    TextView moreReview10; // ?????? ?????? ?????? ?????? ?????????
    TextView moreReview8; // ?????? ?????? ?????? ?????? ?????????
    TextView moreReview5; // ?????? ?????????
    TextView moreReview6; // ?????? ?????? 1
    TextView moreReview7; // ?????? ?????? 2
    RecyclerView recyclerView;

    ArrayList<String> imageList;   // ?????? ????????? 5?????? ???????????? ArrayList
    ArrayList<CafeDetailRatingItem> ratingList; // ?????? ????????? ???????????? ArrayList

    ArrayList<Cafe> cafe_list;
    ArrayList<Review> review_list;
    ArrayList<Personal> personal_list;
    ArrayList<Bookmark> bookmark_list;
    ArrayList<Love> love_list;
    ArrayList<CafeImage> CafeImage_list;

    Long mem_num = MainActivity.mem_num;

    String cafe_name; // Bundle??? ?????? ????????? cafe_name??? ????????? ?????????
    String create_date;    // ?????? ?????? ??????
    Long get_cafe_num; // cafe_num??? ????????? ?????????.
    Long get_bookmark_num; // bookmark_num??? ????????? ?????????.

    Long[] get_keyword = new Long[36]; // ?????? ?????? ?????? ??????

    // ????????? ????????? ?????? ?????? ?????? ??????
    int[] get_seat_point = new int[4];
    int[] get_study_point = new int[4];
    int[] get_taste_point = new int[4];

    // ?????? ?????? ?????? ????????? ?????? ?????? ??????(?????????, ???)
    int get_seat_point_total = 0;
    int get_study_point_total = 0;
    int get_taste_point_total = 0;

    int point_counter = 0; // ????????? ??? ??? ?????????????????? count?????? ?????? ?????? ?????? ?????? ?????????
    boolean love_flag = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cafe_modify_button = root.findViewById(R.id.cafe_modify_button);
        moreReview3 = root.findViewById(R.id.moreReview3);
        moreReview4 = root.findViewById(R.id.moreReview4);
        review_floatingButton = root.findViewById(R.id.review_floatingButton);
        favorite_checkbox = root.findViewById(R.id.favorite_checkbox);
        cafeDetail_favorite_previousButton = root.findViewById(R.id.cafeDetail_favorite_previousButton);
        cafeDetail_favorite_nextButton = root.findViewById(R.id.cafeDetail_favorite_nextButton);
        moreReview2 = root.findViewById(R.id.moreReview2); // ????????? ?????? ?????? ID
        moreReview3 = root.findViewById(R.id.moreReview3); // ?????? ?????? ?????? ?????? ID
        moreReview4 = root.findViewById(R.id.moreReview4); // ?????? ?????? ?????? ?????? ID
        moreReview10 = root.findViewById(R.id.moreReview10); // ?????? ?????? ?????? ?????? ?????????
        moreReview8 = root.findViewById(R.id.moreReview8); // ?????? ?????? ?????? ?????? ?????????
        moreReview5 = root.findViewById(R.id.moreReview5); // ?????? ?????????
        moreReview6 = root.findViewById(R.id.moreReview6); // ?????? ?????? 1
        moreReview7 = root.findViewById(R.id.moreReview7); // ?????? ?????? 2
        recyclerView = root.findViewById(R.id.cafeDetailReviewRecyclerView);


        // cafeDetail ???????????? Bundle??? ?????? ????????????
        Bundle cafeNameBundle = getArguments();
        if(cafeNameBundle != null){
            if(cafeNameBundle.getString("cafeName") != null){
                cafe_name = cafeNameBundle.getString("cafeName");
            }
        }


        //?????? ??????
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        String get_cafe_url = getResources().getString(R.string.url) + "cafe";

        StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                // ???????????? ?????? ??????
                String changeString = new String();
                try {
                    changeString = new String(response.getBytes("8859_1"),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<ArrayList<Cafe>>(){}.getType();

                cafe_list = gson.fromJson(changeString, listType);


                ///////////////////////////////////////////////////////////////////////////////////////////
                // cafeDetail ?????? ??????
                for(Cafe c : cafe_list){
                    if(c.getCafeName().equals(cafe_name)){
                        get_cafe_num = c.getCafeNum();
                        Log.d("check_cafe_num", get_cafe_num.toString()); // ?????? ????????? ????????? ??????????????? ??????

                        moreReview2.setText(c.getCafeName());
                        moreReview3.setText(c.getCafeName());
                        moreReview4.setText(c.getCafeAddress());
                        moreReview10.setText(c.getOpenTime().substring(0,2)+":"+c.getOpenTime().substring(2,4));
                        moreReview8.setText(c.getCloseTime().substring(0,2)+":"+c.getCloseTime().substring(2,4));

                        /////////////////////////////////////////////////////////////////////////////////////////
                        // ?????? ?????? 1, 2 ????????? ??????
                        get_keyword[0] = c.getKeyword1();
                        get_keyword[1] = c.getKeyword2();
                        get_keyword[2] = c.getKeyword3();
                        get_keyword[3] = c.getKeyword4();
                        get_keyword[4] = c.getKeyword5();
                        get_keyword[5] = c.getKeyword6();
                        get_keyword[6] = c.getKeyword7();
                        get_keyword[7] = c.getKeyword8();
                        get_keyword[8] = c.getKeyword9();
                        get_keyword[9] = c.getKeyword10();
                        get_keyword[10] = c.getKeyword11();
                        get_keyword[11] = c.getKeyword12();
                        get_keyword[12] = c.getKeyword13();
                        get_keyword[13] = c.getKeyword14();
                        get_keyword[14] = c.getKeyword15();
                        get_keyword[15] = c.getKeyword16();
                        get_keyword[16] = c.getKeyword17();
                        get_keyword[17] = c.getKeyword18();
                        get_keyword[18] = c.getKeyword19();
                        get_keyword[19] = c.getKeyword20();
                        get_keyword[20] = c.getKeyword21();
                        get_keyword[21] = c.getKeyword22();
                        get_keyword[22] = c.getKeyword23();
                        get_keyword[23] = c.getKeyword24();
                        get_keyword[24] = c.getKeyword25();
                        get_keyword[25] = c.getKeyword26();
                        get_keyword[26] = c.getKeyword27();
                        get_keyword[27] = c.getKeyword28();
                        get_keyword[28] = c.getKeyword29();
                        get_keyword[29] = c.getKeyword30();
                        get_keyword[30] = c.getKeyword31();
                        get_keyword[31] = c.getKeyword32();
                        get_keyword[32] = c.getKeyword33();
                        get_keyword[33] = c.getKeyword34();
                        get_keyword[34] = c.getKeyword35();
                        get_keyword[35] = c.getKeyword36();


                        // keyword ????????? ?????? ?????? count??? ?????? 1, 2 ?????????.
                        Long Max = get_keyword[0];
                        Long secondMax = 0L;
                        int counter_max = 0;
                        int counter_second = 1;
                        for(int i = 1; i < 36; i++){
                            secondMax = get_keyword[i];
                            if(Max <= secondMax){
                                secondMax = Max;
                                counter_second = counter_max;

                                Max = get_keyword[i];
                                counter_max = i;
                            }
                        }

                        // ?????? ????????? ????????? ?????????
                        Log.d("show_Max_and_secondMax", Max.toString() + ", " + secondMax.toString());

                        // ?????? 1 ??????
                        switch (counter_max){
                            case 0:
                                moreReview6.setText("#??????");
                                break;
                            case 1:
                                moreReview6.setText("#??????");
                                break;
                            case 2:
                                moreReview6.setText("#??????");
                                break;
                            case 3:
                                moreReview6.setText("#??????");
                                break;
                            case 4:
                                moreReview6.setText("#??????");
                                break;
                            case 5:
                                moreReview6.setText("#?????????");
                                break;
                            case 6:
                                moreReview6.setText("#????????????");
                                break;
                            case 7:
                                moreReview6.setText("#?????????");
                                break;
                            case 8:
                                moreReview6.setText("#?????????");
                                break;
                            case 9:
                                moreReview6.setText("#?????????");
                                break;
                            case 10:
                                moreReview6.setText("#???????????????");
                                break;
                            case 11:
                                moreReview6.setText("#???????????????");
                                break;
                            case 12:
                                moreReview6.setText("#?????????");
                                break;
                            case 13:
                                moreReview6.setText("#?????????");
                                break;
                            case 14:
                                moreReview6.setText("#??????");
                                break;
                            case 15:
                                moreReview6.setText("#?????????");
                                break;
                            case 16:
                                moreReview6.setText("#?????????");
                                break;
                            case 17:
                                moreReview6.setText("#??????");
                                break;
                            case 18:
                                moreReview6.setText("#?????????");
                                break;
                            case 19:
                                moreReview6.setText("#?????????");
                                break;
                            case 20:
                                moreReview6.setText("#?????????");
                                break;
                            case 21:
                                moreReview6.setText("#??????");
                                break;
                            case 22:
                                moreReview6.setText("#??????");
                                break;
                            case 23:
                                moreReview6.setText("#?????????");
                                break;
                            case 24:
                                moreReview6.setText("#?????????");
                                break;
                            case 25:
                                moreReview6.setText("#?????????");
                                break;
                            case 26:
                                moreReview6.setText("#??????");
                                break;
                            case 27:
                                moreReview6.setText("#?????????");
                                break;
                            case 28:
                                moreReview6.setText("#????????????");
                                break;
                            case 29:
                                moreReview6.setText("#????????????");
                                break;
                            case 30:
                                moreReview6.setText("#?????????");
                                break;
                            case 31:
                                moreReview6.setText("#?????????");
                                break;
                            case 32:
                                moreReview6.setText("#?????????");
                                break;
                            case 33:
                                moreReview6.setText("#?????????");
                                break;
                            case 34:
                                moreReview6.setText("#?????????");
                                break;
                            case 35:
                                moreReview6.setText("#????????????");
                                break;

                        }

                        //?????? 2 ??????
                        switch (counter_second){
                            case 0:
                                moreReview7.setText("#??????");
                                break;
                            case 1:
                                moreReview7.setText("#??????");
                                break;
                            case 2:
                                moreReview7.setText("#??????");
                                break;
                            case 3:
                                moreReview7.setText("#??????");
                                break;
                            case 4:
                                moreReview7.setText("#??????");
                                break;
                            case 5:
                                moreReview7.setText("#?????????");
                                break;
                            case 6:
                                moreReview7.setText("#????????????");
                                break;
                            case 7:
                                moreReview7.setText("#?????????");
                                break;
                            case 8:
                                moreReview7.setText("#?????????");
                                break;
                            case 9:
                                moreReview7.setText("#?????????");
                                break;
                            case 10:
                                moreReview7.setText("#???????????????");
                                break;
                            case 11:
                                moreReview7.setText("#???????????????");
                                break;
                            case 12:
                                moreReview7.setText("#?????????");
                                break;
                            case 13:
                                moreReview7.setText("#?????????");
                                break;
                            case 14:
                                moreReview7.setText("#??????");
                                break;
                            case 15:
                                moreReview7.setText("#?????????");
                                break;
                            case 16:
                                moreReview7.setText("#?????????");
                                break;
                            case 17:
                                moreReview7.setText("#??????");
                                break;
                            case 18:
                                moreReview7.setText("#?????????");
                                break;
                            case 19:
                                moreReview7.setText("#?????????");
                                break;
                            case 20:
                                moreReview7.setText("#?????????");
                                break;
                            case 21:
                                moreReview7.setText("#??????");
                                break;
                            case 22:
                                moreReview7.setText("#??????");
                                break;
                            case 23:
                                moreReview7.setText("#?????????");
                                break;
                            case 24:
                                moreReview7.setText("#?????????");
                                break;
                            case 25:
                                moreReview7.setText("#?????????");
                                break;
                            case 26:
                                moreReview7.setText("#??????");
                                break;
                            case 27:
                                moreReview7.setText("#?????????");
                                break;
                            case 28:
                                moreReview7.setText("#????????????");
                                break;
                            case 29:
                                moreReview7.setText("#????????????");
                                break;
                            case 30:
                                moreReview7.setText("#?????????");
                                break;
                            case 31:
                                moreReview7.setText("#?????????");
                                break;
                            case 32:
                                moreReview7.setText("#?????????");
                                break;
                            case 33:
                                moreReview7.setText("#?????????");
                                break;
                            case 34:
                                moreReview7.setText("#?????????");
                                break;
                            case 35:
                                moreReview7.setText("#????????????");
                                break;

                        }

                        // ?????? ?????? ??????
                        get_seat_point[0] = c.getSeatPoint1();
                        get_seat_point[1] = c.getSeatPoint2();
                        get_seat_point[2] = c.getSeatPoint3();
                        get_seat_point[3] = c.getSeatPoint4();

                        get_study_point[0] = c.getStudyPoint1();
                        get_study_point[1] = c.getStudyPoint2();
                        get_study_point[2] = c.getStudyPoint3();
                        get_study_point[3] = c.getStudyPoint4();

                        get_taste_point[0] = c.getTastePoint1();
                        get_taste_point[1] = c.getTastePoint2();
                        get_taste_point[2] = c.getTastePoint3();
                        get_taste_point[3] = c.getTastePoint4();
                    }
                }


                ///////////////////////////////////////////////////////////////////////////////////////////////////
                // ?????? ????????? ?????? ?????? ??? ??????, ??????
                String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";

                // ?????? ????????? ?????? ??????
                StringRequest bookmark_stringRequest = new StringRequest(Request.Method.GET, get_bookmark_url, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        // ???????????? ?????? ??????
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<Bookmark>>(){}.getType();

                        bookmark_list = gson.fromJson(changeString, listType);

                        for(Bookmark b : bookmark_list){
                            // "???????????? mem_num??? ???????????? mem_num??? ?????? && ???????????? cafe_num??? cafeDetail??? cafe_num??? ??????"??? ??????
                            if(b.getMemNum().equals(mem_num) && b.getCafeNum().equals(get_cafe_num)){
                                get_bookmark_num = b.getBookmarkNum(); // bookmark_num ?????? ??????
                                favorite_checkbox.setChecked(true); // ???????????? ?????? true ??????
                            }
                        }

                        // ???????????? ??????(???) ?????? ???,
                        favorite_checkbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean checked = ((CheckBox) view).isChecked();    // ??????????????? ????????? ??????

                                if(checked) {
                                    // ???????????? ????????? ?????????

                                    Map map = new HashMap();
                                    map.put("cafeNum", get_cafe_num);
                                    map.put("memNum", mem_num);

                                    JSONObject bookmark_jsonObject = new JSONObject(map);
                                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, get_bookmark_url, bookmark_jsonObject,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // ????????? ?????? ??????, ????????? ?????????.
                                                    Toast.makeText(getContext().getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("bookmark_jsonObject_error", error.toString());
                                                }
                                            }) {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    Log.d("json", bookmark_jsonObject.toString());
                                    RequestQueue queue = Volley.newRequestQueue(requireContext());
                                    queue.add(objectRequest);
                                }
                                else {
                                    // ???????????? ???????????? ?????????
                                    String bookmark_delete_url = getResources().getString(R.string.url) + "bookmark/" + get_bookmark_num.toString();
                                    Log.e("bookmark_num", get_bookmark_num.toString());
                                    StringRequest bookmark_delete_stringRequest = new StringRequest(Request.Method.DELETE, bookmark_delete_url, new Response.Listener<String>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onResponse(String response) {
                                            // ????????? ?????? ??????, ????????? ?????????.
                                            Toast.makeText(getContext().getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("bookmark_delete_stringRequest_error",error.toString());
                                        }
                                    });


                                    requestQueue.add(bookmark_delete_stringRequest);
                                }
                            }
                        });

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("bookmark_stringRequest_error",error.toString());
                    }
                });


                requestQueue.add(bookmark_stringRequest);


                ///////////////////////////////////////////////////////////////////////////////////////////
                // review ?????????
                String get_review_url = getResources().getString(R.string.url) + "review";

                StringRequest review_stringRequest = new StringRequest(Request.Method.GET, get_review_url, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        // ???????????? ?????? ??????
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<Review>>(){}.getType();

                        review_list = gson.fromJson(changeString, listType);


                        ////////////////////////////////////////////////////////////////////////////////////////////
                        //?????? ???????????? ?????? ????????????
                        String get_personal_url = getResources().getString(R.string.url) + "personal";

                        StringRequest personal_stringRequest = new StringRequest(Request.Method.GET, get_personal_url, new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(String response) {
                                // ???????????? ?????? ??????
                                String changeString = new String();
                                try {
                                    changeString = new String(response.getBytes("8859_1"),"utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                Type listType = new TypeToken<ArrayList<Personal>>(){}.getType();

                                personal_list = gson.fromJson(changeString, listType);


                                String get_love_url = getResources().getString(R.string.url) + "love";

                                StringRequest love_stringRequest = new StringRequest(Request.Method.GET, get_love_url, new Response.Listener<String>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onResponse(String response) {
                                        // ???????????? ?????? ??????
                                        String changeString = new String();
                                        try {
                                            changeString = new String(response.getBytes("8859_1"),"utf-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                        Type listType = new TypeToken<ArrayList<Love>>(){}.getType();

                                        love_list = gson.fromJson(changeString, listType);


                                        //review ??????????????????
                                        ArrayList<CafeDetailItem> cafeDetailReviewItem = new ArrayList<>();


                                        ///////////////////////////////////////////////////////////////////////////////////////////
                                        // ?????? ???????????? ????????????
                                        // 1. ?????? ???????????? ?????? ????????? ?????? ????????? ????????? ??????, ?????????????????? ?????? ????????? ???????????? ??????
                                        // 2. ?????? ??????????????? ?????????, ?????? ????????? ????????? ?????? Item ??????
                                        // 3. ?????? ?????????????????? ?????? ?????? ?????? 3?????? ???????????? ??????
                                        for(Review r : review_list){
                                            if(r.getCafeNum().equals(get_cafe_num)) {
                                                point_counter++;
                                                love_flag = false;

                                                // DB?????? ????????? ?????? ?????? ????????? ???????????? ?????? ??????
                                                String creatTime = r.getCreateTime();
                                                SimpleDateFormat old_format =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                                old_format.setTimeZone(TimeZone.getTimeZone("KST"));
                                                SimpleDateFormat new_format = new SimpleDateFormat("yyyy/MM/dd   HH:mm");

                                                try {
                                                    Date old_date =  old_format.parse(creatTime);
                                                    create_date = new_format.format(old_date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                for (Personal p : personal_list) {

                                                    String personImage = "";

                                                    // ????????? ????????? ??????????????? ???????????? ???(?????? X???, ?????? ????????? ???????????? ??????)
                                                    if(p.getProfileImageUrl().equals(""))
                                                        // ?????? ????????? URL???????????? ???(?????? ???????????? ??????)
                                                        personImage = "https://w.namu.la/s/0c6301df01fc4f180ec65717bad3d0254258abf0be33299e55df7c261040f517518eb9008a1a2cd3d7b8b7777d70182c185bc891b1054dc57b11cc46fd29130a3474f1b75b816024dfdc16b692a0c77c";
                                                    else
                                                        personImage = p.getProfileImageUrl();

                                                    // 1. ?????? ???????????? ?????? ????????? ?????? ????????? ????????? ??????, ?????????????????? ?????? ????????? ???????????? ??????
                                                    if (r.getMemNum().equals(mem_num) && p.getMemNum().equals(mem_num)) {
                                                        cafeDetailReviewItem.add( 0, new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                r.getReviewText(), create_date, personImage, R.drawable.logo_v2, r.getLikeCount().toString(), true, false, mem_num, get_cafe_num, -1L, r.getReviewNum()));
                                                        Log.d("review_check", r.getReviewNum().toString());
                                                    } // 2. ?????? ??????????????? ?????????, ?????? ????????? ????????? ?????? Item ??????
                                                    else if (r.getMemNum().equals(p.getMemNum())) {
                                                        if(!love_list.isEmpty()) { // love_list??? ???????????? ?????? ??????
                                                            for (Love l : love_list) {
                                                                // love ???????????? reviewNum??? ?????? ?????? && love ???????????? ???????????? memNum??? ?????? ??????
                                                                if (l.getReviewNum().equals(r.getReviewNum()) && l.getMemNum().equals(mem_num)){
                                                                    Log.d("love_for_if_test", "love_for_if_test");
                                                                    love_flag = true;
                                                                    cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                            r.getReviewText(), create_date, personImage, R.drawable.logo_v2, r.getLikeCount().toString(), false, true, mem_num, get_cafe_num, l.getLoveNum(), r.getReviewNum()));
                                                                }
                                                            }
                                                        }else{
                                                            Log.d("love_not_test", "love_not_test");
                                                            cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                    r.getReviewText(), create_date, personImage, R.drawable.logo_v2, r.getLikeCount().toString(), false, false, mem_num, get_cafe_num, -1L, r.getReviewNum()));
                                                        }
                                                        if(!love_flag){
                                                            Log.d("love_not_test", "love_not_test");
                                                            cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                                    r.getReviewText(), create_date, personImage, R.drawable.logo_v2, r.getLikeCount().toString(), false, false, mem_num, get_cafe_num, -1L, r.getReviewNum()));
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        while(cafeDetailReviewItem.size() > 3) {
                                            cafeDetailReviewItem.remove(cafeDetailReviewItem.size() - 1);   // ????????? 3?????? ??? ????????? ????????? ?????? ?????????
                                        }


                                        // Recycler view
                                        // Adapter ??????
                                        CafeDetailAdapter adapter = new CafeDetailAdapter(getContext(), cafeDetailReviewItem, CafeDetailFragment.this);
                                        recyclerView.setAdapter(adapter);

                                        // Layout manager ??????
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);

                                        adapter.setOnItemClickListener_cafeDetail(new CafeDetailAdapter.OnItemClickEventListener_cafeDetail() {
                                            @Override
                                            public void onItemClick(View view, int position) {

                                                if(cafeDetailReviewItem.size() == 0){
                                                    Toast.makeText(getContext().getApplicationContext(), "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    // ?????? ????????? ?????? ???,
                                                    if(position == cafeDetailReviewItem.size()){
                                                        Toast.makeText(getContext().getApplicationContext(), "?????? ????????? ??????", Toast.LENGTH_SHORT).show();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("cafeNum", get_cafe_num.toString());
                                                        //bundle.putString("name",moreReview3.getText().toString());
                                                        navController.navigate(R.id.cafe_detail_to_cafe_detail_more, bundle);
                                                    }

                                                    // ?????? ?????? ???,
                                                    else {
                                                        final CafeDetailItem item = cafeDetailReviewItem.get(position);
                                                        Toast.makeText(getContext().getApplicationContext(), item.getReviewNickName() + " ?????????.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });

                                        ////////////////////////////////////////////////////////////////////////////////////////
                                        // ?????? ?????? ?????? ?????????.

                                        for(int i = 0; i < 4; i++){
                                            get_taste_point_total += get_taste_point[i]; // ??? ?????? ?????? ?????????
                                            if(get_taste_point[i] == 0 || point_counter == 0){
                                                get_taste_point[i] = 0;
                                            }else {
                                                get_taste_point[i] = get_taste_point[i] / point_counter; // ??? ?????? ??? ?????? ?????????
                                            }

                                            get_seat_point_total += get_seat_point[i]; // ?????? ?????? ?????? ?????????
                                            if(get_seat_point[i] == 0 || point_counter == 0){
                                                get_seat_point[i] = 0;
                                            }else {
                                                get_seat_point[i] = get_seat_point[i] / point_counter; // ?????? ?????? ??? ?????? ?????????
                                            }

                                            get_study_point_total += get_study_point[i]; // ????????? ?????? ?????? ?????????
                                            if(get_study_point[i] == 0 || point_counter == 0){
                                                get_study_point[i] = 0;
                                            }else {
                                                get_study_point[i] = get_study_point[i] / point_counter; // ????????? ?????? ??? ?????? ?????????
                                            }
                                        }

                                        // ?????? viewPager??? ?????? ????????????
                                        cafeRatingViewPager = root.findViewById(R.id.ratingViewPager);
                                        cafeRatingViewPager.setOffscreenPageLimit(3);
                                        ratingList = new ArrayList<>();

                                        CafeDetailRatingItem taste = new CafeDetailRatingItem("???", "??????", "??????", "?????????", "????????????", R.drawable.taste_score, String.valueOf(get_taste_point[0]), String.valueOf(get_taste_point[1]), String.valueOf(get_taste_point[2]), String.valueOf(get_taste_point[3]));
                                        CafeDetailRatingItem seat = new CafeDetailRatingItem("??????", "2?????????", "4?????????", "?????????", "????????????", R.drawable.sit_score, String.valueOf(get_seat_point[0]), String.valueOf(get_seat_point[1]), String.valueOf(get_seat_point[2]), String.valueOf(get_seat_point[3]));
                                        CafeDetailRatingItem study = new CafeDetailRatingItem("?????????", "????????????", "?????????", "??????", "?????????", R.drawable.study_score, String.valueOf(get_study_point[0]), String.valueOf(get_study_point[1]), String.valueOf(get_study_point[2]), String.valueOf(get_study_point[3]));

                                        ratingList.add(taste);
                                        ratingList.add(seat);
                                        ratingList.add(study);

                                        cafeRatingViewPager.setAdapter(new CafeDetailRatingViewPagerAdapter(getContext().getApplicationContext(), ratingList));


                                        // ???????????? ?????? ?????? ?????? ???, ?????? ????????? ?????????
                                        cafeDetail_favorite_previousButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = cafeRatingViewPager.getCurrentItem();
                                                cafeRatingViewPager.setCurrentItem(pageNum - 1, true);
                                            }
                                        });


                                        // ???????????? ?????? ?????? ?????? ???, ?????? ????????? ?????????
                                        cafeDetail_favorite_nextButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = cafeRatingViewPager.getCurrentItem();
                                                cafeRatingViewPager.setCurrentItem(pageNum + 1, true);
                                            }
                                        });


                                        ////////////////////////////////////////////////////////////////////////////////////////////
                                        // ?????? ????????? ??????
                                        if(get_taste_point_total > get_study_point_total) { // ??? ????????? ??? ?????? ??????
                                            moreReview5.setText("#???");
                                        }else if(get_taste_point_total < get_study_point_total){ // ????????? ????????? ??? ?????? ??????
                                            moreReview5.setText("#?????????");
                                        }else{
                                            // ??? ????????? ????????? ????????? ?????? ??????, ???????????? 1????????? ?????? ??????.
                                            for(Personal p : personal_list){
                                                if(p.getMemNum().equals(mem_num)){
                                                    if(p.getFavorite1().equals("???")){
                                                        moreReview5.setText("#???");
                                                    }else{
                                                        moreReview5.setText("#?????????");
                                                    }
                                                }
                                            }
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("love_stringRequest_error",error.toString());
                                    }
                                });


                                requestQueue.add(love_stringRequest);






                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("cafeDetail_personal_stringRequest_error",error.toString());
                            }
                        });


                        requestQueue.add(personal_stringRequest);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("cafeDetail_review_stringRequest_error",error.toString());
                    }
                });


                requestQueue.add(review_stringRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cafeDetail_cafe_stringRequest_error",error.toString());
            }
        });

        requestQueue.add(cafe_stringRequest);



        // ?????? ?????? ?????? ????????????
//        Bundle cafeModifyBundle = getArguments();
//        if(cafeModifyBundle != null){
//            if(cafeModifyBundle.getString("time_open_Modi") != null ){
//                moreReview10.setText(cafeModifyBundle.getString("time_open_Modi"));
//                moreReview8.setText(cafeModifyBundle.getString("time_close_Modi"));
//            }
//        }

        // ?????? ??????(??????) ?????? ?????? ???,
        cafe_modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name",moreReview3.getText().toString());
                bundle.putString("address",moreReview4.getText().toString());
                bundle.putString("time_open",moreReview10.getText().toString());
                bundle.putString("time_close",moreReview8.getText().toString());
                navController.navigate(R.id.cafe_detail_to_cafe_modify, bundle);
            }
        });


        // ?????? ????????? ?????? ?????? ???,
        review_floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("floating_flag", true);
                bundle.putString("floating_cafeName", moreReview2.getText().toString());
                navController.navigate(R.id.cafe_detail_to_review, bundle);
            }
        });


        cafeImageViewPager = root.findViewById(R.id.cafeImageViewPager);
        cafeImageViewPager.setOffscreenPageLimit(5);
        imageList = new ArrayList<>();


        String get_cafeImage_url = getResources().getString(R.string.url) + "cafeImage";

        StringRequest cafeImage_stringRequest = new StringRequest(Request.Method.GET, get_cafeImage_url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                // ???????????? ?????? ??????
                String changeString = new String();
                try {
                    changeString = new String(response.getBytes("8859_1"),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<ArrayList<CafeImage>>(){}.getType();

                CafeImage_list = gson.fromJson(changeString, listType);

                for(CafeImage ci : CafeImage_list){
                    if(ci.getCafeNum().equals(get_cafe_num)){
                        Log.d("cafeImage URL", ci.getFileUrl());
                        imageList.add(ci.getFileUrl());
                    }
                }

                cafeImageViewPager.setAdapter(new CafeDetailImageViewPagerAdapter(getContext().getApplicationContext(), imageList, CafeDetailFragment.this));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cafeImage_stringRequest_error",error.toString());
            }
        });


        requestQueue.add(cafeImage_stringRequest);


//        CafeDetailImagePagerAdapter cafeImageAdapter = new CafeDetailImagePagerAdapter(getActivity().getSupportFragmentManager());
//
//        CafeDetailImageViewPager1 page1 = new CafeDetailImageViewPager1();
//        cafeImageAdapter.cafeImageAddItem(page1);
//        CafeDetailImageViewPager2 page2 = new CafeDetailImageViewPager2();
//        cafeImageAdapter.cafeImageAddItem(page2);
//        CafeDetailImageViewPager3 page3 = new CafeDetailImageViewPager3();
//        cafeImageAdapter.cafeImageAddItem(page3);
//        CafeDetailImageViewPager4 page4 = new CafeDetailImageViewPager4();
//        cafeImageAdapter.cafeImageAddItem(page4);
//        CafeDetailImageViewPager5 page5 = new CafeDetailImageViewPager5();
//        cafeImageAdapter.cafeImageAddItem(page5);



        // ?????????????????? ???????????? ???????????? ????????????


        return root;
    }

    // ??????????????? ??????????????? ????????????
//    class CafeDetailImagePagerAdapter extends FragmentStatePagerAdapter {
//
//        ArrayList<Fragment> imageItems = new ArrayList<>();
//        public CafeDetailImagePagerAdapter(FragmentManager fm){
//            super(fm);
//        }
//
//        public void cafeImageAddItem(Fragment item){
//            imageItems.add(item);   // Fragment ??????
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return imageItems.get(position);    // ??????????????? ????????????
//        }
//
//        @Override
//        public int getCount() {
//            return imageItems.size();   // ??????????????? ????????????
//        }
//    }


    // ??????????????? ?????? ????????????
//    class CafeDetailRatingPagerAdapter extends FragmentStatePagerAdapter {
//        ArrayList<Fragment> ratingItems = new ArrayList<>();
//        public CafeDetailRatingPagerAdapter(FragmentManager ratingFm) {
//            super(ratingFm);
//        }
//
//        public void cafeRatingAddItem(Fragment item){
//            ratingItems.add(item);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return ratingItems.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return ratingItems.size();
//        }
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        this.getArguments().clear();
        super.onDestroyView();
        binding = null;
    }


}
