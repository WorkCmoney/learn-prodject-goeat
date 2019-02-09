package com.example.user.goeat_3.e_record_1.For_RecordFragment2.Manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.e_record_1.Manager.Record4AllFragment;
import com.example.user.goeat_3.e_record_1.Manager.Record4SponsorActivity;
import com.example.user.goeat_3.e_record_1.Order.RecordAllAdapter;
import com.example.user.goeat_3.e_record_1.meal_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Record4AllFragment2 extends Fragment implements View.OnClickListener, View.OnTouchListener {
    //這頁是管理者所有訂單統計數量跟總金額的頁面
    //宣告
    private Record4SponsorActivity2 record4SponsorActivity2;
    private RecyclerView rv_record;
    private RecordAllAdapter adapter;
    private ArrayList<HashMap<String,String>> maps;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    String result="";
    private String order_id,store_name,order_status,order_price;
    private LinearLayout forcheck;
    private View v_line;
    private TextView txt_ordersended;

    //for打電話使用
    private FloatingActionButton fab; //打電話用
    private String phoneNumber = "";  //商家電話
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
    private final int ADAPTER_VALUE = 16;
    int check_time=0;
    private ArrayList<com.example.user.goeat_3.e_record_1.meal_class> meal=new ArrayList<>();
    private  ArrayList<meal_class> meall=new ArrayList<>();
    private CheckBox check_ordersended;
    //    private TextView btn_return;
    private TextView txt_Amount;
    private  int Total_money=0;
    meal_class meal_class;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        record4SponsorActivity2 = (Record4SponsorActivity2) context;
        order_id = record4SponsorActivity2.order_id;
        store_name= record4SponsorActivity2.store_name;
        order_status= record4SponsorActivity2.order_status;//這個 如果是"尚未結單" 把勾勾拿掉frag1的      如果是 "餐店送達" 把勾勾勾起來
        order_price=record4SponsorActivity2.order_price;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maps = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record4_all, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.fab);
        fab.setOnTouchListener(this);

        check_ordersended = view.findViewById(R.id.check_ordersended);
        txt_Amount=view.findViewById(R.id.txt_Amount);
        rv_record = view.findViewById(R.id.rv_record);
        forcheck = view.findViewById(R.id.forcheck);
        v_line = view.findViewById(R.id.v_line);
        txt_ordersended = view.findViewById(R.id.txt_ordersended);
        rv_record.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_record.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

//        btn_return.setOnClickListener(this);
        fab.setOnClickListener(this);
        check_ordersended.setOnClickListener(this);


        if(order_status.equals("餐點送達")){
                check_ordersended.setChecked(true);
                check_ordersended.setClickable(false);
//            check_time=1;
        }else if(order_status.equals("已經結單")){
            check_ordersended.setChecked(false);
//            check_time=0;

        }
        else {
            //新進訂單, 餐點是否送達不可勾選
            forcheck.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);
            txt_ordersended.setVisibility(View.GONE);
            check_ordersended.setVisibility(View.GONE);
        }
        new Record4AllFragment2.ccon().execute("orderingcontent", order_id);
        new Record4AllFragment2.ccon().execute("SelectStore", store_name);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_return:   //回到主畫面MainActivity
//                finish();
//                break;
            case R.id.check_ordersended: //餐點是否送達勾選
                if(check_ordersended.isChecked()) {

//                    if(check_time==0) {
//                        check_time += 1;
                    new ccon().execute("update_order", order_id, "餐點送達");
                    Toast.makeText(record4SponsorActivity2, "餐點已經更新送達", Toast.LENGTH_SHORT).show();
                    check_ordersended.setClickable(false);

                }else {
                    new ccon().execute("update_order", order_id,"已經結單");
                    Toast.makeText(record4SponsorActivity2, "餐點通知已經發給用戶摟 目前無法取消喔", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fab:   //撥打電話
                Log.v("hint", "call");

                //跳轉到撥打電話介
                Snackbar snackbar = Snackbar.make(v, "店家電話: " + phoneNumber, Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(Color.parseColor("#66c30d23"));
                snackbar.setActionTextColor(Color.parseColor("#c30d23"));
                snackbar.setAction("撥打", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("tel:" + phoneNumber);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intent);
                    }
                }).show();

                break;
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
        return true;
    }

    private boolean rangeInDefined(int mDownX, int i, int i1) {

        return Math.max(i,mDownX) == Math.min(mDownX,i1);
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            switch (strings[0]){
                case "SelectStore":
                    result = DBConnector.executeQuery(strings[0],strings[1]);break;
                case "orderingcontent" :
                    result = DBConnector.executeQuery(strings[0],strings[1]);break;
                case "update_order" :
                    result = DBConnector.executeQuery(strings[0],strings[1],strings[2]); result="check_update";break;

            }
            if(!result.equals("check_update")) {
                try {
                    jObj = new JSONObject(result);
                    int success = jObj.getInt("success");
                    if (success == 2) {
                        meal.clear();
                        meall.clear();
                        jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);


//                            Total_money += Integer.parseInt(jsonData.getString("person_money"));

                            String[] split_meal = jsonData.getString("order_meal").split(",");//字串切割
                            for (int a = 0; a < split_meal.length; a++) {

                                String[] split_count_food = split_meal[a].split("/");
                                meal_class = new meal_class();
                                meal_class.setMeal(split_count_food[1]);
                                meal_class.setCount(Integer.parseInt(split_count_food[0]));
                                meal.add(meal_class);
                            }
                        }
                    } else if (success == 1) {
                        Log.d("azz", "doInBackground: ");
                        jsonArray = jObj.getJSONArray("data");
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        phoneNumber = jsonData.getString("store_phone");
                        return "dontsetmoney";
                    } else {
                        return "ok";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                meal_class = new meal_class();
                meal_class.setCount(0);

                meall.add(meal_class);
                for (int c = 0; c < meal.size(); c++) {//這是抓資料庫使用者餐點紀錄表格

                    for (int d = 0; d < meall.size(); d++) {//這個是空的統計儲存表格(一開始無資料)

                        if (meal.get(c).getMeal().equals(meall.get(d).getMeal())) {  //把商品數量統計
//                       Log.d("abc", "商品已經重複");
                            meall.get(d).setCount(meall.get(d).getCount() + meal.get(c).getCount());
                            break;
                        } else if (d == meall.size() - 1) {   //如品項還沒有的話建立該品項
                            meal_class meal_class = new meal_class();
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

                return "ok";
            }
            return "dontsetmoney";
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(!result.equals("dontsetmoney")) {
                adapter = new RecordAllAdapter(meall);
                rv_record.setAdapter(adapter);
                txt_Amount.setText(order_price);
//              Total_money = 0;
            }
        }
    }

}
