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
import java.util.Collections;
import java.util.Comparator;

public class CafeDetailMoreFragment extends Fragment {

    private FragmentCafeDetailMoreBinding binding;
    Spinner reviewMore_spinner;

    String[] spinnerItem = {"최신순", "오래된 순", "좋아요 많은 순", "좋아요 적은 순"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ArrayList<CafeDetailMoreItem> cafeDetailMoreReviewItem = new ArrayList<>();

        // 아이템 추가 시, 앞에 index 0을 붙이면 최근에 쓴 아이템 부터 보이기 시작함.
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("지코", "Lv.3", "테이블이 매우 협소합니다.",
                R.drawable.logo, R.drawable.logo, R.drawable.logo_v2, R.drawable.review_click, "4"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("아이유", "Lv.1(위치인증완료)", "징짜 맛있음\n징짜 맛있음\n징짜 맛있음",
                R.drawable.logo, R.drawable.logo, R.drawable.logo_v2, R.drawable.review_click, "1"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("애쉬", "Lv.3", "테이블이 매우 협소합니다. \n" +
                "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                "마카롱이 진짜 최고에요ㅠ",
                R.drawable.logo, R.drawable.logo, R.drawable.logo_v2, R.drawable.review_click, "7"));
        cafeDetailMoreReviewItem.add(0, new CafeDetailMoreItem("스키니", "Lv.3", "테이블이 매우 협소합니다. \n" +
                "하지만, 가격이 매우 저렴하고 맛있습니다!\n" +
                "마카롱이 진짜 최고에요ㅠ",
                R.drawable.logo, R.drawable.logo, R.drawable.logo_v2, R.drawable.review_click, "3"));

        // Recycler view
        RecyclerView cafeDetailMoreRecyclerView = root.findViewById(R.id.cafeDetailMoreRecyclerView);

        // Adapter 추가
        CafeDetailMoreAdapter cafeDetailMoreAdapter = new CafeDetailMoreAdapter(cafeDetailMoreReviewItem);
        cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);

        // Layout manager 추가
        LinearLayoutManager cafeDetailMoreLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);

        // 리뷰 아이템 선택 시,
        cafeDetailMoreAdapter.setOnItemClickListener_cafeDetailMore(new CafeDetailMoreAdapter.OnItemClickEventListener_cafeDetailMore() {
            @Override
            public void onItemClick(View a_view, int a_position) {
            }
        });

        reviewMore_spinner = root.findViewById(R.id.reviewSpinner);
        reviewMore_spinner.setPrompt("정렬기준");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_custom, spinnerItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewMore_spinner.setAdapter(adapter);



        // 스피너 아이템 선택 시, 리뷰 정렬(최신순, 오래된 순, 좋아요 많은 순, 좋아요 적은 순,
        reviewMore_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext().getApplicationContext(), spinnerItem[position], Toast.LENGTH_SHORT).show();

                if(spinnerItem[position].equals("최신순")) {   // 최신순으로 정렬
                
                }
                
                else if(spinnerItem[position].equals("오래된 순")) {   // 오래된 순으로 정렬

                }
                
                else if(spinnerItem[position].equals("좋아요 많은 순")){   // 좋아요 많은 순으로 정렬
                    Comparator<CafeDetailMoreItem> noAsc = new Comparator<CafeDetailMoreItem>() {

                        @Override
                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                            int ret ;

                            if (Integer.parseInt(item1.getGood_count_textView()) > Integer.parseInt(item2.getGood_count_textView()))
                                ret = -1 ;
                            else if (Integer.parseInt(item1.getGood_count_textView()) == Integer.parseInt(item2.getGood_count_textView()))
                                ret = 0 ;
                            else
                                ret = 1 ;

                            return ret ;
                        }
                    };

                    Collections.sort(cafeDetailMoreReviewItem, noAsc) ;
                    adapter.notifyDataSetChanged() ;
                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                }
                
                else if(spinnerItem[position].equals("좋아요 적은 순")){  // 좋아요 적은 순으로 정렬
                    Comparator<CafeDetailMoreItem> noAsc = new Comparator<CafeDetailMoreItem>() {

                        @Override
                        public int compare(CafeDetailMoreItem item1, CafeDetailMoreItem item2) {
                            int ret ;

                            if (Integer.parseInt(item1.getGood_count_textView()) < Integer.parseInt(item2.getGood_count_textView()))
                                ret = -1 ;
                            else if (Integer.parseInt(item1.getGood_count_textView()) == Integer.parseInt(item2.getGood_count_textView()))
                                ret = 0 ;
                            else
                                ret = 1 ;

                            return ret ;
                        }
                    };

                    Collections.sort(cafeDetailMoreReviewItem, noAsc) ;
                    adapter.notifyDataSetChanged() ;
                    cafeDetailMoreRecyclerView.setAdapter(cafeDetailMoreAdapter);
                    cafeDetailMoreRecyclerView.setLayoutManager(cafeDetailMoreLayoutManager);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 스피너 아이템을 클릭하지 않았을 경우
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
