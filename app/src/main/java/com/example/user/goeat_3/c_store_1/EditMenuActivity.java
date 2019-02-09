package com.example.user.goeat_3.c_store_1;

import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.d_order_1.OrderInfoActivity;
import com.example.user.goeat_3.d_order_1.OrderInfoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditMenuActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView btn_finish;
    //菜單元件
    private RecyclerView rv_add_menu;
    private EditMenuAdapter adapter;

    private String store_name;

    private JSONArray josnarray = null;
    private JSONObject jObj = null;
    JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> food_maps = new ArrayList<>();

    //for增加菜單使用
    private FloatingActionButton fab; //搜尋用
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
    private final int ADAPTER_VALUE = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(EditMenuActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_edit_menu);

        //拿到查詢店家名字
        Bundle bundle = getIntent().getExtras();
        store_name = bundle.getString("store_name");


        //RecyclerView與Adapter綁定
        rv_add_menu = findViewById(R.id.rv_add_menu);
        rv_add_menu.setLayoutManager(new LinearLayoutManager(this));
        rv_add_menu.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));

        //1.資料傳入
        new ccon().execute("SelectStoreMenu", store_name);//找店家產品


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("menu_price","");
                map.put("menu_name","");
                food_maps.add(map);
                adapter.notifyItemInserted(food_maps.size());
            }
        });

        fab.setOnTouchListener(this);  //設置懸浮按鈕隨手指移動


        //2.資料修改完成上傳
        btn_finish = findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String menu_name = "";
                String menu_price = "";
                ArrayList<HashMap<String, String>> maps = adapter.getMenuString();
                for (int i = 0; i < maps.size(); i++) {

                    if (!maps.get(i).get("menu_name").equals("")&&!maps.get(i).get("menu_price").equals("")) {
                        if (i != 0) {
                            menu_name += ",";
                            menu_price += ",";
                        }
                        menu_name += maps.get(i).get("menu_name");
                        menu_price += maps.get(i).get("menu_price");
                    }
                }
                new ccon().execute("Update_menu", store_name, menu_name, menu_price);

                finish();  //回到店家資訊
            }
        });
    }

    private class ccon extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            if(strings[0].equals("Update_menu")){
                DBConnector.executeQuery(strings[0],strings[1],strings[2],strings[3]);
                return "ni";
            }
            String result= DBConnector.executeQuery(strings[0],strings[1]);
            try {
                jObj = new JSONObject(result);
                jsonArray = jObj.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("meal_id", jsonData.getString("meal_id"));
                    map.put("menu_price", jsonData.getString("meal_price"));
                    map.put("menu_name", jsonData.getString("meal_name"));
                    food_maps.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return "ok";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if(result.equals("ok")) {
                adapter = new EditMenuAdapter(food_maps);
                rv_add_menu.setAdapter(adapter);
            }

        }
    }

    //設置懸浮按鈕隨手指移動
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                mDownX = (int) event.getRawX();
                mDownY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                int translationX = (int) fab.getTranslationX() + deltaX;
                int translationY = (int) fab.getTranslationY() + deltaY;
                fab.setTranslationX(translationX);
                fab.setTranslationY(translationY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (rangeInDefined(mDownX, (int) event.getRawX() - ADAPTER_VALUE, (int) event.getRawX() + ADAPTER_VALUE)) {
                    if (rangeInDefined(mDownY, (int) event.getRawY() - ADAPTER_VALUE, (int) event.getRawY() + ADAPTER_VALUE)) {
                        fab.callOnClick();
                    }
                }
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        Log.v("hint","mDownX: "+mDownX+",mDownY: "+mDownY);
        Log.v("hint","mLastX: "+mLastX+",mLastY: "+mLastY);
        Log.v("hint","x: "+x+",y: "+y);
        Log.v("hint","ADAPTER_VALUE: "+ADAPTER_VALUE);
        return true;
    }

    private boolean rangeInDefined(int mDownX, int i, int i1) {

        return Math.max(i,mDownX) == Math.min(mDownX,i1);
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
