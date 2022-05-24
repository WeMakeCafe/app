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
import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailMore.CafeDetailMoreAdapter;
import com.example.wmc.CafeDetailMore.CafeDetailMoreItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Personal;
import com.example.wmc.database.Review;
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

    Long mem_num = MainActivity.mem_num;
    Long get_cafe_num;
    String create_date;

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

                        ArrayList<CafeDetailMoreItem> cafeDetailMoreReviewItem = new ArrayList<>();

                        for(Review r : review_list) {
                            if (r.getCafeNum().equals(get_cafe_num)) {


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


                                for (Personal p : personal_list) {
                                    // 1. 어플 사용자가 해당 카페에 대한 리뷰를 작성한 경우, 리사이클러뷰 가장 처음에 나오도록 설정
                                    if (r.getMemNum().equals(mem_num) && p.getMemNum().equals(mem_num)) {
                                        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                r.getReviewText(), create_date, R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, r.getLikeCount().toString(), true, get_cafe_num, mem_num));
                                    }

                                    // 2. 리뷰 작성자들의 닉네임, 회원 등급을 포함한 리뷰 Item 작성
                                    else if (r.getMemNum().equals(p.getMemNum())) {
                                        cafeDetailMoreReviewItem.add(new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                r.getReviewText(), create_date, R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, r.getLikeCount().toString(), false, get_cafe_num, mem_num));
                                    }
                                }
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

                                if(spinnerItem[position].equals("최신순")) {   // 최신순으로 정렬
                                    Comparator<CafeDetailMoreItem> recently = new Comparator<CafeDetailMoreItem>() {

                                        @Override
                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                            return item2.getReviewMore_writeTime().compareTo(item1.getReviewMore_writeTime());
                                        }
                                    };

                                    Collections.sort(cafeDetailMoreReviewItem, recently) ;
                                    adapter.notifyDataSetChanged() ;
                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                }

                                else if(spinnerItem[position].equals("오래된 순")) {   // 오래된 순으로 정렬
                                    Comparator<CafeDetailMoreItem> old = new Comparator<CafeDetailMoreItem>() {

                                        @Override
                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                            return item1.getReviewMore_writeTime().compareTo(item2.getReviewMore_writeTime());
                                        }
                                    };

                                    Collections.sort(cafeDetailMoreReviewItem, old) ;
                                    adapter.notifyDataSetChanged() ;
                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                }

                                else if(spinnerItem[position].equals("좋아요 많은 순")){   // 좋아요 많은 순으로 정렬
                                    Comparator<CafeDetailMoreItem> many = new Comparator<CafeDetailMoreItem>() {

                                        @Override
                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                            int ret ;

                                            if (Integer.parseInt(item1.getGood_count_textView()) > Integer.parseInt(item2.getGood_count_textView()))
                                                ret = -1 ;
                                            else if (Integer.parseInt(item1.getGood_count_textView()) == Integer.parseInt(item2.getGood_count_textView()))
                                                ret = 0 ;
                                            else
                                                ret = 1 ;

                                            return ret ;
                                        }
                                    };

                                    Collections.sort(cafeDetailMoreReviewItem, many) ;
                                    adapter.notifyDataSetChanged() ;
                                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                                }

                                else if(spinnerItem[position].equals("좋아요 적은 순")){  // 좋아요 적은 순으로 정렬
                                    Comparator<CafeDetailMoreItem> little = new Comparator<CafeDetailMoreItem>() {

                                        @Override
                                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                                            int ret ;

                                            if (Integer.parseInt(item1.getGood_count_textView()) < Integer.parseInt(item2.getGood_count_textView()))
                                                ret = -1 ;
                                            else if (Integer.parseInt(item1.getGood_count_textView()) == Integer.parseInt(item2.getGood_count_textView()))
                                                ret = 0 ;
                                            else
                                                ret = 1 ;

                                            return ret ;
                                        }
                                    };

                                    Collections.sort(cafeDetailMoreReviewItem, little) ;
                                    adapter.notifyDataSetChanged() ;
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





        /*// 아이템 추가 시, 앞에 index 0을 붙이면 최근에 쓴 아이템 부터 보이기 시작함.
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("아이유", "Lv.1(위치인증완료)", "징짜 맛있음\n징짜 맛있음\n징짜 맛있음",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "1"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("애쉬", "Lv.3", "테이블이 매우 협소합니다. \n" +
                "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                "마카롱이 진짜 최고에요ㅠ",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "7"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("스키니", "Lv.3", "테이블이 매우 협소합니다. \n" +
                "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                "마카롱이 진짜 최고에요ㅠ",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "3"));*/


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
