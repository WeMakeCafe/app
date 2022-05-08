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
    Button searchText;
    Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCafelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchText = root.findViewById(R.id.review_cafe_search_input);
        searchButton = root.findViewById(R.id.review_search_button);

        // 검색 창 클릭 시,
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "ReviewSearchFragment로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.review_cafelist_to_review_search);
            }
        });

        // 검색창의 돋보기 버튼 클릭 시,
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "ReviewSearchFragment로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.review_cafelist_to_review_search);
            }
        });

        // 리뷰에서 온 카페리스트 리싸이클러뷰
        ArrayList<ReviewCafeListItem> reivewCafeListItems = new ArrayList<>();

        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                "08:00 ~ 21:30", "#가성비", "#맛", R.drawable.logo));
        reivewCafeListItems.add(new ReviewCafeListItem("할리스커피 수원대점", "경기도 화성시 와우리 41-17",
                "09:00 ~ 22:30", "#마카롱", "#회의실", R.drawable.logo));
        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "경기도 수원시 탑동 801-4",
                "10:30 ~ 21:30", "#화장실", "#애견", R.drawable.logo));
        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "경기도 수원시 영통구 판타지움 47-1",
                "08:00 ~ 21:00", "#분위기", "#감성", R.drawable.logo));
        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                "09:00 ~ 21:00", "#힙한", "#화려한", R.drawable.logo));
        reivewCafeListItems.add(new ReviewCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                "12:00 ~ 00:30", "#레트로", "#인스타", R.drawable.logo));

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
