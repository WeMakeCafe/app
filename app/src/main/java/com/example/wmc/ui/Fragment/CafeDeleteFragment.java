package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    Long mem_num = MainActivity.mem_num;
    TextView requester_input3;
    TextView delete_cafe_name_input;
    TextView delete_cafe_address_input;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDeleteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requester_input3 = root.findViewById(R.id.requester_input3);
        delete_cafe_name_input = root.findViewById(R.id.delete_cafe_name_input);
        delete_cafe_address_input = root.findViewById(R.id.delete_cafe_address_input);

        String cafe_name = getArguments().getString("cafeName");  //getArguments로 번들 검색해서 받기
        String cafe_address = getArguments().getString("cafeAddress");

        // 액티비티에서 받아온 mem_num으로 디비 받아온거 연산해서 이름 넣어주는 코드 넣기
        delete_cafe_name_input.setText(cafe_name);
        delete_cafe_address_input.setText(cafe_address);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
