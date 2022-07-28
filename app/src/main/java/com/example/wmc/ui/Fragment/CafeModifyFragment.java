package com.example.wmc.ui.Fragment;

import android.os.Build;
import android.content.ClipData;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.example.wmc.CafeDetailImageViewPager.CafeDetailImageViewPagerAdapter;
import com.example.wmc.CafeModify.CafeModifyAdapter;
import com.example.wmc.CafeModify.CafeModifyItem;
import com.example.wmc.CafeRegistration.CafeRegistrationAdapter;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.database.Cafe;
import com.example.wmc.database.CafeImage;
import com.example.wmc.database.Personal;
import com.example.wmc.databinding.FragmentCafeModifyBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeModifyFragment extends Fragment {
    private FragmentCafeModifyBinding binding;
    private static NavController navController;
    Button add_image_button;
    Button modify_button;
    TextView request_deletion_textView;
    RecyclerView cafeModifyImageRecyclerView;

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<CafeImage> CafeImage_list;
    CafeModifyAdapter cafeModifyAdapter;
    private static final int REQUEST_CODE = 2222;
    private static final String TAG = "CafeModifyFragment";

    ArrayList<Cafe> cafe_list;
    TextView cafe_name_input;
    TextView cafe_address_input;
    EditText cafe_openHours_hour_input;
    EditText cafe_openHours_minute_input;
    EditText cafe_closeHours_hour_input;
    EditText cafe_closeHours_minute_input;
    Long cafe_num;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCafeModifyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        add_image_button = root.findViewById(R.id.add_image_button);
        modify_button = root.findViewById(R.id.modify_button);
        request_deletion_textView = root.findViewById(R.id.request_deletion_textView);
        cafeModifyImageRecyclerView = root.findViewById(R.id.cafeModifyImageRecyclerView);
        cafe_name_input = root.findViewById(R.id.cafe_name_input);
        cafe_address_input = root.findViewById(R.id.cafe_address_input);
        cafe_openHours_hour_input = root.findViewById(R.id.cafe_openHours_hour_input);
        cafe_openHours_minute_input = root.findViewById(R.id.cafe_openHours_minute_input);
        cafe_closeHours_hour_input= root.findViewById(R.id.cafe_closeHours_hour_input);
        cafe_closeHours_minute_input= root.findViewById(R.id.cafe_closeHours_minute_input);
        modify_button = root.findViewById(R.id.modify_button);
        request_deletion_textView = root.findViewById(R.id.request_deletion_textView);

        String cafe_name = getArguments().getString("name");  //getArguments로 번들 검색해서 받기
        String cafe_address = getArguments().getString("address");


        //// 서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String url = getResources().getString(R.string.url) + "cafe";


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

                for(Cafe c : cafe_list){
                    if(c.getCafeName().equals(cafe_name)) {
                        cafe_name_input.setText(c.getCafeName());
                        cafe_address_input.setText(c.getCafeAddress());
                        cafe_openHours_hour_input.setText(c.getOpenTime().substring(0, 2));
                        cafe_openHours_minute_input.setText(c.getOpenTime().substring(2, 4));
                        cafe_closeHours_hour_input.setText(c.getCloseTime().substring(0, 2));
                        cafe_closeHours_minute_input.setText(c.getCloseTime().substring(2,4));
                        cafe_num = c.getCafeNum();
                        //이미지 등록 코드

                        Log.d("cafe_closetime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
                    }
                }


                String get_cafeImage_url = getResources().getString(R.string.url) + "cafeImage";

                StringRequest cafeImage_stringRequest = new StringRequest(Request.Method.GET, get_cafeImage_url, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        // 한글깨짐 해결 코드
                        String changeString = new String();
                        try {
                            changeString = new String(response.getBytes("8859_1"),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        Type listType = new TypeToken<ArrayList<CafeImage>>(){}.getType();

                        CafeImage_list = gson.fromJson(changeString, listType);

                        for(CafeImage ci : CafeImage_list){
                            if(ci.getCafeNum().equals(cafe_num)){
                                Log.d("cafeImage URL", ci.getFileUrl());
                                Uri i = Uri.parse(ci.getFileUrl());
                                uriList.add(i);
                            }
                        }
                        cafeModifyAdapter = new CafeModifyAdapter(uriList, getContext().getApplicationContext());
                        cafeModifyImageRecyclerView.setAdapter(cafeModifyAdapter);
                        cafeModifyImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("cafeImage_stringRequest_error",error.toString());
                    }
                });


                requestQueue.add(cafeImage_stringRequest);
                modify_button.setOnClickListener(new View.OnClickListener() { // 카페 수정하기 버튼 누를 시

                    @Override
                    public void onClick(View v) {

                        for(Cafe c : cafe_list) {
                            if(c.getCafeNum().equals(cafe_num)) {  //bundle에서 가져온 카페아이디값 cafe_name에 넣어서 비교 연산

                                Map map = new HashMap();
                                map.put("cafeName", cafe_name_input.getText().toString());
                                map.put("cafeAddress", cafe_address_input.getText().toString());
                                map.put("openTime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                                map.put("closeTime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
//                                map.put("cafeImage", c.getCafeImage());
//                                map.put("reviewNum", c.getReviewNum());
                                map.put("keyword1", c.getKeyword1());
                                map.put("keyword2", c.getKeyword2());
                                map.put("keyword3", c.getKeyword3());
                                map.put("keyword4", c.getKeyword4());
                                map.put("keyword5", c.getKeyword5());
                                map.put("keyword6", c.getKeyword6());
                                map.put("keyword7", c.getKeyword7());
                                map.put("keyword8", c.getKeyword8());
                                map.put("keyword9", c.getKeyword9());
                                map.put("keyword10", c.getKeyword10());
                                map.put("keyword11", c.getKeyword11());
                                map.put("keyword12", c.getKeyword12());
                                map.put("keyword13", c.getKeyword13());
                                map.put("keyword14", c.getKeyword14());
                                map.put("keyword15", c.getKeyword15());
                                map.put("keyword16", c.getKeyword16());
                                map.put("keyword17", c.getKeyword17());
                                map.put("keyword18", c.getKeyword18());
                                map.put("keyword19", c.getKeyword19());
                                map.put("keyword20", c.getKeyword20());
                                map.put("keyword21", c.getKeyword21());
                                map.put("keyword22", c.getKeyword22());
                                map.put("keyword23", c.getKeyword23());
                                map.put("keyword24", c.getKeyword24());
                                map.put("keyword25", c.getKeyword25());
                                map.put("keyword26", c.getKeyword26());
                                map.put("keyword27", c.getKeyword27());
                                map.put("keyword28", c.getKeyword28());
                                map.put("keyword29", c.getKeyword29());
                                map.put("keyword30", c.getKeyword30());
                                map.put("keyword31", c.getKeyword31());
                                map.put("keyword32", c.getKeyword32());
                                map.put("keyword33", c.getKeyword33());
                                map.put("keyword34", c.getKeyword34());
                                map.put("keyword35", c.getKeyword35());
                                map.put("keyword36", c.getKeyword36());
                                map.put("scoreNum", c.getScoreNum());
                                map.put("tastePoint1", c.getTastePoint1());
                                map.put("tastePoint2", c.getTastePoint2());
                                map.put("tastePoint3", c.getTastePoint3());
                                map.put("tastePoint4", c.getTastePoint4());
                                map.put("seatPoint1", c.getSeatPoint1());
                                map.put("seatPoint2", c.getSeatPoint2());
                                map.put("seatPoint3", c.getSeatPoint3());
                                map.put("seatPoint4", c.getSeatPoint4());
                                map.put("studyPoint1", c.getStudyPoint1());
                                map.put("studyPoint2", c.getStudyPoint2());
                                map.put("studyPoint3", c.getStudyPoint3());
                                map.put("studyPoint4", c.getStudyPoint4());

                                if(!((c.getOpenTime().equals(cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString())) &&
                                        (c.getCloseTime().equals(cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString())))) {
                                    JSONObject jsonObject = new JSONObject(map);

                                    String url2 = getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // 해당 카페에만 데이터 삽입하기 위함

                                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url2, jsonObject,
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



                                    // 카페 수정 완료 시 해당 카페 디테일로 넘어가기 - 송상화
                                    Bundle cafebundle = new Bundle();
                                    cafebundle.putString("cafeName", cafe_name_input.getText().toString());

                                    navController.navigate(R.id.cafe_modify_to_cafe_detail, cafebundle);
                                }

                                // -> 시간 변경이 없을 때 실행되는 문장
                                else {
                                    Toast.makeText(getActivity(), "시간이 변경되지 않았습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                Log.e("test_error2", error.toString());
            }
        });
        requestQueue.add(stringRequest);


        request_deletion_textView.setOnClickListener(new View.OnClickListener() { // 삭제요청 버튼
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", cafe_name);
                bundle.putString("address", cafe_address);
                bundle.putLong("cafeNum", cafe_num);
                navController.navigate(R.id.cafe_modify_to_cafe_delete, bundle);
            }
        });

        //// 서버 엔드

        // 태그 추가 페이지 (ReviewTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
//        TextView name = root.findViewById(R.id.cafe_name_input);
//        TextView address = root.findViewById(R.id.cafe_address_input);
//        TextView time_open = root.findViewById(R.id.cafe_openHours_input);
//        TextView time_close = root.findViewById(R.id.cafe_closeHours_input);
//
//        Bundle argBundle = getArguments();
//        if (argBundle != null) {
//            if (argBundle.getString("name") != null){
//                name.setText(argBundle.getString("name"));
//                address.setText(argBundle.getString("address"));
//                time_open.setText(argBundle.getString("time_open"));
//                time_close.setText(argBundle.getString("time_close"));
//            }
//        }

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
