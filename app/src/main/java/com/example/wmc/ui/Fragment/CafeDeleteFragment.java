package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.Personal;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeDeleteBinding;
import com.example.wmc.databinding.FragmentCafeModifyBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CafeDeleteFragment extends Fragment {
    private FragmentCafeDeleteBinding binding;
    private static NavController navController;
    Button delete_request_button;

    Long cafe_num = MainActivity.cafe_num;
    Long mem_num = MainActivity.mem_num;
    ArrayList<Cafe> cafe_list;
    ArrayList<Personal> personal_list;
    TextView requester_input3;
    TextView delete_cafe_name_input;
    TextView delete_cafe_address_input;

    String url = "http://54.221.33.199:8080/personal";
    String url2 = "http://54.221.33.199:8080/cafe";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDeleteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        delete_request_button = root.findViewById(R.id.delete_request_button);

        requester_input3 = root.findViewById(R.id.requester_input3);
        delete_cafe_name_input = root.findViewById(R.id.delete_cafe_name_input);
        delete_cafe_address_input = root.findViewById(R.id.delete_cafe_address_input);

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
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
                        delete_cafe_name_input.setText(c.getCafeName());
                        delete_cafe_address_input.setText(c.getCafeAddress());
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

        // 요청하기 버튼 클릭
        delete_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 관리자에게 삭제 요청 메시지 전달
                navController.navigate(R.id.cafe_delete_to_cafe_detail);    // 삭제 요청 후, 카페 디테일로 복귀
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
