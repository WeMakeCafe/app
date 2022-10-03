package com.example.wmc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.wmc.FindidActivity;
import com.example.wmc.FindpwActivity;
import com.example.wmc.LoginAcivity;
import com.example.wmc.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class SignupActivity extends AppCompatActivity {
    String[] items = {"내가 다닌 초등학교의 이름은?","나의 어머님의 성함은?","내가 가장 감명깊게 본 영화는?"}; // 스피너 변수
    String checkid; // 아이디 변수
    String checkPn; // 전화번호 변수
    String checkPw; // 비밀번호 변수
    String checkQuestion; // 질문
    String checkAnswer; // 답변
    String checkAddress; // 주소
    String checkFavorite1; // 선호도
    String checkFavorite2; // 선호도

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_signup);

        Spinner spinner = findViewById(R.id.spinner4); // 스피너
        TextView textView = findViewById(R.id.signUp_nickName_input); // 스피너 텍스트뷰
        TextView login_textview = findViewById(R.id.searchID_PW_button3); // 로그인 버튼
        TextView id_textview = findViewById(R.id.searchID_PW_button); // 아이디찾기 버튼
        TextView pw_textview = findViewById(R.id.searchID_PW_button2); // 비밀번호 찾기 버튼
        RadioButton favorite1 = findViewById(R.id.radio_study); // 선호도 라디오 그룹1
        RadioButton favorite2 = findViewById(R.id.radio_taste); // 선호도 라디오 그룹2
        Button id_button = findViewById(R.id.signUp_button2); // 아이디 중복확인 버튼
        TextView id_input = findViewById(R.id.signUp_id_input); // 아이디 입력 텍스트뷰
        TextView pw_input1 = findViewById(R.id.signUp_pw_input); // 비밀번호 입력 텍뷰1
        TextView pw_input2 = findViewById(R.id.signUp_verifyPw_input); // 비밀번호 입력 텍뷰2
        TextView phone_input = findViewById(R.id.signUp_nubmer_input); // 전화번호 입력 텍뷰
        TextView addr_input = findViewById(R.id.signUp_address_input); // 주소 입력 텍뷰
        Button phone_button = findViewById(R.id.signUp_button); // 전화번호 중복확인 버튼
        Button signup = findViewById(R.id.signUp_button1); // 회원가입 버튼

        login_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                startActivity(intent);
                finish();
            }
        }); // 로그인으로 이동

        id_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindidActivity.class);
                startActivity(intent);
                finish();
            }
        }); // 아이디 찾기로 이동

        pw_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
                startActivity(intent);
                finish();
            }
        }); // 비밀번호 찾기로 이동

        // 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setHint(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("비밀번호 확인 질문을 선택해주세요.");
            }
        });

        // 라디오 버튼
        favorite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavorite1 = "스터디";
                checkFavorite2 = "커피맛";
            }
        });

        favorite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavorite1 = "커피맛";
                checkFavorite2 = "스터디";
            }
        });

        //서버호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        // 아이디 중복확인 버튼 클릭시
        id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkid = id_input.getText().toString();

                if(checkid.length() <= 5) Toast.makeText(getApplicationContext(), "아이디는 6자 이상부터 가능합니다.", Toast.LENGTH_SHORT).show();
                else {
                    String url = getResources().getString(R.string.url) + "check/id?checkId=" + checkid;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(String response) {
                            Log.d("test", response);
                            if (response.equals("true")) {
                                Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "가입된 아이디입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                            Log.e("test_error2", error.toString());
                        }
                    });
                    requestQueue.add(stringRequest);
                }
            }
        });

        // 전화번호 중복확인 버튼 클릭시
        phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPn = phone_input.getText().toString();

                if(checkPn.length() <= 9) Toast.makeText(getApplicationContext(), "전화번호는 10자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    String url = getResources().getString(R.string.url) + "check/pn?checkPN=" + checkPn;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(String response) {
                            Log.d("test", response);
                            if (response.equals("true")) {
                                Toast.makeText(getApplicationContext(), "사용 가능한 전화번호입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "가입된 전화번호입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                            Log.e("test_error2", error.toString());
                        }
                    });
                    requestQueue.add(stringRequest);
                }
            }
        });

        // 회원가입 완료 버튼 클릭시 *미완
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkid = id_input.getText().toString();
                checkPn = phone_input.getText().toString();
                checkPw = pw_input1.getText().toString();
                checkQuestion = spinner.getSelectedItem().toString();
                checkAnswer = textView.getText().toString();
                checkAddress = addr_input.getText().toString();
                int numInt = Integer.parseInt(checkPn);

                HashMap<String,Object>params = new HashMap<String, Object>();
                params.put("id",checkid);
                params.put("pwd",checkPw);
                params.put("nickName",checkid);
                params.put("grade",1);
                params.put("address",checkAddress);
                params.put("phoneNum",numInt);
                params.put("favorite1",checkFavorite1);
                params.put("favorite2",checkFavorite2);
                params.put("personalQuestion",checkQuestion);
                params.put("personalAnswer",checkAnswer);

                String url = getResources().getString(R.string.url) + "signup";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "회원가입 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                        startActivity(intent);
                        finish();
                        Log.d("signup1",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("signup2",error.toString());
                    }
                }
                ){
                    public byte[] getBody(){
                        return new JSONObject(params).toString().getBytes();
                    }
                    public String getBodyContentType(){
                        return "application/json";
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(request);
            }
        });
        /////////////////
    }
}