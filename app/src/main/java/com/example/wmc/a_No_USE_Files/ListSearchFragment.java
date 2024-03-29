package com.example.wmc.a_No_USE_Files;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.a_No_USE_Files.ListSearchAdapter;
import com.example.wmc.a_No_USE_Files.ListSearchItem;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentListSearchBinding;

import java.util.ArrayList;

public class ListSearchFragment extends Fragment {

    private FragmentListSearchBinding binding;
    private static NavController navController;
    EditText searchText;
    Button search_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchText = (EditText) root.findViewById(R.id.search_input);
        searchText.requestFocus();  // search페이지로 오면 자동으로 키보드가 올라오게 하기 위함
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); // List에서 검색창을 클릭해서 넘어와서,
                                                                                                    // ListSearchFragment의 검색창 포커스 및 키보드 올리기

        // 돋보기 버튼 클릭 시,
        search_button = root.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().equals("") == true)
                {
                    // 아무것도 안적었을 때,
                    Toast.makeText(getContext().getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 무언가 검색했을 때,
                    Toast.makeText(getContext().getApplicationContext(), searchText.getText().toString() + " 검색됨.", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(search_button.getWindowToken(), 0);
//                    navController.navigate(R.id.list_search_to_list_cafelist);
                }
            }
        });

        
        // 최근 검색 리싸이클러뷰
        ArrayList<ListSearchItem> listSearchItems = new ArrayList<>();

        listSearchItems.add(new ListSearchItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15"));
        listSearchItems.add(new ListSearchItem("할리스커피 수원대점", "수원대학교 1203~~~ 와우리 11-13"));
        listSearchItems.add(new ListSearchItem("와우당 수원대점", "경기도 화성시 1203~~~ 와우리 331-53"));
        listSearchItems.add(new ListSearchItem("메가커피 홍대점", "서울시 ㅇ~~ 홍대~~ 11-17"));
        listSearchItems.add(new ListSearchItem("백다방 탑동우방점", "경기도 수원시 권선구 탑동 ~~ 12-37"));
        listSearchItems.add(new ListSearchItem("스타벅스 수원영통점", "경기도 수원시 영통구 ~~ 판타지움 11-1"));

        // Adapter 추가
        RecyclerView listSearchRecyclerView = root.findViewById(R.id.recent_searches_recyclerView);

        ListSearchAdapter listSearchAdapter = new ListSearchAdapter(listSearchItems);
        listSearchRecyclerView.setAdapter(listSearchAdapter);

        // Layout manager 추가
        LinearLayoutManager listSearchLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listSearchRecyclerView.setLayoutManager(listSearchLayoutManager);

        listSearchAdapter.setOnItemClickListener_listSearch(new ListSearchAdapter.OnItemClickEventListener_listSearch() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final ListSearchItem item = listSearchItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getSearch_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
                imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
//                navController.navigate(R.id.list_search_to_list_cafelist);
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
