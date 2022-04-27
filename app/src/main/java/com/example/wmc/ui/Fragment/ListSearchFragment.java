package com.example.wmc.ui.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wmc.R;
import com.example.wmc.databinding.FragmentListSearchBinding;

public class ListSearchFragment extends Fragment {

    private FragmentListSearchBinding binding;
    private static NavController navController;
    EditText searchText;
    Button search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchText = (EditText) root.findViewById(R.id.search_input);
        searchText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); // List에서 검색창을 클릭해서 넘어와서,
                                                                                                    // ListSearchFragment의 검색창 포커스 및 키보드 올리기


        search = root.findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navController.navigate(R.id.);    // 돋보기 버튼 클릭 시, ListCafelistFragment로 이동
                                                    // 아직 Navigation이 없음
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
