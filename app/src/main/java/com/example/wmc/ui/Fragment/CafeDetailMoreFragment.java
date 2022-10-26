package com.example.wmc.ui.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.wmc.CafeDetailMore.CafeDetailMoreAdapter;
import com.example.wmc.CafeDetailMore.CafeDetailMoreItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Love;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
import com.example.wmc.database.ReviewImage;
import com.example.wmc.databinding.FragmentCafeDetailMoreBinding;
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

public class CafeDetailMoreFragment extends Fragment {

    private FragmentCafeDetailMoreBinding binding;
    Spinner reviewMore_spinner;

    ArrayList<Review> review_list;
    ArrayList<Personal> personal_list;
    ArrayList<Love> love_list;
    ArrayList<ReviewImage> reviewImage_list;

    Long mem_num = MainActivity.mem_num;
    Long get_cafe_num;
    String create_date;
    ArrayList<String> reviewImage = new ArrayList<>();
    int reviewImageCounter = 0;

    boolean love_flag = false;

    String[] spinnerItem = {"최신순", "오래된 순", "좋아요 많은 순", "좋아요 적은 순"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Bundle cafeNumBundle = getArguments();
        if(cafeNumBundle != null){
            if(cafeNumBundle.getString("cafeNum") != null){
                get_cafe_num = Long.parseLong(cafeNumBundle.getString("cafeNum"));
            }
        }

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String get_review_url = getResources().getString(R.string.url) + "review";

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


                String get_personal_url = getResources().getString(R.string.url) + "personal";

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


                        String get_love_url = getResources().getString(R.string.url) + "love";

                        StringRequest love_stringRequest = new StringRequest(Request.Method.GET, get_love_url, new Response.Listener<String>() {
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
                                Type listType = new TypeToken<ArrayList<Love>>() {
                                }.getType();

                                love_list = gson.fromJson(changeString, listType);


                                ArrayList<CafeDetailMoreItem> cafeDetailMoreReviewItem = new ArrayList<>();


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


                                        for (Review r : review_list) {
                                            if (r.getCafeNum().equals(get_cafe_num)) {
                                                love_flag = false;

                                                // DB에서 받아온 리뷰 생성 시간을 변경하기 위한 코드
                                                String creatTime = r.getCreateTime();
                                                SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                                old_format.setTimeZone(TimeZone.getTimeZone("KST"));
                                                SimpleDateFormat new_format = new SimpleDateFormat("yyyy/MM/dd   HH:mm");

                                                try {
                                                    Date old_date = old_format.parse(creatTime);
                                                    create_date = new_format.format(old_date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                reviewImageCounter = 0;

                                                // 리뷰 이미지가 있을때
                                                for(ReviewImage ri : reviewImage_list) {
                                                    if(ri.getReviewNum().equals(r.getReviewNum()) && reviewImageCounter < 3){
                                                        if(ri.getFileUrl().isEmpty()){
                                                            break;
                                                        }
                                                        reviewImage.add(ri.getFileUrl());
                                                        reviewImageCounter++;
                                                    }
                                                }

                                                // 리뷰 이미지가 없을때
                                                if(!(reviewImage.size() == 3)) {
                                                    //(나중에 로고 올리고 바꾸기)
                                                    while(reviewImage.size() < 3){
                                                        reviewImage.add(getString(R.string.default_Review_Cafeimage));
                                                    }
                                                }


                                                for (Personal p : personal_list) {

                                                    String personalProfile = "";

                                                    // 프로필 사진을 설정했는지 확인하는 곳(설정 X시, 기본 프로필 사진으로 설정)
                                                    if(p.getProfileimageurl() == null)
                                                        // 기본 이미지 URL입력하면 됨(현재 뚱이사진 예시)
                                                        personalProfile = getString(R.string.default_Profileimage);
                                                    else
                                                        personalProfile = p.getProfileimageurl();

                                                    // 1. 어플 사용자가 해당 카페에 대한 리뷰를 작성한 경우, 리사이클러뷰 가장 처음에 나오도록 설정
                                                    if (r.getMemNum().equals(mem_num) && p.getMemNum().equals(mem_num)) {
                                                        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                                r.getReviewText(), create_date, personalProfile, reviewImage.get(0), reviewImage.get(1), reviewImage.get(2), r.getLikeCount().toString(), true, false, get_cafe_num, mem_num, -1L, r.getReviewNum(), r.getLocationCheck()));
                                                    }

                                                    // 2. 리뷰 작성자들의 닉네임, 회원 등급을 포함한 리뷰 Item 작성
                                                    else if (r.getMemNum().equals(p.getMemNum())) {
                                                        if (!love_list.isEmpty()) {
                                                            for (Love l : love_list) {
                                                                // love 테이블에 reviewNum이 같은 경우 && love 테이블에 사용자의 memNum이 같은 경우
                                                                if (l.getReviewNum().equals(r.getReviewNum()) && l.getMemNum().equals(mem_num)) {
                                                                    love_flag = true;
                                                                    cafeDetailMoreReviewItem.add(new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                                            r.getReviewText(), create_date, personalProfile, reviewImage.get(0), reviewImage.get(1), reviewImage.get(2), r.getLikeCount().toString(), false, true, get_cafe_num, mem_num, l.getLoveNum(), r.getReviewNum(), r.getLocationCheck()));
                                                                }
                                                            }
                                                        }else{
                                                            cafeDetailMoreReviewItem.add(new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                                    r.getReviewText(), create_date, personalProfile, reviewImage.get(0), reviewImage.get(1), reviewImage.get(2), r.getLikeCount().toString(), false, false, get_cafe_num, mem_num, -1L, r.getReviewNum(), r.getLocationCheck()));
                                                        }
                                                    }
                                                }
                                            }
                                            if(!(reviewImage.isEmpty())) {
                                                reviewImage.clear();
                                            }
                                        }

                                        // Recycler view
                                        RecyclerView cafeDetailMoreRecyclerView = root.findViewById(R.id.cafeDetailMoreRecyclerView);

                                        // Adapter 추가
                                        CafeDetailMoreAdapter cafeDetailMoreAdapter = new CafeDetailMoreAdapter(getContext(), cafeDetailMoreReviewItem, CafeDetailMoreFragment.this);
                                        cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);

                                        // Layout manager 추가
                                        LinearLayoutManager cafeDetailMoreLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);

                                        // 리뷰 아이템 선택 시,
                                        cafeDetailMoreAdapter.setOnItemClickListener_cafeDetailMore(new CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore() {
                                            @Override
                                            public void onItemClick(View a_view, int a_position) {
                                            }
                                        });

                                        reviewMore_spinner = root.findViewById(R.id.reviewSpinner);
                                        reviewMore_spinner.setPrompt("정렬기준");

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_custom, spinnerItem);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        reviewMore_spinner.setAdapter(adapter);


                                        // 스피너 아이템 선택 시, 리뷰 정렬(최신순, 오래된 순, 좋아요 많은 순, 좋아요 적은 순,
                                        reviewMore_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                Toast.makeText(getContext().getApplicationContext(), spinnerItem[position], Toast.LENGTH_SHORT).show();

                                                if (spinnerItem[position].equals("최신순")) {   // 최신순으로 정렬
                                                    Comparator<CafeDetailMoreItem> recently = new Comparator<CafeDetailMoreItem>() {

                                                        @Override
                                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                                            return item2.getReviewMore_writeTime().compareTo(item1.getReviewMore_writeTime());
                                                        }
                                                    };

                                                    Collections.sort(cafeDetailMoreReviewItem, recently);
                                                    adapter.notifyDataSetChanged();
                                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                                } else if (spinnerItem[position].equals("오래된 순")) {   // 오래된 순으로 정렬
                                                    Comparator<CafeDetailMoreItem> old = new Comparator<CafeDetailMoreItem>() {

                                                        @Override
                                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                                            return item1.getReviewMore_writeTime().compareTo(item2.getReviewMore_writeTime());
                                                        }
                                                    };

                                                    Collections.sort(cafeDetailMoreReviewItem, old);
                                                    adapter.notifyDataSetChanged();
                                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                                } else if (spinnerItem[position].equals("좋아요 많은 순")) {   // 좋아요 많은 순으로 정렬
                                                    Comparator<CafeDetailMoreItem> many = new Comparator<CafeDetailMoreItem>() {

                                                        @Override
                                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                                            int ret;

                                                            if (Integer.parseInt(item1.getGood_count_textView()) > Integer.parseInt(item2.getGood_count_textView()))
                                                                ret = -1;
                                                            else if (Integer.parseInt(item1.getGood_count_textView()) == Integer.parseInt(item2.getGood_count_textView()))
                                                                ret = 0;
                                                            else
                                                                ret = 1;

                                                            return ret;
                                                        }
                                                    };

                                                    Collections.sort(cafeDetailMoreReviewItem, many);
                                                    adapter.notifyDataSetChanged();
                                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                                } else if (spinnerItem[position].equals("좋아요 적은 순")) {  // 좋아요 적은 순으로 정렬
                                                    Comparator<CafeDetailMoreItem> little = new Comparator<CafeDetailMoreItem>() {

                                                        @Override
                                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                                            int ret;

                                                            if (Integer.parseInt(item1.getGood_count_textView()) < Integer.parseInt(item2.getGood_count_textView()))
                                                                ret = -1;
                                                            else if (Integer.parseInt(item1.getGood_count_textView()) == Integer.parseInt(item2.getGood_count_textView()))
                                                                ret = 0;
                                                            else
                                                                ret = 1;

                                                            return ret;
                                                        }
                                                    };

                                                    Collections.sort(cafeDetailMoreReviewItem, little);
                                                    adapter.notifyDataSetChanged();
                                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                // 스피너 아이템을 클릭하지 않았을 경우
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
                        Log.e("cafeDetailMore_personal_stringRequest_error",error.toString());
                    }
                });
                requestQueue.add(personal_stringRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cafeDetailMore_review_stringRequest_error",error.toString());
            }
        });


        requestQueue.add(review_stringRequest);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();







        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
