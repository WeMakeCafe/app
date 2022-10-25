package com.example.wmc;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class SignupActivity extends AppCompatActivity {
    String[] items = {"내가 다닌 초등학교의 이름은?","나의 어머님의 성함은?","내가 가장 감명깊게 본 영화는?"}; // 스피너 변수
    String checkid; // 아이디 변수
    String checkPw1; // 비밀번호 변수
    String checkPw2; // 비밀번호 변수
    String checkQuestion; // 질문
    String checkAnswer; // 답변
    String checkFavorite1 = "미선택"; // 선호도
    String checkFavorite2; // 선호도
    Boolean id_boolean = false; // 아이디 중복값

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_signup);

        TextView login_textview = findViewById(R.id.searchID_PW_button3); // 로그인 버튼
        TextView id_textview = findViewById(R.id.searchID_PW_button); // 아이디찾기 버튼
        TextView pw_textview = findViewById(R.id.searchID_PW_button2); // 비밀번호 찾기 버튼

        Spinner spinner = findViewById(R.id.spinner4); // 스피너
        TextView textView = findViewById(R.id.signUp_nickName_input); // 스피너 텍스트뷰
        RadioButton favorite1 = findViewById(R.id.radio_study); // 선호도 라디오 그룹1
        RadioButton favorite2 = findViewById(R.id.radio_taste); // 선호도 라디오 그룹2
        Button id_button = findViewById(R.id.signUp_button2); // 아이디 중복확인 버튼
        TextView id_input = findViewById(R.id.signUp_id_input); // 아이디 입력 텍스트뷰
        TextView pw_input1 = findViewById(R.id.signUp_pw_input); // 비밀번호 입력 텍뷰1
        TextView pw_input2 = findViewById(R.id.signUp_verifyPw_input); // 비밀번호 입력 텍뷰2
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
                checkFavorite2 = "맛";
            }
        });

        favorite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavorite1 = "맛";
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

                if(checkid.length() <= 5) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("아이디는 6자 이상부터 가능합니다.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }
                else {
                    String url = getResources().getString(R.string.url) + "check/id?checkId=" + checkid;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(String response) {
                            Log.d("test", response);
                            if (response.equals("true")) {
                                AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                                dlg.setTitle("We Make Cafe");
                                dlg.setMessage("사용 가능한 아이디입니다.");
                                dlg.setIcon(R.drawable.logo);
                                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dlg.show();
                                id_input.setEnabled(false);
                                id_button.setVisibility(View.INVISIBLE);
                                id_boolean = true;
                            } else {
                                AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                                dlg.setTitle("We Make Cafe");
                                dlg.setMessage("이미 가입된 아이디입니다.");
                                dlg.setIcon(R.drawable.logo);
                                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dlg.show();
                                id_boolean = false;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // 에러가 뜬다면 왜 에러가 떴는지 확인하는 코드
                            Log.e("id_error2", error.toString());
                        }
                    });
                    requestQueue.add(stringRequest);
                }
            }
        });

        // 회원가입 완료 버튼 클릭이벤트
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkid = id_input.getText().toString();
                checkPw1 = pw_input1.getText().toString();
                checkPw2 = pw_input2.getText().toString();
                checkQuestion = spinner.getSelectedItem().toString();
                checkAnswer = textView.getText().toString();

                // 아이디 중복 확인
                if(!id_boolean){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                    dlg.setTitle("We Make Cafe");
                    dlg.setMessage("아이디 중복확인을 완료해주세요.");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }

                // 비밀번호 유효성 검사
                else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{7,15}.$", checkPw1)) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
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
                else if (!checkPw1.equals(checkPw2)){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
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
                else if(checkAnswer.equals("")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
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
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
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

                // 모든 요소 입력완료
                else {
//                    Random random = new Random();
//                    int createNum = 0;
//                    String ranNum = "";
//                    int letter = 6;
//                    String resultNum = "";
//
//                    for(int i = 0; i<letter; i++) {
//                        createNum = random.nextInt(9);
//                        ranNum = Integer.toString(createNum);
//                        resultNum += ranNum;
//                    }
//                    int numInt = Integer.parseInt(resultNum); 난수 발생 위해 만들었던 코드. 서버작업으로 필요없어짐

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("id", checkid);
                    params.put("pwd", checkPw1);
                    params.put("nickName", checkid);
                    params.put("grade", 3);
                    params.put("favorite1", checkFavorite1);
                    params.put("favorite2", checkFavorite2);
                    params.put("personalQuestion", checkQuestion);
                    params.put("personalAnswer", checkAnswer);

                    String url = getResources().getString(R.string.url) + "signup";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                            dlg.setTitle("We Make Cafe");
                            dlg.setMessage("회원가입 성공. 아이디 찾기를 위한 회원코드는 " + response + "입니다. 꼭 기억해주세요!");
                            dlg.setIcon(R.drawable.logo);
                            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), LoginAcivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            dlg.show();
                            Log.d("signup1", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("signup2", error.toString());
                        }
                    }
                    ) {
                        public byte[] getBody() {
                            return new JSONObject(params).toString().getBytes();
                        }

                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                }
            }
        });
    }
}