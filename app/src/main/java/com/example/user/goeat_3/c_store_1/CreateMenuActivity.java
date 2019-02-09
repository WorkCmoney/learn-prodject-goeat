package com.example.user.goeat_3.c_store_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.b_Main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.user_id;

public class CreateMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView btn_menu_return, btn_menu_finish;
    //新增菜單元件
    private RecyclerView rv_add_menu;
    private CreateMenuAdapter adapter;

    private String storeName = "";
    private String storePhone = "";
    private String storeAddress = "";
    private String store_class = "";
    private String store_time = "";
    private String imagepath ="";
    private String fileName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(CreateMenuActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_create_menu);

        //拿到新增店家的資訊
        Bundle bundle = getIntent().getExtras();
        storeName = bundle.getString("storeName");
        storePhone = bundle.getString("storePhone");
        storeAddress=bundle.getString("storeAddress");
        store_class=bundle.getString("store_class");
        store_time=bundle.getString("store_time");
        imagepath = bundle.getString("imagepath");
        fileName = bundle.getString("fileName");

        //RecyclerView與Adapter綁定
        rv_add_menu = findViewById(R.id.rv_add_menu);
        rv_add_menu.setLayoutManager(new LinearLayoutManager(this));
        rv_add_menu.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));
        adapter = new CreateMenuAdapter();
        rv_add_menu.setAdapter(adapter);

        btn_menu_return = findViewById(R.id.btn_menu_return);
        btn_menu_finish = findViewById(R.id.btn_menu_finish);

        btn_menu_return.setOnClickListener(this);
        btn_menu_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_menu_return:
                String menu_name_s = "";
                String menu_price_s = "";
                ArrayList<HashMap<String, String>> maps_s = adapter.getMenuString();
                for (int i = 0; i < maps_s.size(); i++) {
                    if (!maps_s.get(i).get("menu_name").equals("")&&!maps_s.get(i).get("menu_price").equals("")) {
                        if (i != 0) {
                            menu_name_s += ",";
                            menu_price_s += ",";
                        }
                        menu_name_s += maps_s.get(i).get("menu_name");
                        menu_price_s += maps_s.get(i).get("menu_price");
                    }
                }
                finish();
                break;
            case R.id.btn_menu_finish:
                if(imagepath!=null) {
                    UploadFile u = new UploadFile(imagepath);//新增圖片用
                    u.start();//新增圖片用
                }
                String menu_name = "";
                String menu_price = "";
                ArrayList<HashMap<String, String>> maps = adapter.getMenuString();
                for (int i = 0; i < maps.size(); i++) {
                    //if(!maps.get(i).get("menu_price").equals(""))
                    //Log.e("TTT",maps.get(i).get("meal_price"));
                    if (!maps.get(i).get("menu_name").equals("")&&!maps.get(i).get("menu_price").equals("")) {
                        if (i != 0) {
                            menu_name += ",";
                            menu_price += ",";
                        }
                        menu_name += maps.get(i).get("menu_name");
                        menu_price += maps.get(i).get("menu_price");
                    }
                }
                if (!maps.get(0).get("menu_name").equals("") && !maps.get(0).get("menu_price").equals("")) {//第一筆資料不能為空
                    new ccon().execute("CreateStore", storeName, storePhone, storeAddress, store_class, menu_name, menu_price, store_time,fileName,user_id);
//                String result = DBConnector.executeQuery("CreateStore",storeName,meal_name,meal_price);

                    //填寫完畢回到主畫面MainActivity
                    Intent intent = new Intent ( this, MainActivity. class ) ;
                    intent.setFlags( Intent . FLAG_ACTIVITY_CLEAR_TASK | Intent . FLAG_ACTIVITY_NEW_TASK ) ;
                    startActivity(intent) ;
//                    finish();
                } else Toast.makeText(getApplication(), "請輸入完整資訊", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = DBConnector.executeQuery(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7], strings[8],strings[9]);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
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
