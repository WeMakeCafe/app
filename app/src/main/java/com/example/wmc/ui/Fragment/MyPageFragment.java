package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.example.wmc.MainActivity;
import com.example.wmc.MypageFavorite.MypageFavoriteAdapter;
import com.example.wmc.MypageFavorite.MypageFavoriteItem;
import com.example.wmc.MypageReview.MypageReviewAdapter;
import com.example.wmc.MypageReview.MypageReviewItem;
import com.example.wmc.R;
import com.example.wmc.database.Personal;
import com.example.wmc.databinding.FragmentMypageBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private FragmentMypageBinding binding;
    private static NavController navController;

    ArrayList<Personal> personal_list;

    TextView grade, nickname, fav1, fav2;
    Button modify;
    Button logout;

    Long mem_num = MainActivity.mem_num;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        grade = root.findViewById(R.id.level2);
        nickname = root.findViewById(R.id.nickname2);
        fav1 = root.findViewById(R.id.first_important2);
        fav2 = root.findViewById(R.id.second_important2);
        modify = root.findViewById(R.id.change_information_button2);
        logout = root.findViewById(R.id.logout_button2);

        //서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String url = "http://54.221.33.199:8080/personal";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                Type listType = new TypeToken<ArrayList<Personal>>() {
                }.getType();

                personal_list = gson.fromJson(changeString, listType);

                // personal 테이블의 튜플이 제대로 오는지 확인 (테스트 할 때만 만들어두고 해당 기능 다 개발 시 제거하는게 좋음)
                Log.d("test", String.valueOf(personal_list.size()));

                for(Personal p : personal_list) {
                    if(p.getMemNum().equals(mem_num)) {
                        nickname.setText(p.getNickName());
                        fav1.setText("#" + p.getFavorite1());
                        fav2.setText("#" + p.getFavorite2());
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

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 여기까지 서버팀이 만진거

        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.myPage_to_myPage_grade);
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navController.navigate(R.id.); // 정보수정 Fragment로 이동
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navController.navigate(R.id.); // 로그인Fragment 이동
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 찜한 카페에 대한 리사이클러뷰 작성

        ArrayList<MypageFavoriteItem> mypageFavoriteItems = new ArrayList<>();

        mypageFavoriteItems.add(new MypageFavoriteItem("이디아커피 수원대점"));
        mypageFavoriteItems.add(new MypageFavoriteItem("스타벅스 수원역점"));
        mypageFavoriteItems.add(new MypageFavoriteItem("Taxi Coffee"));
        mypageFavoriteItems.add(new MypageFavoriteItem("커피맛을 알면 인생이 보인다 수원대점"));
        mypageFavoriteItems.add(new MypageFavoriteItem("스타벅스 청담점"));
        mypageFavoriteItems.add(new MypageFavoriteItem("메가 커피 정자사거리점"));

        // Recycler view
        RecyclerView mypageFavoriteRecyclerview = root.findViewById(R.id.favorite_mypage);

        // Adapter 추가
        MypageFavoriteAdapter favoriteAdapter = new MypageFavoriteAdapter(mypageFavoriteItems);
        mypageFavoriteRecyclerview.setAdapter(favoriteAdapter);

        // Layout manager 추가
        GridLayoutManager favoriteLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        mypageFavoriteRecyclerview.setLayoutManager(favoriteLayoutManager);

        favoriteAdapter.setOnItemClickListener_MypageFavorite(new MypageFavoriteAdapter.OnItemClickEventListener_MypageFavorite() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final MypageFavoriteItem item = mypageFavoriteItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 내가 쓴 리뷰 에대한 리사이클러뷰 작성

        ArrayList<MypageReviewItem> mypageReviewItem = new ArrayList<>();

        mypageReviewItem.add(new MypageReviewItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        mypageReviewItem.add(new MypageReviewItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        mypageReviewItem.add(new MypageReviewItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        mypageReviewItem.add(new MypageReviewItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));


        // Recycler view
        RecyclerView mypageReviewRecyclerview = root.findViewById(R.id.review_mypage);

        // Adapter 추가
        MypageReviewAdapter reviewAdapter = new MypageReviewAdapter(mypageReviewItem);
        mypageReviewRecyclerview.setAdapter(reviewAdapter);

        // Layout manager 추가
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mypageReviewRecyclerview.setLayoutManager(reviewLayoutManager);

        reviewAdapter.setOnItemClickListener_MypageReview(new MypageReviewAdapter.OnItemClickEventListener_MyPageReview() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final MypageReviewItem item = mypageReviewItem.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getReviewNickName(), Toast.LENGTH_SHORT).show();
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
