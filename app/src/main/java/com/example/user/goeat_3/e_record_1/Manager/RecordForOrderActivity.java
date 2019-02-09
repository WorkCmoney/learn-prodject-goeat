package com.example.user.goeat_3.e_record_1.Manager;

import android.graphics.Color;
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
import android.widget.TextView;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.user_id;


public class RecordForOrderActivity extends AppCompatActivity {

    private RecyclerView rv_record;
    private RecordForOrderAdapter adapter;
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;

    private TextView btn_return;
    private TextView txt_Amount;
    private TextView txt_orderStatus;

    String person_money;
    String all_food;
    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(RecordForOrderActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_record_for_order);

        Toolbar toolbar = findViewById(R.id.toolbar_record);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        btn_return = findViewById(R.id.btn_return);
        txt_Amount=findViewById(R.id.txt_Amount);
        txt_orderStatus=findViewById(R.id.txt_orderStatus);
        //拿到查詢訂單Id
        Bundle bundle = getIntent().getExtras();
        String order_id = bundle.getString("order_id");
        String nick_name1 = bundle.getString("nick_name");
        String personorder_id=bundle.getString("personorder_id");
        rv_record = findViewById(R.id.rv_recordd);
        rv_record.setLayoutManager(new LinearLayoutManager(this));
        rv_record.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));

//        btn_return.setOnClickListener(this);

        if(nick_name1.equals("not_order_person")) {
            new RecordForOrderActivity.ccon().execute("person_order1", order_id,user_id);//這邊是一般人跳進來搜尋自己的
        }
        else{
            new RecordForOrderActivity.ccon().execute("person_order", order_id,personorder_id);//管理者跳進來 是搜尋別人的
        }

//        if(txt_orderStatus.getText().equals("尚未付款")){
//            txt_orderStatus.setTextColor(Color.parseColor("#4CAF50"));
//        }else if(txt_orderStatus.getText().equals("已付款")){
//            txt_orderStatus.setTextColor(Color.parseColor("#c30d23"));
//        }
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = DBConnector.executeQuery(strings[0],strings[1],strings[2]);


                    try {
                        jObj = new JSONObject(result);
                        int success = jObj.getInt("success");


                        if(success==1) {
                            jsonArray = jObj.getJSONArray("data");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                person_money = jsonData.getString("person_money");
                                all_food = jsonData.getString("order_meal");
                                check = jsonData.getString("check");
                            }
                            String[] split_meal = all_food.split(",");

                            for (int i = 0; i < split_meal.length; i++) {

                                String[] split_count_food = split_meal[i].split("/");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("meal_count", split_count_food[0]);
                                map.put("meal_name", split_count_food[1]);
                                maps.add(map);
                            }
                            return "ok";
                        }
                        else {
                            return "nolist";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return "nolist";
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("ok")) {
                if (check.equals("0")) {
                    check = "尚未付款";
                    txt_orderStatus.setTextColor(Color.parseColor("#c30d23"));
                } else {
                    check = "已付款";
                    txt_orderStatus.setTextColor(Color.parseColor("#4CAF50"));
                }
                txt_Amount.setText(person_money);
                txt_orderStatus.setText(check);
                adapter = new RecordForOrderAdapter(maps);
                rv_record.setAdapter(adapter);
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
