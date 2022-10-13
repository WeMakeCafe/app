package com.example.wmc.ui.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

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
    boolean name_test = false;
    Long[] k = new Long[36];
    EditText cafe_openHours_hour_input;
    EditText cafe_openHours_minute_input;
    EditText cafe_closeHours_hour_input;
    EditText cafe_closeHours_minute_input;

    File file;

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
        cafe_openHours_minute_input = root.findViewById(R.id.cafe_openHours_minute_input);
        cafe_closeHours_hour_input = root.findViewById(R.id.cafe_closeHours_hour_input);
        cafe_closeHours_minute_input= root.findViewById(R.id.cafe_closeHours_minute_input);

        // 서버연산을 위한 long형 배열 초기화 코드
        for(int i = 0 ; i<=35; i++){
            k[i] = (long) 0;
        }

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

                // 중복확인 버튼 클릭 시,
                checked_overlap_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String overlap_cafeName = cafe_name_input.getText().toString().replaceAll(" ", "");; // 카페 이름 중복확인할 String

                        // 데이터베이스에서 카페이름이 있는지 중복검사
                        if(overlap_cafeName.equals("")) {

                            AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                            mod.setTitle("잠깐!").setMessage("카페 이름을 입력해주세요!").setIcon(R.drawable.logo);
                            mod.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {

                                }
                            });

                            AlertDialog alertDialog = mod.create();
                            alertDialog.show();
                        }

                        else{
                            for(Cafe c : cafe_list) {
                                // 이미 동일한 이름의 카페가 등록되어있는 경우
                                if (c.getCafeName().replaceAll(" ", "").equals(overlap_cafeName)) {

                                    AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                                    mod.setTitle("잠깐!").setMessage("이미 있는 카페입니다!").setIcon(R.drawable.logo);
                                    mod.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {

                                        }
                                    });

                                    AlertDialog alertDialog = mod.create();
                                    alertDialog.show();
                                    
                                    name_test = false;
                                    break;
                                }

                                // 카페 등록이 가능한경우(이미 등록된 카페가 없을 때)
                                else
                                    name_test = true;
                            }

                            if( name_test ) {

                                AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                                mod.setTitle("잠깐!").setMessage("등록 가능한 카페입니다.").setIcon(R.drawable.logo);
                                mod.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {

                                    }
                                });

                                AlertDialog alertDialog = mod.create();
                                alertDialog.show();
                            }
                        }
                    }
                });


                // 등록하기 버튼 클릭 시
                registration_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (name_test && !cafe_name_input.getText().toString().equals("") && !cafe_address_input.getText().toString().equals("")
                                && !cafe_openHours_hour_input.getText().toString().equals("") && !cafe_closeHours_hour_input.getText().toString().equals("")
                                && !tag1.equals("") && !tag2.equals("") && !tag3.equals("")
                                && (Integer.parseInt(cafe_openHours_hour_input.getText().toString()) >=0) && (Integer.parseInt(cafe_openHours_hour_input.getText().toString()) <= 24)
                                && (Integer.parseInt(cafe_openHours_minute_input.getText().toString()) >=0) && (Integer.parseInt(cafe_openHours_minute_input.getText().toString()) < 60)
                                && (Integer.parseInt(cafe_closeHours_hour_input.getText().toString()) >=0) && (Integer.parseInt(cafe_closeHours_hour_input.getText().toString()) <= 24)
                                && (Integer.parseInt(cafe_closeHours_minute_input.getText().toString()) >=0) && (Integer.parseInt(cafe_closeHours_minute_input.getText().toString()) < 60)
                                && uriList.size() > 0)
                        {
                             Map map = new HashMap();
                                // 이미지, 키워드 추가 코드 작성 할 곳
                             map.put("cafeName", cafe_name_input.getText().toString());
                             map.put("cafeAddress", cafe_address_input.getText().toString());
                             map.put("openTime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                             map.put("closeTime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
                            switch (tag1) {
                                case ("#쓴맛"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#신맛"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#짠맛"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#단맛"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#향미"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#바디감"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#콜드브루"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#메뉴多"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#가성비"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#양많음"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#디저트맛집"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#논커피맛집"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#인스타"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#앤티크"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#모던"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#캐주얼"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#이국적"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#일상"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#따뜻한"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#조용한"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#우드톤"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#채광"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#힙한"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#귀여운"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#친절한"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#청결한"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#애견"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#주차장"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#노키즈존"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#교통편의"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#신속한"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#쾌적한"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#회의실"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#규모大"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#규모小"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#편한좌석"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }

                            switch (tag2) {
                                case ("#쓴맛"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#신맛"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#짠맛"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#단맛"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#향미"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#바디감"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#콜드브루"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#메뉴多"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#가성비"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#양많음"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#디저트맛집"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#논커피맛집"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#인스타"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#앤티크"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#모던"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#캐주얼"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#이국적"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#일상"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#따뜻한"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#조용한"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#우드톤"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#채광"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#힙한"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#귀여운"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#친절한"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#청결한"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#애견"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#주차장"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#노키즈존"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#교통편의"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#신속한"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#쾌적한"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#회의실"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#규모大"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#규모小"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#편한좌석"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }

                            switch (tag3) {
                                case ("#쓴맛"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#신맛"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#짠맛"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#단맛"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#향미"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#바디감"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#콜드브루"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#메뉴多"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#가성비"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#양많음"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#디저트맛집"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#논커피맛집"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#인스타"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#앤티크"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#모던"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#캐주얼"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#이국적"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#일상"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#따뜻한"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#조용한"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#우드톤"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#채광"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#힙한"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#귀여운"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#친절한"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#청결한"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#애견"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#주차장"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#노키즈존"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#교통편의"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#신속한"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#쾌적한"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#회의실"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#규모大"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#규모小"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#편한좌석"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }
                            map.put("keyword1", k[0]);
                            map.put("keyword2", k[1]);
                            map.put("keyword3", k[2]);
                            map.put("keyword4", k[3]);
                            map.put("keyword5", k[4]);
                            map.put("keyword6", k[5]);
                            map.put("keyword7", k[6]);
                            map.put("keyword8", k[7]);
                            map.put("keyword9", k[8]);
                            map.put("keyword10", k[9]);
                            map.put("keyword11", k[10]);
                            map.put("keyword12", k[11]);
                            map.put("keyword13", k[12]);
                            map.put("keyword14", k[13]);
                            map.put("keyword15", k[14]);
                            map.put("keyword16", k[15]);
                            map.put("keyword17", k[16]);
                            map.put("keyword18", k[17]);
                            map.put("keyword19", k[18]);
                            map.put("keyword20", k[19]);
                            map.put("keyword21", k[20]);
                            map.put("keyword22", k[21]);
                            map.put("keyword23", k[22]);
                            map.put("keyword24", k[23]);
                            map.put("keyword25", k[24]);
                            map.put("keyword26", k[25]);
                            map.put("keyword27", k[26]);
                            map.put("keyword28", k[27]);
                            map.put("keyword29", k[28]);
                            map.put("keyword30", k[29]);
                            map.put("keyword31", k[30]);
                            map.put("keyword32", k[31]);
                            map.put("keyword33", k[32]);
                            map.put("keyword34", k[33]);
                            map.put("keyword35", k[34]);
                            map.put("keyword36", k[35]);
                            map.put("tastePoint1", 0);
                            map.put("tastePoint2", 0);
                            map.put("tastePoint3", 0);
                            map.put("tastePoint4", 0);
                            map.put("seatPoint1", 0);
                            map.put("seatPoint2", 0);
                            map.put("seatPoint3", 0);
                            map.put("seatPoint4", 0);
                            map.put("studyPoint1", 0);
                            map.put("studyPoint2", 0);
                            map.put("studyPoint3", 0);
                            map.put("studyPoint4", 0);


                        String url2 = getResources().getString(R.string.url) + "cafe";
                        JSONObject jsonObject = new JSONObject(map);
                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            long cafeNum = response.getLong("cafeNum");

                                            for(Uri u : uriList){

                                                // 이미지 절대주소 만들기
                                                Cursor c = getContext().getContentResolver().query(Uri.parse(u.toString()), null,null,null,null);
                                                c.moveToNext();
                                                String absolutePath = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                                                file = new File(absolutePath);

                                                // 이미지 서버로 전송
                                                FileUploadUtils.sendCafeImage(file, cafeNum);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=UTF-8";
                            }
                        };
                            RequestQueue queue = Volley.newRequestQueue(requireContext());
                            queue.add(objectRequest);


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("카페 등록").setMessage("카페가 등록되었습니다.").setIcon(R.drawable.logo);

                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    navController.navigate(R.id.cafe_registration_to_list_cafelist);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                        else {
                            AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                            mod.setTitle("잠깐!").setMessage("비어 있는 항목이 있거나 시간입력이 부적절합니다!").setIcon(R.drawable.logo);
                            mod.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {

                                }
                            });

                            AlertDialog alertDialog = mod.create();
                            alertDialog.show();
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
                bundle.putString("name", cafe_name_input.getText().toString());
                bundle.putString("address", cafe_address_input.getText().toString());
                bundle.putBoolean("name_test", name_test); // 중복확인 여부 전달

                    bundle.putString("opentime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                    bundle.putString("closetime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());

                bundle.putParcelableArrayList("cafeImage", uriList);



                navController.navigate(R.id.cafe_registration_to_cafe_registration_tag, bundle);
            }
        });

        // 카페 등록의 태그 추가 페이지 (CafeRegistrationTagFragment) 에서 번들로 받아온 정보 반영 위한 코드
        TextView basic_tag1 = root.findViewById(R.id.basic_tag1); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스1
        TextView basic_tag2 = root.findViewById(R.id.basic_tag2); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스2
        TextView basic_tag3 = root.findViewById(R.id.basic_tag3); // 태그 추가 완료 시 반영할 카페 등록 페이지의 태그 박스3


    // 태그 추가 후, 저장되었던 값 다시 가져오기
        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {
                basic_tag1.setText(argBundle.getString("key1"));
                basic_tag2.setText(argBundle.getString("key2"));
                basic_tag3.setText(argBundle.getString("key3"));
                cafe_name_input.setText(argBundle.getString("name"));
                cafe_address_input.setText(argBundle.getString("address"));
                name_test = argBundle.getBoolean("name_test"); // 중복확인 여부


                String openhour_substring = argBundle.getString("opentime");
                String closehour_substring = argBundle.getString("closetime");

                if(openhour_substring.length() >= 4 ){
                    cafe_openHours_hour_input.setText(openhour_substring.substring(0,2));
                    cafe_openHours_minute_input.setText(openhour_substring.substring(2,4));
                }
                if(closehour_substring.length() >= 4 ){
                    cafe_closeHours_hour_input.setText(closehour_substring.substring(0,2));
                    cafe_closeHours_minute_input.setText(closehour_substring.substring(2,4));
                }
                if(openhour_substring.length() < 4){
                    cafe_openHours_hour_input.setText("");
                    cafe_openHours_minute_input.setText("");
                }
                if( closehour_substring.length() < 4 ) {
                    cafe_closeHours_hour_input.setText("");
                    cafe_closeHours_minute_input.setText("");
                }

                uriList = argBundle.getParcelableArrayList("cafeImage");

                //카페이미지 문장
                tag1 = argBundle.getString("key1");
                tag2 = argBundle.getString("key2");
                tag3 = argBundle.getString("key3");
            }

            registrationAdapter = new CafeRegistrationAdapter(uriList, getContext().getApplicationContext());
            cafeRegistrationImageRecyclerView.setAdapter(registrationAdapter);
            cafeRegistrationImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
            mod.setTitle("잠깐!").setMessage("이미지를 선택하지 않았습니다!").setIcon(R.drawable.logo).setNeutralButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                if(uriList.size() >= 5) {
                    AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                    mod.setTitle("잠깐!").setMessage("이미지 5개를 모두 선택하셨습니다.").setIcon(R.drawable.logo).setNeutralButton("확인", new DialogInterface.OnClickListener() {
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

                registrationAdapter = new CafeRegistrationAdapter(uriList, getContext().getApplicationContext());
                cafeRegistrationImageRecyclerView.setAdapter(registrationAdapter);
                cafeRegistrationImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 5){   // 선택한 이미지가 6장 이상인 경우
                    AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                    mod.setTitle("잠깐!").setMessage("사진은 5장까지 선택 가능합니다.").setIcon(R.drawable.logo).setNeutralButton("확인", new DialogInterface.OnClickListener() {
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
                        else {
                            AlertDialog.Builder mod = new AlertDialog.Builder(getActivity());
                            mod.setTitle("잠깐!").setMessage("사진은 5장까지 선택 가능합니다.").setIcon(R.drawable.logo).setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
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
