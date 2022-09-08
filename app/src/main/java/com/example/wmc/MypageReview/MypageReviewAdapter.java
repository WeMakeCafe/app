package com.example.wmc.MypageReview;

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
import androidx.fragment.app.FragmentTransaction;
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
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Love;
import com.example.wmc.database.Review;
import com.example.wmc.ui.Fragment.MyPageFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MypageReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<MypageReviewItem> myPageReview_items;
    MyPageFragment myPageFragment;
    private static NavController navController;

    ArrayList<Review> review_list;
    ArrayList<Love> love_list;
    ArrayList<Cafe> cafe_list;

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

    public interface OnItemClickEventListener_MyPageReview { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private MypageReviewAdapter.OnItemClickEventListener_MyPageReview mItemClickListener_MyPageReview;    // 인터페이스 객체 생성

    public MypageReviewAdapter(Context context, ArrayList<MypageReviewItem> list, MyPageFragment myPageFragment){
        this.context = context;
        myPageReview_items = list;
        this.myPageFragment = myPageFragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new MypageReviewViewHolder(view, mItemClickListener_MyPageReview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder a_holder, int a_position) {

        final MypageReviewItem item = myPageReview_items.get(a_position);
        MypageReviewViewHolder viewHolder = (MypageReviewViewHolder) a_holder;

        viewHolder.mypageReview_CafeName.setText(item.getMypageReview_CafeName());
        viewHolder.mypageReview_writeTime.setText(item.getMypageReview_writeTime());
        viewHolder.myPageReview_comment.setText(item.getReview_comment());
        viewHolder.good_count_textView.setText(item.getGood_count_textView());
        viewHolder.myPageReview_image1.setImageResource(item.getReviewImage1());
        viewHolder.myPageReview_image2.setImageResource(item.getReviewImage2());
        viewHolder.myPageReview_image3.setImageResource(item.getReviewImage3());

        viewHolder.check_user_flag = (item.getCheck_user_flag());   // 작성자와 로그인한 유저가 같은지 확인

        for(int i = 0 ; i<=35; i++){
            k[i] = (long) 0;
        }


        // 리뷰 작성자와 로그인한 유저가 같을 경우,
        if(viewHolder.check_user_flag){

            viewHolder.myPageReview_modify.setVisibility(View.VISIBLE);
            viewHolder.myPageReview_modifyLine.setVisibility(View.VISIBLE);
            viewHolder.myPageReview_delete.setVisibility(View.VISIBLE);
            viewHolder.myPageReview_deleteLine.setVisibility(View.VISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.VISIBLE);
            viewHolder.good_button.setVisibility(View.INVISIBLE);

            // 마이페이지 리뷰의 수정 버튼 클릭 시,
            viewHolder.myPageReview_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                    navController = Navigation.findNavController(v);

                    Bundle bundle = new Bundle();
                    bundle.putLong("cafeNum", item.getGet_cafe_num());
                    bundle.putLong("memNum", item.getMem_num());
                    bundle.putBoolean("mypage_reviewModify_flag", true);

                    navController.navigate(R.id.myPage_to_mypage_review, bundle);

                }
            });


            // 마이페이지 리뷰의 삭제 버튼 클릭 시,
            viewHolder.myPageReview_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "리뷰 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(myPageFragment.getActivity());
                    builder.setTitle("리뷰 삭제").setMessage("리뷰를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            // DB에서 테이블 삭제 하는 코드 추가하기
                            RequestQueue requestQueue;
                            Cache cache = new DiskBasedCache(myPageFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                            Network network = new BasicNetwork(new HurlStack());
                            requestQueue = new RequestQueue(cache, network);
                            requestQueue.start();
                            String review_url = myPageFragment.getResources().getString(R.string.url) + "review";

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
                                    Log.d("test3", String.valueOf(review_list.size()));

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
                                            Log.d("reviewnum", item.get_review_num.toString());
                                            String delete_review = myPageFragment.getResources().getString(R.string.url) + "review/" + item.get_review_num.toString();

                                            StringRequest delete_review_stringRequest = new StringRequest(Request.Method.DELETE, delete_review, new Response.Listener<String>() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(v.getContext().getApplicationContext(), "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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
                            myPageReview_items.remove(item);    // 리사이클러뷰에서도 아이템 삭제
                            notifyDataSetChanged();
//                            FragmentTransaction ft = myPageFragment.getFragmentManager().beginTransaction();
//                            ft.detach(myPageFragment).attach(myPageFragment).commit();



                            String cafe_url = myPageFragment.getResources().getString(R.string.url) + "cafe";
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

                                            String url3 = myPageFragment.getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // 해당 카페에만 데이터 삽입하기 위함

                                            JsonObjectRequest objectRequest2 = new JsonObjectRequest(Request.Method.PUT, url3, jsonObject2,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            FragmentTransaction ft = myPageFragment.getFragmentManager().beginTransaction();
                                                            ft.detach(myPageFragment).attach(myPageFragment).commit();
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

                            FragmentTransaction ft = myPageFragment.getFragmentManager().beginTransaction();
                            ft.detach(myPageFragment).attach(myPageFragment).commit();
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

        // 리뷰 작성자와 로그인한 유저가 다를 경우,
        else {
            viewHolder.myPageReview_modify.setVisibility(View.INVISIBLE);
            viewHolder.myPageReview_modifyLine.setVisibility(View.INVISIBLE);
            viewHolder.myPageReview_delete.setVisibility(View.INVISIBLE);
            viewHolder.myPageReview_deleteLine.setVisibility(View.INVISIBLE);
            viewHolder.good_button_imageView.setVisibility(View.INVISIBLE);
            viewHolder.good_button.setVisibility(View.VISIBLE);

            // 리뷰 더보기의 좋아요 버튼 클릭 시,
            viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인

                    // 자신이 쓴 글일 경우 좋아요 버튼 클릭 불가로 변경
                    if(checked) {
                        // 좋아요 추가
                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // 좋아요 취소
                        Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



        // 마이페이지 리뷰의 좋아요 버튼 클릭 시,
        viewHolder.good_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();    // 좋아요가 됐는지 확인

                // 자신이 쓴 글일 경우 좋아요 버튼 클릭 불가로 변경
                if(checked) {
                    // 좋아요 추가
                    Toast.makeText(v.getContext().getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 좋아요 취소
                    Toast.makeText(v.getContext().getApplicationContext(), "좋아요 취소", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return myPageReview_items.size(); }

    @Override
    public int getItemViewType(int a_position) {
        return MypageReviewViewHolder.MYPAGE_REVIEW_VIEW_TYPE;
    }

    public void setOnItemClickListener_MypageReview(MypageReviewAdapter.OnItemClickEventListener_MyPageReview a_listener) {
        mItemClickListener_MyPageReview = a_listener;
    }
}
