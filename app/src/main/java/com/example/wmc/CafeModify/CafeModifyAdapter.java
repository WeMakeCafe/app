package com.example.wmc.CafeModify;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.CafeImage;
import com.example.wmc.database.Review;
import com.example.wmc.ui.Fragment.CafeModifyFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeModifyAdapter extends RecyclerView.Adapter<CafeModifyViewHolder>{

    private ArrayList<Uri> modifyData = null;
    private Context context = null;
    CafeModifyFragment cafeModifyFragment;

    ArrayList<CafeImage> cafeImageArrayList = new ArrayList<>();
    ArrayList<String> cafeImage_URL = new ArrayList<>();
    Long delete_ImageNUM;
//    String cafeImage_URL = "";

    public CafeModifyAdapter(Context context, ArrayList<Uri> uriList, CafeModifyFragment cafeModifyFragment) {
        this.context = context;
        modifyData = uriList ;
        this.cafeModifyFragment = cafeModifyFragment;
    }


    @NonNull
    @Override
    public CafeModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_add_image, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        CafeModifyViewHolder viewHolder = new CafeModifyViewHolder(view) ;

        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull CafeModifyViewHolder holder, int position) {
        Uri image_uri = modifyData.get(position) ;

        Glide.with(cafeModifyFragment.getContext())
                .load(image_uri)
                .into(holder.modifyImage);


        // 이미지 삭제 버튼(X) 클릭 시,
        holder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭" + image_uri, Toast.LENGTH_SHORT).show();

                //이미지 DELETE 코드 추가
                if(image_uri.toString().contains("http")) { // 이미 서버에 올라가있는 사진은 DELETE처리

                    AlertDialog.Builder builder = new AlertDialog.Builder(cafeModifyFragment.getContext());
                    builder.setTitle("이미지 삭제").setMessage("이미지를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            RequestQueue requestQueue;
                            Cache cache = new DiskBasedCache(cafeModifyFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                            Network network = new BasicNetwork(new HurlStack());
                            requestQueue = new RequestQueue(cache, network);
                            requestQueue.start();

                            String get_cafeImage_url = cafeModifyFragment.getResources().getString(R.string.url) + "cafeImage";

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, get_cafeImage_url, new Response.Listener<String>() {
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
                                    Type listType = new TypeToken<ArrayList<CafeImage>>() {
                                    }.getType();

                                    cafeImageArrayList = gson.fromJson(changeString, listType);

                                    for(CafeImage ci : cafeImageArrayList){
                                        if(ci.getFileUrl().equals(image_uri.toString())){
                                            delete_ImageNUM = ci.getcimageNum();
                                            Log.d("delete_ImageNum", ci.getcimageNum() + ", " + ci.getCafeNum() +", " + ci.getFileUrl());
                                        }
                                    }

                                    // 서버에서 이미지 삭제
                                    String delete_cafeImage = cafeModifyFragment.getResources().getString(R.string.url) + "cafeImage/" + delete_ImageNUM.toString();

                                    StringRequest delete_cafeImage_stringRequest = new StringRequest(Request.Method.DELETE, delete_cafeImage, new Response.Listener<String>() {
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
                                    requestQueue.add(delete_cafeImage_stringRequest);
                                    modifyData.remove(image_uri);
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

                else {  // 방금 갤러리에서 올라온 사진은 List에서만 제거
                    AlertDialog.Builder builder = new AlertDialog.Builder(cafeModifyFragment.getContext());
                    builder.setTitle("이미지 삭제").setMessage("이미지를 삭제하시겠습니까?").setIcon(R.drawable.logo);

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            modifyData.remove(image_uri);
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
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modifyData.size();
    }

//    private ArrayList<CafeModifyItem> modifyImage_items;
//
//    public interface OnItemClickEventListener_CafeModify { // 클릭 이벤트를 위한 인터페이스
//        void onItemClick(View a_view, int a_position);
//    }
//    private CafeModifyAdapter.OnItemClickEventListener_CafeModify mItemClickListener_CafeModify;    // 인터페이스 객체 생성
//
//    public CafeModifyAdapter(ArrayList<CafeModifyItem> list){
//        modifyImage_items = list;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
//        final RecyclerView.ViewHolder viewHolder;
//        viewHolder = new CafeModifyViewHolder(view, mItemClickListener_CafeModify);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            // 기본적으로 header 를 빼고 item 을 구한다.
//            final CafeModifyItem item = modifyImage_items.get(position);
//            CafeModifyViewHolder viewHolder = (CafeModifyViewHolder) holder;
//
//            viewHolder.add_modify_imageView.setImageResource(item.getModifyImage());
//
//            // 카페 수정에서 이미지 삭제 버튼(X) 클릭 시,
//            viewHolder.imageDeleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "이미지 삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
//                    modifyImage_items.remove(item);
//                    notifyDataSetChanged();
//                }
//            });
//    }
//
//    @Override
//    public int getItemCount() {
//        return modifyImage_items.size();
//    }
//
//    @Override
//    public int getItemViewType(int a_position) {
//        return CafeModifyViewHolder.CAFEMODIFY_VIEW_TYPE;
//    }
//
//    public void setOnItemClickListener_CafeModify(CafeModifyAdapter.OnItemClickEventListener_CafeModify a_listener) {
//        mItemClickListener_CafeModify = a_listener;
//    }
}
