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
import com.example.wmc.ListSearch.ListSearchAdapter;
import com.example.wmc.ListSearch.ListSearchItem;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentListCafelistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListCafelistFragment extends Fragment {

    private FragmentListCafelistBinding binding;
    private static NavController navController;
    Button searchText;
    Button searchButton;
    FloatingActionButton add_cafe;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListCafelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchText = root.findViewById(R.id.cafe_search_input);
        searchButton = root.findViewById(R.id.search_button);
        add_cafe = root.findViewById(R.id.addCafe_floatingButton);


        // 검색 창 클릭 시,
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "ListSearchFragment로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.list_cafelist_to_list_search);
            }
        });


        // 검색창의 돋보기 버튼 클릭 시,
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "ListSearchFragment로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.list_cafelist_to_list_search);
            }
        });


        // 카페 추가 플로팅 버튼 클릭 시,
        add_cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "CafeRegistrationFragment로 이동", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.list_cafelist_to_cafe_registration);
            }
        });


        // 카페 리스트 리싸이클러뷰
        ArrayList<ListCafeListItem> listCafeListItems = new ArrayList<>();

        listCafeListItems.add(new ListCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                "08:00 ~ 21:30", "#가성비", "#맛", R.drawable.logo));
        listCafeListItems.add(new ListCafeListItem("할리스커피 수원대점", "경기도 화성시 와우리 41-17",
                "09:00 ~ 22:30", "#마카롱", "#회의실", R.drawable.logo));
        listCafeListItems.add(new ListCafeListItem("이디야커피 수원대점", "경기도 수원시 탑동 801-4",
                "10:30 ~ 21:30", "#화장실", "#애견", R.drawable.logo));
        listCafeListItems.add(new ListCafeListItem("이디야커피 수원대점", "경기도 수원시 영통구 판타지움 47-1",
                "08:00 ~ 21:00", "#분위기", "#감성", R.drawable.logo));
        listCafeListItems.add(new ListCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                "09:00 ~ 21:00", "#힙한", "#화려한", R.drawable.logo));
        listCafeListItems.add(new ListCafeListItem("이디야커피 수원대점", "수원대학교 1203~~~ 와우리 42-15",
                "12:00 ~ 00:30", "#레트로", "#인스타", R.drawable.logo));

        // Adapter 추가
        RecyclerView listCafeListRecyclerView = root.findViewById(R.id.cafeListRecyclerView);

        ListCafeListAdapter listCafeListAdapter = new ListCafeListAdapter(listCafeListItems);
        listCafeListRecyclerView.setAdapter(listCafeListAdapter);

        // Layout manager 추가
        LinearLayoutManager listCafeListLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listCafeListRecyclerView.setLayoutManager(listCafeListLayoutManager);

        listCafeListAdapter.setOnItemClickListener_ListCafeList(new ListCafeListAdapter.OnItemClickEventListener_ListCafeList() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final ListCafeListItem item = listCafeListItems.get(a_position);
                Toast.makeText(getContext().getApplicationContext(), item.getCafeList_cafeName() + " 클릭됨.", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("cafeName", item.getCafeList_cafeName());
                navController.navigate(R.id.list_cafelist_to_cafe_detail, bundle);
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
