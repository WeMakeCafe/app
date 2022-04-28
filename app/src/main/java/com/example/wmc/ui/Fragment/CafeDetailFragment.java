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

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeDetailBinding;

import java.util.ArrayList;

public class CafeDetailFragment extends Fragment {

    private FragmentCafeDetailBinding binding;
    private static NavController navController;
    Button cafe_modify_button;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cafe_modify_button = root.findViewById(R.id.cafe_modify_button);

        cafe_modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.cafe_detail_to_cafe_modify);
            }
        });

        ArrayList<CafeDetailItem> cafeDetailReviewItem = new ArrayList<>();

        cafeDetailReviewItem.add(new CafeDetailItem("아이유", "Lv.3",
                "징짜 맛있음\n징짜 맛있음\n징짜 맛있음", R.drawable.logo, R.drawable.logo_v2, "7"));
        cafeDetailReviewItem.add(new CafeDetailItem("지코", "Lv.3",
                "테이블이 매우 협소합니다. \n" +
                        "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                        "마카롱이 진짜 최고에요ㅠ", R.drawable.logo, R.drawable.logo_v2, "5"));
        cafeDetailReviewItem.add(new CafeDetailItem("애쉬", "Lv.1(위치 인증 완료)",
                "테이블이 협소해서 공부하기는 어렵지만\n" +
                        "노래도 나오고 친구들이랑 같이 이야기하기에는\n" +
                        "좋아요.", R.drawable.logo, R.drawable.logo_v2, "1"));

        // Recycler view
        RecyclerView recyclerView = root.findViewById(R.id.cafeDetailReviewRecyclerView);

        // Adapter 추가
        CafeDetailAdapter adapter = new CafeDetailAdapter(cafeDetailReviewItem);
        recyclerView.setAdapter(adapter);

        // Layout manager 추가
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        adapter.setOnItemClickListener_cafeDetail(new CafeDetailAdapter.OnItemClickEventListener_cafeDetail() {
            @Override
            public void onItemClick(View a_view, int a_position) {

                if (a_position == cafeDetailReviewItem.size()){
                    Toast.makeText(getContext().getApplicationContext(), "리뷰 더보기 클릭", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.cafe_detail_to_cafe_detail_more);
                }
                else{
                    final CafeDetailItem item = cafeDetailReviewItem.get(a_position);
                    Toast.makeText(getContext().getApplicationContext(), item.getReviewNickName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
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
