package com.example.wmc.a_No_USE_Files;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wmc.R;
import com.example.wmc.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;    // FragmentListBinding 변수이름 Ctrl + 좌클릭하면 연결된 .xml로 이동함
    private static NavController navController;
    Button add;
    Button searchText;
    Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        add = root.findViewById(R.id.add_cafe_button);
        searchText = root.findViewById(R.id.cafe_search_input);
        searchButton = root.findViewById(R.id.search_button);

        // 추가 버튼 클릭 시,
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "추가버튼 클릭", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.list_to_cafe_registration);
            }
        });


        // 검색 창 클릭 시,
        searchText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.list_to_list_search);
            }
        });


        // 돋보기 버튼 클릭 시,
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.list_to_list_search);
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