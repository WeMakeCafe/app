package com.example.wmc.ui.Fragment;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeModifyBinding;

import java.util.ArrayList;

public class CafeModifyFragment extends Fragment {
    private FragmentCafeModifyBinding binding;
    private static NavController navController;
    Button add_image_button;
    Button modify_button;
    TextView request_deletion_textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeModifyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        add_image_button = root.findViewById(R.id.add_image_button);
        modify_button = root.findViewById(R.id.modify_button);
        request_deletion_textView = root.findViewById(R.id.request_deletion_textView);

        // 이미지 수정 + 버튼 클릭 시,
        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리로 이동
            }
        });


        // 수정하기 버튼 클릭 시,
        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.cafe_modify_to_cafe_detail);    // 내가 수정한 카페디테일로 이동
            }
        });


        // 삭제요청 버튼 클릭 시,
        request_deletion_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.cafe_modify_to_cafe_delete);    // 삭제 요청 페이지로 이동
            }
        });


        // 이미지 수정 리싸이클러뷰
        ArrayList<CafeModifyItem> modifyImageItems = new ArrayList<>();

        modifyImageItems.add(new CafeModifyItem(R.drawable.logo));
        modifyImageItems.add(new CafeModifyItem(R.drawable.logo_v2));
        modifyImageItems.add(new CafeModifyItem(R.drawable.bean_grade1));
        modifyImageItems.add(new CafeModifyItem(R.drawable.bean_grade2));
        modifyImageItems.add(new CafeModifyItem(R.drawable.bean_grade3));

        // Adapter 추가
        RecyclerView modifyRecyclerView = root.findViewById(R.id.cafeModifyImageRecyclerView);

        CafeModifyAdapter modifyAdapter = new CafeModifyAdapter(modifyImageItems);
        modifyRecyclerView.setAdapter(modifyAdapter);

        // Layout manager 추가
        LinearLayoutManager modifyLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        modifyRecyclerView.setLayoutManager(modifyLayoutManager);

        modifyAdapter.setOnItemClickListener_CafeModify(new CafeModifyAdapter.OnItemClickEventListener_CafeModify() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final CafeModifyItem item = modifyImageItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getModifyImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
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
