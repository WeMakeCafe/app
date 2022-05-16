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

import com.example.wmc.R;
import com.example.wmc.a_No_USE_Files.ReviewSearchAdapter;
import com.example.wmc.a_No_USE_Files.ReviewSearchItem;
import com.example.wmc.databinding.FragmentListSearchBinding;

import java.util.ArrayList;

public class ReviewSearchFragment extends Fragment{

    private FragmentListSearchBinding binding;
    private static NavController navController;
    EditText searchText;
    Button search_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchText = (EditText) root.findViewById(R.id.search_input);
        searchText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); // List에서 검색창을 클릭해서 넘어와서,
        // ReivewSearchFragment의 검색창 포커스 및 키보드 올리기


        // 돋보기 버튼 클릭 시,
        search_button = root.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().equals("") == true)
                {
                    Toast.makeText(getContext().getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext().getApplicationContext(), searchText.getText().toString() + " 검색됨.", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(search_button.getWindowToken(), 0);
                    navController.navigate(R.id.review_search_to_reivew_cafelist);
                }
            }
        });

        // 최근 검색 리싸이클러뷰
        ArrayList<ReviewSearchItem> reviewSearchItems = new ArrayList<>();

        reviewSearchItems.add(new ReviewSearchItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15"));
        reviewSearchItems.add(new ReviewSearchItem("할리스커피 수원대점", "수원대학교 1203~~~ 와우리 11-13"));
        reviewSearchItems.add(new ReviewSearchItem("와우당 수원대점", "경기도 화성시 1203~~~ 와우리 331-53"));
        reviewSearchItems.add(new ReviewSearchItem("메가커피 홍대점", "서울시 ㅇ~~ 홍대~~ 11-17"));
        reviewSearchItems.add(new ReviewSearchItem("백다방 탑동우방점", "경기도 수원시 권선구 탑동 ~~ 12-37"));
        reviewSearchItems.add(new ReviewSearchItem("스타벅스 수원영통점", "경기도 수원시 영통구 ~~ 판타지움 11-1"));

        // Adapter 추가
        RecyclerView reviewSearchRecyclerView = root.findViewById(R.id.recent_searches_recyclerView);

        ReviewSearchAdapter reviewSearchAdapter = new ReviewSearchAdapter(reviewSearchItems);
        reviewSearchRecyclerView.setAdapter(reviewSearchAdapter);

        // Layout manager 추가
        LinearLayoutManager reivewSearchLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        reviewSearchRecyclerView.setLayoutManager(reivewSearchLayoutManager);

        // 최근 검색 아이템 클릭 시,
        reviewSearchAdapter.setOnItemClickListener_reviewSearch(new ReviewSearchAdapter.OnItemClickEventListener_reviewSearch() {
            @Override
            public void onItemClick(View view, int position) {
                final ReviewSearchItem item = reviewSearchItems.get(position);
                Toast.makeText(getContext().getApplicationContext(), item.getSearch_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
                imm.hideSoftInputFromWindow(container.getWindowToken(), 0);

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getSearch_cafeName());
                navController.navigate(R.id.review_search_to_review, bundle);
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
