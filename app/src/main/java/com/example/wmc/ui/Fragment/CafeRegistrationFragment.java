package com.example.wmc.ui.Fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.CafeRegistration.CafeRegistrationItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.databinding.FragmentCafeRegistrationBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CafeRegistrationFragment extends Fragment {

    private FragmentCafeRegistrationBinding binding;
    private static NavController navController;
    Button cafeRegistratin_add_image_button;    // 이미지 추가 버튼
    Button checked_overlap_button;              //  중복확인 버튼
    Button add_tag_button;                      // 태그의 추가 버튼
    Button registration_button;                 // 등록하기 버튼
    RecyclerView cafeRegistrationImageRecyclerView; // 이미지 리싸이클러뷰
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    CafeRegistrationAdapter registrationAdapter;
    private static final int REQUEST_CODE = 1111;
    private static final String TAG = "CafeRegistrationFragment";
    String url = "http://54.221.33.199:8080/cafe";
    ArrayList<Cafe> cafe_list;
    Long cafe_num = MainActivity.cafe_num;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cafeRegistratin_add_image_button = root.findViewById(R.id.cafeRegistratin_add_image_button);    // 이미지 추가 버튼
        checked_overlap_button = root.findViewById(R.id.checked_overlap_button);    // 중복확인 버튼
        add_tag_button = root.findViewById(R.id.add_tag_button);    // 태그의 추가 버튼
        registration_button = root.findViewById(R.id.registration_button);  // 등록하기 버튼

        cafeRegistrationImageRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);  // 리싸이클러뷰 버튼


        // 카페 등록 페이지의 이미지 추가 버튼(+) 클릭 시,
        cafeRegistratin_add_image_button.setOnClickListener(new View.OnClickListener() {
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


        // 중복확인 버튼 클릭 시,
        checked_overlap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터베이스에서 카페이름이 있는지 중복검사
                Toast.makeText(getContext().getApplicationContext(), "중복확인 검사 시작", Toast.LENGTH_SHORT).show();
            }
        });


        //// 서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        // 태그의 추가 버튼 클릭 시,
        add_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 한글깨짐 해결 코드
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<Cafe>>() {
                        }.getType();

                        cafe_list = gson.fromJson(changeString, listType);

                        // cafe 테이블의 튜플이 제대로 오는지 확인 (테스트 할 때만 만들어두고 해당 기능 다 개발 시 제거하는게 좋음)
                        Log.d("test", String.valueOf(cafe_list.size()));

                        for(Cafe c : cafe_list) {
                            if(c.getCafeNum().equals(cafe_num)) {
                                Toast.makeText(getContext(), "이미 있는 카페입니다!", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                        Log.e("test_error", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
                navController.navigate(R.id.cafe_registration_to_cafe_registration_tag);
            }
        });


        // 등록하기 버튼 클릭 시
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내가 만든 카페의 카페디테일로 이동
            }
        });

        // 카페 등록의 태그 추가 페이지 (CafeRegistrationTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView setTag1 = root.findViewById(R.id.basic_tag1); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스1
        TextView setTag2 = root.findViewById(R.id.basic_tag2); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스2
        TextView setTag3 = root.findViewById(R.id.basic_tag3); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스3

        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {
                setTag1.setText(argBundle.getString("key1"));
                setTag2.setText(argBundle.getString("key2"));
                setTag3.setText(argBundle.getString("key3"));
            }
        }

        // Adapter 추가
        RecyclerView registrationRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);

//        // 카페 이미지 등록 리싸이클러뷰
//        ArrayList<CafeRegistrationItem> registrationImageItems = new ArrayList<>();
//
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.logo));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.logo_v2));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade1));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade2));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade3));
//
//        // Adapter 추가
//        RecyclerView registrationRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);
//
//        CafeRegistrationAdapter registrationAdapter = new CafeRegistrationAdapter(registrationImageItems);
//        registrationRecyclerView.setAdapter(registrationAdapter);
//
//        // Layout manager 추가
//        LinearLayoutManager registrationLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        registrationRecyclerView.setLayoutManager(registrationLayoutManager);
//
//        registrationAdapter.setOnItemClickListener_CafeRegistration(new CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final CafeRegistrationItem item = registrationImageItems.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getRegistrationImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
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

                registrationAdapter = new CafeRegistrationAdapter(uriList, getContext().getApplicationContext());
                cafeRegistrationImageRecyclerView.setAdapter(registrationAdapter);
                cafeRegistrationImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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

                    registrationAdapter = new CafeRegistrationAdapter(uriList, getContext().getApplicationContext());
                    cafeRegistrationImageRecyclerView.setAdapter(registrationAdapter);   // 리사이클러뷰에 어댑터 세팅
                    cafeRegistrationImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
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
