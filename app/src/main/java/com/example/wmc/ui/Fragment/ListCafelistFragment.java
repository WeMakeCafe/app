package com.example.wmc.ui.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.wmc.ListCafeList.ListCafeListAdapter;
import com.example.wmc.ListCafeList.ListCafeListItem;
//import com.example.wmc.ListSearch.ListSearchAdapter;
//import com.example.wmc.ListSearch.ListSearchItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.databinding.FragmentListCafelistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListCafelistFragment extends Fragment {

    private FragmentListCafelistBinding binding;
    private static NavController navController;
    EditText searchText;    // 검색창
    Button searchButton;    // 돋보기

    TextView cafe_search_textView;  // 카페 추가 설명
    TextView cafe_add_textView;     // 카페 추가 설명
    Button add_cafe_button;         // 카페 추가 버튼
    LinearLayout cafeList_footer;   // 푸터
    FloatingActionButton add_cafe;  // 카페리스트가 나왔을 때의 추가 버튼(플로팅 버튼)


    ArrayList<Cafe> cafe_list;
    ArrayList<Bookmark> bookmark_list;

    Long mem_num = MainActivity.mem_num;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListCafelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchText = root.findViewById(R.id.cafe_search_input);
        searchButton = root.findViewById(R.id.search_button);
        add_cafe = root.findViewById(R.id.addCafe_floatingButton);
        cafe_search_textView = root.findViewById(R.id.cafe_search_textView);
        cafe_add_textView = root.findViewById(R.id.cafe_add_textView);
        add_cafe_button = root.findViewById(R.id.add_cafe_button);
        cafeList_footer = root.findViewById(R.id.cafeList_footer);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  // 키보드 제어 InputManager


        // 기본 VISIBLE 설정
        cafeList_footer.setVisibility(View.INVISIBLE);      // 하단 Footer
        add_cafe.setVisibility(View.INVISIBLE);             // 카페추가 플로팅 버튼
        cafe_search_textView.setVisibility(View.VISIBLE);   // 카페 추가 설명
        cafe_add_textView.setVisibility(View.VISIBLE);      // 카페 추가 설명
        add_cafe_button.setVisibility(View.VISIBLE);        // 카페 추가 버튼


        // 카페 추가 플로팅 버튼 클릭 시,
        add_cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "카페 등록으로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.list_cafelist_to_cafe_registration);
            }
        });


        // 초기 및 카페 리스트 0개일 경우, 생기는 추가 버튼 클릭 시,
        add_cafe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "카페 등록으로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.list_cafelist_to_cafe_registration);
            }
        });

        // 카페 리스트 리싸이클러뷰
        ArrayList<ListCafeListItem> listCafeListItems = new ArrayList<>();

        // 검색창의 돋보기 버튼 클릭 시,
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString().replaceAll(" ", "");

                if(search.equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 검색어와 관련된 아이템들 출력
                    Toast.makeText(getContext().getApplicationContext(), search + " 검색됨.", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(searchButton.getWindowToken(), 0);

                    listCafeListItems.clear();  // 이전에 보였던 리싸이클러뷰 아이템 모두 제거 후, 검색관련 아이템을 띄움


                    // 서버 연결하여 cafe_list 검색
                    RequestQueue requestQueue;
                    Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                    Network network = new BasicNetwork(new HurlStack());
                    requestQueue = new RequestQueue(cache, network);
                    requestQueue.start();


                    String get_cafe_url = getResources().getString(R.string.url) + "cafe";


                    StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
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

                            String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, get_bookmark_url, new Response.Listener<String>() {
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

                                    ///////////////////////////////////////////////////////////////////////////////////
                                    // 카페 이름으로 찾기
                                    for(Cafe c : cafe_list){
                                        boolean flag = false;
                                        if(c.getCafeName().contains(search)){ // 카페 이름에 검색어가 포함되는지 확인
                                            for(Bookmark b : bookmark_list) {
                                                if (b.getCafeNum().equals(c.getCafeNum()) && b.getMemNum().equals(mem_num)) {
                                                    flag = true;
                                                }
                                            }
                                            listCafeListItems.add(new ListCafeListItem(c.getCafeName(), c.getCafeAddress(),
                                                    c.getOpenTime() + " ~ " + c.getCloseTime(), "#레트로", "#인스타", R.drawable.logo, flag));
                                        }
                                    }

                                    // Adapter 추가
                                    RecyclerView listCafeListRecyclerView = root.findViewById(R.id.cafeListRecyclerView);

                                    ListCafeListAdapter listCafeListAdapter = new ListCafeListAdapter(listCafeListItems);
                                    listCafeListRecyclerView.setAdapter(listCafeListAdapter);

                                    // Layout manager 추가
                                    LinearLayoutManager listCafeListLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    listCafeListRecyclerView.setLayoutManager(listCafeListLayoutManager);

                                    listCafeListAdapter.setOnItemClickListener_ListCafeList(new ListCafeListAdapter.OnItemClickEventListener_ListCafeList() {
                                        @Override
                                        public void onItemClick(View a_view, int a_position) {
                                            final ListCafeListItem item = listCafeListItems.get(a_position);
                                            Toast.makeText(getContext().getApplicationContext(), item.getCafeList_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                                            Bundle bundle = new Bundle();
                                            bundle.putString("cafeName", item.getCafeList_cafeName());
                                            navController.navigate(R.id.list_cafelist_to_cafe_detail, bundle);
                                        }
                                    });

                                    // 리사이클러뷰의 아이템을 갱신해주는 코드
                                    listCafeListAdapter.notifyItemInserted(listCafeListItems.size());

                                    // 리싸이클러뷰 아이템이 없을 경우, 카페 추가 버튼과 설명 글 생성
                                    if(listCafeListItems.size() == 0) {
                                        cafeList_footer.setVisibility(View.INVISIBLE);
                                        add_cafe.setVisibility(View.INVISIBLE);
                                        cafe_search_textView.setVisibility(View.VISIBLE);
                                        cafe_add_textView.setVisibility(View.VISIBLE);
                                        add_cafe_button.setVisibility(View.VISIBLE);
                                    }
                                    else{   // 아이템이 있을 경우
                                        cafeList_footer.setVisibility(View.VISIBLE);
                                        add_cafe.setVisibility(View.VISIBLE);
                                        cafe_search_textView.setVisibility(View.INVISIBLE);
                                        cafe_add_textView.setVisibility(View.INVISIBLE);
                                        add_cafe_button.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Dd","feee");
                                }
                            });


                            requestQueue.add(stringRequest);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ListCafelist_cafe_stringRequest_error",error.toString());
                        }
                    });


                    requestQueue.add(cafe_stringRequest);
                }
            }
        });
        return root;
    }



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
