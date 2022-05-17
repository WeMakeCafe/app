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
import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailImageViewPagerAdapter;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingViewPagerAdapter;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Category;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    TextView moreReview2; // 최상단 카페 이름 ID
    TextView moreReview3; // 사진 아래 카페 이름 ID
    TextView moreReview4; // 사진 아래 카페 주소 ID
    TextView moreReview10; // 사진 아래 운영 시간 첫번째
    TextView moreReview8; // 사진 아래 운영 시간 두번째

    ArrayList<Integer> imageList;   // 카페 이미지 5장을 저장하는 ArrayList
    ArrayList<CafeDetailRatingItem> ratingList; // 카페 평점을 저장하는 ArrayList

    ArrayList<Cafe> cafe_list;
    ArrayList<Review> review_list;
    ArrayList<Personal> personal_list;
    ArrayList<Category> category_list;
    ArrayList<Bookmark> bookmark_list;

    Long mem_num = MainActivity.mem_num;

    String cafe_name; // Bundle을 통해 받아온 cafe_name을 임시로 저장함
    Long get_cafe_num; // cafe_num을 임시로 저장함.
    Long get_bookmark_num;

    int get_seat_point1_total = 0;
    int get_seat_point2_total = 0;
    int get_seat_point3_total = 0;
    int get_seat_point4_total = 0;

    int get_study_point1_total = 0;
    int get_study_point2_total = 0;
    int get_study_point3_total = 0;
    int get_study_point4_total = 0;

    int get_taste_point1_total = 0;
    int get_taste_point2_total = 0;
    int get_taste_point3_total = 0;
    int get_taste_point4_total = 0;

    int get_seat_point1_avg = 0;
    int get_seat_point2_avg = 0;
    int get_seat_point3_avg = 0;
    int get_seat_point4_avg = 0;

    int get_study_point1_avg = 0;
    int get_study_point2_avg = 0;
    int get_study_point3_avg = 0;
    int get_study_point4_avg = 0;

    int get_taste_point1_avg = 0;
    int get_taste_point2_avg = 0;
    int get_taste_point3_avg = 0;
    int get_taste_point4_avg = 0;

    int category_counter = 0;



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
        moreReview2 = root.findViewById(R.id.moreReview2); // 최상단 카페 이름 ID
        moreReview3 = root.findViewById(R.id.moreReview3); // 사진 아래 카페 이름 ID
        moreReview4 = root.findViewById(R.id.moreReview4); // 사진 아래 카페 주소 ID
        moreReview10 = root.findViewById(R.id.moreReview10); // 사진 아래 운영 시간 첫번째
        moreReview8 = root.findViewById(R.id.moreReview8); // 사진 아래 운영 시간 두번째


        // cafeDetail 외부에서 Bundle로 이름 받아오기
        Bundle cafeNameBundle = getArguments();
        if(cafeNameBundle != null){
            if(cafeNameBundle.getString("cafeName") != null){
                cafe_name = cafeNameBundle.getString("cafeName");
            }
        }



        //서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        String get_cafe_url = "http://54.196.209.1:8080/cafe";

        StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
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
                Type listType = new TypeToken<ArrayList<Cafe>>(){}.getType();

                cafe_list = gson.fromJson(changeString, listType);


                ///////////////////////////////////////////////////////////////////////////////////////////
                // cafeDetail 정보 세팅
                for(Cafe c : cafe_list){
                    if(c.getCafeName().equals(cafe_name)){
                        get_cafe_num = c.getCafeNum();
                        Log.d("check_cafe_num", get_cafe_num.toString());

                        moreReview2.setText(c.getCafeName());
                        moreReview3.setText(c.getCafeName());
                        moreReview4.setText(c.getCafeAddress());
                        moreReview10.setText(c.getOpenTime().substring(0,2)+":"+c.getOpenTime().substring(2,4));
                        moreReview8.setText(c.getCloseTime().substring(0,2)+":"+c.getCloseTime().substring(2,4));
                    }
                }


                ///////////////////////////////////////////////////////////////////////////////////////////////////
                // 카페 북마크 여부 확인 및 등록, 삭제
                String get_bookmark_url = "http://54.196.209.1:8080/bookmark";

                // 카페 북마크 여부 확인
                StringRequest bookmark_stringRequest = new StringRequest(Request.Method.GET, get_bookmark_url, new Response.Listener<String>() {
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
                        Type listType = new TypeToken<ArrayList<Bookmark>>(){}.getType();

                        bookmark_list = gson.fromJson(changeString, listType);

                        for(Bookmark b : bookmark_list){
                            // "북마크의 mem_num과 사용자의 mem_num이 일치 && 북마크의 cafe_num과 cafeDetail의 cafe_num이 일치"할 경우
                            if(b.getMemNum().equals(mem_num) && b.getCafeNum().equals(get_cafe_num)){
                                get_bookmark_num = b.getBookmarkNum(); // bookmark_num 일시 저장
                                favorite_checkbox.setChecked(true); // 즐겨찾기 버튼 true 세팅
                            }
                        }

                        // 즐겨찾기 버튼(별) 클릭 시,
                        favorite_checkbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean checked = ((CheckBox) view).isChecked();    // 즐겨찾기가 됐는지 확인

                                if(checked) {
                                    // 즐겨찾기 항목에 추가함

                                    Map map = new HashMap();
                                    map.put("cafeNum", get_cafe_num);
                                    map.put("memNum", mem_num);

                                    JSONObject bookmark_jsonObject = new JSONObject(map);
                                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, get_bookmark_url, bookmark_jsonObject,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // 북마크 추가 성공, 토스트 띄우기.
                                                    Toast.makeText(getContext().getApplicationContext(), "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
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
                                    // 즐겨찾기 항목에서 제거됨
                                    String bookmark_delete_url = "http://54.196.209.1:8080/bookmark/" + get_bookmark_num.toString();
                                    StringRequest bookmark_delete_stringRequest = new StringRequest(Request.Method.DELETE, bookmark_delete_url, new Response.Listener<String>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onResponse(String response) {
                                            // 북마크 제거 성공, 토스트 띄우기.
                                            Toast.makeText(getContext().getApplicationContext(), "즐겨찾기 삭제", Toast.LENGTH_SHORT).show();
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
                // review 채우기
                String get_review_url = "http://54.196.209.1:8080/review";

                StringRequest review_stringRequest = new StringRequest(Request.Method.GET, get_review_url, new Response.Listener<String>() {
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
                        Type listType = new TypeToken<ArrayList<Review>>(){}.getType();

                        review_list = gson.fromJson(changeString, listType);


                        ////////////////////////////////////////////////////////////////////////////////////////////
                        //리뷰 작성자의 정보 가져오기
                        String get_personal_url = "http://54.196.209.1:8080/personal";

                        StringRequest personal_stringRequest = new StringRequest(Request.Method.GET, get_personal_url, new Response.Listener<String>() {
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
                                Type listType = new TypeToken<ArrayList<Personal>>(){}.getType();

                                personal_list = gson.fromJson(changeString, listType);

                                //review 리사이클러뷰
                                ArrayList<CafeDetailItem> cafeDetailReviewItem = new ArrayList<>();


                                ///////////////////////////////////////////////////////////////////////////////////////////
                                // 리뷰 작성자를 비교해서
                                // 1. 어플 사용자가 해당 카페에 대한 리뷰를 작성한 경우, 리사이클러뷰 가장 처음에 나오도록 설정
                                // 2. 리뷰 작성자들의 닉네임, 회원 등급을 포함한 리뷰 Item 작성
                                for(Review r : review_list){
                                    if(r.getCafeNum().equals(get_cafe_num)) {
                                        for (Personal p : personal_list) {
                                            // 1. 어플 사용자가 해당 카페에 대한 리뷰를 작성한 경우, 리사이클러뷰 가장 처음에 나오도록 설정
                                            if (r.getMemNum().equals(mem_num) && p.getMemNum().equals(mem_num)) {
                                                cafeDetailReviewItem.add(0, new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                        r.getReviewText(), R.drawable.logo, R.drawable.logo_v2, r.getLikeCount().toString(), true));
                                            } // 2. 리뷰 작성자들의 닉네임, 회원 등급을 포함한 리뷰 Item 작성
                                            else if (r.getMemNum().equals(p.getMemNum())) {
                                                cafeDetailReviewItem.add(new CafeDetailItem(p.getNickName(), p.getGrade().toString(),
                                                        r.getReviewText(), R.drawable.logo, R.drawable.logo_v2, r.getLikeCount().toString(), false));
                                            }
                                        }
                                    }
                                }
                                // Recycler view
                                RecyclerView recyclerView = root.findViewById(R.id.cafeDetailReviewRecyclerView);

                                        // Adapter 추가
                                        CafeDetailAdapter adapter = new CafeDetailAdapter(cafeDetailReviewItem);
                                        recyclerView.setAdapter(adapter);

                                        // Layout manager 추가
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);

                                        adapter.setOnItemClickListener_cafeDetail(new CafeDetailAdapter.OnItemClickEventListener_cafeDetail() {
                                            @Override
                                            public void onItemClick(View view, int position) {

                                        if(cafeDetailReviewItem.size() == 0){
                                            Toast.makeText(getContext().getApplicationContext(), "작성된 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            // 리뷰 더보기 클릭 시,
                                            if(position == cafeDetailReviewItem.size()){
                                                Toast.makeText(getContext().getApplicationContext(), "리뷰 더보기 클릭", Toast.LENGTH_SHORT).show();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("cafeNum", get_cafe_num.toString());
                                                //bundle.putString("name",moreReview3.getText().toString());
                                                navController.navigate(R.id.cafe_detail_to_cafe_detail_more, bundle);
                                            }

                                            // 리뷰 클릭 시,
                                            else {
                                                final CafeDetailItem item = cafeDetailReviewItem.get(position);
                                                Toast.makeText(getContext().getApplicationContext(), item.getReviewNickName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });

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



        // 카페 운영 시간 가져오기
//        Bundle cafeModifyBundle = getArguments();
//        if(cafeModifyBundle != null){
//            if(cafeModifyBundle.getString("time_open_Modi") != null ){
//                moreReview10.setText(cafeModifyBundle.getString("time_open_Modi"));
//                moreReview8.setText(cafeModifyBundle.getString("time_close_Modi"));
//            }
//        }

        // 카페 수정(연필) 버튼 클릭 시,
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


        // 리뷰 플로팅 버튼 클릭 시,
        review_floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("cafeName", moreReview2.getText().toString());
                navController.navigate(R.id.cafe_detail_to_review, bundle);
            }
        });


        // 카페디테일에 해당하는 카페이미지 보여주기
        cafeImageViewPager = root.findViewById(R.id.cafeImageViewPager);
        cafeImageViewPager.setOffscreenPageLimit(5);
        imageList = new ArrayList<>();

        imageList.add(R.drawable.logo);
        imageList.add(R.drawable.logo_v2);
        imageList.add(R.drawable.bean_grade1);
        imageList.add(R.drawable.bean_grade2);
        imageList.add(R.drawable.bean_grade3);

        cafeImageViewPager.setAdapter(new CafeDetailImageViewPagerAdapter(getContext().getApplicationContext(), imageList));
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



        // 카페디테일에 해당하는 카페별점 보여주기
        cafeRatingViewPager = root.findViewById(R.id.ratingViewPager);
        cafeRatingViewPager.setOffscreenPageLimit(3);
        ratingList = new ArrayList<>();

        CafeDetailRatingItem taste = new CafeDetailRatingItem("맛", "산미", "쓴맛", "디저트", "기타음료", R.drawable.taste_score, "3", "1", "2", "5");
        CafeDetailRatingItem seat = new CafeDetailRatingItem("좌석", "2인좌석", "4인좌석", "화장실", "다인좌석", R.drawable.sit_score, "1", "5", "1", "3");
        CafeDetailRatingItem study = new CafeDetailRatingItem("스터디", "와이파이", "콘센트", "조명", "조용함", R.drawable.study_score, "3", "5", "5", "5");

        ratingList.add(taste);
        ratingList.add(seat);
        ratingList.add(study);

        cafeRatingViewPager.setAdapter(new CafeDetailRatingViewPagerAdapter(getContext().getApplicationContext(), ratingList));
//        CafeDetailRatingPagerAdapter cafeRatingAdapter = new CafeDetailRatingPagerAdapter(getActivity().getSupportFragmentManager());
//
//        RatingViewPagerTaste tasteRating = new RatingViewPagerTaste();
//        cafeRatingAdapter.cafeRatingAddItem(tasteRating);
//        RatingViewPagerSeat seatRating = new RatingViewPagerSeat();
//        cafeRatingAdapter.cafeRatingAddItem(seatRating);
//        RatingViewPagerStudy studyRating = new RatingViewPagerStudy();
//        cafeRatingAdapter.cafeRatingAddItem(studyRating);


        // 별점에서 좌측 버튼 클릭 시, 별점 페이지 넘어감
        cafeDetail_favorite_previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNum = cafeRatingViewPager.getCurrentItem();
                cafeRatingViewPager.setCurrentItem(pageNum - 1, true);
            }
        });


        // 별점에서 우측 버튼 클릭 시, 별점 페이지 넘어감
        cafeDetail_favorite_nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNum = cafeRatingViewPager.getCurrentItem();
                cafeRatingViewPager.setCurrentItem(pageNum + 1, true);
            }
        });


        return root;
    }

    // 카페디테일 카페이미지 뷰페이저
//    class CafeDetailImagePagerAdapter extends FragmentStatePagerAdapter {
//
//        ArrayList<Fragment> imageItems = new ArrayList<>();
//        public CafeDetailImagePagerAdapter(FragmentManager fm){
//            super(fm);
//        }
//
//        public void cafeImageAddItem(Fragment item){
//            imageItems.add(item);   // Fragment 추가
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return imageItems.get(position);    // 프래그먼트 가져오기
//        }
//
//        @Override
//        public int getCount() {
//            return imageItems.size();   // 프래그먼트 개수반환
//        }
//    }


    // 카페디테일 별점 뷰페이저
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
        super.onDestroyView();
        binding = null;
    }


}
