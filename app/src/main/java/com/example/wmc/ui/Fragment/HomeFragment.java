package com.example.wmc.ui.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.wmc.HomeFavorite.HomeFavoriteAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPager;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPagerAdapter;
import com.example.wmc.HomeTag1ViewPager.HomeTag1ViewPagerItem;
import com.example.wmc.MainActivity;
import com.example.wmc.HomeTag2ViewPager.HomeTag2ViewPager;
import com.example.wmc.HomeTag2ViewPager.HomeTag2ViewPagerAdapter;
import com.example.wmc.HomeTag2ViewPager.HomeTag2ViewPagerItem;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static NavController navController;

    TextView favoirte_default_textView;
    HomeTag1ViewPager first_viewPager;
    HomeTag2ViewPager second_viewPager;
    Button first_previousButton;
    Button first_nextButton;
    Button second_previousButton;
    Button second_nextButton;
    TextView first_hashtag_textview;
    TextView second_hashtag_textview;
    TextView first_hastage_cafelist_textview;
    TextView second_hastage_cafelist_textview;

    HomeTag1ViewPagerAdapter tag1Adapter;
    HomeTag2ViewPagerAdapter tag2Adapter;
    ArrayList<HomeTag1ViewPagerItem> tag1_List;
    ArrayList<HomeTag2ViewPagerItem> tag2_List;

    // ?????? ????????? ??????
    ArrayList<Personal> personal_list;
    ArrayList<Cafe> cafe_list;
    ArrayList<Bookmark> bookmark_list;
    ArrayList<Review> review_list;

    Long mem_num = MainActivity.mem_num; // ?????? ?????? ??????
    String get_user_fav;
    String get_user_address;
    Long[] get_keyword = new Long[36];

    int get_study_point_total = 0;
    int get_taste_point_total = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        favoirte_default_textView = root.findViewById(R.id.favoirte_default_textView);
        first_viewPager = root.findViewById(R.id.first_viewPager);
        second_viewPager = root.findViewById(R.id.second_viewPager);
        first_previousButton = root.findViewById(R.id.first_previousButton);
        first_nextButton = root.findViewById(R.id.first_nextButton);
        second_previousButton = root.findViewById(R.id.second_previousButton);
        second_nextButton = root.findViewById(R.id.second_nextButton);
        first_hashtag_textview = root.findViewById(R.id.first_hashtag_textView);
        second_hashtag_textview = root.findViewById(R.id.second_hashtag_textView);
        first_hastage_cafelist_textview = root.findViewById(R.id.first_hashtag_cafeList_textView);
        second_hastage_cafelist_textview = root.findViewById(R.id.second_hashtag_cafeList_textView);



        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Home ?????? ?????? ????????? ?????? ?????????????????? ??????
        ArrayList<HomeFavoriteItem> homeFavoriteItems = new ArrayList<>();

        // ???????????? ?????? ???????????? ?????? ?????? ?????? ??????
        // ?????? ??????
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        String get_personal_url = getResources().getString(R.string.url) + "personal";
        String get_cafe_url = getResources().getString(R.string.url) + "cafe";
        String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";

        // Personal ??????
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

                /////////////////////////////////////////////////////////////////////////////////////////////////
                // ???????????? ?????? ??????????????? ?????? Home?????? ??????.
                for(Personal p : personal_list){
                    if(p.getMemNum().equals(mem_num)){
                        first_hashtag_textview.setText("#" + p.getFavorite1()); // ???????????? ?????? 1??????
                        second_hashtag_textview.setText("#" + p.getFavorite2()); // ???????????? ?????? 2??????

                        // #?????? ?????? ????????? ??????
                        if(p.getFavorite1().equals("???")) {
                            first_hastage_cafelist_textview.setText("??? 1????????? ????????? ?????? ??????!");
                        }
                        else{
                            second_hastage_cafelist_textview.setText("??? 2????????? ????????? ?????? ??????!");
                        }
                    }
                }

                // Bookmark ?????? -> ?????? ?????? ??????
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

                        // Cafe ?????? -> Bookmark??? cafeNum??? Cafe??? cafeNum??? ???????????? ?????? ?????? ??????
                        StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
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

                                for(Bookmark b : bookmark_list) {
                                    if (b.getMemNum().equals(mem_num)) { // bookmark_list?????? ???????????? mem_num??? ???????????? ?????? ??????
                                        for(Cafe c : cafe_list){
                                            if(c.getCafeNum().equals(b.getCafeNum())){ // cafe_list?????? ???????????? bookmark??? ?????? ??????
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
                                                for (int i = 1; i < 36; i++) {
                                                    secondMax = get_keyword[i];
                                                    if (Max <= secondMax) {
                                                        secondMax = Max;
                                                        counter_second = counter_max;

                                                        Max = get_keyword[i];
                                                        counter_max = i;
                                                    }
                                                }


                                                // ?????? ????????? ????????? ?????????
                                                Log.d("show_Max_and_secondMax", Max.toString() + ", " + secondMax.toString());

                                                String tag1 = "??????";
                                                String tag2 = "??????";

                                                // ?????? 1 ??????
                                                switch (counter_max) {
                                                    case 0:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 1:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 2:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 3:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 4:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 5:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 6:
                                                        tag1 = "#????????????";
                                                        break;
                                                    case 7:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 8:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 9:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 10:
                                                        tag1 = "#???????????????";
                                                        break;
                                                    case 11:
                                                        tag1 = "#???????????????";
                                                        break;
                                                    case 12:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 13:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 14:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 15:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 16:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 17:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 18:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 19:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 20:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 21:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 22:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 23:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 24:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 25:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 26:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 27:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 28:
                                                        tag1 = "#????????????";
                                                        break;
                                                    case 29:
                                                        tag1 = "#????????????";
                                                        break;
                                                    case 30:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 31:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 32:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 33:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 34:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 35:
                                                        tag1 = "#????????????";
                                                        break;

                                                }

                                                //?????? 2 ??????
                                                switch (counter_second) {
                                                    case 0:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 1:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 2:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 3:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 4:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 5:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 6:
                                                        tag2 = "#????????????";
                                                        break;
                                                    case 7:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 8:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 9:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 10:
                                                        tag2 = "#???????????????";
                                                        break;
                                                    case 11:
                                                        tag2 = "#???????????????";
                                                        break;
                                                    case 12:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 13:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 14:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 15:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 16:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 17:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 18:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 19:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 20:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 21:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 22:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 23:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 24:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 25:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 26:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 27:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 28:
                                                        tag2 = "#????????????";
                                                        break;
                                                    case 29:
                                                        tag2 = "#????????????";
                                                        break;
                                                    case 30:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 31:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 32:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 33:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 34:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 35:
                                                        tag2 = "#????????????";
                                                        break;

                                                }



                                                // ?????? ?????? recycler view??? ????????????
                                                homeFavoriteItems.add(new HomeFavoriteItem(c.getCafeName(), tag1, tag2 ,R.drawable.logo_v2));
                                            }
                                        }
                                    }
                                }

                                // Recycler view
                                RecyclerView homeFavoriteRecyclerView = root.findViewById(R.id.favorite_recyclerView);

                                // Adapter ??????
                                HomeFavoriteAdapter favoriteAdapter = new HomeFavoriteAdapter(homeFavoriteItems);
                                homeFavoriteRecyclerView.setAdapter(favoriteAdapter);

                                // Layout manager ??????
                                LinearLayoutManager favoriteLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                homeFavoriteRecyclerView.setLayoutManager(favoriteLayoutManager);

                                if (homeFavoriteItems.size() == 0){
                                    favoirte_default_textView.setVisibility(View.VISIBLE);
                                }

                                favoriteAdapter.setOnItemClickListener_HomeFavorite(new HomeFavoriteAdapter.OnItemClickEventListener_HomeFavorite() {
                                    @Override
                                    public void onItemClick(View a_view, int a_position) {
                                        final HomeFavoriteItem item = homeFavoriteItems.get(a_position);
                                        Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " ?????????.", Toast.LENGTH_SHORT).show();

                                        Bundle bundle = new Bundle();
                                        bundle.putString("cafeName", item.getCafeName());
                                        navController.navigate(R.id.home_to_cafe_detail, bundle);
                                    }
                                });


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



                                        // for??? ????????? fav1??? ???????????? ?????? ??????
                                        for (Personal p : personal_list){
                                            if(p.getMemNum().equals(mem_num)){
                                                get_user_fav = p.getFavorite1();
                                                get_user_address = p.getAddress();
                                            }
                                        }

                                        first_viewPager.setOffscreenPageLimit(5);
                                        tag1_List = new ArrayList<>();


                                        second_viewPager.setOffscreenPageLimit(5);
                                        tag2_List = new ArrayList<>();


                                        for(Cafe c : cafe_list) {
                                            if (c.getCafeAddress().contains(get_user_address)) {

                                                get_taste_point_total = 0;
                                                get_study_point_total = 0;

                                                get_taste_point_total = c.getTastePoint1() + c.getTastePoint2() + c.getTastePoint3() + c.getTastePoint4();
                                                get_study_point_total = c.getStudyPoint1() + c.getStudyPoint2() + c.getStudyPoint3() + c.getStudyPoint4();


                                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                                // Home?????? 1?????? ??????????????? ?????? ???????????? ??????

                                                int review_counter = 0;
                                                String get_review = "";
                                                int like_counter_max = 0;

                                                for (Review r : review_list) {
                                                    if (r.getCafeNum().equals(c.getCafeNum())) {
                                                        review_counter++;
                                                        if (like_counter_max <= r.getLikeCount()) {
                                                            like_counter_max = r.getLikeCount();
                                                            get_review = r.getReviewText();
                                                        }
                                                    }
                                                }

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
                                                for (int i = 1; i < 36; i++) {
                                                    secondMax = get_keyword[i];
                                                    if (Max <= secondMax) {
                                                        secondMax = Max;
                                                        counter_second = counter_max;

                                                        Max = get_keyword[i];
                                                        counter_max = i;
                                                    }
                                                }


                                                // ?????? ????????? ????????? ?????????
                                                Log.d("show_Max_and_secondMax", Max.toString() + ", " + secondMax.toString());

                                                String tag1 = "??????";
                                                String tag2 = "??????";

                                                // ?????? 1 ??????
                                                switch (counter_max) {
                                                    case 0:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 1:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 2:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 3:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 4:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 5:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 6:
                                                        tag1 = "#????????????";
                                                        break;
                                                    case 7:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 8:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 9:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 10:
                                                        tag1 = "#???????????????";
                                                        break;
                                                    case 11:
                                                        tag1 = "#???????????????";
                                                        break;
                                                    case 12:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 13:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 14:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 15:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 16:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 17:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 18:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 19:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 20:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 21:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 22:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 23:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 24:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 25:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 26:
                                                        tag1 = "#??????";
                                                        break;
                                                    case 27:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 28:
                                                        tag1 = "#????????????";
                                                        break;
                                                    case 29:
                                                        tag1 = "#????????????";
                                                        break;
                                                    case 30:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 31:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 32:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 33:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 34:
                                                        tag1 = "#?????????";
                                                        break;
                                                    case 35:
                                                        tag1 = "#????????????";
                                                        break;

                                                }

                                                //?????? 2 ??????
                                                switch (counter_second) {
                                                    case 0:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 1:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 2:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 3:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 4:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 5:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 6:
                                                        tag2 = "#????????????";
                                                        break;
                                                    case 7:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 8:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 9:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 10:
                                                        tag2 = "#???????????????";
                                                        break;
                                                    case 11:
                                                        tag2 = "#???????????????";
                                                        break;
                                                    case 12:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 13:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 14:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 15:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 16:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 17:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 18:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 19:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 20:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 21:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 22:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 23:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 24:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 25:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 26:
                                                        tag2 = "#??????";
                                                        break;
                                                    case 27:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 28:
                                                        tag2 = "#????????????";
                                                        break;
                                                    case 29:
                                                        tag2 = "#????????????";
                                                        break;
                                                    case 30:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 31:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 32:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 33:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 34:
                                                        tag2 = "#?????????";
                                                        break;
                                                    case 35:
                                                        tag2 = "#????????????";
                                                        break;

                                                }

                                                int get_study_point_avg;
                                                int get_taste_point_avg;

                                                if(get_study_point_total == 0 || review_counter == 0){
                                                    get_study_point_avg = 0;
                                                }
                                                else
                                                    get_study_point_avg = get_study_point_total / review_counter;

                                                if(get_taste_point_total == 0 || review_counter == 0){
                                                    get_taste_point_avg = 0;
                                                }
                                                else
                                                    get_taste_point_avg = get_taste_point_total / review_counter;

                                                if (get_user_fav.equals("?????????")) {
                                                    if (get_study_point_total >= get_taste_point_total) {

                                                        HomeTag1ViewPagerItem firstTag_item = new HomeTag1ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#?????????", tag1, tag2,
                                                                get_review, R.drawable.logo, get_study_point_avg);
                                                        tag1_List.add(firstTag_item);
                                                    }
                                                    else{
                                                        HomeTag2ViewPagerItem secondTag_item = new HomeTag2ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#???", tag1, tag2,
                                                                get_review, R.drawable.logo, get_taste_point_avg);
                                                        tag2_List.add(secondTag_item);
                                                    }
                                                }
                                                else{
                                                    if (get_study_point_total <= get_taste_point_total) {

                                                        HomeTag1ViewPagerItem firstTag_item = new HomeTag1ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#???", tag1, tag2,
                                                                get_review, R.drawable.logo, get_study_point_avg);
                                                        tag1_List.add(firstTag_item);
                                                    }
                                                    else{
                                                        HomeTag2ViewPagerItem secondTag_item = new HomeTag2ViewPagerItem(c.getCafeName(), c.getCafeAddress(), "#?????????", tag1, tag2,
                                                                get_review, R.drawable.logo, get_taste_point_avg);
                                                        tag2_List.add(secondTag_item);
                                                    }
                                                }
                                            }
                                        }


                                        first_viewPager.setAdapter(new HomeTag1ViewPagerAdapter(getContext().getApplicationContext(), tag1_List));
                                        tag1Adapter = new HomeTag1ViewPagerAdapter(getContext().getApplicationContext(), tag1_List);


                                        first_viewPager.setOnItemClickListener(new HomeTag1ViewPager.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {
                                                final HomeTag1ViewPagerItem item = tag1_List.get(position);
                                                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " ?????????.", Toast.LENGTH_SHORT).show();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("cafeName", item.getCafeName());
                                                navController.navigate(R.id.home_to_cafe_detail, bundle);
                                            }
                                        });

                                        // ????????? ?????? ??????????????? ?????? ?????? ?????? ???, ?????? ?????? ?????????
                                        first_previousButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = first_viewPager.getCurrentItem();
                                                first_viewPager.setCurrentItem(pageNum - 1, true);
                                            }
                                        });


                                        // ????????? ?????? ??????????????? ?????? ?????? ?????? ???, ?????? ?????? ?????????
                                        first_nextButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = first_viewPager.getCurrentItem();
                                                first_viewPager.setCurrentItem(pageNum + 1, true);
                                            }
                                        });


                                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        // Home?????? 2?????? ??????????????? ?????? ???????????? ??????
                                        second_viewPager.setAdapter(new HomeTag2ViewPagerAdapter(getContext().getApplicationContext(), tag2_List));
                                        tag2Adapter = new HomeTag2ViewPagerAdapter(getContext().getApplicationContext(), tag2_List);


                                        second_viewPager.setOnItemClickListener_second(new HomeTag2ViewPager.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {
                                                final HomeTag2ViewPagerItem item = tag2_List.get(position);
                                                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " ?????????.", Toast.LENGTH_SHORT).show();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("cafeName", item.getCafeName());
                                                navController.navigate(R.id.home_to_cafe_detail, bundle);
                                            }
                                        });

                                        // ????????? ?????? ??????????????? ?????? ?????? ?????? ???, ?????? ?????? ?????????
                                        second_previousButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = second_viewPager.getCurrentItem();
                                                second_viewPager.setCurrentItem(pageNum - 1, true);
                                            }
                                        });


                                        // ????????? ?????? ??????????????? ?????? ?????? ?????? ???, ?????? ?????? ?????????
                                        second_nextButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pageNum = second_viewPager.getCurrentItem();
                                                second_viewPager.setCurrentItem(pageNum + 1, true);
                                            }
                                        });


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("home_review_stringRequest_error",error.toString());
                                    }
                                });


                                requestQueue.add(review_stringRequest);






                                // for??? ????????? fav2??? ???????????? ?????? ??????



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error_cafe",error.toString()); // cafe_list??? ?????? ?????? ?????? ?????????
                            }
                        });

                        requestQueue.add(cafe_stringRequest);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_bookmark",error.toString());
                    }
                });
                requestQueue.add(bookmark_stringRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error_personal",error.toString());
            }
        });

        requestQueue.add(personal_stringRequest);


//        ArrayList<HomeTag1Item> homeTag1Items = new ArrayList<>();
//
//        homeTag1Items.add(new HomeTag1Item("??????????????? ????????????", "????????? ????????? ????????? 46", "#?????????", "#?????????", "#?????????",
//                "???????????? ?????? ???????????????. \n" + "?????????, ????????? ?????? ???????????? ???????????????!\n" + "???????????? ?????? ???????????????", R.drawable.logo, 3));
//        homeTag1Items.add(new HomeTag1Item("??????????????? ????????????", "????????? ????????? ????????? 41-17", "#?????????", "#?????????", "#??????",
//                "???????????? ???????????? ??????????????? ????????????\n" + "????????? ????????? ??????????????? ?????? ??????????????????????????????.", R.drawable.logo, 2));
//        homeTag1Items.add(new HomeTag1Item("???????????? ???????????????", "????????? ????????? ?????? 801-4", "#??????", "#?????????", "#?????????",
//                "?????? ?????????\n?????? ?????????\n?????? ?????????", R.drawable.logo_v2, 5));
//
//
//        // Recycler view
//        RecyclerView tag1RecyclerView = root.findViewById(R.id.first_recyclerView);
//
//        // Adapter ??????
//        HomeTag1Adapter tag1Adapter = new HomeTag1Adapter(homeTag1Items);
//        tag1RecyclerView.setAdapter(tag1Adapter);
//
//        // Layout manager ??????
//        LinearLayoutManager tag1LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        tag1RecyclerView.setLayoutManager(tag1LayoutManager);
//
//        tag1Adapter.setOnItemClickListener_HomeTag1(new HomeTag1Adapter.OnItemClickEventListener_HomeTag1() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final HomeTag1Item item = homeTag1Items.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " ?????????.", Toast.LENGTH_SHORT).show();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("cafeName", item.getCafeName());
//                navController.navigate(R.id.home_to_cafe_detail, bundle);
//            }
//        });




//        ArrayList<HomeTag2Item> homeTag2Items = new ArrayList<>();
//
//        homeTag2Items.add(new HomeTag2Item("????????? ?????????", "????????? ~~~~ 167", "#?????????", "#?????????", "#?????????",
//                "???????????? ?????? ???????????????. \n" + "?????????, ????????? ?????? ???????????? ???????????????!\n" + "???????????? ?????? ???????????????", R.drawable.logo, 1));
//        homeTag2Items.add(new HomeTag2Item("???????????? ????????????", "????????? ????????? ????????? 42-11", "#?????????", "#??????", "#?????????",
//                "???????????? ???????????? ??????????????? ????????????\n" + "????????? ????????? ??????????????? ?????? ??????????????????????????????.", R.drawable.logo, 3));
//        homeTag2Items.add(new HomeTag2Item("???????????? ????????????", "????????? ????????? ????????? ~~~ 84-4", "#?????????", "#?????????", "#??????",
//                "?????? ?????????\n?????? ?????????\n?????? ?????????", R.drawable.logo_v2, 4));
//
//
//        // Recycler view
//        RecyclerView tag2RecyclerView = root.findViewById(R.id.second_recyclerView);
//
//        // Adapter ??????
//        HomeTag2Adapter tag2Adapter = new HomeTag2Adapter(homeTag2Items);
//        tag2RecyclerView.setAdapter(tag2Adapter);
//
//        // Layout manager ??????
//        LinearLayoutManager tag2LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        tag2RecyclerView.setLayoutManager(tag2LayoutManager);
//
//        tag2Adapter.setOnItemClickListener_HomeTag2(new HomeTag2Adapter.OnItemClickEventListener_HomeTag2() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final HomeTag2Item item = homeTag2Items.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " ?????????.", Toast.LENGTH_SHORT).show();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("cafeName", item.getCafeName());
//                navController.navigate(R.id.home_to_cafe_detail, bundle);
//            }
//        });


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