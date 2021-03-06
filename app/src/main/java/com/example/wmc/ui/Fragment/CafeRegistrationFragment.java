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
    ArrayList<Uri> uriList = new ArrayList<>();     // ???????????? uri??? ?????? ArrayList ??????
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

//        Button test_Button = root.findViewById(R.id.test_Button);
//
//        test_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Long mem_num = 1L;
//                Long review_num = 1l;
//                FileUploadUtils.goSend(file, mem_num, review_num);
//            }
//        });

        // ??????????????? ?????? long??? ?????? ????????? ??????
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
                // ???????????? ?????? ??????
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

                // cafe ???????????? ????????? ????????? ????????? ?????? (????????? ??? ?????? ??????????????? ?????? ?????? ??? ?????? ??? ??????????????? ??????)
                Log.d("test", String.valueOf(cafe_list.size()));

                // ???????????? ?????? ?????? ???,
                checked_overlap_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        name_test = true;
//                        String overlap_cafeName = cafe_name_input.getText().toString().replaceAll(" ", "");; // ?????? ?????? ??????????????? String
//
//                        // ???????????????????????? ??????????????? ????????? ????????????
//                        if(overlap_cafeName.equals("")) {
//                            Toast.makeText(getContext().getApplicationContext(), "?????? ????????? ??????????????????!", Toast.LENGTH_LONG).show();
//                        }
//                        else{
//                            for(Cafe c : cafe_list) {
//                                if (c.getCafeName().replaceAll(" ", "").equals(overlap_cafeName)) {
//                                    Toast.makeText(getContext().getApplicationContext(), "?????? ?????? ???????????????!", Toast.LENGTH_LONG).show();
//                                    name_test = false;
//                                    break;
//                                } else { name_test = true; }
//                            }
//                            if( name_test ) Toast.makeText(getContext().getApplicationContext(), "????????? ???????????????!", Toast.LENGTH_LONG).show();
//                        }
                    }
                });

                // ???????????? ?????? ?????? ???
                registration_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (name_test && !cafe_name_input.getText().toString().equals("") && !cafe_address_input.getText().toString().equals("")
                                && !cafe_openHours_hour_input.getText().toString().equals("") && !cafe_closeHours_hour_input.getText().toString().equals("")
                                && !tag1.equals("") && !tag2.equals("") && !tag3.equals("")
                                && (Integer.parseInt(cafe_openHours_hour_input.getText().toString()) >=0) && (Integer.parseInt(cafe_openHours_hour_input.getText().toString()) <= 24)
                                && (Integer.parseInt(cafe_openHours_minute_input.getText().toString()) >=0) && (Integer.parseInt(cafe_openHours_minute_input.getText().toString()) < 60)
                                && (Integer.parseInt(cafe_closeHours_hour_input.getText().toString()) >=0) && (Integer.parseInt(cafe_closeHours_hour_input.getText().toString()) <= 24)
                                && (Integer.parseInt(cafe_closeHours_minute_input.getText().toString()) >=0) && (Integer.parseInt(cafe_closeHours_minute_input.getText().toString()) < 60))
                        {
                             Map map = new HashMap();
                                // ?????????, ????????? ?????? ?????? ?????? ??? ???
                             map.put("cafeName", cafe_name_input.getText().toString());
                             map.put("cafeAddress", cafe_address_input.getText().toString());
                             map.put("openTime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                             map.put("closeTime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
                            // ????????? ?????? ?????? ?????? ??? ???
                            // ?????? ?????????????????? ????????? ??????
                            switch (tag1) {
                                case ("#??????"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#???????????????"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#???????????????"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }

                            switch (tag2) {
                                case ("#??????"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#???????????????"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#???????????????"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[35] = Long.valueOf(1);
                                    break;
                            }

                            switch (tag3) {
                                case ("#??????"):
                                    k[0] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[1] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[2] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[3] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[4] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[5] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[6] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[7] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[8] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[9] = Long.valueOf(1);
                                    break;
                                case ("#???????????????"):
                                    k[10] = Long.valueOf(1);
                                    break;
                                case ("#???????????????"):
                                    k[11] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[12] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[13] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[14] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[15] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[16] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[17] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[18] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[19] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[20] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[21] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[22] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[23] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[24] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[25] = Long.valueOf(1);
                                    break;
                                case ("#??????"):
                                    k[26] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[27] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[28] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
                                    k[29] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[30] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[31] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[32] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[33] = Long.valueOf(1);
                                    break;
                                case ("#?????????"):
                                    k[34] = Long.valueOf(1);
                                    break;
                                case ("#????????????"):
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

                                                // ????????? ???????????? ?????????
                                                Cursor c = getContext().getContentResolver().query(Uri.parse(u.toString()), null,null,null,null);
                                                c.moveToNext();
                                                String absolutePath = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                                                Log.d("test_check" , absolutePath);
                                                file = new File(absolutePath);

                                                // ????????? ????????? ??????
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


                            // ?????? ?????? ?????? ??? ?????? ?????? ???????????? ???????????? - ?????????
//                            Bundle bundle = new Bundle();
//                            bundle.putString("cafeName", cafe_name_input.getText().toString());
//                            navController.navigate(R.id.cafe_registration_to_list_cafelist);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("?????? ??????").setMessage("????????? ?????????????????????.").setIcon(R.drawable.logo);

                            builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
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
                            Toast.makeText(getContext().getApplicationContext(), "?????? ?????? ????????? ????????? ??????????????? ??????????????????!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // ????????? ????????? ??? ????????? ????????? ???????????? ??????
                Log.e("test_error", error.toString());
            }
        });
        requestQueue.add(stringRequest);

        // ?????? ?????? ?????? ?????? ?????? ???
        add_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(); // ??????????????? ??? ????????? ?????? ?????? ??????
                bundle.putString("name", cafe_name_input.getText().toString());
                bundle.putString("address", cafe_address_input.getText().toString());
                bundle.putBoolean("name_test", name_test); // ???????????? ?????? ??????

//                if(!cafe_openHours_hour_input.getText().toString().equals("") && !cafe_openHours_minute_input.getText().toString().equals("") &&
//                        !cafe_closeHours_hour_input.getText().toString().equals("") && !cafe_closeHours_minute_input.getText().toString().equals("")){
                    bundle.putString("opentime", cafe_openHours_hour_input.getText().toString() + cafe_openHours_minute_input.getText().toString());
                    bundle.putString("closetime", cafe_closeHours_hour_input.getText().toString() + cafe_closeHours_minute_input.getText().toString());
//                }

                navController.navigate(R.id.cafe_registration_to_cafe_registration_tag, bundle);
            }
        });

        // ?????? ????????? ?????? ?????? ????????? (CafeRegistrationTagFragment) ?????? ????????? ????????? ?????? ?????? ?????? ??????
        TextView basic_tag1 = root.findViewById(R.id.basic_tag1); // ?????? ?????? ?????? ??? ????????? ?????? ?????? ???????????? ?????? ??????1
        TextView basic_tag2 = root.findViewById(R.id.basic_tag2); // ?????? ?????? ?????? ??? ????????? ?????? ?????? ???????????? ?????? ??????2
        TextView basic_tag3 = root.findViewById(R.id.basic_tag3); // ?????? ?????? ?????? ??? ????????? ?????? ?????? ???????????? ?????? ??????3



        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            if (argBundle.getString("key1") != null) {
                basic_tag1.setText(argBundle.getString("key1"));
                basic_tag2.setText(argBundle.getString("key2"));
                basic_tag3.setText(argBundle.getString("key3"));
                cafe_name_input.setText(argBundle.getString("name"));
                cafe_address_input.setText(argBundle.getString("address"));
                name_test = argBundle.getBoolean("name_test"); // ???????????? ??????

                cafe_openHours_hour_input.setText(argBundle.getString("opentime"));
                cafe_openHours_minute_input.setText(argBundle.getString("opentime"));
                cafe_closeHours_hour_input.setText(argBundle.getString("closetime"));
                cafe_closeHours_minute_input.setText(argBundle.getString("closetime"));

                //??????????????? ??????
                tag1 = argBundle.getString("key1");
                tag2 = argBundle.getString("key2");
                tag3 = argBundle.getString("key3");
            }
        }

        String openhour_substring = cafe_openHours_hour_input.getText().toString();
        String closehour_substring = cafe_closeHours_hour_input.getText().toString();

        if(openhour_substring.length() >= 4 ){
            cafe_openHours_hour_input.setText(openhour_substring.substring(0,2));
            cafe_openHours_minute_input.setText(openhour_substring.substring(2,4));
        }
        if(closehour_substring.length() >= 4 ){
            cafe_closeHours_hour_input.setText(closehour_substring.substring(0,2));
            cafe_closeHours_minute_input.setText(closehour_substring.substring(2,4));
        }
        if(openhour_substring.length() < 4 || closehour_substring.length() < 4 ){
            cafe_openHours_hour_input.setText("");
            cafe_openHours_minute_input.setText("");
            cafe_closeHours_hour_input.setText("");
            cafe_closeHours_minute_input.setText("");
        }



        // ?????? ?????? ???????????? ????????? ?????? ??????(+) ?????? ???,
        cafeRegistratin_add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ???????????? ??????
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // ?????? ???????????? ????????? ??? ????????? ??????
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        // Adapter ??????
        RecyclerView registrationRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);

//        // ?????? ????????? ?????? ??????????????????
//        ArrayList<CafeRegistrationItem> registrationImageItems = new ArrayList<>();
//
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.logo));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.logo_v2));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade1));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade2));
//        registrationImageItems.add(new CafeRegistrationItem(R.drawable.bean_grade3));
//
//        // Adapter ??????
//        RecyclerView registrationRecyclerView = root.findViewById(R.id.cafeRegistrationImageRecyclerView);
//
//        CafeRegistrationAdapter registrationAdapter = new CafeRegistrationAdapter(registrationImageItems);
//        registrationRecyclerView.setAdapter(registrationAdapter);
//
//        // Layout manager ??????
//        LinearLayoutManager registrationLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        registrationRecyclerView.setLayoutManager(registrationLayoutManager);
//
//        registrationAdapter.setOnItemClickListener_CafeRegistration(new CafeRegistrationAdapter.OnItemClickEventListener_CafeRegistration() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                final CafeRegistrationItem item = registrationImageItems.get(a_position);
//                Toast.makeText(getContext().getApplicationContext(), item.getRegistrationImage() + " ?????????.", Toast.LENGTH_SHORT).show();
//            }
//        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // ?????? ???????????? ???????????? ?????? ??????
            Toast.makeText(getContext().getApplicationContext(), "???????????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
        }

        else{   // ???????????? ???????????? ????????? ??????
            if(data.getClipData() == null){     // ???????????? ????????? ????????? ??????
                if(uriList.size() >= 5) {
                    Toast.makeText(getContext().getApplicationContext(), "????????? 5?????? ?????? ?????????????????????.", Toast.LENGTH_LONG).show();
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

            else{      // ???????????? ????????? ????????? ??????
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 5){   // ????????? ???????????? 6??? ????????? ??????
                    Toast.makeText(getContext().getApplicationContext(), "????????? 5????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                }

                else{   // ????????? ???????????? 1??? ?????? 5??? ????????? ??????
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){

                        if(uriList.size() <= 4){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // ????????? ??????????????? uri??? ????????????.

//                            Cursor c = getContext().getContentResolver().query(Uri.parse(imageUri.toString()), null,null,null,null);
//                            c.moveToNext();
//                            String absolutePath = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
//                            Log.d("test_check" , absolutePath);
//                            file = new File(absolutePath);

                            try {
                                uriList.add(imageUri);  //uri??? list??? ?????????.

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), "????????? 5????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                    registrationAdapter = new CafeRegistrationAdapter(uriList, getContext().getApplicationContext());
                    cafeRegistrationImageRecyclerView.setAdapter(registrationAdapter);   // ????????????????????? ????????? ??????
                    cafeRegistrationImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));     // ?????????????????? ?????? ????????? ??????
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
