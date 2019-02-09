package com.example.user.goeat_3.d_order_1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import static java.security.AccessController.getContext;

public class OrderInfoActivity extends AppCompatActivity {

    private RecyclerView rv_orderInfo;
    private OrderInfoAdapter adapter;
    private ArrayList<HashMap<String, String>> food_maps = new ArrayList<>();
    private ArrayList<HashMap<String, String>> store_maps = new ArrayList<>();
    private ArrayList<HashMap<String, String>> set_food = new ArrayList<>();
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    private TextView person_price;
    private TextView person_count;
    private TextView store_name,store_type,store_phone,store_address,store_open,btn_sendOrder;
    private int count =0;
    private int price =0;
    String order_id,store_name1,result,person_money,personorder_id,order_meal,isorder,meal_name;
    String[] time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(OrderInfoActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_order_info);
        person_price = findViewById(R.id.person_price);
        person_count = findViewById(R.id.person_count);
        Toolbar toolbar = findViewById(R.id.toolbar_order);

        //拿到查詢訂單
        Bundle bundle = getIntent().getExtras();
         order_id = bundle.getString("order_id");
         store_name1 = bundle.getString("store_name");
        Log.d("azz", store_name1+order_id);
        isorder=bundle.getString("update");
         if(isorder.equals("true")){//這個人已經下單過了

             personorder_id=bundle.getString("personorder_id");
             person_money=bundle.getString("person_money");
             order_meal=bundle.getString("order_meal");
             person_price.setText("總金額:"+person_money+"元");
             Log.d("azz", personorder_id+order_meal+person_money);

         }


        findViews();

        rv_orderInfo=findViewById(R.id.rv_orderInfo);
        btn_sendOrder=findViewById(R.id.btn_sendOrder);
        btn_sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();
                map=  adapter.getFood_maps();
                map.get("person_money");
                map.get("order_meal");
                Log.d("qqq", map.get("order_meal")+map.get("person_money")+order_id+nick_name);

                if(isorder.equals("false")&&!map.get("person_money").equals("0")){

                    new ccon().execute("order_meal", map.get("order_meal"),map.get("person_money"),order_id,nick_name,user_id,store_name1);
                    Toast.makeText(OrderInfoActivity.this, "送出訂單", Toast.LENGTH_SHORT).show();
                    finish();


                }else if(!map.get("person_money").equals("0")) {

                    Log.d("kay",  map.get("order_meal"));
                    new ccon().execute("update_meal", map.get("order_meal"),map.get("person_money"),personorder_id,order_id);
                    Toast.makeText(OrderInfoActivity.this, "訂單已經更新", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(OrderInfoActivity.this, "請確實填寫訂單數量", Toast.LENGTH_SHORT).show();
                }

            }
        });


        rv_orderInfo.setLayoutManager(new LinearLayoutManager(this));
        rv_orderInfo.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));

       new ccon().execute("SelectStore", store_name1); //找店家資訊

        new ccon().execute("SelectStoreMenu", store_name1);//找店家產品

    }

    private void findViews() {

    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if(strings[0].equals("order_meal")){
                result = DBConnector.executeQuery(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6]);

                return result;
            }
            else if(strings[0].equals("update_meal")){
                result = DBConnector.executeQuery(strings[0],strings[1],strings[2],strings[3],strings[4]);

                return result;
            }else result = DBConnector.executeQuery(strings[0],strings[1]);

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("qqq", result.toString());


            if (!result.equals("")) {
                try {
                    jObj = new JSONObject(result);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        food_maps.clear();
                        jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("store_id", jsonData.getString("store_id"));
                            map.put("store_name", jsonData.getString("store_name"));
                            map.put("store_type", jsonData.getString("store_type"));
                            map.put("store_phone", jsonData.getString("store_phone"));
                            map.put("store_address", jsonData.getString("store_address"));
                            map.put("store_pic", jsonData.getString("store_pic"));
                            time = jsonData.getString("store_time").split(",");

                             map.put("store_open", "營業時間:" + time[0] + ":" + time[1] + "~" + time[2] + ":" + time[3]);

                            food_maps.add(map);
                        }

                    } else if (success == 4) {

                        if(isorder.equals("true")) {
                            String[] split_meal;

                            split_meal = order_meal.split(",");
                            for (int i = 0; i < split_meal.length; i++) {

                                String[] split_count_food = split_meal[i].split("/");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("meal_count", split_count_food[0]);
                                map.put("meal_name", split_count_food[1]);

                                set_food.add(map);
//
                                count+=Integer.parseInt(split_count_food[0]);
                            }
                            person_count.setText("總數量:"+count+"個");

                            jsonArray = jObj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                meal_name =jsonData.getString("meal_name");

                                Log.d("ert", "數量:"+set_food.size());

                                if(set_food.isEmpty()){
                                    map.put("meal_count", "0");
                                    map.put("meal_name", jsonData.getString("meal_name"));
                                }else{
                                    for(int j=0;j<set_food.size();j++){
                                        if(set_food.get(j).get("meal_name").equals(meal_name)){
                                            map.put("meal_name", jsonData.getString("meal_name"));
                                            map.put("meal_count", set_food.get(j).get("meal_count"));
                                            Log.d("ttt", jsonData.getString("meal_name")+ set_food.get(j).get("meal_count"));
                                            set_food.remove(j);
                                            break;
                                        }
                                        else if(j==set_food.size()-1){

                                            map.put("meal_count", "0");
                                            map.put("meal_name", jsonData.getString("meal_name"));

                                            break;
                                        }
                                    }
                                }
                                map.put("meal_id", jsonData.getString("meal_id"));
                                map.put("meal_price", jsonData.getString("meal_price"));
                                map.put("meal_store", jsonData.getString("meal_store"));
                                food_maps.add(map);
                            }
//                            for (int i=0;i<food_maps.size();i++){
//                                Log.d("ttt",i+"目前次數");
//                                Log.d("ttt",  food_maps.get(i).get("meal_count"));
//
//                            }
                            adapter = new OrderInfoAdapter(food_maps,OrderInfoActivity.this);
                            adapter.get(Integer.parseInt(person_money));
                            rv_orderInfo.setAdapter(adapter);
                        }else{
                            jsonArray = jObj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("meal_id", jsonData.getString("meal_id"));
                                map.put("meal_price", jsonData.getString("meal_price"));
                                map.put("meal_store", jsonData.getString("meal_store"));
                                map.put("meal_name", jsonData.getString("meal_name"));
                                map.put("meal_count", "0");
                                food_maps.add(map);
                            }
                            adapter = new OrderInfoAdapter(food_maps,OrderInfoActivity.this);
                            rv_orderInfo.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //狀態欄顏色設定
    public void setSateToolbarColor(AppCompatActivity appCompatActivity, Integer color) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(ContextCompat.getColor(appCompatActivity, color));
        }
    }
}
