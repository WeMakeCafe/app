package com.example.wmc.ListCafeList;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.ui.Fragment.ListCafelistFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListCafeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ListCafeListItem> listCafeList_items;
    private Context context;
    ListCafelistFragment listCafelistFragment;

    ArrayList<Bookmark> bookmark_list;

    Long mem_num = MainActivity.mem_num;
    Long get_bookmark_num; // bookmark_num을 임시로 저장함.

    public interface OnItemClickEventListener_ListCafeList { // 클릭 이벤트를 위한 인터페이스
        void onItemClick(View a_view, int a_position);
    }
    private ListCafeListAdapter.OnItemClickEventListener_ListCafeList mItemClickListener_ListCafeList;    // 인터페이스 객체 생성

    public ListCafeListAdapter(Context context, ArrayList<ListCafeListItem> list, ListCafelistFragment listCafelistFragment){
        this.context = context;
        listCafeList_items = list;
        this.listCafelistFragment = listCafelistFragment;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        final RecyclerView.ViewHolder viewHolder;
        viewHolder = new ListCafeListViewHolder(view, mItemClickListener_ListCafeList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ListCafeListItem item = listCafeList_items.get(position);
        ListCafeListViewHolder viewHolder = (ListCafeListViewHolder) holder;

        viewHolder.cafeList_cafe_name_textView.setText(item.getCafeList_cafeName());
        viewHolder.cafeList_cafe_address_textView.setText(item.getCafeList_cafeAddress());
        viewHolder.opening_hours.setText(item.getOpenTime());
        viewHolder.cafeList_hashTag1.setText(item.getTag1());
        viewHolder.cafeList_hashTag2.setText(item.getTag2());

        Glide.with(listCafelistFragment.getActivity()).load(item.getCafeList_cafeImage()).into(viewHolder.cafeList_cafeImage);

        viewHolder.check_user_flag = item.getCheck_user_flag();


        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(listCafelistFragment.getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        // 즐겨찾기 여부 나타내기
        if(viewHolder.check_user_flag){
            viewHolder.favorite_button.setChecked(true);
        }
        else{
            viewHolder.favorite_button.setChecked(false);
        }


        // 즐겨찾기 버튼 클릭 시,
        viewHolder.favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checked = ((CheckBox) view).isChecked();    // 즐겨찾기가 됐는지 확인


                // 카페 북마크 여부 확인 및 등록, 삭제
                String get_bookmark_url = listCafelistFragment.getResources().getString(R.string.url) + "bookmark";
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

                            if(b.getMemNum().equals(mem_num) && b.getCafeNum().equals(item.getGet_cafe_num())){
                                get_bookmark_num = b.getBookmarkNum(); // bookmark_num 일시 저장
                            }
                        }

                        if(checked) {   // 불이 꺼져있을 때 누르는 경우,
                            // 즐겨찾기 항목에 추가함

                            Map map = new HashMap();
                            map.put("cafeNum", item.getGet_cafe_num());
                            map.put("memNum", mem_num);

                            JSONObject bookmark_jsonObject = new JSONObject(map);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, get_bookmark_url, bookmark_jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // 북마크 추가 성공, 토스트 띄우기.
                                            Toast.makeText(view.getContext().getApplicationContext(), "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
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
                            RequestQueue queue = Volley.newRequestQueue(listCafelistFragment.requireContext());
                            queue.add(objectRequest);
                        }


                        else {  // 불이 켜져있을 때 누르는 경우
                            // 즐겨찾기 항목에서 제거됨
                            String bookmark_delete_url = listCafelistFragment.getResources().getString(R.string.url) + "bookmark/" + get_bookmark_num.toString();
                            StringRequest bookmark_delete_stringRequest = new StringRequest(Request.Method.DELETE, bookmark_delete_url, new Response.Listener<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onResponse(String response) {
                                    // 북마크 제거 성공, 토스트 띄우기.
                                    Toast.makeText(view.getContext().getApplicationContext(), "즐겨찾기 삭제", Toast.LENGTH_SHORT).show();
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
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("bookmark_stringRequest_error",error.toString());
                    }
                });
                requestQueue.add(bookmark_stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCafeList_items.size();
    }

    @Override
    public int getItemViewType(int a_position) {
        return ListCafeListViewHolder.LISTCAFELIST_VIEW_TYPE;
    }

    public void setOnItemClickListener_ListCafeList(ListCafeListAdapter.OnItemClickEventListener_ListCafeList a_listener) {
        mItemClickListener_ListCafeList = a_listener;
    }
}
