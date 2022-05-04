package com.example.wmc.ui.Fragment;

import android.os.Bundle;
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

import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.CafeRegistration.CafeRegistrationItem;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeRegistrationBinding;

import java.util.ArrayList;

public class CafeRegistrationFragment extends Fragment {

    private FragmentCafeRegistrationBinding binding;
    private static NavController navController;
    Button cafeRegistratin_add_image_button;
    Button tag;
    Button registration_button;
    Button checked_overlap_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tag = root.findViewById(R.id.add_tag_button);
        registration_button = root.findViewById(R.id.registration_button);
        checked_overlap_button = root.findViewById(R.id.checked_overlap_button);
        cafeRegistratin_add_image_button = root.findViewById(R.id.cafeRegistratin_add_image_button);
        
        
        // 카페 등록 페이지의 이미지 추가 버튼(+) 클릭 시,
        cafeRegistratin_add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리로 이동
            }
        });
        
        
        // 중복확인 버튼 클릭 시,
        checked_overlap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터베이스에서 카페이름이 있는지 중복검사
                Toast.makeText(getContext().getApplicationContext(), "중복확인 검사 시작", Toast.LENGTH_SHORT).show();
            }
        });


        // 태그 추가 버튼 클릭 시
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_review_tag);
            }
        });


        // 등록하기 버튼 클릭 시
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내가 만든 카페의 카페디테일로 이동
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
