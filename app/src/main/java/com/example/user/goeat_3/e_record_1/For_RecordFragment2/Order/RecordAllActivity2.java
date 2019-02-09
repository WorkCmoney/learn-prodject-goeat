package com.example.user.goeat_3.e_record_1.For_RecordFragment2.Order;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.e_record_1.Order.RecordAllActivity;
import com.example.user.goeat_3.e_record_1.Order.RecordAllAdapter;
import com.example.user.goeat_3.e_record_1.meal_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordAllActivity2 extends AppCompatActivity {

    private RecyclerView rv_record;
    private RecordAllAdapter adapter;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    private ArrayList<com.example.user.goeat_3.e_record_1.meal_class> meal=new ArrayList<>();
    private  ArrayList<meal_class> meall=new ArrayList<>();
    //    private TextView btn_return;
    private TextView txt_Amount;
    private  int Total_money=0;
    meal_class meal_class;
    String order_price,order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(RecordAllActivity2.this, R.color.colorAccent);
        setContentView(R.layout.activity_record_all);


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

        //拿到查詢訂單Id
        Bundle bundle = getIntent().getExtras();
         order_id = bundle.getString("order_id");
        order_price = bundle.getString("order_price");


        findViews();
        txt_Amount=findViewById(R.id.txt_Amount);
        rv_record = findViewById(R.id.rv_record);
        rv_record.setLayoutManager(new LinearLayoutManager(this));
        rv_record.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));

//        btn_return.setOnClickListener(this);

        new RecordAllActivity2.ccon().execute("orderingcontent", order_id);
    }

    private void findViews() {
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_return:   //回到主畫面MainActivity
//                finish();
//                break;
//        }
//    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = DBConnector.executeQuery(strings[0],strings[1]);
            meal.clear();
            meall.clear();
            try {
                jObj = new JSONObject(result);
                jsonArray = jObj.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);

//                    Total_money+=Integer.parseInt(jsonData.getString("person_money"));

                    String[] split_meal=jsonData.getString("order_meal").split(",");//字串切割
                    for (  int a =0;a<split_meal.length;a++){

                        String[] split_count_food =split_meal[a].split("/");
                        meal_class=new meal_class();
                        meal_class.setMeal(split_count_food[1]);
                        meal_class.setCount(Integer.parseInt(split_count_food[0]));
                        meal.add(meal_class);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            meal_class=new meal_class();
            meal_class.setCount(0);
            meal_class.setMeal("測試");
            meall.add(meal_class);
            for(int c=0;c<meal.size();c++){//這是抓資料庫使用者餐點紀錄表格

                for (int d=0;d<meall.size();d++){//這個是空的統計儲存表格(一開始無資料)

                    if(meal.get(c).getMeal().equals(meall.get(d).getMeal())){  //把商品數量統計
//                       Log.d("abc", "商品已經重複");
                        meall.get(d).setCount(meall.get(d).getCount()+meal.get(c).getCount());
                        break;
                    }
                    else if(d==meall.size()-1){   //如品項還沒有的話建立該品項
                        meal_class meal_class=new meal_class();
                        meal_class.setCount(meal.get(c).getCount());
                        meal_class.setMeal(meal.get(c).getMeal());
                        meall.add(meal_class);
//                       Log.d("abc", "建立新商品"+meal.get(c).getMeal());
//                       Log.d("abc", "建立新數量"+meal.get(c).getCount());
                        break;
                    }
                }
            }
            meall.remove(0);

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            adapter = new RecordAllAdapter(meall);
            rv_record.setAdapter(adapter);

            txt_Amount.setText(order_price);
//            Total_money=0;

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

