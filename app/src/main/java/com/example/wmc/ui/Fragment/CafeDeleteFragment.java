package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeDeleteBinding;

public class CafeDeleteFragment extends Fragment {
    private FragmentCafeDeleteBinding binding;
    private static NavController navController;
    Button delete_request_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDeleteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        delete_request_button = root.findViewById(R.id.delete_request_button);

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
