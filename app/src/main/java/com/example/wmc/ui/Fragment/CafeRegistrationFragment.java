package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.CafeRegistration.CafeRegistrationItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.databinding.FragmentCafeRegistrationBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CafeRegistrationFragment extends Fragment {

    private FragmentCafeRegistrationBinding binding;
    private static NavController navController;
    Button tag;
    Button add_cafe_button;
    String url = "http://54.221.33.199:8080/cafe";
    ArrayList<Cafe> cafe_list;
    Long cafe_num = MainActivity.cafe_num;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tag = root.findViewById(R.id.add_tag_button);
        add_cafe_button = root.findViewById(R.id.add_cafe_button);

        //// 서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        add_cafe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        Type listType = new TypeToken<ArrayList<Cafe>>() {
                        }.getType();

                        cafe_list = gson.fromJson(changeString, listType);

                        // cafe 테이블의 튜플이 제대로 오는지 확인 (테스트 할 때만 만들어두고 해당 기능 다 개발 시 제거하는게 좋음)
                        Log.d("test", String.valueOf(cafe_list.size()));

                        for(Cafe c : cafe_list) {
                            if(c.getCafeNum().equals(cafe_num)) {
                                Toast.makeText(getContext(), "이미 있는 카페입니다!", Toast.LENGTH_SHORT).show();
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
            }
        });









        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_review_tag);
            }
        });


        ArrayList<CafeRegistrationItem> registrationImageItems = new ArrayList<>();

        registrationImageItems.add(new CafeRegistrationItem(R.drawable.logo));
        registrationImageItems.add(new CafeRegistrationItem(R.drawable.logo_v2));
        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade1));
        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade2));
        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade3));

        // Adapter 추가
        RecyclerView registrationRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);

        CafeRegistrationAdapter registrationAdapter = new CafeRegistrationAdapter(registrationImageItems);
        registrationRecyclerView.setAdapter(registrationAdapter);

        // Layout manager 추가
        LinearLayoutManager registrationLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        registrationRecyclerView.setLayoutManager(registrationLayoutManager);

        registrationAdapter.setOnItemClickListener_CafeRegistration(new CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final CafeRegistrationItem item = registrationImageItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getRegistrationImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
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
