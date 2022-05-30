package com.example.wmc.ui.Fragment;

import android.util.Log;

import com.example.wmc.R;

import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadUtils {

    public static void sendCafeImage(File file, Long cafe_Num) {

        String cafeImageUpload_URL = R.string.url + "cafeImage/upload";

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .addFormDataPart("cafeNum", cafe_Num.toString())
                .build();


        Request request = new Request.Builder()
                .url("http://52.90.60.43:8080/cafeImage/upload")
                        .post(requestBody)
                        .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("test_error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("test : ", response.body().string());
            }
        });
    }

    public static void sendReviewImage(File file, Long mem_num, Long review_num) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .addFormDataPart("memNum", mem_num.toString())
                .addFormDataPart("reviewNum", review_num.toString())
                .build();

        Request request = new Request.Builder()
                .url("http://52.90.60.43:8080/reviewImage/upload")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("test_error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("test : ", response.body().string());
            }
        });
    }
}
