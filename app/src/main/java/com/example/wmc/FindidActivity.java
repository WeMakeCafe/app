package com.example.wmc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;

public class FindidActivity extends AppCompatActivity {
    String stringID; // 전화번호 string 담기 위한 변수

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_search_id);

        TextView login_textview = findViewById(R.id.to_login_Page_textView); // 로그인 버튼
        TextView pw_textview = findViewById(R.id.to_login_Page_textView2); // 비밀번호 찾기 버튼
        TextView signup_textview = findViewById(R.id.to_signUp_Page_textView); // 회원가입 버튼

        TextView pn_input = findViewById(R.id.searchIDPW_number_input); // 전화번호 입력 텍스트뷰
        TextView id_output = findViewById(R.id.searchIDPW_ID_output); // 아이디 출력 텍스트뷰
        Button but_find = findViewById(R.id.search_id_button); // 찾기 버튼

        login_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                startActivity(intent);
                finish();
            }
        }); // 로그인 액티비티 이동

        pw_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
                startActivity(intent);
                finish();
            }
        }); // 비밀번호 찾기 액티비티 이동

        signup_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        }); // 회원가입 액티비티 이동

        //서버호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        // 아이디 찾기 버튼 클릭시 *완성
        but_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringID = pn_input.getText().toString();

                String url = getResources().getString(R.string.url) + "find/id?confirmString=" + stringID;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success_setting = "입력하신 회원코드로 가입된 아이디는 " + response + " 입니다.";
                        id_output.setText(success_setting);
                    }
                    }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String fail_setting = "입력하신 회원코드로 가입된 아이디가 없습니다.";
                        id_output.setText(fail_setting);
                    }
                });
                requestQueue.add(stringRequest);
            }
        });
    }
}
