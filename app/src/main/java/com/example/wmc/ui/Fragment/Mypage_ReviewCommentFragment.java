package com.example.wmc.ui.Fragment;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.wmc.R;
import com.example.wmc.ReviewComment.ReviewCommentAdapter;
import com.example.wmc.database.Cafe;
import com.example.wmc.databinding.FragmentReviewCommentBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mypage_ReviewCommentFragment extends Fragment {
    private FragmentReviewCommentBinding binding;
    private static NavController navController;
    Button reviewComment_add_image_button;
    Button reviewComment_finish_button;
    EditText reviewComment_editText;
    TextView commentCount_textView;
    RecyclerView reviewCommentImageRecyclerView;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ReviewCommentAdapter reviewCommentAdapter;
    private static final int REQUEST_CODE = 3333;
    private static final String TAG = "ReviewCommentFragment";

    String tag1;
    String tag2;
    String tag3;
    Long cafeNum;
    String comment = "";
    String cafeName;
    int likeCount = 0;
    boolean flag = false;
    Long mem_num;
    Long reviewNum;
    Long[] k = new Long[36];
    Long[] k2 = new Long[36];

    Integer p1 = 0; // 이번에 수정하면서 바뀐 점수 보관용
    Integer p2 = 0;
    Integer p3 = 0;
    Integer p4 = 0;
    Integer p5 = 0;
    Integer p6 = 0;
    Integer p7 = 0;
    Integer p8 = 0;
    Integer p9 = 0;
    Integer p10 = 0;
    Integer p11 = 0;
    Integer p12 = 0;

    Integer score1; // 본래의 리뷰에 있던 점수 보관용
    Integer score2;
    Integer score3;
    Integer score4;
    Integer score5;
    Integer score6;
    Integer score7;
    Integer score8;
    Integer score9;
    Integer score10;
    Integer score11;
    Integer score12;

    Boolean floating_flag = false;
    Boolean reviewCafeList_flag = false;
    Boolean cafeDetail_reviewModify_flag = false;
    Boolean moreReview_reviewModify_flag = false;
    Boolean mypage_reviewModify_flag = false;

    ArrayList<Cafe> cafe_list;

    File file;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCommentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        reviewComment_add_image_button = root.findViewById(R.id.reviewComment_add_image_button);
        reviewComment_finish_button = root.findViewById(R.id.reviewComment_finish_button);
        reviewComment_editText = root.findViewById(R.id.reviewComment_editText);
        commentCount_textView = root.findViewById(R.id.commentCount_textView);
        reviewCommentImageRecyclerView = root.findViewById(R.id.reviewCommentImageRecyclerView);

        // 서버연산을 위한 long형 배열 초기화 코드
        for(int i = 0 ; i<=35; i++){
            k[i] = (long) 0;
        }

        for(int i = 0 ; i<=35; i++){
            k2[i] = (long) 0;
        }



        // 리뷰 프래그먼트에서 가져온 정보들
        Bundle argBundle = getArguments();
        if( argBundle != null ) {
            // 리뷰 작성에서 온 경우(수정 X)
            if (argBundle.getBoolean("flag") == false) {

                cafeName = argBundle.getString("cafeName");

                tag1 = argBundle.getString("tag1");
                tag2 = argBundle.getString("tag2");
                tag3 = argBundle.getString("tag3");

                // 이번에 바꾼 점수들
                p1 = Integer.valueOf((int)argBundle.getFloat("tastePoint1"));
                p2 = Integer.valueOf((int)argBundle.getFloat("tastePoint2"));
                p3 = Integer.valueOf((int)argBundle.getFloat("tastePoint3"));
                p4 = Integer.valueOf((int)argBundle.getFloat("tastePoint4"));
                p5 = Integer.valueOf((int)argBundle.getFloat("seatPoint1"));
                p6 = Integer.valueOf((int)argBundle.getFloat("seatPoint2"));
                p7 = Integer.valueOf((int)argBundle.getFloat("seatPoint3"));
                p8 = Integer.valueOf((int)argBundle.getFloat("seatPoint4"));
                p9 = Integer.valueOf((int)argBundle.getFloat("studyPoint1"));
                p10 = Integer.valueOf((int)argBundle.getFloat("studyPoint2"));
                p11 = Integer.valueOf((int)argBundle.getFloat("studyPoint3"));
                p12 = Integer.valueOf((int)argBundle.getFloat("studyPoint4"));

                cafeNum = argBundle.getLong("cafeNum");
                mem_num = argBundle.getLong("memNum");

                flag = argBundle.getBoolean("flag");    // 수정에서 넘어온 것인지 확인

                Log.d("리뷰에서 받음 -> flag", String.valueOf(flag));
                Log.d("리뷰에서 받음 -> tag1", String.valueOf(tag1));
                Log.d("리뷰에서 받음 -> p1", String.valueOf(p1));

            }

            // 리뷰 수정에서 온 경우,
            else if(argBundle.getBoolean("flag") == true){
                cafeName = argBundle.getString("cafeName");

                tag1 = argBundle.getString("tag1");
                tag2 = argBundle.getString("tag2");
                tag3 = argBundle.getString("tag3");

                // 이번에 바꾼 점수들
                p1 = Integer.valueOf((int)argBundle.getFloat("tastePoint1"));
                p2 = Integer.valueOf((int)argBundle.getFloat("tastePoint2"));
                p3 = Integer.valueOf((int)argBundle.getFloat("tastePoint3"));
                p4 = Integer.valueOf((int)argBundle.getFloat("tastePoint4"));
                p5 = Integer.valueOf((int)argBundle.getFloat("seatPoint1"));
                p6 = Integer.valueOf((int)argBundle.getFloat("seatPoint2"));
                p7 = Integer.valueOf((int)argBundle.getFloat("seatPoint3"));
                p8 = Integer.valueOf((int)argBundle.getFloat("seatPoint4"));
                p9 = Integer.valueOf((int)argBundle.getFloat("studyPoint1"));
                p10 = Integer.valueOf((int)argBundle.getFloat("studyPoint2"));
                p11 = Integer.valueOf((int)argBundle.getFloat("studyPoint3"));
                p12 = Integer.valueOf((int)argBundle.getFloat("studyPoint4"));


//                for(int i = 0; i < k2.length; i++){
//                    k2[i] = argBundle.getLong("k2-" + String.valueOf(i+1));
//                }

                // 이번에 선택한 키워드
                k[0] = getArguments().getLong("k-1");
                k[1] = getArguments().getLong("k-2");
                k[2] = getArguments().getLong("k-3");
                k[3] = getArguments().getLong("k-4");
                k[4] = getArguments().getLong("k-5");
                k[5] = getArguments().getLong("k-6");
                k[6] = getArguments().getLong("k-7");
                k[7] = getArguments().getLong("k-8");
                k[8] = getArguments().getLong("k-9");
                k[9] = getArguments().getLong("k-10");
                k[10] = getArguments().getLong("k-11");
                k[11] = getArguments().getLong("k-12");
                k[12] = getArguments().getLong("k-13");
                k[13] = getArguments().getLong("k-14");
                k[14] = getArguments().getLong("k-15");
                k[15] = getArguments().getLong("k-16");
                k[16] = getArguments().getLong("k-17");
                k[17] = getArguments().getLong("k-18");
                k[18] = getArguments().getLong("k-19");
                k[19] = getArguments().getLong("k-20");
                k[20] = getArguments().getLong("k-21");
                k[21] = getArguments().getLong("k-22");
                k[22] = getArguments().getLong("k-23");
                k[23] = getArguments().getLong("k-24");
                k[24] = getArguments().getLong("k-25");
                k[25] = getArguments().getLong("k-26");
                k[26] = getArguments().getLong("k-27");
                k[27] = getArguments().getLong("k-28");
                k[28] = getArguments().getLong("k-29");
                k[29] = getArguments().getLong("k-30");
                k[30] = getArguments().getLong("k-31");
                k[31] = getArguments().getLong("k-32");
                k[32] = getArguments().getLong("k-33");
                k[33] = getArguments().getLong("k-34");
                k[34] = getArguments().getLong("k-35");
                k[35] = getArguments().getLong("k-36");

                // 수정하기 전, 선택했던 태그가 저장되어있음
                k2[0] = getArguments().getLong("k2-1");
                k2[1] = getArguments().getLong("k2-2");
                k2[2] = getArguments().getLong("k2-3");
                k2[3] = getArguments().getLong("k2-4");
                k2[4] = getArguments().getLong("k2-5");
                k2[5] = getArguments().getLong("k2-6");
                k2[6] = getArguments().getLong("k2-7");
                k2[7] = getArguments().getLong("k2-8");
                k2[8] = getArguments().getLong("k2-9");
                k2[9] = getArguments().getLong("k2-10");
                k2[10] = getArguments().getLong("k2-11");
                k2[11] = getArguments().getLong("k2-12");
                k2[12] = getArguments().getLong("k2-13");
                k2[13] = getArguments().getLong("k2-14");
                k2[14] = getArguments().getLong("k2-15");
                k2[15] = getArguments().getLong("k2-16");
                k2[16] = getArguments().getLong("k2-17");
                k2[17] = getArguments().getLong("k2-18");
                k2[18] = getArguments().getLong("k2-19");
                k2[19] = getArguments().getLong("k2-20");
                k2[20] = getArguments().getLong("k2-21");
                k2[21] = getArguments().getLong("k2-22");
                k2[22] = getArguments().getLong("k2-23");
                k2[23] = getArguments().getLong("k2-24");
                k2[24] = getArguments().getLong("k2-25");
                k2[25] = getArguments().getLong("k2-26");
                k2[26] = getArguments().getLong("k2-27");
                k2[27] = getArguments().getLong("k2-28");
                k2[28] = getArguments().getLong("k2-29");
                k2[29] = getArguments().getLong("k2-30");
                k2[30] = getArguments().getLong("k2-31");
                k2[31] = getArguments().getLong("k2-32");
                k2[32] = getArguments().getLong("k2-33");
                k2[33] = getArguments().getLong("k2-34");
                k2[34] = getArguments().getLong("k2-35");
                k2[35] = getArguments().getLong("k2-36");

                score1 = argBundle.getInt("score1");
                score2 = argBundle.getInt("score2");
                score3 = argBundle.getInt("score3");
                score4 = argBundle.getInt("score4");
                score5 = argBundle.getInt("score5");
                score6 = argBundle.getInt("score6");
                score7 = argBundle.getInt("score7");
                score8 = argBundle.getInt("score8");
                score9 = argBundle.getInt("score9");
                score10 = argBundle.getInt("score10");
                score11 = argBundle.getInt("score11");
                score12 = argBundle.getInt("score12");

                cafeNum = argBundle.getLong("cafeNum");
                mem_num = argBundle.getLong("memNum");
                reviewNum = argBundle.getLong("reviewNum");

                reviewComment_editText.setText(comment);                        // 코멘토리에 기존에 작성해놨던 리뷰 코멘토리 세팅

                comment = argBundle.getString("comment");
                if(comment.equals(""))
                    commentCount_textView.setText("0/200 Bytes"); // 코멘토리에 쓰여있는 글자 수 세팅
                else
                    commentCount_textView.setText(comment.length() + "/200 Bytes"); // 코멘토리에 쓰여있는 글자 수 세팅


                likeCount = argBundle.getInt("likeCount");
                flag = argBundle.getBoolean("flag");    // 수정에서 넘어온 것인지 확인

                Log.d("리뷰에서 받음 -> flag", String.valueOf(flag));
                Log.d("리뷰에서 받음 -> tag1", tag1);
                Log.d("리뷰에서 받음 -> 산미", String.valueOf(p1));
                Log.d("리뷰에서 받음 -> 쓴맛", String.valueOf(p2));
                Log.d("리뷰에서 받음 -> 디저트", String.valueOf(p3));
                Log.d("리뷰에서 받음 -> 기타음료", String.valueOf(p4));
            }
        }


        //코멘트의 글자수 출력
        reviewComment_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String comment = reviewComment_editText.getText().toString();
                commentCount_textView.setText(comment.length() + "/200 Bytes");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 이미지 추가 버튼 클릭 시
        reviewComment_add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리 여는 이벤트 추가
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // 다중 이미지를 가져올 수 있도록 세팅
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

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
                for(Cafe c : cafe_list) {
                    if(c.getCafeNum().equals(cafeNum)) {
                        cafeName = c.getCafeName();
                    }
                }
                // 작성완료 버튼 클릭 시
                reviewComment_finish_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 리뷰 처음 작성 시,(수정 X) POST
                        if(flag == false) {

                            Map map = new HashMap();
                            map.put("tastePoint1", p1);
                            map.put("tastePoint2", p2);
                            map.put("tastePoint3", p3);
                            map.put("tastePoint4", p4);
                            map.put("seatPoint1", p5);
                            map.put("seatPoint2", p6);
                            map.put("seatPoint3", p7);
                            map.put("seatPoint4", p8);
                            map.put("studyPoint1", p9);
                            map.put("studyPoint2", p10);
                            map.put("studyPoint3", p11);
                            map.put("studyPoint4", p12);
                            map.put("cafeNum", cafeNum);
                            map.put("likeCount", 0);
                            map.put("reviewText", reviewComment_editText.getText().toString());
                            map.put("memNum", mem_num);
                            // 이미지 처리 문장 올 곳
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


                            String url2 = getResources().getString(R.string.url) + "review";
                            JSONObject jsonObject = new JSONObject(map);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                long member_num = response.getLong("memNum");
                                                long review_num = response.getLong("reviewNum");

                                                for (Uri u : uriList) {

                                                    // 이미지 절대주소 만들기
                                                    Cursor c = getContext().getContentResolver().query(Uri.parse(u.toString()), null, null, null, null);
                                                    c.moveToNext();
                                                    String absolutePath = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                                                    Log.d("test_check", absolutePath);
                                                    file = new File(absolutePath);

                                                    // 이미지 서버로 전송
                                                    FileUploadUtils.sendReviewImage(file, member_num, review_num);
                                                }
                                            }
                                            catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("test1", error.toString());
                                        }
                                    }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=UTF-8";
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(requireContext());
                            queue.add(objectRequest);

                            // 여기는 cafePut작업해야할 곳
                            for(Cafe c : cafe_list) {
                                if(c.getCafeNum().equals(cafeNum)) {  //bundle에서 가져온 카페아이디값 cafe_name에 넣어서 비교 연산
                                    Map map2 = new HashMap();
                                    map2.put("cafeName", c.getCafeName());
                                    map2.put("cafeAddress", c.getCafeAddress());
                                    map2.put("openTime", c.getOpenTime());
                                    map2.put("closeTime", c.getCloseTime());
                                    map2.put("bookmarkNum", c.getBookmarkNum());
                                    map2.put("scoreNum", c.getScoreNum());
                                    map2.put("keyword1", c.getKeyword1()+k[0]);
                                    map2.put("keyword2", c.getKeyword2()+k[1]);
                                    map2.put("keyword3", c.getKeyword3()+k[2]);
                                    map2.put("keyword4", c.getKeyword4()+k[3]);
                                    map2.put("keyword5", c.getKeyword5()+k[4]);
                                    map2.put("keyword6", c.getKeyword6()+k[5]);
                                    map2.put("keyword7", c.getKeyword7()+k[6]);
                                    map2.put("keyword8", c.getKeyword8()+k[7]);
                                    map2.put("keyword9", c.getKeyword9()+k[8]);
                                    map2.put("keyword10", c.getKeyword10()+k[9]);
                                    map2.put("keyword11", c.getKeyword11()+k[10]);
                                    map2.put("keyword12", c.getKeyword12()+k[11]);
                                    map2.put("keyword13", c.getKeyword13()+k[12]);
                                    map2.put("keyword14", c.getKeyword14()+k[13]);
                                    map2.put("keyword15", c.getKeyword15()+k[14]);
                                    map2.put("keyword16", c.getKeyword16()+k[15]);
                                    map2.put("keyword17", c.getKeyword17()+k[16]);
                                    map2.put("keyword18", c.getKeyword18()+k[17]);
                                    map2.put("keyword19", c.getKeyword19()+k[18]);
                                    map2.put("keyword20", c.getKeyword20()+k[19]);
                                    map2.put("keyword21", c.getKeyword21()+k[20]);
                                    map2.put("keyword22", c.getKeyword22()+k[21]);
                                    map2.put("keyword23", c.getKeyword23()+k[22]);
                                    map2.put("keyword24", c.getKeyword24()+k[23]);
                                    map2.put("keyword25", c.getKeyword25()+k[24]);
                                    map2.put("keyword26", c.getKeyword26()+k[25]);
                                    map2.put("keyword27", c.getKeyword27()+k[26]);
                                    map2.put("keyword28", c.getKeyword28()+k[27]);
                                    map2.put("keyword29", c.getKeyword29()+k[28]);
                                    map2.put("keyword30", c.getKeyword30()+k[29]);
                                    map2.put("keyword31", c.getKeyword31()+k[30]);
                                    map2.put("keyword32", c.getKeyword32()+k[31]);
                                    map2.put("keyword33", c.getKeyword33()+k[32]);
                                    map2.put("keyword34", c.getKeyword34()+k[33]);
                                    map2.put("keyword35", c.getKeyword35()+k[34]);
                                    map2.put("keyword36", c.getKeyword36()+k[35]);
                                    map2.put("tastePoint1", c.getTastePoint1()+p1);
                                    map2.put("tastePoint2", c.getTastePoint2()+p2);
                                    map2.put("tastePoint3", c.getTastePoint3()+p3);
                                    map2.put("tastePoint4", c.getTastePoint4()+p4);
                                    map2.put("seatPoint1", c.getSeatPoint1()+p5);
                                    map2.put("seatPoint2", c.getSeatPoint2()+p6);
                                    map2.put("seatPoint3", c.getSeatPoint3()+p7);
                                    map2.put("seatPoint4", c.getSeatPoint4()+p8);
                                    map2.put("studyPoint1", c.getStudyPoint1()+p9);
                                    map2.put("studyPoint2", c.getStudyPoint2()+p10);
                                    map2.put("studyPoint3", c.getStudyPoint3()+p11);
                                    map2.put("studyPoint4", c.getStudyPoint4()+p12);


                                    JSONObject jsonObject2 = new JSONObject(map2);

                                    String url3 = getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // 해당 카페에만 데이터 삽입하기 위함

                                    JsonObjectRequest objectRequest2 = new JsonObjectRequest(Request.Method.PUT, url3, jsonObject2,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("qwer", error.toString());
                                                }
                                            }) {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    RequestQueue queue2 = Volley.newRequestQueue(requireContext());
                                    queue2.add(objectRequest2);

                                }
                            }


                            Bundle bundle = new Bundle();
                            bundle.putString("cafeName",cafeName);
                            // 내가 리뷰를 작성한 카페의 카페디테일로 이동

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("리뷰 작성").setMessage("리뷰가 등록되었습니다.").setIcon(R.drawable.logo);

                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    navController.navigate(R.id.mypage_review_comment_to_mypage);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                        // 리뷰 수정에서 온 경우, PUT
                        else if(flag == true) {
                            Map map = new HashMap();
                            map.put("tastePoint1", p1);
                            map.put("tastePoint2", p2);
                            map.put("tastePoint3", p3);
                            map.put("tastePoint4", p4);
                            map.put("seatPoint1", p5);
                            map.put("seatPoint2", p6);
                            map.put("seatPoint3", p7);
                            map.put("seatPoint4", p8);
                            map.put("studyPoint1", p9);
                            map.put("studyPoint2", p10);
                            map.put("studyPoint3", p11);
                            map.put("studyPoint4", p12);
                            map.put("cafeNum", cafeNum);
                            map.put("likeCount", likeCount);
                            map.put("memNum", mem_num);
                            map.put("reviewText", reviewComment_editText.getText().toString());

                            Log.d("들어가는 point 산미", String.valueOf(p1));
                            Log.d("들어가는 point 쓴맛", String.valueOf(p1));

                            // 이미지 처리 문장 올 곳
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

                            String url2 = getResources().getString(R.string.url) + "review/" + reviewNum;
                            JSONObject jsonObject = new JSONObject(map);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url2, jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("test_check", "onResponse 응답");
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("test1", error.toString());
                                            Log.d("test_check_error", "에러발생");
                                        }
                                    }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=UTF-8";
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(requireContext());
                            queue.add(objectRequest);

                            // 여기는 cafePut작업해야할 곳
                            for(Cafe c : cafe_list) {
                                if(c.getCafeNum().equals(cafeNum)) {  //bundle에서 가져온 카페아이디값 cafe_name에 넣어서 비교 연산
                                    Map map2 = new HashMap();
                                    map2.put("cafeName", c.getCafeName());
                                    map2.put("cafeAddress", c.getCafeAddress());
                                    map2.put("openTime", c.getOpenTime());
                                    map2.put("closeTime", c.getCloseTime());
                                    map2.put("bookmarkNum", c.getBookmarkNum());
                                    map2.put("scoreNum", c.getScoreNum());

                                    Log.d("asdf k-keyWord4", String.valueOf(k[3]));
                                    Log.d("asdf k2-keyWord4", String.valueOf(k2[3]));
                                    Log.d("asdf k-keyWord5", String.valueOf(k[4]));
                                    Log.d("asdf k2-keyWord5", String.valueOf(k2[4]));
                                    Log.d("asdf k-keyWord6", String.valueOf(k[5]));
                                    Log.d("asdf k2-keyWord6", String.valueOf(k2[5]));
                                    Log.d("asdf k-keyWord7", String.valueOf(k[6]));
                                    Log.d("asdf k2-keyWord7", String.valueOf(k2[6]));
                                    Log.d("asdf k-keyWord8", String.valueOf(k[7]));
                                    Log.d("asdf k2-keyWord8", String.valueOf(k2[7]));
                                    Log.d("asdf k-keyWord9", String.valueOf(k[8]));
                                    Log.d("asdf k2-keyWord9", String.valueOf(k2[8]));

                                    map2.put("keyword1", c.getKeyword1()+k[0] - k2[0]);
                                    map2.put("keyword2", c.getKeyword2()+k[1] - k2[1]);
                                    map2.put("keyword3", c.getKeyword3()+k[2] - k2[2]);
                                    map2.put("keyword4", c.getKeyword4()+k[3] - k2[3]);
                                    map2.put("keyword5", c.getKeyword5()+k[4] - k2[4]);
                                    map2.put("keyword6", c.getKeyword6()+k[5] - k2[5]);
                                    map2.put("keyword7", c.getKeyword7()+k[6] - k2[6]);
                                    map2.put("keyword8", c.getKeyword8()+k[7] - k2[7]);
                                    map2.put("keyword9", c.getKeyword9()+k[8] - k2[8]);
                                    map2.put("keyword10", c.getKeyword10()+k[9] - k2[9]);
                                    map2.put("keyword11", c.getKeyword11()+k[10] - k2[10]);
                                    map2.put("keyword12", c.getKeyword12()+k[11] - k2[11]);
                                    map2.put("keyword13", c.getKeyword13()+k[12] - k2[12]);
                                    map2.put("keyword14", c.getKeyword14()+k[13] - k2[13]);
                                    map2.put("keyword15", c.getKeyword15()+k[14] - k2[14]);
                                    map2.put("keyword16", c.getKeyword16()+k[15] - k2[15]);
                                    map2.put("keyword17", c.getKeyword17()+k[16] - k2[16]);
                                    map2.put("keyword18", c.getKeyword18()+k[17] - k2[17]);
                                    map2.put("keyword19", c.getKeyword19()+k[18] - k2[18]);
                                    map2.put("keyword20", c.getKeyword20()+k[19] - k2[19]);
                                    map2.put("keyword21", c.getKeyword21()+k[20] - k2[20]);
                                    map2.put("keyword22", c.getKeyword22()+k[21] - k2[21]);
                                    map2.put("keyword23", c.getKeyword23()+k[22] - k2[22]);
                                    map2.put("keyword24", c.getKeyword24()+k[23] - k2[23]);
                                    map2.put("keyword25", c.getKeyword25()+k[24] - k2[24]);
                                    map2.put("keyword26", c.getKeyword26()+k[25] - k2[25]);
                                    map2.put("keyword27", c.getKeyword27()+k[26] - k2[26]);
                                    map2.put("keyword28", c.getKeyword28()+k[27] - k2[27]);
                                    map2.put("keyword29", c.getKeyword29()+k[28] - k2[28]);
                                    map2.put("keyword30", c.getKeyword30()+k[29] - k2[29]);
                                    map2.put("keyword31", c.getKeyword31()+k[30] - k2[30]);
                                    map2.put("keyword32", c.getKeyword32()+k[31] - k2[31]);
                                    map2.put("keyword33", c.getKeyword33()+k[32] - k2[32]);
                                    map2.put("keyword34", c.getKeyword34()+k[33] - k2[33]);
                                    map2.put("keyword35", c.getKeyword35()+k[34] - k2[34]);
                                    map2.put("keyword36", c.getKeyword36()+k[35] - k2[35]);
                                    map2.put("tastePoint1", c.getTastePoint1()+p1 - score1);
                                    map2.put("tastePoint2", c.getTastePoint2()+p2 - score2);
                                    map2.put("tastePoint3", c.getTastePoint3()+p3 - score3);
                                    map2.put("tastePoint4", c.getTastePoint4()+p4 - score4);
                                    map2.put("seatPoint1", c.getSeatPoint1()+p5 - score5);
                                    map2.put("seatPoint2", c.getSeatPoint2()+p6 - score6);
                                    map2.put("seatPoint3", c.getSeatPoint3()+p7 - score7);
                                    map2.put("seatPoint4", c.getSeatPoint4()+p8 - score8);
                                    map2.put("studyPoint1", c.getStudyPoint1()+p9 - score9);
                                    map2.put("studyPoint2", c.getStudyPoint2()+p10 - score10);
                                    map2.put("studyPoint3", c.getStudyPoint3()+p11 - score11);
                                    map2.put("studyPoint4", c.getStudyPoint4()+p12 - score12);


                                    JSONObject jsonObject2 = new JSONObject(map2);

                                    String url3 = getResources().getString(R.string.url) + "cafe/" + c.getCafeNum().toString(); // 해당 카페에만 데이터 삽입하기 위함

                                    JsonObjectRequest objectRequest2 = new JsonObjectRequest(Request.Method.PUT, url3, jsonObject2,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("qwer", error.toString());
                                                }
                                            }) {
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset=UTF-8";
                                        }
                                    };
                                    RequestQueue queue2 = Volley.newRequestQueue(requireContext());
                                    queue2.add(objectRequest2);

                                }
                            }


                            Bundle bundle = new Bundle();
                            bundle.putString("cafeName",cafeName);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("리뷰 작성").setMessage("리뷰가 등록되었습니다.").setIcon(R.drawable.logo);

                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    // 내가 리뷰를 작성한 카페의 카페디테일로 이동
                                    navController.navigate(R.id.mypage_review_comment_to_mypage);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
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




//        // 이미지 추가 리싸이클러뷰
//        ArrayList<ReviewCommentItem> reviewCommentImageItems = new ArrayList<>();
//
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.logo));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.logo_v2));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade1));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade2));
//        reviewCommentImageItems.add(new ReviewCommentItem(R.drawable.bean_grade3));
//
//        // Adapter 추가
//        RecyclerView reviewCommentRecyclerView = root.findViewById(R.id.reviewCommentImageRecyclerView);
//
//        ReviewCommentAdapter reviewCommentAdapter = new ReviewCommentAdapter(reviewCommentImageItems);
//        reviewCommentRecyclerView.setAdapter(reviewCommentAdapter);
//
//        // Layout manager 추가
//        LinearLayoutManager reviewCommentLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        reviewCommentRecyclerView.setLayoutManager(reviewCommentLayoutManager);
//
//        // 이미지 아이템 클릭 시,
//        reviewCommentAdapter.setOnItemClickListener_ReviewComment(new ReviewCommentAdapter.OnItemClickEventListener_ReviewComment() {
//            @Override
//            public void onItemClick(View view, int position) {
//                final ReviewCommentItem item = reviewCommentImageItems.get(position);
//                Toast.makeText(getContext().getApplicationContext(), item.getReviewCommentImage() + " 클릭됨.", Toast.LENGTH_SHORT).show();
//            }
//        });

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
                if(uriList.size() >= 3) {
                    Toast.makeText(getContext().getApplicationContext(), "이미지 3개를 모두 선택하셨습니다.", Toast.LENGTH_LONG).show();
                }

                else{
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);
                }

                reviewCommentAdapter = new ReviewCommentAdapter(uriList, getContext().getApplicationContext());
                reviewCommentImageRecyclerView.setAdapter(reviewCommentAdapter);
                reviewCommentImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 3){   // 선택한 이미지가 4장 이상인 경우
                    Toast.makeText(getContext().getApplicationContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 5장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){

                        if(uriList.size() <= 2){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                            try {
                                uriList.add(imageUri);  //uri를 list에 담는다.

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                    reviewCommentAdapter = new ReviewCommentAdapter(uriList, getContext().getApplicationContext());
                    reviewCommentImageRecyclerView.setAdapter(reviewCommentAdapter);   // 리사이클러뷰에 어댑터 세팅
                    reviewCommentImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
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
