package com.example.wmc.CafeDetailMore;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Love;
import com.example.wmc.database.Review;
import com.example.wmc.ui.Fragment.CafeDetailMoreFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeDetailMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<CafeDetailMoreItem> reviewMore_items;
    CafeDetailMoreFragment cafeDetailMoreFragment;
    private static NavController navController;

    ArrayList<Review> review_list;
    ArrayList<Love> love_list;

    Long counter = 0L;

    Long mem_num = MainActivity.mem_num; // 임시 유저 넘버
    Long get_love_num = 0L;

    int clicked_good = 0;



    public interface OnItemClickEventListener_cafeDetailMore { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore mItemClickListener_cafeDetailMore;    // 인터페이스 객체 생성

    public CafeDetailMoreAdapter(Context context, ArrayList<CafeDetailMoreItem> list, CafeDetailMoreFragment cafeDetailMoreFragment){
        this.context = context;
        reviewMore_items = list;
        this.cafeDetailMoreFragment = cafeDetailMoreFragment;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new CafeDetailMoreViewHolder(view, mItemClickListener_cafeDetailMore);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final CafeDetailMoreItem item = reviewMore_items.get(position);
        CafeDetailMoreViewHolder viewHolder = (CafeDetailMoreViewHolder) holder;

//        viewHolder.cafeName.setText(item.getCafeName());
        viewHolder.nickName.setText(item.getReviewNickName());
        viewHolder.level_and_location.setText(item.getLevel_and_location());
        viewHolder.review_comment.setText(item.getReview_comment());
        viewHolder.reviewMore_writeTime.setText(item.getReviewMore_writeTime());// 작성자가 리뷰를 작성한 시간
        viewHolder.good_count_textView.setText(item.getGood_count_textView());

        viewHolder.reviewProfile_image.setImageResource(item.getReviewProfile_image());
        viewHolder.reviewMore_image1.setImageResource(item.getReviewImage1());
        viewHolder.reviewMore_image2.setImageResource(item.getReviewImage2());
        viewHolder.reviewMore_image3.setImageResource(item.getReviewImage3());

        viewHolder.check_user_flag = (item.getCheck_user_flag());   // 작성자와 로그인한 유저가 같은지 확인
        viewHolder.check_love_flag = (item.getCheck_love_flag());



        // 리뷰 작성자와 로그인한 사람이 같을 때,
        if(viewHolder.check_user_flag){
            viewHolder.review_modify.setVisibility(View.VISIBLE);
            viewHolder.review_modifyLine.setVisibility(View.VISIBLE);
            viewHolder.review_modify.setVisibility(View.VISIBLE);
            viewHolder.review_modify.setVisibility(View.VISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.VISIBLE);
            viewHolder.good_button.setVisibility(View.INVISIBLE);


            // 리뷰 더보기의 수정 버튼 클릭 시,
            viewHolder.review_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();

                    navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putLong("cafeNum", item.getGet_cafe_num());
                    bundle.putLong("memNum", item.getMem_num());
                    bundle.putBoolean("moreReview_reviewModify_flag", true);
                    navController.navigate(R.id.cafe_detail_more_to_review, bundle);

                }
            });


            // 리뷰 더보기의 삭제 버튼 클릭 시,
            viewHolder.review_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(cafeDetailMoreFragment.getActivity());
                    builder.setTitle("리뷰 삭제").setMessage("리뷰를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(v.getContext().getApplicationContext(), "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            // DB에서 테이블 삭제 하는 코드 추가하기
//                            review_items.remove(item);    // 리사이클러뷰에서도 아이템 삭제
//                            notifyDataSetChanged();
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


        // 리뷰 작성자와 로그인한 사람이 다를를 때,
       else{
            viewHolder.review_modify.setVisibility(View.INVISIBLE);
            viewHolder.review_modifyLine.setVisibility(View.INVISIBLE);
            viewHolder.review_delete.setVisibility(View.INVISIBLE);
            viewHolder.review_deleteLine.setVisibility(View.INVISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.INVISIBLE);
            viewHolder.good_button.setVisibility(View.VISIBLE);


            // 서버 호출
            RequestQueue requestQueue;
            Cache cache = new DiskBasedCache(cafeDetailMoreFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
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
                    if(checked) {
                        // 좋아요 추가
                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                        clicked_good = 1;
                        int basic_good_count = Integer.parseInt(viewHolder.good_count_textView.getText().toString());
                        viewHolder.good_count_textView.setText(String.valueOf(basic_good_count + clicked_good));


                        //////////////////////////////////////////////////////////////////////////////////////////////////
                        // 좋아요 버튼 눌러 등록한 경우
                        // Love 테이블에 추가
                        String get_love_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "love";

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
                        Log.d("json", love_jsonObject.toString());
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        queue.add(love_objectRequest);


                        ///////////////////////////////////////////////////////////////////////////////////////////////
                        // 리뷰 테이블에 likeCount + 1 해주기
                        // 1. review 매핑을 위한 get문
                        Map review_map = new HashMap();


                        String get_review_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "review";

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
                                String update_review_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "review/" + item.get_review_num.toString();

                                JSONObject update_review_jsonObject = new JSONObject(review_map);
                                JsonObjectRequest update_review_objectRequest = new JsonObjectRequest(Request.Method.PUT, update_review_url, update_review_jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // 좋아요 추가 성공, 토스트 띄우기.
                                                Log.d("add_good", "좋아요 추가 성공");
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
                                Log.d("json", update_review_jsonObject.toString());
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
                    else {
                        // 좋아요 취소
                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
                        clicked_good = -1;
                        int basic_good_count = Integer.parseInt(viewHolder.good_count_textView.getText().toString());
                        viewHolder.good_count_textView.setText(String.valueOf(basic_good_count + clicked_good));


                        //////////////////////////////////////////////////////////////////////////////////////////////////
                        // 좋아요 취소 시 love 테이블 삭제
                        // 1. love_num 구하기
                        String get_love_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "love";


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


                                Log.d("check_loveNum", get_love_num.toString()); // love_num 확인


                                // 2. love_num 가지고 해당 love 테이블 삭제
                                String delete_love_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "like/" + get_love_num.toString();

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


                                String get_review_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "review";

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
                                                Log.d("likecount", r.getLikeCount().toString());
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


                                        Log.d("review_map2", review_map2.toString());


                                        // 2. review (likeCount - 1) PUT 해주기
                                        String update_review_url = cafeDetailMoreFragment.getResources().getString(R.string.url) + "review/" + item.get_review_num.toString();

                                        JSONObject update_review_jsonObject2 = new JSONObject(review_map2);
                                        JsonObjectRequest update_review_objectRequest = new JsonObjectRequest(Request.Method.PUT, update_review_url, update_review_jsonObject2,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        // 좋아요 삭제 성공, 토스트 띄우기.
                                                        Log.d("add_good", "좋아요 삭제 성공");

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
                                        Log.d("json", update_review_jsonObject2.toString());
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

    @Override
    public int getItemCount() {
        return reviewMore_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return CafeDetailMoreViewHolder.REVIEWMORE_VIEW_TYPE;
    }

    public void setOnItemClickListener_cafeDetailMore(CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore a_listener) {
        mItemClickListener_cafeDetailMore = a_listener;
    }
}
