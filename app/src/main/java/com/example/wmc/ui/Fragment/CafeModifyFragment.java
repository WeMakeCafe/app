package com.example.wmc.ui.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.R;
import com.example.wmc.databinding.FragmentCafeModifyBinding;

import java.util.ArrayList;

public class CafeModifyFragment extends Fragment {
    private FragmentCafeModifyBinding binding;
    private static NavController navController;
    Button add_image_button;
    Button modify_button;
    TextView request_deletion_textView;
    RecyclerView cafeModifyImageRecyclerView;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    CafeModifyAdapter cafeModifyAdapter;
    private static final int REQUEST_CODE = 2222;
    private static final String TAG = "CafeModifyFragment";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeModifyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        add_image_button = root.findViewById(R.id.add_image_button);
        modify_button = root.findViewById(R.id.modify_button);
        request_deletion_textView = root.findViewById(R.id.request_deletion_textView);
        cafeModifyImageRecyclerView = root.findViewById(R.id.cafeModifyImageRecyclerView);

        // 태그 추가 페이지 (ReviewTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView name = root.findViewById(R.id.cafe_name_input);
        TextView address = root.findViewById(R.id.cafe_address_input);
        TextView time_open = root.findViewById(R.id.cafe_openHours_input);
        TextView time_close = root.findViewById(R.id.cafe_closeHours_input);

        Bundle argBundle = getArguments();
        if (argBundle != null) {
            if (argBundle.getString("name") != null){
                name.setText(argBundle.getString("name"));
                address.setText(argBundle.getString("address"));
                time_open.setText(argBundle.getString("time_open"));
                time_close.setText(argBundle.getString("time_close"));
            }
        }

        // 수정하기 버튼 클릭 시,
        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("time_open_Modi",time_open.getText().toString());
                bundle.putString("time_close_Modi",time_close.getText().toString());
                Toast.makeText(getContext().getApplicationContext(), "영업시간 수정 완료! ", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.cafe_modify_to_cafe_detail,bundle);    // 내가 수정한 카페디테일로 이동
            }
        });

        // 삭제요청 버튼 클릭 시,
        request_deletion_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.cafe_modify_to_cafe_delete);    // 삭제 요청 페이지로 이동
            }
        });

        // 이미지 수정 + 버튼 클릭 시,
        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리로 이동
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // 다중 이미지를 가져올 수 있도록 세팅
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

//        // 이미지 수정 리싸이클러뷰
//        ArrayList<CafeModifyItem> modifyImageItems = new ArrayList<>();
//
//        modifyImageItems.add(new CafeModifyItem(R.drawable.logo));
//        modifyImageItems.add(new CafeModifyItem(R.drawable.logo_v2));
//        modifyImageItems.add(new CafeModifyItem(R.drawable.bean_grade1));
//        modifyImageItems.add(new CafeModifyItem(R.drawable.bean_grade2));
//        modifyImageItems.add(new CafeModifyItem(R.drawable.bean_grade3));
//
//        // Adapter 추가
//        RecyclerView modifyRecyclerView = root.findViewById(R.id.cafeModifyImageRecyclerView);
//
//        CafeModifyAdapter modifyAdapter = new CafeModifyAdapter(modifyImageItems);
//        modifyRecyclerView.setAdapter(modifyAdapter);
//
//        // Layout manager 추가
//        LinearLayoutManager modifyLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        modifyRecyclerView.setLayoutManager(modifyLayoutManager);
//
//        modifyAdapter.setOnItemClickListener_CafeModify(new CafeModifyAdapter.OnItemClickEventListener_CafeModify() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final CafeModifyItem item = modifyImageItems.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getModifyImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
//            }
//        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getContext().getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }

        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                if(uriList.size() >= 5) {
                    Toast.makeText(getContext().getApplicationContext(), "이미지 5개를 모두 선택하셨습니다.", Toast.LENGTH_LONG).show();
                }

                else{
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);
                }

                cafeModifyAdapter = new CafeModifyAdapter(uriList, getContext().getApplicationContext());
                cafeModifyImageRecyclerView.setAdapter(cafeModifyAdapter);
                cafeModifyImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 5){   // 선택한 이미지가 6장 이상인 경우
                    Toast.makeText(getContext().getApplicationContext(), "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 5장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){

                        if(uriList.size() <= 4){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                            try {
                                uriList.add(imageUri);  //uri를 list에 담는다.

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                    cafeModifyAdapter = new CafeModifyAdapter(uriList, getContext().getApplicationContext());
                    cafeModifyImageRecyclerView.setAdapter(cafeModifyAdapter);   // 리사이클러뷰에 어댑터 세팅
                    cafeModifyImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
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
