package com.example.wmc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FindpwActivity extends AppCompatActivity {
    String[] items = {"내가 다닌 초등학교의 이름은?","나의 어머님의 성함은?","내가 가장 감명깊게 본 영화는?"}; // 스피너 변수
    String string_id; // 아이디 변수
    String string_answer; // 질문 답변 변수

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_search_pw);

        TextView login_textview = findViewById(R.id.to_login_Page_textView); // 로그인 버튼
        TextView id_textview = findViewById(R.id.to_id_Page_textView); // 아이디 찾기 버튼
        TextView signup_textview = findViewById(R.id.to_signUp_Page_textView); // 회원가입 버튼

        Spinner question_spinner = findViewById(R.id.spinner); // 스피너
        TextView id_input = findViewById(R.id.searchIDPW_ID_input); // 아이디 입력 텍스트뷰
        TextView answer = findViewById(R.id.searchIDPW_ID_input2); // 비밀번호 확인 질문 텍스트뷰
        Button but_find = findViewById(R.id.search_pw_button); // 비밀번호 찾기 버튼
        Button but_modify = findViewById(R.id.search_pw_button2); // 비밀번호 변경 버튼
        TextView pw_output = findViewById(R.id.searchIDPW_PW_output); // 비밀번호 출력 텍스트뷰

        login_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                startActivity(intent);
                finish();
            }
        }); // 로그인 액티비티 이동

        id_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindidActivity.class);
                startActivity(intent);
                finish();
            }
        }); // 아이디 찾기 액티비티 이동

        signup_textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        }); // 회원가입 액티비티 이동

        // 비밀번호 확인 질문 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question_spinner.setAdapter(adapter);
        question_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                answer.setHint(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                answer.setText("비밀번호 확인 질문을 선택해주세요.");
            }
        });

        //서버호출
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        // 비밀번호 찾기 버튼 클릭시 * 미완, 글자수 같은 세부설정 미완에 500에러까지 등장
        but_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_id = id_input.getText().toString();
                string_answer = answer.getText().toString();

                HashMap<String,Object>params = new HashMap<String, Object>();
                params.put("id",string_id);
                params.put("question",question_spinner.getSelectedItem().toString());
                params.put("answer",string_answer);

                String url = getResources().getString(R.string.url) + "find/pw";
                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("개인 확인용 질문이 미일치합니다.") &&
                        !response.equals("개인 확인용 답안이 미일치합니다.")){
                            id_input.setEnabled(false);
                            question_spinner.setEnabled(false);
                            answer.setEnabled(false);
                            but_find.setVisibility(View.INVISIBLE);
                            pw_output.setText("");
                            pw_output.setEnabled(true);
                            pw_output.setHint("새로운 비밀번호를 입력해주세요.");
                            but_modify.setVisibility(View.VISIBLE);

                            but_modify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(pw_output.length() <= 7)
                                        Toast.makeText(getApplicationContext(), "비밀번호는 8자 이상입니다.", Toast.LENGTH_SHORT).show();
                                    else {
                                        HashMap<String, Object> params2 = new HashMap<String, Object>();
                                        params2.put("memNum",response);
                                        params2.put("changePW",pw_output.getText().toString());

                                        String url = getResources().getString(R.string.url) + "change/pw";
                                        StringRequest request1 = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getApplicationContext(), "비밀번호 변경을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                                Log.d("modi_pw1",response);
                                                Log.d("modi_pw2",params2.toString());
                                                Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("modify_pw","비밀번호 변경 오류발생" + error.toString());
                                            }
                                        }
                                        ){
                                            public byte[] getBody(){
                                                return new JSONObject(params2).toString().getBytes();
                                            }
                                            public String getBodyContentType(){
                                                return "application/json";
                                            }
                                        };
                                        Volley.newRequestQueue(getApplicationContext()).add(request1);
                                    }
                                }
                            });
                        }
                        else {
                            pw_output.setText(response);
//                            pw_output.setEnabled(false);
//                            but_modify.setVisibility(View.INVISIBLE);
//                            question_spinner.setEnabled(false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pw_output.setText("가입된 아이디가 아닙니다.");
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
