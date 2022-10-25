package com.example.wmc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.wmc.database.Personal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ModifyActivity extends AppCompatActivity {
    String[] items = {"내가 다닌 초등학교의 이름은?", "나의 어머님의 성함은?", "내가 가장 감명깊게 본 영화는?"}; // 스피너 변수
    ArrayList<Personal> personal_list;
    String pass1;
    String pass2;
    String ques;
    String answer;
    String checkFavorite1="미선택";
    String checkFavorite2;
    String nickName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mypage_modified);

        Intent intent = getIntent();
        String text = intent.getStringExtra("mem_num");
        Long numLong = Long.parseLong(text);

        TextView nickname = findViewById(R.id.signUp_pw_input0);
        TextView password1 = findViewById(R.id.signUp_pw_input);
        TextView password2 = findViewById(R.id.signUp_verifyPw_input);
        Spinner spinner = findViewById(R.id.spinner4);
        TextView question = findViewById(R.id.signUp_nickName_input);
        RadioGroup radio = findViewById(R.id.preferenceGroup);
        RadioButton but1 = findViewById(R.id.radio_study);
        RadioButton but2 = findViewById(R.id.radio_taste);
        Button modify = findViewById(R.id.signUp_button1);

        // 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question.setHint(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                question.setText("비밀번호 확인 질문을 선택해주세요.");
            }
        });

        // 라디오 버튼
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavorite1 = "스터디";
                checkFavorite2 = "맛";
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavorite1 = "맛";
                checkFavorite2 = "스터디";
            }
        });

        // 서버 호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String url = getResources().getString(R.string.url) + "personal";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                Type listType = new TypeToken<ArrayList<Personal>>() {
                }.getType();

                personal_list = gson.fromJson(changeString, listType);

                for (Personal p : personal_list) {
                    if (p.getMemNum().equals(numLong)) {
                        if(p.getPersonalQuestion().equals("내가 다닌 초등학교의 이름은?")) spinner.setSelection(0);
                        if(p.getPersonalQuestion().equals("나의 어머님의 성함은?")) spinner.setSelection(1);
                        if(p.getPersonalQuestion().equals("내가 가장 감명깊게 본 영화는?")) spinner.setSelection(2);
                        if(p.getFavorite1().equals("스터디")) radio.check(but1.getId());
                        if(p.getFavorite1().equals("맛")) radio.check(but2.getId());
                        nickname.setText(p.getNickName());
                        question.setText(p.getPersonalAnswer());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickName = nickname.getText().toString();
                pass1 = password1.getText().toString();
                pass2 = password2.getText().toString();
                ques = spinner.getSelectedItem().toString();
                answer = question.getText().toString();


                // 닉네임 길이 검사
                if(nickname.length() < 6) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ModifyActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("닉네임은 6자 이상부터 가능합니다.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }

                // 비밀번호 유효성 검사
                else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{7,15}.$", pass1)) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ModifyActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("비밀번호는 숫자,문자,특수문자(@!%*#?&)를 모두 포함한 8~15자입니다.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }

                // 비밀번호 확인 검사
                else if (!pass1.equals(pass2)){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ModifyActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("비밀번호와 비밀번호 확인이 다릅니다.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }

                // 비밀번호 확인 답변 검사
                else if(answer.equals("")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ModifyActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("비밀번호 확인 질문에 대한 답변을 입력해주세요.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }

                // 우선순위 검사
                else if(checkFavorite1.equals("미선택")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ModifyActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("우선순위를 선택해주세요.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }

                else{
                    for(Personal p : personal_list){
                        if(p.getMemNum().equals(numLong)){

                            Map map = new HashMap();

                            map.put("memNum",p.getMemNum());
                            map.put("id",p.getId());
                            map.put("pwd",pass1);
                            map.put("nickName",nickName);
                            map.put("grade",p.getGrade());
                            map.put("profileimageurl",p.getProfileimageurl());
                            map.put("favorite1",checkFavorite1);
                            map.put("favorite2",checkFavorite2);
                            map.put("personalQuestion",ques);
                            map.put("personalAnswer",answer);
//                            map.put("confirmstring",p.getConfirmstring());

                            JSONObject jsonObject = new JSONObject(map);
                            String url2 = getResources().getString(R.string.url) + "personal/" + p.getMemNum().toString();
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
                                    }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=UTF-8";
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(objectRequest);
                        }
                    }
                }
            }
        });
    }
}