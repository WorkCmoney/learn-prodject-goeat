package com.example.user.goeat_3.e_record_1.Manager;

import android.content.Intent;
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

public class RecordForInitActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv_record;
    private RecordForInitAdapter adapter;
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();
    private ArrayList<HashMap<String, String>> update_chick ;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;

    private TextView btn_return,btn_sendRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(RecordForInitActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_record_for_init);

        //ToolBar 設定
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
        btn_sendRecord = findViewById(R.id.btn_sendRecord);

        //拿到查詢訂單Id
        Bundle bundle = getIntent().getExtras();
        String order_id = bundle.getString("order_id");

        findViews();

        rv_record = findViewById(R.id.rv_record);
        rv_record.setLayoutManager(new LinearLayoutManager(this));
        rv_record.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));

//        btn_return.setOnClickListener(this);
        btn_sendRecord.setOnClickListener(this);

        new ccon().execute("orderingcontent", order_id);
    }

    private void findViews() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_return:   //回到主畫面MainActivity
//                finish();
//                break;
            case R.id.btn_sendRecord: //狀態確認完成,上傳

                String check_status="";
                update_chick=adapter.getMaps();

                Log.d("abc", "數量:"+update_chick.size());

                for(int i =0;i<update_chick.size();i++){
                    //if(!maps.get(i).get("menu_price").equals(""))
                    //Log.e("TTT",maps.get(i).get("meal_price"));
                    if(!update_chick.get(i).get("personorder_id").equals("")) {
                        if (i != 0){
                            check_status += ",";
                        }
                        check_status += update_chick.get(i).get("personorder_id")+"/"+update_chick.get(i).get("check");
                    }
                }

                new ccon().execute("UpDate_check",check_status);

                Log.d("abc", check_status);
                update_chick.clear();

                finish();
                break;
        }
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            switch (strings[0]) {
                case "orderingcontent":
                String result = DBConnector.executeQuery("orderingcontent", strings[1]);
                    return result;
                case "UpDate_check":
                     result = DBConnector.executeQuery("UpDate_check", strings[1]);
                    return result;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                jObj = new JSONObject(result);
                jsonArray = jObj.getJSONArray("data");
                Log.d("abc", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("personorder_id", jsonData.getString("personorder_id"));

                    map.put("user_id", jsonData.getString("user_id"));
                    map.put("order_id", jsonData.getString("order_id"));
                    map.put("nick_name", jsonData.getString("nick_name"));
                    map.put("order_meal", jsonData.getString("order_meal"));
                    map.put("person_money", jsonData.getString("person_money"));
                    map.put("check", jsonData.getString("check"));

                    maps.add(map);
                    // 將資料交給adapter
                    adapter = new RecordForInitAdapter(maps);
                    rv_record.setAdapter(adapter);
                    adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(RecordForInitActivity.this, RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
