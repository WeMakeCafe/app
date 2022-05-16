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
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeRegistrationFragment extends Fragment {

    private FragmentCafeRegistrationBinding binding;
    private static NavController navController;
    Button cafeRegistratin_add_image_button;
    Button add_tag_button;
    Button registration_button;
    Button checked_overlap_button;
    RecyclerView cafeRegistrationImageRecyclerView;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    CafeRegistrationAdapter registrationAdapter;
    private static final int REQUEST_CODE = 1111;
    private static final String TAG = "CafeRegistrationFragment";
    Button add_cafe_button;
    ArrayList<Cafe> cafe_list;
    TextView cafe_name_input;
    TextView cafe_address_input;
    String tag1;
    String tag2;
    String tag3;

    EditText cafe_openHours_hour_input;
    EditText cafe_openHours_minute_input;
    EditText cafe_closeHours_hour_input;
    EditText cafe_closeHours_minute_input;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        add_tag_button = root.findViewById(R.id.add_tag_button);
        add_cafe_button = root.findViewById(R.id.add_cafe_button);
        registration_button = root.findViewById(R.id.registration_button);
        checked_overlap_button = root.findViewById(R.id.checked_overlap_button);
        cafeRegistratin_add_image_button = root.findViewById(R.id.cafeRegistratin_add_image_button);
        cafeRegistrationImageRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);
        cafe_name_input = root.findViewById(R.id.cafe_name_input);
        cafe_address_input = root.findViewById(R.id.cafe_address_input);
        cafe_openHours_hour_input = root.findViewById(R.id.cafe_openHours_hour_input);
        cafe_closeHours_minute_input= root.findViewById(R.id.cafe_closeHours_minute_input);

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String url = "http://54.221.33.199:8080/cafe";

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

                // 중복확인 버튼 클릭 시,
                checked_overlap_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 데이터베이스에서 카페이름이 있는지 중복검사
                        for(Cafe c : cafe_list) {
                            if(c.getCafeName().equals(cafe_name_input.getText().toString())) {
                                Toast.makeText(getContext().getApplicationContext(), "이미 있는 카페입니다!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getContext().getApplicationContext(), "가능한 카페입니다!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                // 등록하기 버튼 클릭 시
                registration_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((cafe_name_input != null) && (cafe_address_input != null) && (cafe_openHours_hour_input != null)
                                && (cafe_closeHours_hour_input != null) && (tag1 !=null) && (tag2 != null) && (tag3 != null)) {
                             Map map = new HashMap();
                                // 이미지, 키워드 추가 코드 작성 할 곳
                             map.put("cafeName", cafe_name_input.getText().toString());
                             map.put("cafeAddress", cafe_address_input.getText().toString());
                             map.put("openTime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                             map.put("closeTime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
                            // 이미지, 키워드 추가 코드 작성 할 곳
                            // 태그 문자열형식을 숫자로 변환
                            switch(tag1) {
                                case ("#쓴맛"):
                                    map.put("keyword1", 1);
                                    break;
                                    case ("#신맛"):
                                    map.put("keyword2", 1);
                                    break;
                                case ("#짠맛"):
                                    map.put("keyword3", 1);
                                    break;
                                case ("#단맛"):
                                    map.put("keyword4", 1);
                                    break;
                                case ("#향미"):
                                    map.put("keyword5", 1);
                                    break;
                                case ("#바디감"):
                                    map.put("keyword6", 1);
                                    break;
                                case ("#콜드브루"):
                                    map.put("keyword7", 1);
                                    break;
                                case ("#메뉴多"):
                                    map.put("keyword8", 1);
                                    break;
                                case ("#가성비"):
                                    map.put("keyword9", 1);
                                    break;
                                case ("#양많음"):
                                    map.put("keyword10", 1);
                                    break;
                                case ("#디저트맛집"):
                                    map.put("keyword11", 1);
                                    break;
                                case ("#논커피맛집"):
                                    map.put("keyword12", 1);
                                    break;
                                case ("#인스타"):
                                    map.put("keyword13", 1);
                                    break;
                                case ("#앤티크"):
                                    map.put("keyword14", 1);
                                    break;
                                case ("#모던"):
                                    map.put("keyword15", 1);
                                    break;
                                case ("#캐주얼"):
                                    map.put("keyword16", 1);
                                    break;
                                case ("#이국적"):
                                    map.put("keyword17", 1);
                                    break;
                                case ("#일상"):
                                    map.put("keyword18", 1);
                                    break;
                                case ("#따뜻한"):
                                    map.put("keyword19", 1);
                                    break;
                                case ("#조용한"):
                                    map.put("keyword20", 1);
                                    break;
                                case ("#우드톤"):
                                    map.put("keyword21", 1);
                                    break;
                                case ("#채광"):
                                    map.put("keyword22", 1);
                                    break;
                                case ("#힙한"):
                                    map.put("keyword23", 1);
                                    break;
                                case ("#귀여운"):
                                    map.put("keyword24", 1);
                                    break;
                                case ("#친절한"):
                                    map.put("keyword25", 1);
                                    break;
                                case ("#청결한"):
                                    map.put("keyword26", 1);
                                    break;
                                case ("#애견"):
                                    map.put("keyword27", 1);
                                    break;
                                case ("#주차장"):
                                    map.put("keyword28", 1);
                                    break;
                                case ("#노키즈존"):
                                    map.put("keyword29", 1);
                                    break;
                                case ("#교통편의"):
                                    map.put("keyword30", 1);
                                    break;
                                case ("#신속한"):
                                    map.put("keyword31", 1);
                                    break;
                                case ("#쾌적한"):
                                    map.put("keyword32", 1);
                                    break;
                                case ("#회의실"):
                                    map.put("keyword33", 1);
                                    break;
                                case ("#규모大"):
                                    map.put("keyword34", 1);
                                    break;
                                case ("#규모小"):
                                    map.put("keyword35", 1);
                                    break;
                                case ("#편한좌석"):
                                    map.put("keyword36", 1);
                            }

                            switch (tag2) {
                                case ("#쓴맛"):
                                    map.put("keyword1", 1);
                                    break;
                                case ("#신맛"):
                                    map.put("keyword2", 1);
                                    break;
                                case ("#짠맛"):
                                    map.put("keyword3", 1);
                                    break;
                                case ("#단맛"):
                                    map.put("keyword4", 1);
                                    break;
                                case ("#향미"):
                                    map.put("keyword5", 1);
                                    break;
                                case ("#바디감"):
                                    map.put("keyword6", 1);
                                    break;
                                case ("#콜드브루"):
                                    map.put("keyword7", 1);
                                    break;
                                case ("#메뉴多"):
                                    map.put("keyword8", 1);
                                    break;
                                case ("#가성비"):
                                    map.put("keyword9", 1);
                                    break;
                                case ("#양많음"):
                                    map.put("keyword10", 1);
                                    break;
                                case ("#디저트맛집"):
                                    map.put("keyword11", 1);
                                    break;
                                case ("#논커피맛집"):
                                    map.put("keyword12", 1);
                                case ("#인스타"):
                                    map.put("keyword13", 1);
                                    break;
                                case ("#앤티크"):
                                    map.put("keyword14", 1);
                                    break;
                                case ("#모던"):
                                    map.put("keyword15", 1);
                                    break;
                                case ("#캐주얼"):
                                    map.put("keyword16", 1);
                                    break;
                                case ("#이국적"):
                                    map.put("keyword17", 1);
                                    break;
                                case ("#일상"):
                                    map.put("keyword18", 1);
                                    break;
                                case ("#따뜻한"):
                                    map.put("keyword19", 1);
                                    break;
                                case ("#조용한"):
                                    map.put("keyword20", 1);
                                    break;
                                case ("#우드톤"):
                                    map.put("keyword21", 1);
                                    break;
                                case ("#채광"):
                                    map.put("keyword22", 1);
                                    break;
                                case ("#힙한"):
                                    map.put("keyword23", 1);
                                    break;
                                case ("#귀여운"):
                                    map.put("keyword24", 1);
                                    break;
                                case ("#친절한"):
                                    map.put("keyword25", 1);
                                    break;
                                case ("#청결한"):
                                    map.put("keyword26", 1);
                                    break;
                                case ("#애견"):
                                    map.put("keyword27", 1);
                                    break;
                                case ("#주차장"):
                                    map.put("keyword28", 1);
                                    break;
                                case ("#노키즈존"):
                                    map.put("keyword29", 1);
                                    break;
                                case ("#교통편의"):
                                    map.put("keyword30", 1);
                                    break;
                                case ("#신속한"):
                                    map.put("keyword31", 1);
                                    break;
                                case ("#쾌적한"):
                                    map.put("keyword32", 1);
                                    break;
                                case ("#회의실"):
                                    map.put("keyword33", 1);
                                    break;
                                case ("#규모大"):
                                    map.put("keyword34", 1);
                                    break;
                                case ("#규모小"):
                                    map.put("keyword35", 1);
                                    break;
                                case ("#편한좌석"):
                                    map.put("keyword36", 1);
                            }

                            switch (tag3) {
                                case ("#쓴맛"):
                                    map.put("keyword1", 1);
                                    break;
                                case ("#신맛"):
                                    map.put("keyword2", 1);
                                    break;
                                case ("#짠맛"):
                                    map.put("keyword3", 1);
                                    break;
                                case ("#단맛"):
                                    map.put("keyword4", 1);
                                    break;
                                case ("#향미"):
                                    map.put("keyword5", 1);
                                    break;
                                case ("#바디감"):
                                    map.put("keyword6", 1);
                                    break;
                                case ("#콜드브루"):
                                    map.put("keyword7", 1);
                                    break;
                                case ("#메뉴多"):
                                    map.put("keyword8", 1);
                                    break;
                                case ("#가성비"):
                                    map.put("keyword9", 1);
                                    break;
                                case ("#양많음"):
                                    map.put("keyword10", 1);
                                    break;
                                case ("#디저트맛집"):
                                    map.put("keyword11", 1);
                                    break;
                                case ("#논커피맛집"):
                                    map.put("keyword12", 1);
                                case ("#인스타"):
                                    map.put("keyword13", 1);
                                    break;
                                case ("#앤티크"):
                                    map.put("keyword14", 1);
                                    break;
                                case ("#모던"):
                                    map.put("keyword15", 1);
                                    break;
                                case ("#캐주얼"):
                                    map.put("keyword16", 1);
                                    break;
                                case ("#이국적"):
                                    map.put("keyword17", 1);
                                    break;
                                case ("#일상"):
                                    map.put("keyword18", 1);
                                    break;
                                case ("#따뜻한"):
                                    map.put("keyword19", 1);
                                    break;
                                case ("#조용한"):
                                    map.put("keyword20", 1);
                                    break;
                                case ("#우드톤"):
                                    map.put("keyword21", 1);
                                    break;
                                case ("#채광"):
                                    map.put("keyword22", 1);
                                    break;
                                case ("#힙한"):
                                    map.put("keyword23", 1);
                                    break;
                                case ("#귀여운"):
                                    map.put("keyword24", 1);
                                    break;
                                case ("#친절한"):
                                    map.put("keyword25", 1);
                                    break;
                                case ("#청결한"):
                                    map.put("keyword26", 1);
                                    break;
                                case ("#애견"):
                                    map.put("keyword27", 1);
                                    break;
                                case ("#주차장"):
                                    map.put("keyword28", 1);
                                    break;
                                case ("#노키즈존"):
                                    map.put("keyword29", 1);
                                    break;
                                case ("#교통편의"):
                                    map.put("keyword30", 1);
                                    break;
                                case ("#신속한"):
                                    map.put("keyword31", 1);
                                    break;
                                case ("#쾌적한"):
                                    map.put("keyword32", 1);
                                    break;
                                case ("#회의실"):
                                    map.put("keyword33", 1);
                                    break;
                                case ("#규모大"):
                                    map.put("keyword34", 1);
                                    break;
                                case ("#규모小"):
                                    map.put("keyword35", 1);
                                    break;
                                case ("#편한좌석"):
                                    map.put("keyword36", 1);
                            }


                            String url2 = "http://54.221.33.199:8080/cafe";
                            JSONObject jsonObject = new JSONObject(map);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("test", error.toString());
                                    }
                                }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=UTF-8";
                            }
                        };
                            RequestQueue queue = Volley.newRequestQueue(requireContext());
                            queue.add(objectRequest);

                            // 카페 등록 완료 시 해당 카페 디테일로 넘어가기 - 송상화
                            Bundle bundle = new Bundle();
                            bundle.putString("cafeName", cafe_name_input.getText().toString());
                            navController.navigate(R.id.cafe_registration_to_cafe_detail, bundle);
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), "비어 있는 항목이 있습니다!", Toast.LENGTH_LONG).show();
                        }
                    }

                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                Log.e("test_error", error.toString());
            }
        });
        requestQueue.add(stringRequest);

        // 기본 태그 추가 버튼 클릭 시
        add_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(); // 프래그먼트 간 데이터 전달 위한 번들
                bundle.putString("name",cafe_name_input.getText().toString());
                bundle.putString("address", cafe_address_input.getText().toString());
                bundle.putString("opentime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                bundle.putString("closetime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
                navController.navigate(R.id.cafe_registration_to_cafe_registration_tag, bundle);
            }
        });

        // 카페 등록의 태그 추가 페이지 (CafeRegistrationTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView basic_tag1 = root.findViewById(R.id.basic_tag1); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스1
        TextView basic_tag2 = root.findViewById(R.id.basic_tag2); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스2
        TextView basic_tag3 = root.findViewById(R.id.basic_tag3); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스3



        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {
                basic_tag1.setText(argBundle.getString("key1"));
                basic_tag2.setText(argBundle.getString("key2"));
                basic_tag3.setText(argBundle.getString("key3"));
                cafe_name_input.setText(argBundle.getString("name"));
                cafe_address_input.setText(argBundle.getString("address"));
                cafe_openHours_hour_input.setText(String.valueOf(Integer.parseInt(argBundle.getString("opentime"))/100));
                cafe_openHours_minute_input.setText(String.valueOf(Integer.parseInt(argBundle.getString("opentime"))%100));
                cafe_closeHours_hour_input.setText(String.valueOf(Integer.parseInt(argBundle.getString("closetime"))/100));
                cafe_closeHours_minute_input.setText(String.valueOf(Integer.parseInt(argBundle.getString("closetime"))%100));

                //카페이미지 문장
                tag1 = argBundle.getString("key1");
                tag2 = argBundle.getString("key2");
                tag3 = argBundle.getString("key3");
            }
        }



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
