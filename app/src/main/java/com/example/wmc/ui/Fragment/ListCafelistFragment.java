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

import com.example.wmc.R;
import com.example.wmc.databinding.FragmentListCafelistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "ListSearchFragment로 이동", Toast.LENGTH_SHORT).show();
//                navController.navigate(R.id.list_to_list_search);     // 아직 ListCafelistFragment의 Navigation이 없음
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "ListSearchFragment로 이동", Toast.LENGTH_SHORT).show();
//                navController.navigate(R.id.list_to_list_search);     // 아직 ListCafelistFragment의 Navigation이 없음
            }
        });

        add_cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), "CafeRegistrationFragment로 이동", Toast.LENGTH_SHORT).show();
//                navController.navigate(R.id.);                         // 아직 ListCafelistFragment의 Navigation이 없음
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
