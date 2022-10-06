package com.example.wmc.ui.Fragment;

import android.content.DialogInterface;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.io.File;
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

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체 (갤러리에서 가져온 사진)
    ArrayList<Uri> cafeImage_uriList= new ArrayList<>();     //이미 설정되어있는 카페 사진(절대주소)
    ArrayList<Uri> unify_uriList = new ArrayList<>();      // 수정할 카페 사진
    ArrayList<CafeImage> CafeImage_list;
    CafeModifyAdapter cafeModifyAdapter;
    File file;

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

    Button imageList_LOG;

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

                for(Cafe c : cafe_list){
                    if(c.getCafeName().equals(cafe_name)) {
                        cafe_name_input.setText(c.getCafeName());
                        cafe_address_input.setText(c.getCafeAddress());
                        cafe_openHours_hour_input.setText(c.getOpenTime().substring(0, 2));
                        cafe_openHours_minute_input.setText(c.getOpenTime().substring(2, 4));
                        cafe_closeHours_hour_input.setText(c.getCloseTime().substring(0, 2));
                        cafe_closeHours_minute_input.setText(c.getCloseTime().substring(2,4));
                        cafe_num = c.getCafeNum();

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
                                Uri i = Uri.parse(ci.getFileUrl());
                                uriList.add(i);
                            }
                        }

                        cafeModifyAdapter = new CafeModifyAdapter(getContext(), uriList, CafeModifyFragment.this);
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

//

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            })
                                    {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    RequestQueue queue = Volley.newRequestQueue(requireContext());
                                    queue.add(objectRequest);



                                    // 리스트에있는 이미지들 서버로 전송하는 코드
                                    // 갤러리에서 가져온 애들 서버로 전송
                                    for(Uri u : uriList){
                                        // 이미지 절대주소 만들기
                                        if(u.toString().contains("http")) {
                                            file = new File(u.toString());

                                            // 이미지 서버로 전송
                                            FileUploadUtils.sendCafeImage(file, c.getCafeNum());
                                        }

                                        else {
                                            Cursor cursor = getContext().getContentResolver().query(Uri.parse(u.toString()), null,null,null,null);
                                            cursor.moveToNext();
                                            String absolutePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                                            file = new File(absolutePath);

                                            // 이미지 서버로 전송
                                            FileUploadUtils.sendCafeImage(file, c.getCafeNum());
                                        }
                                    }


                                    // 카페 수정 완료 시 해당 카페 디테일로 넘어가기 - 송상화
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("카페 수정").setMessage("카페가 수정되었습니다.").setIcon(R.drawable.logo);

                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            Bundle cafebundle = new Bundle();
                                            cafebundle.putString("cafeName", cafe_name_input.getText().toString());

                                            navController.navigate(R.id.cafe_modify_to_cafe_detail, cafebundle);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }

                                // -> 시간 변경이 없을 때 실행되는 문장
                                else {
                                    AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                                    mod.setTitle("잠깐!").setMessage("시간이 변경되지 않았습니다!").setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();

                                }
                            }
                        }
                    }

                }); // 수정하기 버튼 클릭 시, 동작 할 내용들

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
            AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
            mod.setTitle("잠깐!").setMessage("이미지를 선택하지 않았습니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                if(uriList.size() >= 5) {
                    AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                    mod.setTitle("잠깐!").setMessage("이미지 5개를 모두 선택하셨습니다!").setNeutralButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }

                else{
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);
                }

                cafeModifyAdapter = new CafeModifyAdapter(getContext(), uriList, CafeModifyFragment.this);
                cafeModifyImageRecyclerView.setAdapter(cafeModifyAdapter);
                cafeModifyImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 5){   // 선택한 이미지가 6장 이상인 경우
                    AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                    mod.setTitle("잠깐!").setMessage("사진은 5장까지 선택 가능합니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
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

                    }

                    cafeModifyAdapter = new CafeModifyAdapter(getContext(), uriList, CafeModifyFragment.this);
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
