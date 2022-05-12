package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailImageViewPagerAdapter;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingItem;
import com.example.wmc.CafeDetailImageViewPager.CafeDetailRatingViewPagerAdapter;
import com.example.wmc.R;
import com.example.wmc.RatingViewPager.RatingViewPagerSeat;
import com.example.wmc.RatingViewPager.RatingViewPagerStudy;
import com.example.wmc.RatingViewPager.RatingViewPagerTaste;
import com.example.wmc.databinding.FragmentCafeDetailBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CafeDetailFragment extends Fragment {

    private FragmentCafeDetailBinding binding;
    private static NavController navController;
    Button cafe_modify_button;
    FloatingActionButton review_floatingButton;
    CheckBox favorite_checkbox;
    ViewPager cafeImageViewPager;
    ViewPager cafeRatingViewPager;
    Button cafeDetail_favorite_previousButton;
    Button cafeDetail_favorite_nextButton;
    TextView moreReview2; // 최상단 카페 이름 ID
    TextView moreReview3; // 사진 아래 카페 이름 ID
    TextView moreReview4; // 사진 아래 카페 주소 ID
    TextView moreReview10; // 사진 아래 운영 시간 첫번째
    TextView moreReview8; // 사진 아래 운영 시간 두번째

    ArrayList<Integer> imageList;   // 카페 이미지 5장을 저장하는 ArrayList
    ArrayList<CafeDetailRatingItem> ratingList; // 카페 평점을 저장하는 ArrayList

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cafe_modify_button = root.findViewById(R.id.cafe_modify_button);
        moreReview3 = root.findViewById(R.id.moreReview3);
        moreReview4 = root.findViewById(R.id.moreReview4);
        review_floatingButton = root.findViewById(R.id.review_floatingButton);
        favorite_checkbox = root.findViewById(R.id.favorite_checkbox);
        cafeDetail_favorite_previousButton = root.findViewById(R.id.cafeDetail_favorite_previousButton);
        cafeDetail_favorite_nextButton = root.findViewById(R.id.cafeDetail_favorite_nextButton);
        moreReview2 = root.findViewById(R.id.moreReview2); // 최상단 카페 이름 ID
        moreReview3 = root.findViewById(R.id.moreReview3); // 사진 아래 카페 이름 ID

        // 카페 이름 가져오기(상단)
        Bundle cafeNameBundle1 = getArguments();
        if(cafeNameBundle1 != null){
            if(cafeNameBundle1.getString("cafeName") != null){
                moreReview2.setText(cafeNameBundle1.getString("cafeName"));
            }
        }

        // 카페 이름 가져오기(상세)
        Bundle cafeNameBundle2 = getArguments();
        if(cafeNameBundle2 != null){
            if(cafeNameBundle2.getString("cafeName") != null){
                moreReview3.setText(cafeNameBundle2.getString("cafeName"));
            }
        }

        moreReview4 = root.findViewById(R.id.moreReview4); // 사진 아래 카페 주소 ID
        moreReview10 = root.findViewById(R.id.moreReview10); // 사진 아래 운영 시간 첫번째
        moreReview8 = root.findViewById(R.id.moreReview8); // 사진 아래 운영 시간 두번째

        // 카페 운영 시간 가져오기
        Bundle cafeModifyBundle = getArguments();
        if(cafeModifyBundle != null){
            if(cafeModifyBundle.getString("time_open_Modi") != null ){
                moreReview10.setText(cafeModifyBundle.getString("time_open_Modi"));
                moreReview8.setText(cafeModifyBundle.getString("time_close_Modi"));
            }
        }

        // 카페 수정(연필) 버튼 클릭 시,
        cafe_modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name",moreReview3.getText().toString());
                bundle.putString("address",moreReview4.getText().toString());
                bundle.putString("time_open",moreReview10.getText().toString());
                bundle.putString("time_close",moreReview8.getText().toString());
                navController.navigate(R.id.cafe_detail_to_cafe_modify, bundle);
            }
        });


        // 즐겨찾기 버튼(별) 클릭 시,
        favorite_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();    // 즐겨찾기가 됐는지 확인

                if(checked) {
                    // 즐겨찾기 항목에 추가함
                    Toast.makeText(getContext().getApplicationContext(), "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 즐겨찾기 항목에서 제거됨
                    Toast.makeText(getContext().getApplicationContext(), "즐겨찾기 삭제", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // 리뷰 플로팅 버튼 클릭 시,
        review_floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("cafeName", moreReview2.getText().toString());
                navController.navigate(R.id.cafe_detail_to_review, bundle);
            }
        });


        // 카페디테일의 리뷰 리싸이클러뷰
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
            public void onItemClick(View view, int position) {

                if(position == cafeDetailReviewItem.size()){
                    Toast.makeText(getContext().getApplicationContext(), "리뷰 더보기 클릭", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.cafe_detail_to_cafe_detail_more);
                }

                else {
                    final CafeDetailItem item = cafeDetailReviewItem.get(position);
                    Toast.makeText(getContext().getApplicationContext(), item.getReviewNickName() + " 클릭됨.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 카페디테일에 해당하는 카페이미지 보여주기
        cafeImageViewPager = root.findViewById(R.id.cafeImageViewPager);
        cafeImageViewPager.setOffscreenPageLimit(5);
        imageList = new ArrayList<>();

        imageList.add(R.drawable.logo);
        imageList.add(R.drawable.logo_v2);
        imageList.add(R.drawable.bean_grade1);
        imageList.add(R.drawable.bean_grade2);
        imageList.add(R.drawable.bean_grade3);

        cafeImageViewPager.setAdapter(new CafeDetailImageViewPagerAdapter(getContext().getApplicationContext(), imageList));
//        CafeDetailImagePagerAdapter cafeImageAdapter = new CafeDetailImagePagerAdapter(getActivity().getSupportFragmentManager());
//
//        CafeDetailImageViewPager1 page1 = new CafeDetailImageViewPager1();
//        cafeImageAdapter.cafeImageAddItem(page1);
//        CafeDetailImageViewPager2 page2 = new CafeDetailImageViewPager2();
//        cafeImageAdapter.cafeImageAddItem(page2);
//        CafeDetailImageViewPager3 page3 = new CafeDetailImageViewPager3();
//        cafeImageAdapter.cafeImageAddItem(page3);
//        CafeDetailImageViewPager4 page4 = new CafeDetailImageViewPager4();
//        cafeImageAdapter.cafeImageAddItem(page4);
//        CafeDetailImageViewPager5 page5 = new CafeDetailImageViewPager5();
//        cafeImageAdapter.cafeImageAddItem(page5);



        // 카페디테일에 해당하는 카페별점 보여주기
        cafeRatingViewPager = root.findViewById(R.id.ratingViewPager);
        cafeRatingViewPager.setOffscreenPageLimit(3);
        ratingList = new ArrayList<>();

        CafeDetailRatingItem taste = new CafeDetailRatingItem("맛", "산미", "쓴맛", "디저트", "기타음료", R.drawable.taste_score, "3", "1", "2", "5");
        CafeDetailRatingItem seat = new CafeDetailRatingItem("좌석", "2인좌석", "4인좌석", "화장실", "다인좌석", R.drawable.sit_score, "1", "5", "1", "3");
        CafeDetailRatingItem study = new CafeDetailRatingItem("스터디", "와이파이", "콘센트", "조명", "조용함", R.drawable.study_score, "3", "5", "5", "5");

        ratingList.add(taste);
        ratingList.add(seat);
        ratingList.add(study);

        cafeRatingViewPager.setAdapter(new CafeDetailRatingViewPagerAdapter(getContext().getApplicationContext(), ratingList));
//        CafeDetailRatingPagerAdapter cafeRatingAdapter = new CafeDetailRatingPagerAdapter(getActivity().getSupportFragmentManager());
//
//        RatingViewPagerTaste tasteRating = new RatingViewPagerTaste();
//        cafeRatingAdapter.cafeRatingAddItem(tasteRating);
//        RatingViewPagerSeat seatRating = new RatingViewPagerSeat();
//        cafeRatingAdapter.cafeRatingAddItem(seatRating);
//        RatingViewPagerStudy studyRating = new RatingViewPagerStudy();
//        cafeRatingAdapter.cafeRatingAddItem(studyRating);


        // 별점에서 좌측 버튼 클릭 시, 별점 페이지 넘어감
        cafeDetail_favorite_previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNum = cafeRatingViewPager.getCurrentItem();
                cafeRatingViewPager.setCurrentItem(pageNum - 1, true);
            }
        });


        // 별점에서 우측 버튼 클릭 시, 별점 페이지 넘어감
        cafeDetail_favorite_nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNum = cafeRatingViewPager.getCurrentItem();
                cafeRatingViewPager.setCurrentItem(pageNum + 1, true);
            }
        });


        return root;
    }

    // 카페디테일 카페이미지 뷰페이저
//    class CafeDetailImagePagerAdapter extends FragmentStatePagerAdapter {
//
//        ArrayList<Fragment> imageItems = new ArrayList<>();
//        public CafeDetailImagePagerAdapter(FragmentManager fm){
//            super(fm);
//        }
//
//        public void cafeImageAddItem(Fragment item){
//            imageItems.add(item);   // Fragment 추가
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return imageItems.get(position);    // 프래그먼트 가져오기
//        }
//
//        @Override
//        public int getCount() {
//            return imageItems.size();   // 프래그먼트 개수반환
//        }
//    }


    // 카페디테일 별점 뷰페이저
//    class CafeDetailRatingPagerAdapter extends FragmentStatePagerAdapter {
//        ArrayList<Fragment> ratingItems = new ArrayList<>();
//        public CafeDetailRatingPagerAdapter(FragmentManager ratingFm) {
//            super(ratingFm);
//        }
//
//        public void cafeRatingAddItem(Fragment item){
//            ratingItems.add(item);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return ratingItems.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return ratingItems.size();
//        }
//    }


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
