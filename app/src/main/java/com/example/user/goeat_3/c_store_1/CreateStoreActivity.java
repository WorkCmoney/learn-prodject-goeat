package com.example.user.goeat_3.c_store_1;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.RoundImageView;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateStoreActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //新增店家元件
    private TextView btn_store_next, btn_store_return;
    private TextView btn_menu_return, btn_menu_finish;
    private EditText edi_storeName, edi_storePhone, edi_storeAddress;
    //    private EditText start_hour,start_min,end_hour,end_min;
    private Calendar c;  //建立日曆物件
    private TextView start_time, end_time; //營業開始時間,營業結束時間
    private Spinner spinner_store_class;
    private String[] storeClassSet = {"中式", "日式", "西式", "泰式", "韓式", "複合式", "炸物", "點心", "飲料","複合式"};
    private View include_add_store;
    private String storeName = "";
    private String storePhone = "";
    private String storeAddress = "";
    private String store_class = "";
    private String store_start_time = "";
    private String store_end_time = "";
    private String store_time = "";
    private String set_calss = "";

    //新增圖片用
    private ImageView Img_store;       //新增圖片按鈕
    private RoundImageView store_img;  //圖片顯示
    private String imagepath=null;     //圖片路徑
    private String fileName="";        //放圖片資料夾

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(CreateStoreActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_create_store); //新增店家畫面


        Toolbar toolbar = findViewById(R.id.toolbar_createStore);
        findViews();

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
        Img_store.setOnClickListener(this);//新增圖片用

        btn_store_next.setOnClickListener(this);
        btn_store_return.setOnClickListener(this);
        spinner_store_class.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Img_store:
                //圖片點擊添加放這邊
                Intent intent = new Intent(Intent.ACTION_PICK,//新增圖片用
                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//新增圖片用
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);//新增圖片用
                break;
            case R.id.btn_store_return:
                finish();
                break;
            case R.id.btn_store_next:
                //這邊要放圖片新增上去
                storeName = edi_storeName.getText().toString();
                storePhone = edi_storePhone.getText().toString();
                storeAddress = edi_storeAddress.getText().toString();
                store_class = set_calss;
//                store_time=start_hour.getText().toString()+","+start_min.getText().toString()+","+end_hour.getText().toString()+","+end_min.getText().toString();
                store_time = store_start_time + "," + store_end_time;
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(storePhone);


                if (!storeName.equals("") && !storePhone.equals("") && !storeAddress.equals("") && !store_start_time.equals("") && !store_end_time.equals("")&&isNum.matches() ) {
                    Intent intent_menu = new Intent(CreateStoreActivity.this,CreateMenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("storeName", storeName);
                    bundle.putString("storePhone", storePhone);
                    bundle.putString("storeAddress", storeAddress);
                    bundle.putString("store_class", store_class);
                    bundle.putString("store_time", store_time);
                    bundle.putString("imagepath", imagepath);
                    bundle.putString("fileName", fileName);
                    intent_menu.putExtras(bundle);
                    startActivity(intent_menu);
                } else {
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
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//新增圖片用
        if (requestCode == 1 && resultCode == RESULT_OK) {//新增圖片用
            Uri selectedImageUri = data.getData();//新增圖片用
            imagepath = getPath(selectedImageUri);//新增圖片用
            if(imagepath!=null) {
                Log.e("a123",imagepath);
                File tempFile = new File(imagepath);
                Bitmap bitmap= BitmapFactory.decodeFile(imagepath);//新增圖片用
//                Img_store.setImageBitmap(bitmap);//新增圖片用
                Img_store.setVisibility(View.GONE);
                store_img.setImageBitmap(bitmap);//新增圖片用
                store_img.setVisibility(View.VISIBLE);
                String[] path = imagepath.split("/");//新增圖片用
                int pathSize = path.length;//新增圖片用
                fileName = "uploads/" + path[pathSize - 1];//新增圖片用
            }
        }
    }

    public String getPath(Uri uri) {//新增圖片用
        String[] projection = { MediaStore.Images.Media.DATA };//新增圖片用
        Cursor cursor = managedQuery(uri, projection, null, null, null);//新增圖片用
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//新增圖片用
        cursor.moveToFirst();//新增圖片用
        return cursor.getString(column_index);//新增圖片用
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        set_calss = storeClassSet[position];
        Log.v("hint", store_class);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void findViews() {
        btn_store_next = findViewById(R.id.btn_store_next);
        btn_store_return = findViewById(R.id.btn_store_return);
        View subView = findViewById(R.id.add_store);
        edi_storeName = subView.findViewById(R.id.edi_storeName);
        edi_storePhone = subView.findViewById(R.id.edi_storePhone);
        edi_storeAddress = subView.findViewById(R.id.edi_storeAddress);
        start_time = subView.findViewById(R.id.start_time);
        end_time = subView.findViewById(R.id.end_time);
        spinner_store_class = subView.findViewById(R.id.spinner_store_class);
        Img_store = subView.findViewById(R.id.Img_store);
        store_img = subView.findViewById(R.id.store_img);
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
