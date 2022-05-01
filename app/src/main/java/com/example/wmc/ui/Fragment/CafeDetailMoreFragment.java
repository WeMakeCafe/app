package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailMore.CafeDetailMoreAdapter;
import com.example.wmc.CafeDetailMore.CafeDetailMoreItem;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeDetailMoreBinding;

import java.util.ArrayList;

public class CafeDetailMoreFragment extends Fragment {

    private FragmentCafeDetailMoreBinding binding;
    Spinner reviewMore_spinner;

    String[] spinnerItem = {"최신순", "오래된 순", "별점 높은 순", "별점 낮은 순"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        reviewMore_spinner = root.findViewById(R.id.reviewSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext().getApplicationContext(), android.R.layout.simple_spinner_item, spinnerItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewMore_spinner.setAdapter(adapter);

        reviewMore_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext().getApplicationContext(), spinnerItem[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); // 스피너 클릭 시, 나올


        ArrayList<CafeDetailMoreItem> cafeDetailMoreReviewItem = new ArrayList<>();

        cafeDetailMoreReviewItem.add(new CafeDetailMoreItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        cafeDetailMoreReviewItem.add(new CafeDetailMoreItem("아이유", "Lv.1(위치인증완료)", "징짜 맛있음\n징짜 맛있음\n징짜 맛있음",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        cafeDetailMoreReviewItem.add(new CafeDetailMoreItem("애쉬", "Lv.3", "테이블이 매우 협소합니다. \n" +
                "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                "마카롱이 진짜 최고에요ㅠ",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));
        cafeDetailMoreReviewItem.add(new CafeDetailMoreItem("스키니", "Lv.3", "테이블이 매우 협소합니다. \n" +
                "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                "마카롱이 진짜 최고에요ㅠ",
                R.drawable.logo, R.drawable.logo_v2, R.drawable.bean_grade1, R.drawable.bean_grade3, "4"));

        // Recycler view
        RecyclerView cafeDetailMoreRecyclerView = root.findViewById(R.id.cafeDetailMoreRecyclerView);

        // Adapter 추가
        CafeDetailMoreAdapter cafeDetailMoreAdapter = new CafeDetailMoreAdapter(cafeDetailMoreReviewItem);
        cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);

        // Layout manager 추가
        LinearLayoutManager cafeDetailMoreLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);

        cafeDetailMoreAdapter.setOnItemClickListener_cafeDetailMore(new CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore() {
            @Override
            public void onItemClick(View a_view, int a_position) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
