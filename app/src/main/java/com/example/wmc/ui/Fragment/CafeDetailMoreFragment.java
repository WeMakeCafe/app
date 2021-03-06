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
import com.example.wmc.database.Love;
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
    ArrayList<Love> love_list;

    Long mem_num = MainActivity.mem_num;
    Long get_cafe_num;
    String create_date;

    boolean love_flag = false;

    String[] spinnerItem = {"?????????", "????????? ???", "????????? ?????? ???", "????????? ?????? ???"};

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
                                    changeString = new String(response.getBytes("8859_1"), "utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                Type listType = new TypeToken<ArrayList<Love>>() {
                                }.getType();

                                love_list = gson.fromJson(changeString, listType);


                                ArrayList<CafeDetailMoreItem> cafeDetailMoreReviewItem = new ArrayList<>();

                                for (Review r : review_list) {
                                    if (r.getCafeNum().equals(get_cafe_num)) {
                                        love_flag = false;

                                        // DB?????? ????????? ?????? ?????? ????????? ???????????? ?????? ??????
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


                                        for (Personal p : personal_list) {

                                            String personalProfile = "";

                                            // ????????? ????????? ??????????????? ???????????? ???(?????? X???, ?????? ????????? ???????????? ??????)
                                            if(p.getProfileImageUrl().equals(""))
                                                // ?????? ????????? URL???????????? ???(?????? ???????????? ??????)
                                                personalProfile = "https://w.namu.la/s/0c6301df01fc4f180ec65717bad3d0254258abf0be33299e55df7c261040f517518eb9008a1a2cd3d7b8b7777d70182c185bc891b1054dc57b11cc46fd29130a3474f1b75b816024dfdc16b692a0c77c";
                                            else
                                                personalProfile = p.getProfileImageUrl();

                                            // 1. ?????? ???????????? ?????? ????????? ?????? ????????? ????????? ??????, ?????????????????? ?????? ????????? ???????????? ??????
                                            if (r.getMemNum().equals(mem_num) && p.getMemNum().equals(mem_num)) {
                                                cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                        r.getReviewText(), create_date, personalProfile, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, r.getLikeCount().toString(), true, false, get_cafe_num, mem_num, -1L, r.getReviewNum()));
                                            }

                                            // 2. ?????? ??????????????? ?????????, ?????? ????????? ????????? ?????? Item ??????
                                            else if (r.getMemNum().equals(p.getMemNum())) {
                                                if (!love_list.isEmpty()) {
                                                    for (Love l : love_list) {
                                                        // love ???????????? reviewNum??? ?????? ?????? && love ???????????? ???????????? memNum??? ?????? ??????
                                                        if (l.getReviewNum().equals(r.getReviewNum()) && l.getMemNum().equals(mem_num)) {
                                                            Log.d("love_for_if_test", "love_for_if_test");
                                                            love_flag = true;
                                                            cafeDetailMoreReviewItem.add(new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                                    r.getReviewText(), create_date, personalProfile, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, r.getLikeCount().toString(), false, true, get_cafe_num, mem_num, l.getLoveNum(), r.getReviewNum()));
                                                        }
                                                    }
                                                }else{
                                                    cafeDetailMoreReviewItem.add(new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                            r.getReviewText(), create_date, personalProfile, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, r.getLikeCount().toString(), false, false, get_cafe_num, mem_num, -1L, r.getReviewNum()));
                                                }
                                                if(!love_flag){
                                                    Log.d("!love_flag", "!love_flag");

                                                    cafeDetailMoreReviewItem.add(new CafeDetailMoreItem(p.getNickName(), p.getGrade().toString(),
                                                            r.getReviewText(), create_date, personalProfile, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, r.getLikeCount().toString(), false, false, get_cafe_num, mem_num, -1L, r.getReviewNum()));
                                                }
                                            }
                                        }
                                    }
                                }

                                // Recycler view
                                RecyclerView cafeDetailMoreRecyclerView = root.findViewById(R.id.cafeDetailMoreRecyclerView);

                                // Adapter ??????
                                CafeDetailMoreAdapter cafeDetailMoreAdapter = new CafeDetailMoreAdapter(getContext(), cafeDetailMoreReviewItem, CafeDetailMoreFragment.this);
                                cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);

                                // Layout manager ??????
                                LinearLayoutManager cafeDetailMoreLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);

                                // ?????? ????????? ?????? ???,
                                cafeDetailMoreAdapter.setOnItemClickListener_cafeDetailMore(new CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore() {
                                    @Override
                                    public void onItemClick(View a_view, int a_position) {
                                    }
                                });

                                reviewMore_spinner = root.findViewById(R.id.reviewSpinner);
                                reviewMore_spinner.setPrompt("????????????");

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_custom, spinnerItem);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                reviewMore_spinner.setAdapter(adapter);


                                // ????????? ????????? ?????? ???, ?????? ??????(?????????, ????????? ???, ????????? ?????? ???, ????????? ?????? ???,
                                reviewMore_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(getContext().getApplicationContext(), spinnerItem[position], Toast.LENGTH_SHORT).show();

                                        if (spinnerItem[position].equals("?????????")) {   // ??????????????? ??????
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
                                        } else if (spinnerItem[position].equals("????????? ???")) {   // ????????? ????????? ??????
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
                                        } else if (spinnerItem[position].equals("????????? ?????? ???")) {   // ????????? ?????? ????????? ??????
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
                                        } else if (spinnerItem[position].equals("????????? ?????? ???")) {  // ????????? ?????? ????????? ??????
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
                                        // ????????? ???????????? ???????????? ????????? ??????
                                    }
                                });
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





        /*// ????????? ?????? ???, ?????? index 0??? ????????? ????????? ??? ????????? ?????? ????????? ?????????.
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("??????", "Lv.3", "???????????? ?????? ???????????????.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("?????????", "Lv.1(??????????????????)", "?????? ?????????\n?????? ?????????\n?????? ?????????",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "1"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("??????", "Lv.3", "???????????? ?????? ???????????????. \n" +
                "?????????, ????????? ?????? ???????????? ???????????????!\n" +
                "???????????? ?????? ???????????????",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "7"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("?????????", "Lv.3", "???????????? ?????? ???????????????. \n" +
                "?????????, ????????? ?????? ???????????? ???????????????!\n" +
                "???????????? ?????? ???????????????",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "3"));*/


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
