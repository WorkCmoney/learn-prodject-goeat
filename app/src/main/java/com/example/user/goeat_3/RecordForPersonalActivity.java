package com.example.user.goeat_3;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.user_id;

public class RecordForPersonalActivity extends AppCompatActivity {

    private RecyclerView rv_record;
    private RecordForPersonalAdapter adapter;
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;

    private TextView txt_Amount;
    private TextView txt_orderStatus;

    String person_money;
    String all_food;
    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(RecordForPersonalActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_record_for_personal);

        //ToolBar設定
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

        txt_Amount=findViewById(R.id.txt_Amount);
        txt_orderStatus=findViewById(R.id.txt_orderStatus);
        //拿到查詢訂單Id
        Bundle bundle = getIntent().getExtras();
        String order_id = bundle.getString("order_id");

        String personorder_id=bundle.getString("personorder_id");

        rv_record = findViewById(R.id.rv_recordd);
        rv_record.setLayoutManager(new LinearLayoutManager(this));
        rv_record.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));


            new RecordForPersonalActivity.ccon().execute("person_order1", order_id,user_id);//這邊是一般人跳進來

    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = DBConnector.executeQuery(strings[0],strings[1],strings[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                jObj = new JSONObject(result);
                jsonArray = jObj.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    person_money=jsonData.getString("person_money");
                    all_food=jsonData.getString("order_meal");
                    check=jsonData.getString("check");
                }
                if(check.equals("0")){
                    check="尚未付款";
                }else check="已付款";
                txt_Amount.setText(person_money);
                txt_orderStatus.setText(check);
                String[] split_meal = all_food.split(",");

                for (  int i =0;i<split_meal.length;i++){

                    String[] split_count_food =split_meal[i].split("/");

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("meal_count", split_count_food[0]);
                    map.put("meal_name", split_count_food[1]);
                    maps.add(map);
                }
                adapter=new RecordForPersonalAdapter(maps);
                rv_record.setAdapter(adapter);

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
