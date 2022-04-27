package com.example.wmc.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeDetail.CafeDetailAdapter;
import com.example.wmc.CafeDetail.CafeDetailItem;
import com.example.wmc.HomeFavorite.HomeFavoriteAdapter;
import com.example.wmc.HomeFavorite.HomeFavoriteItem;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<HomeFavoriteItem> homeFavoriteItems = new ArrayList<>();

        homeFavoriteItems.add(new HomeFavoriteItem("이디야커피 수원대점", "#가성비", "#마카롱", R.drawable.logo_v2));
        homeFavoriteItems.add(new HomeFavoriteItem("할리스커피 수원대점", "#버블티", "#스터디", R.drawable.logo));
        homeFavoriteItems.add(new HomeFavoriteItem("메가커피 탑동점", "#맛집", "#분위기", R.drawable.home));
        homeFavoriteItems.add(new HomeFavoriteItem("스타벅스 홍대점", "#스터디", "#조용함", R.drawable.logo_v2));
        homeFavoriteItems.add(new HomeFavoriteItem("백다방 성균관대점", "#회의실", "#다인석", R.drawable.logo));
        homeFavoriteItems.add(new HomeFavoriteItem("잇츠커피 수원대점", "#다채로운", "#감성", R.drawable.review));

        // Recycler view
        RecyclerView recyclerView = root.findViewById(R.id.favorite_recyclerView);

        // Adapter 추가
        HomeFavoriteAdapter adapter = new HomeFavoriteAdapter(homeFavoriteItems);
        recyclerView.setAdapter(adapter);

        // Layout manager 추가
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}