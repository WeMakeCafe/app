package com.example.wmc.ReviewComment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.example.wmc.CafeRegistration.CafeRegistrationItem;
import com.example.wmc.CafeRegistration.CafeRegistrationViewHolder;
import com.example.wmc.R;
import com.example.wmc.database.CafeImage;
import com.example.wmc.database.ReviewImage;
import com.example.wmc.ui.Fragment.Mypage_ReviewCommentFragment;
import com.example.wmc.ui.Fragment.ReviewCommentFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewCommentAdapter extends RecyclerView.Adapter<ReviewCommentViewHolder>{

    private Context reviewContext;
    private ArrayList<Uri> reviewData = new ArrayList<>();
    ArrayList<ReviewImage> reviewImages_List = new ArrayList<>();
    ReviewCommentFragment reviewCommentFragment;
    Long delete_reviewImage;

    public ReviewCommentAdapter(Context context, ArrayList<Uri> list, ReviewCommentFragment reviewCommentFragment) {
        reviewContext = context;
        reviewData = list ;
        this.reviewCommentFragment = reviewCommentFragment;
    }

    @NonNull
    @Override
    public ReviewCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_add_image, viewGroup, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        ReviewCommentViewHolder vh = new ReviewCommentViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewCommentViewHolder viewHolder, int position) {
        Uri image_uri = reviewData.get(position) ;

        Glide.with(reviewCommentFragment.getContext())
                .load(image_uri)
                .into(viewHolder.reviewComment_imageView);


        // 이미지 삭제 버튼(X) 클릭 시,
        viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //이미지 DELETE 코드 추가
                if (image_uri.toString().contains("http")) { // 이미 서버에 올라가있는 사진은 DELETE처리

                    AlertDialog.Builder builder = new AlertDialog.Builder(reviewCommentFragment.getContext());
                    builder.setTitle("이미지 삭제").setMessage("이미지를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            RequestQueue requestQueue;
                            Cache cache = new DiskBasedCache(reviewCommentFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                            Network network = new BasicNetwork(new HurlStack());
                            requestQueue = new RequestQueue(cache, network);
                            requestQueue.start();

                            String get_reviewImage_url = reviewCommentFragment.getResources().getString(R.string.url) + "reviewImage";

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, get_reviewImage_url, new Response.Listener<String>() {
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

                                    reviewImages_List = gson.fromJson(changeString, listType);

                                    for (ReviewImage ri : reviewImages_List) {
                                        if (ri.getFileUrl().equals(image_uri.toString())) {
                                            delete_reviewImage = ri.getrimageNum();
                                            Log.d("delete_ImageNum", ri.getrimageNum() + ", " + ri.getReviewNum() + ", " + ri.getFileUrl());
                                        }
                                    }

                                    // 서버에서 이미지 삭제
                                    String delete_reviewImage_URL = reviewCommentFragment.getResources().getString(R.string.url) + "reviewImage/" + delete_reviewImage.toString();

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
                                    reviewData.remove(image_uri);
                                    notifyDataSetChanged();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                                    Log.e("test_error", error.toString());
                                }
                            });
                            requestQueue.add(stringRequest);

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

                else{// 방금 갤러리에서 올라온 사진은 List에서만 제거
                    AlertDialog.Builder builder = new AlertDialog.Builder(reviewCommentFragment.getContext());
                    builder.setTitle("이미지 삭제").setMessage("이미지를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            reviewData.remove(image_uri);
                            notifyDataSetChanged();
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewData.size();
    }
}
