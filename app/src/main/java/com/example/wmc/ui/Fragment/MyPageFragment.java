package com.example.wmc.ui.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.bumptech.glide.Glide;
import com.example.wmc.CafeDetailMore.CafeDetailMoreAdapter;
import com.example.wmc.CafeDetailMore.CafeDetailMoreItem;
import com.example.wmc.MainActivity;
import com.example.wmc.MypageFavorite.MypageFavoriteAdapter;
import com.example.wmc.MypageFavorite.MypageFavoriteItem;
import com.example.wmc.MypageReview.MypageReviewAdapter;
import com.example.wmc.MypageReview.MypageReviewItem;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Love;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.database.ReviewImage;
import com.example.wmc.databinding.FragmentMypageBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {

    private FragmentMypageBinding binding;
    private static NavController navController;

    ArrayList<Personal> personal_list;
    ArrayList<Bookmark> bookmark_list;
    ArrayList<Cafe> cafe_list;
    ArrayList<Review> review_list;
    ArrayList<ReviewImage> reviewImage_list;
    ArrayList<String> reviewImage = new ArrayList<>();

    TextView grade, nickname, fav1, fav2;
    Button modify;
    Button logout;
    ImageView profileImage;

    Long mem_num = MainActivity.mem_num;
    String create_date; // 리뷰 등록 시간
    String profile_Image = ""; // 프로필사진
    int reviewImageCounter = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        grade = root.findViewById(R.id.level2);
        nickname = root.findViewById(R.id.nickname2);
        fav1 = root.findViewById(R.id.first_important2);
        fav2 = root.findViewById(R.id.second_important2);
        modify = root.findViewById(R.id.change_information_button2);
        logout = root.findViewById(R.id.logout_button2);
//        mypage_profile_image2 = root.findViewById(R.id.mypage_profile_image2);
        profileImage = root.findViewById(R.id.profileImage);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        // 서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        // personal 데이터 베이스 연결
        String url = getResources().getString(R.string.url) + "personal";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                // 한글깨짐 해결 코드
                String changeString = new String();
                try {
                    changeString = new String(response.getBytes("8859_1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<ArrayList<Personal>>() {
                }.getType();

                personal_list = gson.fromJson(changeString, listType);

                // bookmark 데이터 베이스 연결
                String bookmark_url = getResources().getString(R.string.url) + "bookmark";

                StringRequest book_stringRequest = new StringRequest(Request.Method.GET, bookmark_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 한글깨짐 해결 코드
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<Bookmark>>() {
                        }.getType();

                        bookmark_list = gson.fromJson(changeString, listType);

                        // cafe 데이터 베이스 연결
                        String cafe_url = getResources().getString(R.string.url) + "cafe";

                        StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, cafe_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

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

                                // review 데이터 베이스 연결
                                String review_url = getResources().getString(R.string.url) + "review";

                                StringRequest review_stringRequest = new StringRequest(Request.Method.GET, review_url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // 한글깨짐 해결 코드
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

                                        ///////////////////////////////////////////////////////////////////////////////////////
                                        // 마이페이지의 회원 정보를 세팅
                                        for (Personal p : personal_list) {
                                            if (p.getMemNum().equals(mem_num)) {
                                                grade.setText(p.getGrade() + "등급 원두");
                                                nickname.setText(p.getNickName() + "님");
                                                fav1.setText("#" + p.getFavorite1());
                                                fav2.setText("#" + p.getFavorite2());

                                                if(p.getProfileimageurl() == null)
                                                    profile_Image = "https://w.namu.la/s/0c6301df01fc4f180ec65717bad3d0254258abf0be33299e55df7c261040f517518eb9008a1a2cd3d7b8b7777d70182c185bc891b1054dc57b11cc46fd29130a3474f1b75b816024dfdc16b692a0c77c";
                                                else
                                                    profile_Image = p.getProfileimageurl();

                                                Glide.with(MyPageFragment.this).load(profile_Image).into(profileImage);
                                            }
                                        }

                                        // 찜한카페를 위한 리사이클러뷰 아이템 arraylist 선언
                                        ArrayList<MypageFavoriteItem> mypageFavoriteItems = new ArrayList<>();

                                        // Recycler view
                                        RecyclerView mypageFavoriteRecyclerview = root.findViewById(R.id.favorite_mypage);

                                        // Adapter 추가
                                        MypageFavoriteAdapter favoriteAdapter = new MypageFavoriteAdapter(mypageFavoriteItems);
                                        mypageFavoriteRecyclerview.setAdapter(favoriteAdapter);

                                        // Layout manager 추가
                                        GridLayoutManager favoriteLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
                                        mypageFavoriteRecyclerview.setLayoutManager(favoriteLayoutManager);

                                        // 마이페이지의 찜한 카페 리사이클러뷰를 세팅
                                        for (Bookmark b : bookmark_list) {
                                            if (b.getMemNum().equals(mem_num)) {
                                                for (Cafe c : cafe_list) {
                                                    if (c.getCafeNum().equals(b.getCafeNum())) {
                                                        mypageFavoriteItems.add(new MypageFavoriteItem(c.getCafeName()));
                                                    }
                                                }
                                            }
                                        }

                                        // 찜한카페 아이템 클릭 시 카페 디테일로 네비게이트
                                        favoriteAdapter.setOnItemClickListener_MypageFavorite(new MypageFavoriteAdapter.OnItemClickEventListener_MypageFavorite() {
                                            @Override
                                            public void onItemClick(View a_view, int a_position) {
                                                final MypageFavoriteItem item = mypageFavoriteItems.get(a_position);
                                                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("cafeName", item.getCafeName());
                                                navController.navigate(R.id.myPage_to_cafe_detail, bundle);
                                            }
                                        });


                                        // 내 리뷰를 위한 리사이클러뷰 아이템 arraylist 선언
                                        ArrayList<MypageReviewItem> mypageReviewItems = new ArrayList<>();

                                        // 마이페이지의 내 리뷰 리사이클러뷰를 세팅
                                        for (Review r : review_list){

                                            // DB에서 받아온 리뷰 생성 시간을 변경하기 위한 코드
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

                                            if (r.getMemNum().equals(mem_num)){
                                                for(Cafe c : cafe_list){
                                                    if (c.getCafeNum().equals(r.getCafeNum())){


                                                        String get_reviewImage_url = getResources().getString(R.string.url) + "reviewImage";

                                                        StringRequest reviewImage_stringRequest = new StringRequest(Request.Method.GET, get_reviewImage_url, new Response.Listener<String>() {
                                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                                            @Override
                                                            public void onResponse(String response) {
                                                                // 한글깨짐 해결 코드
                                                                String changeString = new String();
                                                                try {
                                                                    changeString = new String(response.getBytes("8859_1"),"utf-8");
                                                                } catch (UnsupportedEncodingException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                                                Type listType = new TypeToken<ArrayList<ReviewImage>>(){}.getType();

                                                                reviewImage_list = gson.fromJson(changeString, listType);

                                                                reviewImageCounter = 0;

                                                                for(ReviewImage ri : reviewImage_list) {
                                                                    if(ri.getReviewNum().equals(r.getReviewNum()) && reviewImageCounter < 3){
                                                                        if(ri.getFileUrl().isEmpty()){
                                                                            break;
                                                                        }
                                                                        reviewImage.add(ri.getFileUrl());
                                                                        Log.d("reviewImage", ri.getFileUrl());
                                                                        reviewImageCounter++;
                                                                    }
                                                                }

                                                                // 리뷰 이미지가 없을때
                                                                if(!(reviewImage.size() == 3)) {
                                                                    //(나중에 로고 올리고 바꾸기)
                                                                    while(reviewImage.size() < 3){
                                                                        reviewImage.add("https://w.namu.la/s/0c6301df01fc4f180ec65717bad3d0254258abf0be33299e55df7c261040f517518eb9008a1a2cd3d7b8b7777d70182c185bc891b1054dc57b11cc46fd29130a3474f1b75b816024dfdc16b692a0c77c");
                                                                    }
                                                                }

                                                                mypageReviewItems.add(new MypageReviewItem(c.getCafeName(), create_date,
                                                                        r.getReviewText(), reviewImage.get(0), reviewImage.get(1),
                                                                        reviewImage.get(2), r.getLikeCount().toString(),
                                                                        true, mem_num, r.getCafeNum(), r.getReviewNum(), r.getLocationCheck()));


                                                                // 다음 리뷰로 넘어갈때, 리뷰 이미지 ArrayList 비워주는 코드
                                                                if(!(reviewImage.isEmpty())) {
                                                                    reviewImage.clear();
                                                                }

                                                                // Recycler view
                                                                RecyclerView mypageReviewRecyclerview = root.findViewById(R.id.review_mypage);

                                                                // Adapter 추가
                                                                MypageReviewAdapter reviewAdapter = new MypageReviewAdapter(getContext(), mypageReviewItems, MyPageFragment.this);
                                                                mypageReviewRecyclerview.setAdapter(reviewAdapter);

                                                                // Layout manager 추가
                                                                LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                                                mypageReviewRecyclerview.setLayoutManager(reviewLayoutManager);


                                                                // 내 리뷰 리사이클러뷰 아이템 클릭시 카페이름 토스트 출력
                                                                reviewAdapter.setOnItemClickListener_MypageReview(new MypageReviewAdapter.OnItemClickEventListener_MyPageReview() {
                                                                    @Override
                                                                    public void onItemClick(View a_view, int a_position) {
                                                                        final MypageReviewItem item = mypageReviewItems.get(a_position);
//                                                                        Toast.makeText(getContext().getApplicationContext(), item.getMypageReview_CafeName(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e("reviewImage_stringRequest_error",error.toString());
                                                            }
                                                        });

                                                        requestQueue.add(reviewImage_stringRequest);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("test_error", error.toString());
                                    }
                                });
                                requestQueue.add(review_stringRequest);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("test_error", error.toString());
                            }
                        });
                        requestQueue.add(cafe_stringRequest);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("test_error", error.toString());
                    }
                });
                requestQueue.add(book_stringRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("test_error", error.toString());
            }
        });
        requestQueue.add(stringRequest);

        /////////////////////////////////////////////////////////////
        // 서버를 통한 데이터 표시, 리사이클러뷰 연결 끝

        // 현재 내 등급 클릭 시,
        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.myPage_to_myPage_grade);
            }
        });

        // 정보수정 버튼 클릭 시,
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navController.navigate(R.id.); // 정보수정 Fragment로 이동
            }
        });

        // 로그아웃 버튼 클릭 시,
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navController.navigate(R.id.); // 로그인Fragment 이동
            }
        });

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
