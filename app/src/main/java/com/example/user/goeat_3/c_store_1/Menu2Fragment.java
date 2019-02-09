package com.example.user.goeat_3.c_store_1;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;


public class Menu2Fragment extends Fragment  {

    //宣告
    private StoreInfoActivity storeInfoActivity;
    private RecyclerView rv_menu2;
    private Menu2Adapter adapter;
    private ArrayList<HashMap<String,Object>> maps;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    private boolean  check=true;
    private String store_name;
    private RatingBar rb_normal;
    int star_avg=0;
    int stay_star;
    int n=0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        storeInfoActivity = (StoreInfoActivity) context;

        store_name = storeInfoActivity.store_name;
        Log.d("qqq", store_name);


//        star_avg= Integer.parseInt(storeInfoActivity.store_star); //報錯
        Log.d("qqq", "初始化星星數量"+storeInfoActivity.store_star);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        maps = new ArrayList<>();


        return inflater.inflate(R.layout.fragment_menu2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_menu2 = view.findViewById(R.id.rv_menu2);
        rv_menu2.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_menu2.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));



        new ccon().execute("Select_Comment", store_name);

    }

//    public void fresh_like() {
//        ArrayList<HashMap<String,String>> Update_maps= adapter.ger_Update_maps();
////        Log.d("qwer", Update_maps.get(0).get("comment_like"));
//
//    }

    public class ccon extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result;
            if(strings[0].equals("New_Comment")){

                result = DBConnector.executeQuery(strings[0], strings[1],strings[2],strings[3],strings[4],strings[5]);
                return result;
            }else {
                result = DBConnector.executeQuery(strings[0], strings[1]);

                return result;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                jObj = new JSONObject(result);
                int success = jObj.getInt("success");
                if(success==1) {
                    Log.d("aqq", "有進來嗎 ");
                    maps.clear();
                    jsonArray = jObj.getJSONArray("data");
                    String a[];
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("comment_id", jsonData.getString("comment_id"));
                        map.put("nick_name", jsonData.getString("nick_name"));
                        map.put("comment", jsonData.getString("comment"));
                        map.put("comment_like", jsonData.getString("comment_like"));
                        map.put("store_name", jsonData.getString("store_name"));
                        map.put("Date", jsonData.getString("Date"));
                        switch (jsonData.getString("score")){
                            case "5": map.put("score", R.drawable.comfive);break;
                            case "4": map.put("score", R.drawable.comfour);break;
                            case "3": map.put("score", R.drawable.comthree);break;
                            case "2": map.put("score", R.drawable.comtwo);break;
                            case "1": map.put("score", R.drawable.comone);break;
                        }
                        map.put("score1", jsonData.getString("score"));
                        maps.add(map);
                    }
                    n=jsonArray.length()+1;
                    Log.d("aqq", n+"多少");
                    adapter = new Menu2Adapter(maps);
                    rv_menu2.setAdapter(adapter);

                    adapter.setClickListener(new Menu2Adapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            if (view.getId() == R.id.btn_share && position == 0) {
                                //按送出按鈕
                                if (check) {
                                    String comment = adapter.get_coment();
                                    if (!comment.equals("")&&!nick_name.equals("")) {
                                        rb_normal = adapter.getstart();

                                        if ((int) rb_normal.getRating() == 0) { //你打的分數

                                            stay_star = 1;
                                        } else {
                                            stay_star = (int) rb_normal.getRating();
                                        }

                                        for (int i = 0; i < maps.size(); i++) {

                                            star_avg += Integer.parseInt(maps.get(i).get("score1").toString());
                                        }
                                        Log.d("azz", "個人星星" + stay_star);
                                        star_avg += stay_star;
                                        Log.d("azz", "總星星後" + star_avg);


                                        star_avg=Math.round((float) star_avg/(float)n);
//                                        star_avg = Math.round(star_avg / n);//算星星的平均
                                        Log.d("azz", "總評論人數" + n);
                                        Log.d("azz", "平均星星" + star_avg);

                                        new ccon().execute("New_Comment", store_name, nick_name, comment, String.valueOf(stay_star), String.valueOf(star_avg));

                                        Toast.makeText(getActivity(), "訊息已經發送了", Toast.LENGTH_SHORT).show();
                                        new ccon().execute("Select_Comment", store_name);
                                        check = false;
                                    }else{
                                        Toast.makeText(getActivity(), "是不是還沒登入或者沒輸入資訊呢?", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), "訊息不要連續發送喔~", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }else{
                    HashMap<String,Object> map=new HashMap<>();

                    map.put("nick_name", "沒有評論者");
                    map.put("comment","尚未有評論");
                    map.put("comment_like", "0");
                    map.put("store_name", store_name);
                    map.put("score", R.drawable.comno);
                    map.put("Date", "");
                    maps.add(map);
                    adapter = new Menu2Adapter(maps);
                    rv_menu2.setAdapter(adapter);
                    adapter.setClickListener(new Menu2Adapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            if (view.getId() == R.id.btn_share && position == 0) {
                                //按送出按鈕
                                if (check) {
                                    String comment = adapter.get_coment();
                                    rb_normal= adapter.getstart();

                                    if(!comment.equals("")&&!nick_name.equals("")) {

                                        if ((int) rb_normal.getRating() == 0) {

                                            stay_star = 1;
                                        } else {
                                            stay_star = (int) rb_normal.getRating();
                                        }
                                        new ccon().execute("New_Comment", store_name, nick_name, comment, String.valueOf(stay_star), String.valueOf(stay_star));
                                        Toast.makeText(getActivity(), "訊息已經發送了", Toast.LENGTH_SHORT).show();
                                        new ccon().execute("Select_Comment", store_name);
                                        check = false;
                                    }
                                    else{ Toast.makeText(getActivity(), "是不是還沒登入或者沒輸入資訊呢?", Toast.LENGTH_SHORT).show();}
                                } else {

                                    Toast.makeText(getActivity(), "訊息不要連續發送喔~", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
