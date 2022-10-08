package com.example.wmc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.wmc.database.Cafe;
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

import kotlin.ParameterName;
import retrofit2.http.GET;

public class LoginAcivity extends AppCompatActivity {
    String id; // id 정보 위한 변수
    String pw; // password 정보 위한 변수

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_login);

        TextView signup_textview = findViewById(R.id.signUp_button0); // 회원가입 버튼
        TextView id_textview = findViewById(R.id.search_id_pw_button); // 아이디 찾기 버튼
        TextView pw_textview = findViewById(R.id.search_id_pw_button2); // 비밀번호 찾기 버튼

        TextView id_input = findViewById(R.id.login_id_input); // 아이디 텍스트뷰
        TextView pw_input = findViewById(R.id.login_pw_input); // 비밀번호 텍스트뷰
        Button login = findViewById(R.id.login_button); // 로그인 버튼

        signup_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        }); // 회원가입 액티비티 이동

        id_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindidActivity.class);
                startActivity(intent);
            }
        }); // 아이디 찾기 액티비티 이동

        pw_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
                startActivity(intent);
            }
        }); // 비밀번호 찾기 액티비티 이동

        // 서버호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        // 로그인 이벤트
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = id_input.getText().toString(); // 6자 이상, max_length 12자
                pw = pw_input.getText().toString(); // 8자 이상, max_length 15자

                HashMap<String,Object>params = new HashMap<String,Object>();
                params.put("id",id);
                params.put("password",pw);

                String url = getResources().getString(R.string.url) + "login";
                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("login",response);
                        if(response == "0")
                            Toast.makeText(getApplicationContext(), "등록되지 않은 아이디입니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("mem_num",response);
                            startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("no_use",error.toString());
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
    }
}