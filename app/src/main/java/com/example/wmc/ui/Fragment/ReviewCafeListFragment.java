package com.example.wmc.ui.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.android.volley.toolbox.StringRequest;
import com.example.wmc.ListCafeList.ListCafeListAdapter;
import com.example.wmc.ListCafeList.ListCafeListItem;
import com.example.wmc.MainActivity;
import com.example.wmc.R;
import com.example.wmc.ReviewCafeList.ReviewCafeListAdapter;
import com.example.wmc.ReviewCafeList.ReviewCafeListItem;
import com.example.wmc.database.Bookmark;
import com.example.wmc.database.Cafe;
import com.example.wmc.databinding.FragmentListCafelistBinding;
import com.example.wmc.databinding.FragmentListSearchBinding;
import com.example.wmc.databinding.FragmentReviewCafelistBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewCafeListFragment extends Fragment {

    private FragmentReviewCafelistBinding binding;
    private static NavController navController;
    EditText searchText; // ?????????
    Button searchButton; // ?????????
    TextView review_cafeSearch_textView; // ?????? ?????? ??????
    RecyclerView review_cafeListRecyclerView; // ?????? ?????? ??????
    LinearLayout cafeList_footer; // ??????

    ArrayList<Cafe> cafe_list;
    ArrayList<Bookmark> bookmark_list;
    Long mem_num = MainActivity.mem_num;

    Long[] get_keyword = new Long[36]; // ?????? ?????? ?????? ??????
    String tag1, tag2; // ?????? ??????

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewCafelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchText = root.findViewById(R.id.review_cafe_search_input);  // ?????? ???
        searchButton = root.findViewById(R.id.review_search_button);    // ????????? ??????
        review_cafeSearch_textView = root.findViewById(R.id.review_cafeSearch_textView);    // ????????? ????????? ??????????????????(??????)
        review_cafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);  // ?????? ????????? ??????????????????
        cafeList_footer = root.findViewById(R.id.cafeList_footer);                          // ?????? Footer

        searchText.requestFocus();  // ?????? ??? ?????????
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  // ????????? ?????? InputManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);                 // ???????????? ???????????? ?????? ???????????? ????????? ???????????? ??????

        // ?????? VISIBLE ??????
        review_cafeSearch_textView.setVisibility(View.VISIBLE);
        cafeList_footer.setVisibility(View.INVISIBLE);

        // ????????????????????? ??????????????????
        ArrayList<ReviewCafeListItem> reivewCafeListItems = new ArrayList<>();

        // ???????????? ????????? ?????? ?????? ???,
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString().replaceAll(" ", "");
                if (search.equals("")) {
                    Toast.makeText(getContext().getApplicationContext(), "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    // ???????????? ????????? ???????????? ??????
                    Toast.makeText(getContext().getApplicationContext(), search + " ?????????.", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(searchButton.getWindowToken(), 0);

                    reivewCafeListItems.clear(); // ????????? ????????? ?????????????????? ????????? ?????? ?????? ???, ???????????? ???????????? ??????

                    // ?????? ???????????? cafe_list ??????
                    RequestQueue requestQueue;
                    Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
                    Network network = new BasicNetwork(new HurlStack());
                    requestQueue = new RequestQueue(cache, network);
                    requestQueue.start();

                    String get_cafe_url = getResources().getString(R.string.url) + "cafe";

                    StringRequest cafe_stringRequest = new StringRequest(Request.Method.GET, get_cafe_url, new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
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

                            String get_bookmark_url = getResources().getString(R.string.url) + "bookmark";

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, get_bookmark_url, new Response.Listener<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
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
                                    Type listType = new TypeToken<ArrayList<Bookmark>>() {
                                    }.getType();

                                    bookmark_list = gson.fromJson(changeString, listType);

                                    ///////////////////////////////////////////////////////////////////////////////////
                                    // ?????? ???????????? ??????
                                    for (Cafe c : cafe_list) {
                                        boolean flag = false; // ???????????? ??????
                                        if (c.getCafeName().contains(search)) { // ?????? ????????? ???????????? ??????????????? ??????
                                            // ?????? ?????? 1, 2 ????????? ??????
                                            get_keyword[0] = c.getKeyword1();
                                            get_keyword[1] = c.getKeyword2();
                                            get_keyword[2] = c.getKeyword3();
                                            get_keyword[3] = c.getKeyword4();
                                            get_keyword[4] = c.getKeyword5();
                                            get_keyword[5] = c.getKeyword6();
                                            get_keyword[6] = c.getKeyword7();
                                            get_keyword[7] = c.getKeyword8();
                                            get_keyword[8] = c.getKeyword9();
                                            get_keyword[9] = c.getKeyword10();
                                            get_keyword[10] = c.getKeyword11();
                                            get_keyword[11] = c.getKeyword12();
                                            get_keyword[12] = c.getKeyword13();
                                            get_keyword[13] = c.getKeyword14();
                                            get_keyword[14] = c.getKeyword15();
                                            get_keyword[15] = c.getKeyword16();
                                            get_keyword[16] = c.getKeyword17();
                                            get_keyword[17] = c.getKeyword18();
                                            get_keyword[18] = c.getKeyword19();
                                            get_keyword[19] = c.getKeyword20();
                                            get_keyword[20] = c.getKeyword21();
                                            get_keyword[21] = c.getKeyword22();
                                            get_keyword[22] = c.getKeyword23();
                                            get_keyword[23] = c.getKeyword24();
                                            get_keyword[24] = c.getKeyword25();
                                            get_keyword[25] = c.getKeyword26();
                                            get_keyword[26] = c.getKeyword27();
                                            get_keyword[27] = c.getKeyword28();
                                            get_keyword[28] = c.getKeyword29();
                                            get_keyword[29] = c.getKeyword30();
                                            get_keyword[30] = c.getKeyword31();
                                            get_keyword[31] = c.getKeyword32();
                                            get_keyword[32] = c.getKeyword33();
                                            get_keyword[33] = c.getKeyword34();
                                            get_keyword[34] = c.getKeyword35();
                                            get_keyword[35] = c.getKeyword36();

                                            // keyword ????????? ?????? ?????? count??? ?????? 1, 2 ?????????.
                                            Long Max = get_keyword[0];
                                            Long secondMax = 0L;
                                            int counter_max = 0;
                                            int counter_second = 1;
                                            for (int i = 1; i < 36; i++) {
                                                secondMax = get_keyword[i];
                                                if (Max <= secondMax) {
                                                    secondMax = Max;
                                                    counter_second = counter_max;

                                                    Max = get_keyword[i];
                                                    counter_max = i;
                                                }
                                            }

                                            // ?????? 1 ??????
                                            switch (counter_max) {
                                                case 0:
                                                    tag1 = "#??????";
                                                    break;
                                                case 1:
                                                    tag1 = " #??????";
                                                    break;
                                                case 2:
                                                    tag1 = "#??????";
                                                    break;
                                                case 3:
                                                    tag1 = "#??????";
                                                    break;
                                                case 4:
                                                    tag1 = "#??????";
                                                    break;
                                                case 5:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 6:
                                                    tag1 = "#????????????";
                                                    break;
                                                case 7:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 8:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 9:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 10:
                                                    tag1 = "#???????????????";
                                                    break;
                                                case 11:
                                                    tag1 = "#???????????????";
                                                    break;
                                                case 12:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 13:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 14:
                                                    tag1 = "#??????";
                                                    break;
                                                case 15:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 16:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 17:
                                                    tag1 = "#??????";
                                                    break;
                                                case 18:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 19:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 20:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 21:
                                                    tag1 = "#??????";
                                                    break;
                                                case 22:
                                                    tag1 = "#??????";
                                                    break;
                                                case 23:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 24:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 25:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 26:
                                                    tag1 = "#??????";
                                                    break;
                                                case 27:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 28:
                                                    tag1 = "#????????????";
                                                    break;
                                                case 29:
                                                    tag1 = "#????????????";
                                                    break;
                                                case 30:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 31:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 32:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 33:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 34:
                                                    tag1 = "#?????????";
                                                    break;
                                                case 35:
                                                    tag1 = "#????????????";
                                                    break;

                                            }

                                            //?????? 2 ??????
                                            switch (counter_second) {
                                                case 0:
                                                    tag2 = "#??????";
                                                    break;
                                                case 1:
                                                    tag2 = "#??????";
                                                    break;
                                                case 2:
                                                    tag2 = "#??????";
                                                    break;
                                                case 3:
                                                    tag2 = "#??????";
                                                    break;
                                                case 4:
                                                    tag2 = "#??????";
                                                    break;
                                                case 5:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 6:
                                                    tag2 = "#????????????";
                                                    break;
                                                case 7:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 8:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 9:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 10:
                                                    tag2 = "#???????????????";
                                                    break;
                                                case 11:
                                                    tag2 = "#???????????????";
                                                    break;
                                                case 12:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 13:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 14:
                                                    tag2 = "#??????";
                                                    break;
                                                case 15:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 16:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 17:
                                                    tag2 = "#??????";
                                                    break;
                                                case 18:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 19:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 20:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 21:
                                                    tag2 = "#??????";
                                                    break;
                                                case 22:
                                                    tag2 = "#??????";
                                                    break;
                                                case 23:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 24:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 25:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 26:
                                                    tag2 = "#??????";
                                                    break;
                                                case 27:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 28:
                                                    tag2 = "#????????????";
                                                    break;
                                                case 29:
                                                    tag2 = "#????????????";
                                                    break;
                                                case 30:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 31:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 32:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 33:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 34:
                                                    tag2 = "#?????????";
                                                    break;
                                                case 35:
                                                    tag2 = "#????????????";
                                                    break;

                                            }
                                            for (Bookmark b : bookmark_list) {
                                                if (b.getCafeNum().equals(c.getCafeNum()) && b.getMemNum().equals(mem_num)) {
                                                    flag = true;
                                                }
                                            }
                                            reivewCafeListItems.add(new ReviewCafeListItem(c.getCafeName(), c.getCafeAddress(),
                                                    c.getOpenTime().substring(0, 2) + ":" + c.getOpenTime().substring(2, 4)
                                                            + " ~ " + c.getCloseTime().substring(0, 2) + ":" + c.getCloseTime().substring(2, 4),
                                                    tag1, tag2, R.drawable.logo, flag));
                                        }
                                    }

                                    // Recycler view
                                    RecyclerView reviewCafeListRecyclerView = root.findViewById(R.id.review_cafeListRecyclerView);

                                    // Adapter ??????
                                    ReviewCafeListAdapter reviewCafeListAdapter = new ReviewCafeListAdapter(reivewCafeListItems);
                                    reviewCafeListRecyclerView.setAdapter(reviewCafeListAdapter);

                                    // Layout manager ??????
                                    LinearLayoutManager reviewCafeListLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    reviewCafeListRecyclerView.setLayoutManager(reviewCafeListLayoutManager);

                                    // ????????????????????? ?????????????????? ????????? ????????? ?????????????????? ???????????????
                                    reviewCafeListAdapter.setOnItemClickListener_ReviewCafeList(new ReviewCafeListAdapter.OnItemClickEventListener_ReviewCafeList() {
                                        @Override
                                        public void onItemClick(View a_view, int a_position) {
                                            final ReviewCafeListItem item = reivewCafeListItems.get(a_position);
                                            Toast.makeText(getContext().getApplicationContext(), item.getCafeList_cafeName() + " ?????????.", Toast.LENGTH_SHORT).show();

                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean("reviewCafeList_flag", true);
                                            bundle.putString("reviewCafeList_flag_cafeName", item.getCafeList_cafeName());
                                            navController.navigate(R.id.review_cafelist_to_review, bundle);
                                        }
                                    });

                                    // ????????????????????? ???????????? ??????????????? ??????
                                    reviewCafeListAdapter.notifyItemInserted(reivewCafeListItems.size());

                                    // ?????????????????? ???????????? ?????? ??????, ?????? ?????? ????????? ?????? ??? ??????
                                    if (reivewCafeListItems.size() == 0) {
                                        Toast.makeText(getContext().getApplicationContext(), "???????????? ?????? ?????????\n???????????? ????????????", Toast.LENGTH_SHORT).show();
                                        review_cafeSearch_textView.setVisibility(View.VISIBLE);
                                        cafeList_footer.setVisibility(View.INVISIBLE);
                                    } else {   // ???????????? ?????? ??????
                                        review_cafeSearch_textView.setVisibility(View.INVISIBLE);
                                        cafeList_footer.setVisibility(View.VISIBLE);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("ReviewCafelist_cafe_stringRequest_error", error.toString());
                                }
                            });

                            requestQueue.add(stringRequest);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ListCafelist_cafe_stringRequest_error", error.toString());
                        }
                    });

                    requestQueue.add(cafe_stringRequest);
                }
            }
        });
        return root;
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
