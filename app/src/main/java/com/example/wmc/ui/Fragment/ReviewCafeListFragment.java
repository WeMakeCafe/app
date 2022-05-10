package com.example.wmc.ui.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.ListCafeList.ListCafeListAdapter;
import com.example.wmc.ListCafeList.ListCafeListItem;
import com.example.wmc.R;
import com.example.wmc.ReviewCafeList.ReviewCafeListAdapter;
import com.example.wmc.ReviewCafeList.ReviewCafeListItem;
import com.example.wmc.databinding.FragmentListCafelistBinding;
import com.example.wmc.databinding.FragmentListSearchBinding;
import com.example.wmc.databinding.FragmentReviewCafelistBinding;

import java.util.ArrayList;

public class ReviewCafeListFragment extends Fragment {

    private FragmentReviewCafelistBinding binding;
    private static NavController navController;
    EditText searchText;
    Button searchButton;
    TextView review_cafeSearch_textView;
    RecyclerView review_cafeListRecyclerView;
    LinearLayout cafeList_footer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCafelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchText = root.findViewById(R.id.review_cafe_search_input);  // 검색 창
        searchButton = root.findViewById(R.id.review_search_button);    // 돋보기 버튼
        review_cafeSearch_textView = root.findViewById(R.id.review_cafeSearch_textView);    // 리뷰할 카페를 입력해주세요(설명)
        review_cafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);  // 카페 리스트 리싸이클러뷰
        cafeList_footer = root.findViewById(R.id.cafeList_footer);                          // 하단 Footer

        searchText.requestFocus();  // 검색 창 포커스
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  // 키보드 제어 InputManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);                 // 검색창에 포커스를 주어 자동으로 키보드 올라오게 하기


        // 기본 VISIBLE 설정
        review_cafeSearch_textView.setVisibility(View.VISIBLE);
        cafeList_footer.setVisibility(View.INVISIBLE);



        // 리뷰에서 온 카페리스트 리싸이클러뷰
        ArrayList<ReviewCafeListItem> reivewCafeListItems = new ArrayList<>();

//        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
//                "08:00 ~ 21:30", "#가성비", "#맛", R.drawable.logo));
//        reivewCafeListItems.add(new ReviewCafeListItem("할리스커피 수원대점", "경기도 화성시 와우리 41-17",
//                "09:00 ~ 22:30", "#마카롱", "#회의실", R.drawable.logo));
//        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "경기도 수원시 탑동 801-4",
//                "10:30 ~ 21:30", "#화장실", "#애견", R.drawable.logo));
//        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "경기도 수원시 영통구 판타지움 47-1",
//                "08:00 ~ 21:00", "#분위기", "#감성", R.drawable.logo));
//        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
//                "09:00 ~ 21:00", "#힙한", "#화려한", R.drawable.logo));
//        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
//                "12:00 ~ 00:30", "#레트로", "#인스타", R.drawable.logo));


        // Adapter 추가
        RecyclerView reviewCafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);

        ReviewCafeListAdapter reviewCafeListAdapter = new ReviewCafeListAdapter(reivewCafeListItems);
        reviewCafeListRecyclerView.setAdapter(reviewCafeListAdapter);

        // Layout manager 추가
        LinearLayoutManager reviewCafeListLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        reviewCafeListRecyclerView.setLayoutManager(reviewCafeListLayoutManager);

        // 카페리스트 아이템 클릭 시, 리뷰로 돌아감
        reviewCafeListAdapter.setOnItemClickListener_ReviewCafeList(new ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList() {
            @Override
            public void onItemClick(View view, int position) {
                final ReviewCafeListItem item = reivewCafeListItems.get(position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeList_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getCafeList_cafeName());
                navController.navigate(R.id.review_cafelist_to_review, bundle);
            }
        });


        // 검색창의 돋보기 버튼 클릭 시,
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString();
                if(search.equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 검색어와 관련된 아이템들 출력
                    Toast.makeText(getContext().getApplicationContext(), searchText.getText().toString() + " 검색됨.", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(searchButton.getWindowToken(), 0);

                    reivewCafeListItems.clear();  // 이전에 보였던 리싸이클러뷰 아이템 모두 제거 후, 검색관련 아이템을 띄움
                    reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                            "12:00 ~ 00:30", "#레트로", "#인스타", R.drawable.logo));

                    // 리싸이클러뷰 아이템이 없을 경우, 카페 추가 버튼과 설명 글 생성 // VISIBLE 여부
                    if(reivewCafeListItems.size() == 0) {
                        review_cafeSearch_textView.setVisibility(View.VISIBLE);
                        cafeList_footer.setVisibility(View.INVISIBLE);
                    }
                    else{   // 아이템이 있을 경우   // VISIBLE 여부
                        review_cafeSearch_textView.setVisibility(View.INVISIBLE);
                        cafeList_footer.setVisibility(View.VISIBLE);
                    }
                }
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
