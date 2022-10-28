package com.example.wmc.CafeDetail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

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
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Love;
import com.example.wmc.database.Review;
import com.example.wmc.database.ReviewImage;
import com.example.wmc.ui.Fragment.CafeDetailFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<CafeDetailItem> review_items;
    CafeDetailFragment cafeDetailFragment;

    int clicked_good = 0;

    private static NavController navController;

    ArrayList<Review> review_list;
    ArrayList<ReviewImage> reviewImage_list = new ArrayList<>();
    ArrayList<Love> love_list;
    ArrayList<Cafe> cafe_list;

    Long delete_ReviewImageNum;

    Long mem_num = MainActivity.mem_num; // 임시 유저 넘버
    Long get_review_num = 0L;
    Long get_love_num = 0L; // love 넘버 일시 저장
    Long counter = 0L; // love 넘버 + 카운터를 통해 같은 화면에서 여러번 좋아요 버튼 누를 수 있게.
    Integer score1 = 0; // 본래의 리뷰에 있던 점수 보관용
    Integer score2 = 0;
    Integer score3 = 0;
    Integer score4 = 0;
    Integer score5 = 0;
    Integer score6 = 0;
    Integer score7 = 0;
    Integer score8 = 0;
    Integer score9 = 0;
    Integer score10 = 0;
    Integer score11 = 0;
    Integer score12 = 0;
    Long[] k = new Long[36];

    public interface OnItemClickEventListener_cafeDetail { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeDetailAdapter.OnItemClickEventListener_cafeDetail mItemClickListener_cafeDetail;    // 인터페이스 객체 생성

    public CafeDetailAdapter(Context context, ArrayList<CafeDetailItem> list, CafeDetailFragment cafeDetailFragment){
        this.context = context;
        review_items = list;
        this.cafeDetailFragment = cafeDetailFragment;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup a_viewGroup, int a_viewType) {

        View view = LayoutInflater.from(a_viewGroup.getContext()).inflate(a_viewType, a_viewGroup, false);

        final RecyclerView.ViewHolder viewHolder;

        if (a_viewType == CafeDetailFooterViewHolder.MORE_VIEW_TYPE) {
            viewHolder = new CafeDetailFooterViewHolder(view, mItemClickListener_cafeDetail);
        } else {
            viewHolder = new CafeDetailViewHolder(view, mItemClickListener_cafeDetail);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        if (a_holder instanceof CafeDetailFooterViewHolder) {
            CafeDetailFooterViewHolder footerViewHolder = (CafeDetailFooterViewHolder) a_holder;
        } else {
            // 기본적으로 header 를 빼고 item 을 구한다.
            final CafeDetailItem item = review_items.get(a_position);
            CafeDetailViewHolder viewHolder = (CafeDetailViewHolder) a_holder;

            if(item.isLocationCheck_flag()) {
                viewHolder.location_false_image.setVisibility(View.INVISIBLE);
                viewHolder.location_true_image.setVisibility(View.VISIBLE);
            }

            viewHolder.reviewNickName.setText(item.getReviewNickName());
            viewHolder.level_and_location.setText(item.getLevel_and_location());
            viewHolder.review_comment.setText(item.getReview_comment());
            viewHolder.review_writeTime.setText(item.getReview_writeTime());
            viewHolder.good_count_textView.setText(item.getGood_count_textView());

            Glide.with(cafeDetailFragment.getActivity()).load(item.getReviewProfile_image()).into(viewHolder.reviewProfile_image);
            Glide.with(cafeDetailFragment.getActivity()).load(item.getReviewImage()).into(viewHolder.reviewImage);

            viewHolder.check_user_flag = (item.getCheck_user_flag());
            viewHolder.check_love_flag = (item.getCheck_love_flag());
            for(int i = 0 ; i<=35; i++){
                k[i] = (long) 0;
            }
            // 키워드가 적용x, 카테고리 2점씩빠짐
            // 리뷰 작성자와 로그인한 사람이 같을 때,
            if (viewHolder.check_user_flag) {
                viewHolder.reviewModify.setVisibility(View.VISIBLE);
                viewHolder.reviewModifyLine.setVisibility(View.VISIBLE);
                viewHolder.reviewDelete.setVisibility(View.VISIBLE);
                viewHolder.reviewDeleteLine.setVisibility(View.VISIBLE);
                viewHolder.good_button_image.setVisibility(View.VISIBLE);
                viewHolder.good_button.setVisibility(View.INVISIBLE);

                // 리뷰에서 수정 버튼 클릭 시,
                viewHolder.reviewModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController = Navigation.findNavController(v);
                        Bundle bundle = new Bundle();
                        bundle.putLong("cafeNum", item.getGet_cafe_num());
                        bundle.putLong("memNum", item.getMem_num());
                        bundle.putLong("reviewNum", item.getGet_review_num());
                        bundle.putBoolean("cafeDetail_reviewModify_flag", true);

                        navController.navigate(R.id.cafe_detail_to_review, bundle);
                    }
                });

                // 리뷰에서 삭제 버튼 클릭 시,
                viewHolder.reviewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(cafeDetailFragment.getActivity());
                        builder.setTitle("리뷰 삭제").setMessage("리뷰를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                        builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // DB에서 테이블 삭제 하는 코드 추가하기
                                RequestQueue requestQueue;
                                Cache cache = new DiskBasedCache(cafeDetailFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                                Network network = new BasicNetwork(new HurlStack());
                                requestQueue = new RequestQueue(cache, network);
                                requestQueue.start();
                                String review_url = cafeDetailFragment.getResources().getString(R.string.url) + "review";

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, review_url, new Response.Listener<String>() {
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

                                        // cafe 테이블의 튜플이 제대로 오는지 확인 (테스트 할 때만 만들어두고 해당 기능 다 개발 시 제거하는게 좋음)

                                        for(Review r : review_list){
                                            if(item.get_review_num.equals(r.getReviewNum())) {
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
                                                k[0] = r.getKeyword1();
                                                k[1] = r.getKeyword2();
                                                k[2] = r.getKeyword3();
                                                k[3] = r.getKeyword4();
                                                k[4] = r.getKeyword5();
                                                k[5] = r.getKeyword6();
                                                k[6] = r.getKeyword7();
                                                k[7] = r.getKeyword8();
                                                k[8] = r.getKeyword9();
                                                k[9] = r.getKeyword10();
                                                k[10] = r.getKeyword11();
                                                k[11] = r.getKeyword12();
                                                k[12] = r.getKeyword13();
                                                k[13] = r.getKeyword14();
                                                k[14] = r.getKeyword15();
                                                k[15] = r.getKeyword16();
                                                k[16] = r.getKeyword17();
                                                k[17] = r.getKeyword18();
                                                k[18] = r.getKeyword19();
                                                k[19] = r.getKeyword20();
                                                k[20] = r.getKeyword21();
                                                k[21] = r.getKeyword22();
                                                k[22] = r.getKeyword23();
                                                k[23] = r.getKeyword24();
                                                k[24] = r.getKeyword25();
                                                k[25] = r.getKeyword26();
                                                k[26] = r.getKeyword27();
                                                k[27] = r.getKeyword28();
                                                k[28] = r.getKeyword29();
                                                k[29] = r.getKeyword30();
                                                k[30] = r.getKeyword31();
                                                k[31] = r.getKeyword32();
                                                k[32] = r.getKeyword33();
                                                k[33] = r.getKeyword34();
                                                k[34] = r.getKeyword35();
                                                k[35] = r.getKeyword36();
                                                // 리뷰 정보들 받아서 카페 테이블 put연산 해주어야함
                                                String delete_review = cafeDetailFragment.getResources().getString(R.string.url) + "review/" + item.getGet_review_num().toString();

                                                StringRequest delete_review_stringRequest = new StringRequest(Request.Method.DELETE, delete_review, new Response.Listener<String>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Toast.makeText(v.getContext().getApplicationContext(), "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

//                                                        AlertDialog.Builder builder = new AlertDialog.Builder(cafeDetailFragment.getActivity());
//                                                        builder.setTitle("리뷰 삭제").setMessage("리뷰가 삭제되었습니다.").setIcon(R.drawable.logo);
//
//                                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int id)
//                                                            {
//
//                                                            }
//                                                        });
//
//                                                        AlertDialog alertDialog = builder.create();
//                                                        alertDialog.show();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("delete_error",error.toString());
                                                    }
                                                });
                                                requestQueue.add(delete_review_stringRequest);
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                                        Log.e("test_error", error.toString());
                                    }
                                });
                                requestQueue.add(stringRequest);
                                review_items.remove(item);    // 리사이클러뷰에서도 아이템 삭제
                                notifyDataSetChanged();


                                // 리뷰 이미지 삭제
                                String get_reviewImage_url = cafeDetailFragment.getResources().getString(R.string.url) + "reviewImage";

                                StringRequest delete_reviewImageRequest = new StringRequest(Request.Method.GET, get_reviewImage_url, new Response.Listener<String>() {
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
                                        Type listType = new TypeToken<ArrayList<ReviewImage>>() {
                                        }.getType();

                                        reviewImage_list = gson.fromJson(changeString, listType);

                                        for (ReviewImage ri : reviewImage_list) {
                                            if (ri.getReviewNum().equals(item.getGet_review_num())) {
                                                delete_ReviewImageNum = ri.getrimageNum();

                                                // 서버에서 이미지 삭제
                                                String delete_reviewImage_URL = cafeDetailFragment.getResources().getString(R.string.url) + "reviewImage/" + delete_ReviewImageNum.toString();

                                                StringRequest delete_reviewImage_stringRequest = new StringRequest(Request.Method.DELETE, delete_reviewImage_URL, new Response.Listener<String>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Toast.makeText(v.getContext().getApplicationContext(), "리뷰 이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("delete_error", error.toString());
                                                    }
                                                });
                                                requestQueue.add(delete_reviewImage_stringRequest);
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                                        Log.e("test_error", error.toString());
                                    }
                                });
                                requestQueue.add(delete_reviewImageRequest);
                                // 여기까지 리뷰 이미지 삭제 코드



                                String cafe_url = cafeDetailFragment.getResources().getString(R.string.url) + "cafe";
                                StringRequest stringRequest2= new StringRequest(Request.Method.GET, cafe_url, new Response.Listener<String>() {
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
                                        Type listType = new TypeToken<ArrayList<Cafe>>() {
                                        }.getType();

                                        cafe_list = gson.fromJson(changeString, listType);

                                        // cafe 테이블의 튜플이 제대로 오는지 확인 (테스트 할 때만 만들어두고 해당 기능 다 개발 시 제거하는게 좋음)
                                        Log.d("test", String.valueOf(cafe_list.size()));

                                        for (Cafe c : cafe_list) {
                                            if (c.getCafeNum().equals(item.get_cafe_num)) {  //bundle에서 가져온 카페아이디값 cafe_name에 넣어서 비교 연산
                                                Map map2 = new HashMap();
                                                map2.put("cafeName", c.getCafeName());
                                                map2.put("cafeAddress", c.getCafeAddress());
                                                map2.put("openTime", c.getOpenTime());
                                                map2.put("closeTime", c.getCloseTime());
                                                map2.put("scoreNum", c.getScoreNum());
                                                map2.put("keyword1", c.getKeyword1() - k[0]);
                                                map2.put("keyword2", c.getKeyword2() - k[1]);
                                                map2.put("keyword3", c.getKeyword3() - k[2]);
                                                map2.put("keyword4", c.getKeyword4() - k[3]);
                                                map2.put("keyword5", c.getKeyword5() - k[4]);
                                                map2.put("keyword6", c.getKeyword6() - k[5]);
                                                map2.put("keyword7", c.getKeyword7() - k[6]);
                                                map2.put("keyword8", c.getKeyword8() - k[7]);
                                                map2.put("keyword9", c.getKeyword9() - k[8]);
                                                map2.put("keyword10", c.getKeyword10() - k[9]);
                                                map2.put("keyword11", c.getKeyword11() - k[10]);
                                                map2.put("keyword12", c.getKeyword12() - k[11]);
                                                map2.put("keyword13", c.getKeyword13() - k[12]);
                                                map2.put("keyword14", c.getKeyword14() - k[13]);
                                                map2.put("keyword15", c.getKeyword15() - k[14]);
                                                map2.put("keyword16", c.getKeyword16() - k[15]);
                                                map2.put("keyword17", c.getKeyword17() - k[16]);
                                                map2.put("keyword18", c.getKeyword18() - k[17]);
                                                map2.put("keyword19", c.getKeyword19() - k[18]);
                                                map2.put("keyword20", c.getKeyword20() - k[19]);
                                                map2.put("keyword21", c.getKeyword21() - k[20]);
                                                map2.put("keyword22", c.getKeyword22() - k[21]);
                                                map2.put("keyword23", c.getKeyword23() - k[22]);
                                                map2.put("keyword24", c.getKeyword24() - k[23]);
                                                map2.put("keyword25", c.getKeyword25() - k[24]);
                                                map2.put("keyword26", c.getKeyword26() - k[25]);
                                                map2.put("keyword27", c.getKeyword27() - k[26]);
                                                map2.put("keyword28", c.getKeyword28() - k[27]);
                                                map2.put("keyword29", c.getKeyword29() - k[28]);
                                                map2.put("keyword30", c.getKeyword30() - k[29]);
                                                map2.put("keyword31", c.getKeyword31() - k[30]);
                                                map2.put("keyword32", c.getKeyword32() - k[31]);
                                                map2.put("keyword33", c.getKeyword33() - k[32]);
                                                map2.put("keyword34", c.getKeyword34() - k[33]);
                                                map2.put("keyword35", c.getKeyword35() - k[34]);
                                                map2.put("keyword36", c.getKeyword36() - k[35]);
                                                map2.put("tastePoint1", c.getTastePoint1() - score1);
                                                map2.put("tastePoint2", c.getTastePoint2() - score2);
                                                map2.put("tastePoint3", c.getTastePoint3() - score3);
                                                map2.put("tastePoint4", c.getTastePoint4() - score4);
                                                map2.put("seatPoint1", c.getSeatPoint1() - score5);
                                                map2.put("seatPoint2", c.getSeatPoint2() - score6);
                                                map2.put("seatPoint3", c.getSeatPoint3() - score7);
                                                map2.put("seatPoint4", c.getSeatPoint4() - score8);
                                                map2.put("studyPoint1", c.getStudyPoint1() - score9);
                                                map2.put("studyPoint2", c.getStudyPoint2() - score10);
                                                map2.put("studyPoint3", c.getStudyPoint3() - score11);
                                                map2.put("studyPoint4", c.getStudyPoint4() - score12);

                                                JSONObject jsonObject2 = new JSONObject(map2);

                                                String url3 = cafeDetailFragment.getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // 해당 카페에만 데이터 삽입하기 위함

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
                                                RequestQueue queue2 = Volley.newRequestQueue(v.getContext());
                                                queue2.add(objectRequest2);

                                            }
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                                        Log.e("test_error2", error.toString());
                                    }
                                });
                                requestQueue.add(stringRequest2);
                            }
                        });

                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }

            // 리뷰 작성자와 로그인한 사람이 다를 때,
            else {
                viewHolder.reviewModify.setVisibility(View.INVISIBLE);
                viewHolder.reviewModifyLine.setVisibility(View.INVISIBLE);
                viewHolder.reviewDelete.setVisibility(View.INVISIBLE);
                viewHolder.reviewDeleteLine.setVisibility(View.INVISIBLE);
                viewHolder.good_button_image.setVisibility(View.INVISIBLE);
                viewHolder.good_button.setVisibility(View.VISIBLE);


                // 서버 호출
                RequestQueue requestQueue;
                Cache cache = new DiskBasedCache(cafeDetailFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                Network network = new BasicNetwork(new HurlStack());
                requestQueue = new RequestQueue(cache, network);
                requestQueue.start();


                // DB를 확인하여 좋아요 등록 여부에 따라 버튼 세팅
                if (viewHolder.check_love_flag) {
                    viewHolder.good_button.setChecked(true); // 좋아요 설정 되어 있는 경우
                } else {
                    viewHolder.good_button.setChecked(false); // 좋아요 설정 안되어 있는 경우
                }


                viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인

                        // 자신이 쓴 글일 경우 좋아요 버튼 클릭 불가로 변경
                        if (checked) {
                            // 좋아요 추가
                            clicked_good = 1;
                            int basic_good_count = Integer.parseInt(viewHolder.good_count_textView.getText().toString());
                            viewHolder.good_count_textView.setText(String.valueOf(basic_good_count + clicked_good));


                            //////////////////////////////////////////////////////////////////////////////////////////////////
                            // 좋아요 버튼 눌러 등록한 경우
                            String get_love_url = cafeDetailFragment.getResources().getString(R.string.url) + "love";

                            Map map = new HashMap();
                            map.put("reviewNum", item.get_review_num);
                            map.put("memNum", mem_num);

                            JSONObject love_jsonObject = new JSONObject(map);
                            JsonObjectRequest love_objectRequest = new JsonObjectRequest(Request.Method.POST, get_love_url, love_jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // 좋아요 추가 성공, 토스트 띄우기.
                                            Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("love_jsonObject_error", error.toString());
                                        }
                                    }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=UTF-8";
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(v.getContext());
                            queue.add(love_objectRequest);


                            ///////////////////////////////////////////////////////////////////////////////////////////////
                            // 리뷰 테이블에 likeCount + 1 해주기
                            // 1. review 매핑을 위한 get문
                            Map review_map = new HashMap();


                            String get_review_url = cafeDetailFragment.getResources().getString(R.string.url) + "review";

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

                                    for(Review r : review_list){
                                        if(r.getReviewNum().equals(item.get_review_num)){
                                            review_map.put("tastePoint1", r.getTastePoint1());
                                            review_map.put("tastePoint2", r.getTastePoint2());
                                            review_map.put("tastePoint3", r.getTastePoint3());
                                            review_map.put("tastePoint4", r.getTastePoint4());
                                            review_map.put("seatPoint1", r.getSeatPoint1());
                                            review_map.put("seatPoint2", r.getSeatPoint2());
                                            review_map.put("seatPoint3", r.getSeatPoint3());
                                            review_map.put("seatPoint4", r.getSeatPoint4());
                                            review_map.put("studyPoint1", r.getStudyPoint1());
                                            review_map.put("studyPoint2", r.getStudyPoint2());
                                            review_map.put("studyPoint3", r.getStudyPoint3());
                                            review_map.put("studyPoint4", r.getStudyPoint4());
                                            review_map.put("cafeNum", item.get_cafe_num);
                                            review_map.put("likeCount", r.getLikeCount() + 1);
                                            review_map.put("reviewText", r.getReviewText());
                                            review_map.put("memNum", r.getMemNum());
                                            review_map.put("keyword1", r.getKeyword1());
                                            review_map.put("keyword2", r.getKeyword2());
                                            review_map.put("keyword3", r.getKeyword3());
                                            review_map.put("keyword4", r.getKeyword4());
                                            review_map.put("keyword5", r.getKeyword5());
                                            review_map.put("keyword6", r.getKeyword6());
                                            review_map.put("keyword7", r.getKeyword7());
                                            review_map.put("keyword8", r.getKeyword8());
                                            review_map.put("keyword9", r.getKeyword9());
                                            review_map.put("keyword10", r.getKeyword10());
                                            review_map.put("keyword11", r.getKeyword11());
                                            review_map.put("keyword12", r.getKeyword12());
                                            review_map.put("keyword13", r.getKeyword13());
                                            review_map.put("keyword14", r.getKeyword14());
                                            review_map.put("keyword15", r.getKeyword15());
                                            review_map.put("keyword16", r.getKeyword16());
                                            review_map.put("keyword17", r.getKeyword17());
                                            review_map.put("keyword18", r.getKeyword18());
                                            review_map.put("keyword19", r.getKeyword19());
                                            review_map.put("keyword20", r.getKeyword20());
                                            review_map.put("keyword21", r.getKeyword21());
                                            review_map.put("keyword22", r.getKeyword22());
                                            review_map.put("keyword23", r.getKeyword23());
                                            review_map.put("keyword24", r.getKeyword24());
                                            review_map.put("keyword25", r.getKeyword25());
                                            review_map.put("keyword26", r.getKeyword26());
                                            review_map.put("keyword27", r.getKeyword27());
                                            review_map.put("keyword28", r.getKeyword28());
                                            review_map.put("keyword29", r.getKeyword29());
                                            review_map.put("keyword30", r.getKeyword30());
                                            review_map.put("keyword31", r.getKeyword31());
                                            review_map.put("keyword32", r.getKeyword32());
                                            review_map.put("keyword33", r.getKeyword33());
                                            review_map.put("keyword34", r.getKeyword34());
                                            review_map.put("keyword35", r.getKeyword35());
                                            review_map.put("keyword36", r.getKeyword36());
                                        }
                                    }


                                    // 2. review 테이블에 맵 POST 하기
                                    String update_review_url = cafeDetailFragment.getResources().getString(R.string.url) + "review/" + item.get_review_num.toString();

                                    JSONObject update_review_jsonObject = new JSONObject(review_map);
                                    JsonObjectRequest update_review_objectRequest = new JsonObjectRequest(Request.Method.PUT, update_review_url, update_review_jsonObject,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("update_review_jsonObject_error", error.toString());
                                                }
                                            }) {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    RequestQueue review_queue = Volley.newRequestQueue(v.getContext());
                                    review_queue.add(update_review_objectRequest);


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("cafeDetailAdapter_review_stringRequest_error",error.toString());
                                }
                            });


                            requestQueue.add(review_stringRequest);


                        } else {
                            // 좋아요 취소
                            clicked_good = -1;
                            int basic_good_count = Integer.parseInt(viewHolder.good_count_textView.getText().toString());
                            viewHolder.good_count_textView.setText(String.valueOf(basic_good_count + clicked_good));


                            //////////////////////////////////////////////////////////////////////////////////////////////////
                            // 좋아요 취소 시 love 테이블 삭제
                            // 1. love_num 구하기
                            String get_love_url = cafeDetailFragment.getResources().getString(R.string.url) + "love";


                            StringRequest stringRequest = new StringRequest(Request.Method.GET, get_love_url, new Response.Listener<String>() {
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
                                    Type listType = new TypeToken<ArrayList<Love>>(){}.getType();

                                    love_list = gson.fromJson(changeString, listType);

                                    for(Love l : love_list){
                                        if(l.getReviewNum().equals(item.get_review_num) && l.getMemNum().equals(mem_num)){
                                            get_love_num = l.getLoveNum();
                                        }
                                    }


                                    // 2. love_num 가지고 해당 love 테이블 삭제
                                    String delete_love_url = cafeDetailFragment.getResources().getString(R.string.url) + "like/" + get_love_num.toString();

                                    StringRequest delete_love_stringRequest = new StringRequest(Request.Method.DELETE, delete_love_url, new Response.Listener<String>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("delete_love_stringRequest_error",error.toString());
                                        }
                                    });
                                    requestQueue.add(delete_love_stringRequest);


                                    /////////////////////////////////////////////////////////////////////////////////////////
                                    // review 테이블의 likeCount - 1 해주기
                                    // 1. review 테이블 매핑
                                    Map review_map2 = new HashMap();


                                    String get_review_url = cafeDetailFragment.getResources().getString(R.string.url) + "review";

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

                                            for(Review r : review_list){
                                                if(r.getReviewNum().equals(item.get_review_num)){
                                                    review_map2.put("tastePoint1", r.getTastePoint1());
                                                    review_map2.put("tastePoint2", r.getTastePoint2());
                                                    review_map2.put("tastePoint3", r.getTastePoint3());
                                                    review_map2.put("tastePoint4", r.getTastePoint4());
                                                    review_map2.put("seatPoint1", r.getSeatPoint1());
                                                    review_map2.put("seatPoint2", r.getSeatPoint2());
                                                    review_map2.put("seatPoint3", r.getSeatPoint3());
                                                    review_map2.put("seatPoint4", r.getSeatPoint4());
                                                    review_map2.put("studyPoint1", r.getStudyPoint1());
                                                    review_map2.put("studyPoint2", r.getStudyPoint2());
                                                    review_map2.put("studyPoint3", r.getStudyPoint3());
                                                    review_map2.put("studyPoint4", r.getStudyPoint4());
                                                    review_map2.put("cafeNum", r.getCafeNum());
                                                    review_map2.put("likeCount", r.getLikeCount() - 1);
                                                    review_map2.put("reviewText", r.getReviewText());
                                                    review_map2.put("memNum", r.getMemNum());
                                                    review_map2.put("keyword1", r.getKeyword1());
                                                    review_map2.put("keyword2", r.getKeyword2());
                                                    review_map2.put("keyword3", r.getKeyword3());
                                                    review_map2.put("keyword4", r.getKeyword4());
                                                    review_map2.put("keyword5", r.getKeyword5());
                                                    review_map2.put("keyword6", r.getKeyword6());
                                                    review_map2.put("keyword7", r.getKeyword7());
                                                    review_map2.put("keyword8", r.getKeyword8());
                                                    review_map2.put("keyword9", r.getKeyword9());
                                                    review_map2.put("keyword10", r.getKeyword10());
                                                    review_map2.put("keyword11", r.getKeyword11());
                                                    review_map2.put("keyword12", r.getKeyword12());
                                                    review_map2.put("keyword13", r.getKeyword13());
                                                    review_map2.put("keyword14", r.getKeyword14());
                                                    review_map2.put("keyword15", r.getKeyword15());
                                                    review_map2.put("keyword16", r.getKeyword16());
                                                    review_map2.put("keyword17", r.getKeyword17());
                                                    review_map2.put("keyword18", r.getKeyword18());
                                                    review_map2.put("keyword19", r.getKeyword19());
                                                    review_map2.put("keyword20", r.getKeyword20());
                                                    review_map2.put("keyword21", r.getKeyword21());
                                                    review_map2.put("keyword22", r.getKeyword22());
                                                    review_map2.put("keyword23", r.getKeyword23());
                                                    review_map2.put("keyword24", r.getKeyword24());
                                                    review_map2.put("keyword25", r.getKeyword25());
                                                    review_map2.put("keyword26", r.getKeyword26());
                                                    review_map2.put("keyword27", r.getKeyword27());
                                                    review_map2.put("keyword28", r.getKeyword28());
                                                    review_map2.put("keyword29", r.getKeyword29());
                                                    review_map2.put("keyword30", r.getKeyword30());
                                                    review_map2.put("keyword31", r.getKeyword31());
                                                    review_map2.put("keyword32", r.getKeyword32());
                                                    review_map2.put("keyword33", r.getKeyword33());
                                                    review_map2.put("keyword34", r.getKeyword34());
                                                    review_map2.put("keyword35", r.getKeyword35());
                                                    review_map2.put("keyword36", r.getKeyword36());
                                                }
                                            }

                                            // 2. review (likeCount - 1) PUT 해주기
                                            String update_review_url = cafeDetailFragment.getResources().getString(R.string.url) + "review/" + item.get_review_num.toString();

                                            JSONObject update_review_jsonObject2 = new JSONObject(review_map2);
                                            JsonObjectRequest update_review_objectRequest = new JsonObjectRequest(Request.Method.PUT, update_review_url, update_review_jsonObject2,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {

                                                            ////////////////////////////////////////////////////////////////////////
                                                            // 같은 화면에서 좋아요 버튼 여러 번 누를 때 발생하는 오류 방지
                                                            // 같은 화면에서 좋아요 여러 번 누르면 love 테이블은 삭제, 추가되어 loveNum이 증가
                                                            // 하지만 cafeDetail에서 받아온 loveNum은 그대로 이기 때문에 counter를 더하여 유지
                                                            counter++;
                                                            item.get_love_num = counter;
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.d("update2_review_jsonObject_error", error.toString());
                                                        }
                                                    }) {
                                                @Override
                                                public String getBodyContentType() {
                                                    return "application/json; charset=UTF-8";
                                                }
                                            };
                                            RequestQueue review_queue = Volley.newRequestQueue(v.getContext());
                                            review_queue.add(update_review_objectRequest);


                                        }


                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("cafeDetailAdapter_review_stringRequest_error",error.toString());
                                        }
                                    });


                                    requestQueue.add(review_stringRequest);


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Dd","feee");
                                }
                            });


                            requestQueue.add(stringRequest);

                        }
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return review_items.size() + 1;
    }

    @Override
    public int getItemViewType(int a_position) {

        if (a_position == review_items.size()) {
            return CafeDetailFooterViewHolder.MORE_VIEW_TYPE;
        }

        else {
            return CafeDetailViewHolder.REVIEW_VIEW_TYPE;
        }
    }

    public void setOnItemClickListener_cafeDetail(CafeDetailAdapter.OnItemClickEventListener_cafeDetail a_listener) {
        mItemClickListener_cafeDetail = a_listener;
    }
}

