package com.example.user.goeat_3.c_store_1;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditStoreActivity extends AppCompatActivity{

    //店家元件
    private TextView btn_finish;
    private EditText edi_storeName, edi_storePhone, edi_storeAddress;
    private Calendar c;  //建立日曆物件
    private TextView start_time, end_time; //營業開始時間,營業結束時間
    private Spinner spinner_store_class;
    private String[] storeClassSet = {"中式", "日式", "西式", "泰式", "韓式", "複合式", "炸物", "點心", "飲料","複合式"};
    private String storeName = "";
    private String storePhone = "";
    private String storeAddress = "";
    private String store_class = "";
    private String store_start_time = "";
    private String store_end_time = "";
    private String store_time = "";
    private String set_calss = "";
    private JSONArray josnarray = null;
    private JSONObject jObj = null;
    JSONArray jsonArray;

    //新增圖片用
    private ImageView Img_store;       //新增圖片按鈕
    private RoundImageView store_img;  //圖片顯示
    private String imagepath=null;     //圖片路徑
    private String fileName="";        //放圖片資料夾

    private String store_name;
    String store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(EditStoreActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_edit_store);

        //拿到查詢店家名字
        Bundle bundle = getIntent().getExtras();
        store_name = bundle.getString("store_name");


        new ccon().execute("SelectStore",store_name);

        Toolbar toolbar = findViewById(R.id.toolbar_createStore);

        //findViews
        btn_finish = findViewById(R.id.btn_finish); //修改完成按鈕
        View subView = findViewById(R.id.add_store);
        edi_storeName = subView.findViewById(R.id.edi_storeName);
        edi_storePhone = subView.findViewById(R.id.edi_storePhone);
        edi_storeAddress = subView.findViewById(R.id.edi_storeAddress);
        start_time = subView.findViewById(R.id.start_time);
        end_time = subView.findViewById(R.id.end_time);
        spinner_store_class = subView.findViewById(R.id.spinner_store_class);
        Img_store = subView.findViewById(R.id.Img_store);
        store_img = subView.findViewById(R.id.store_img);

        //1.資料傳入

//        start_time.setText();
//        end_time.setText();
//        spinner_store_class.setSelection(class_position);  //用類別位置做設定

        //2.資料修改
        //2.1 時間修改
        c = Calendar.getInstance();   //建立日曆物件
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(v.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                if (hourOfDay < 10 && minute < 10) {
                                    start_time.setText("0" + hourOfDay + ":0" + minute);
                                    store_start_time = "0" + hourOfDay + ",0" + minute;
                                } else if (hourOfDay < 10) {
                                    start_time.setText("0" + hourOfDay + ":" + minute);
                                    store_start_time = "0" + hourOfDay + "," + minute;
                                } else if (minute < 10) {
                                    start_time.setText(hourOfDay + ":0" + minute);
                                    store_start_time = hourOfDay + ",0" + minute;
                                } else {
                                    start_time.setText(hourOfDay + ":" + minute);
                                    store_start_time = hourOfDay + "," + minute;
                                }
//                                store_start_time = start_time.getText().toString();
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), true);
                dialog.show();

            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(v.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                if (hourOfDay < 10 && minute < 10) {
                                    end_time.setText("0" + hourOfDay + ":0" + minute);
                                    store_end_time = "0" + hourOfDay + ",0" + minute;
                                } else if (hourOfDay < 10) {
                                    end_time.setText("0" + hourOfDay + ":" + minute);
                                    store_end_time = "0" + hourOfDay + "," + minute;
                                } else if (minute < 10) {
                                    end_time.setText(hourOfDay + ":0" + minute);
                                    store_end_time = hourOfDay + ",0" + minute;
                                } else {
                                    end_time.setText(hourOfDay + ":" + minute);
                                    store_end_time = hourOfDay + "," + minute;
                                }
//                                store_end_time = end_time.getText().toString();
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), true);
                dialog.show();

            }
        });

        //2.2 類別修改
        spinner_store_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_calss = storeClassSet[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //3.修改完成資料上傳
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeName = edi_storeName.getText().toString();
                storePhone = edi_storePhone.getText().toString();
                storeAddress = edi_storeAddress.getText().toString();
                store_class = set_calss;
                store_time = store_start_time + "," + store_end_time;


                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(storePhone);
                if (!storeName.equals("") && !storePhone.equals("") && !storeAddress.equals("") && !store_start_time.equals("") && !store_end_time.equals("")&&isNum.matches()) {

                    new ccon().execute("update_store_ifo",storeName,storePhone,storeAddress,store_time,store_class,store_id);
                    finish(); //回到店家資訊

                }
                else {
                    Toast.makeText(getApplication(), "請輸入完整資訊", Toast.LENGTH_SHORT).show();
                    if (storeName.equals("")) {
                        edi_storeName.setError("店名不可為空");
                    }
                    if (storePhone.equals("")) {
                        edi_storePhone.setError("電話不可為空");
                    }
                    if (storeAddress.equals("")) {
                        edi_storeAddress.setError("地址不可為空");
                    }
                    if (store_start_time.equals("")) {
                        start_time.setError("開始營業時間不可為空");
                    }
                    if (store_end_time.equals("")) {
                        end_time.setError("結束營業時間不可為空");
                    }


                    if( !isNum.matches() )
                    {
                        edi_storePhone.setError("電話請輸入數字");
                    }
                }

            }
        });

    }


    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result;
            if(strings[0].equals("update_store_ifo")){

                 DBConnector.executeQuery(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6]);
                Log.d("7878", "doInBackground: "+strings[0]+strings[1]+strings[2]+strings[3]+strings[4]+strings[5]+strings[6]);
                return  null;

            }else
            {
                result = DBConnector.executeQuery(strings[0],strings[1]);
                return result;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {

                try {
                    jObj = new JSONObject(result);

                    josnarray = jObj.getJSONArray("data");

                    JSONObject jsonData = josnarray.getJSONObject(0);

                    store_id = jsonData.getString("store_id");
                    jsonData.getString("store_name");
                    jsonData.getString("store_phone");
                    jsonData.getString("store_address");
                    jsonData.getString("store_type");


                    edi_storeName.setText(jsonData.getString("store_name"));
                    edi_storePhone.setText(jsonData.getString("store_phone"));
                    edi_storeAddress.setText(jsonData.getString("store_address"));
                    String time[] = jsonData.getString("store_time").split(",");
                   start_time.setText(time[0] + ":" + time[1]);
                    store_start_time=time[0]+","+time[1];
                    end_time.setText(time[2] + ":" + time[3]);
                    store_end_time=time[2]+","+time[3];

                    for (int i = 0; i <= 9; i++) {
                        if (storeClassSet[i].equals(jsonData.getString("store_type"))) {
                            spinner_store_class.setSelection(i);
                            break;
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
