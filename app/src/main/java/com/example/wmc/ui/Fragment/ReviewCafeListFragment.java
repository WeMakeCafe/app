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
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.ReviewCafeList.ReviewCafeListAdapter;
import com.example.wmc.ReviewCafeList.ReviewCafeListItem;
import com.example.wmc.database.Cafe;
import com.example.wmc.databinding.FragmentListCafelistBinding;
import com.example.wmc.databinding.FragmentListSearchBinding;
import com.example.wmc.databinding.FragmentReviewCafelistBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewCafeListFragment extends Fragment {

    private FragmentReviewCafelistBinding binding;
    private static NavController navController;
    EditText searchText; // 검색창
    Button searchButton; // 돋보기
    TextView review_cafeSearch_textView; // 카페 추가 설명
    RecyclerView review_cafeListRecyclerView; // 카페 추가 설명
    LinearLayout cafeList_footer; // 푸터

    ArrayList<Cafe> cafe_list;
    Long mem_num = MainActivity.mem_num;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCafelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchText = root.findViewById(R.id.review_cafe_search_input);  // 검색 창
        searchButton = root.findViewById(R.id.review_search_button);    // 돋보기 버튼
        review_cafeSearch_textView = root.findViewById(R.id.review_cafeSearch_textView);    // 리뷰할 카페를 입력해주세요(설명)
        review_cafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);  // 카페 리스트 리싸이클러뷰
        cafeList_footer = root.findViewById(R.id.cafeList_footer);                          // 하단 Footer

        searchText.requestFocus();  // 검색 창 포커스
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  // 키보드 제어 InputManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);                 // 검색창에 포커스를 주어 자동으로 키보드 올라오게 하기


        // 기본 VISIBLE 설정
        review_cafeSearch_textView.setVisibility(View.VISIBLE);
        cafeList_footer.setVisibility(View.INVISIBLE);



        // 리뷰에서 온 카페리스트 리싸이클러뷰
        ArrayList<ReviewCafeListItem> reivewCafeListItems = new ArrayList<>();

        // Adapter 추가
        RecyclerView reviewCafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);

        ReviewCafeListAdapter reviewCafeListAdapter = new ReviewCafeListAdapter(reivewCafeListItems);
        reviewCafeListRecyclerView.setAdapter(reviewCafeListAdapter);

        // Layout manager 추가
        LinearLayoutManager reviewCafeListLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        reviewCafeListRecyclerView.setLayoutManager(reviewCafeListLayoutManager);

        // 카페리스트 아이템 클릭 시, 리뷰로 돌아감
        reviewCafeListAdapter.setOnItemClickListener_ReviewCafeList(new ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList() {
            @Override
            public void onItemClick(View view, int position) {
                final ReviewCafeListItem item = reivewCafeListItems.get(position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeList_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getCafeList_cafeName());
                navController.navigate(R.id.review_cafelist_to_review, bundle);
            }
        });


        // 검색창의 돋보기 버튼 클릭 시,
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString();
                if(search.equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 검색어와 관련된 아이템들 출력
                    Toast.makeText(getContext().getApplicationContext(), searchText.getText().toString() + " 검색됨.", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(searchButton.getWindowToken(), 0);

                    reivewCafeListItems.clear();
                    // 이전에 보였던 리싸이클러뷰 아이템 모두 제거 후, 검색관련 아이템을 띄움

                    // 서버 연결하여 cafe_list 검색
                    RequestQueue requestQueue;
                    Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                    Network network = new BasicNetwork(new HurlStack());
                    requestQueue = new RequestQueue(cache, network);
                    requestQueue.start();

                    String get_cafe_url = getResources().getString(R.string.url) +  "cafe";

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

                            ///////////////////////////////////////////////////////////////////////////////////
                            // 카페 이름으로 찾기
                            for(Cafe c : cafe_list){
                                if(c.getCafeName().contains(search)){ // 카페 이름에 검색어가 포함되는지 확인
                                    reivewCafeListItems.add(new ReviewCafeListItem(c.getCafeName(), c.getCafeAddress(),
                                            c.getOpenTime() + " ~ " + c.getCloseTime(), "#레트로", "#인스타", R.drawable.logo));
                                }
                            }

                            // Adapter 추가
                            RecyclerView reviewCafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);

                            ReviewCafeListAdapter reviewCafeListAdapter = new ReviewCafeListAdapter(reivewCafeListItems);
                            reviewCafeListRecyclerView.setAdapter(reviewCafeListAdapter);

                            // Layout manager 추가
                            LinearLayoutManager reviewCafeListLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            reviewCafeListRecyclerView.setLayoutManager(reviewCafeListLayoutManager);

                            reviewCafeListAdapter.setOnItemClickListener_ReviewCafeList(new ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList() {
                                @Override
                                public void onItemClick(View a_view, int a_position) {
                                    final ReviewCafeListItem item = reivewCafeListItems.get(a_position);
                                    Toast.makeText(getContext().getApplicationContext(), item.getCafeList_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("cafeName", item.getCafeList_cafeName());
                                    navController.navigate(R.id.review_cafelist_to_review, bundle);
                                }
                            });

                            // 리사이클러뷰의 아이템을 갱신해주는 코드
                            reviewCafeListAdapter.notifyItemInserted(reivewCafeListItems.size());

                            // 리싸이클러뷰 아이템이 없을 경우, 카페 추가 버튼과 설명 글 생성
                            if(reivewCafeListItems.size() == 0) {
                                Toast.makeText(getContext().getApplicationContext(), "검색어에 맞는 카페가\n존재하지 않습니다", Toast.LENGTH_SHORT).show();
                                review_cafeSearch_textView.setVisibility(View.VISIBLE);
                                cafeList_footer.setVisibility(View.INVISIBLE);
                            }
                            else{   // 아이템이 있을 경우
                                review_cafeSearch_textView.setVisibility(View.INVISIBLE);
                                cafeList_footer.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ReviewCafelist_cafe_stringRequest_error",error.toString());
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
