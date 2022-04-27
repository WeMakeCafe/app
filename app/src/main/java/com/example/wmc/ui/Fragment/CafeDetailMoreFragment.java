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

import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeDetailMoreBinding;

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
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
