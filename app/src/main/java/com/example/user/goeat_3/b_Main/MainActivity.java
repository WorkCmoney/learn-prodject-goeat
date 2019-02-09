package com.example.user.goeat_3.b_Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.MyFragmentAdapter;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.SignInUpActivity;
import com.example.user.goeat_3.c_store_1.StoreFragment;
import com.example.user.goeat_3.d_order_1.OrderFragment;
import com.example.user.goeat_3.e_record_1.RecordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
//
//import cn.jpush.android.api.JPushInterface;


public class MainActivity extends AppCompatActivity {
    static public String nick_name = "";
    static public String user_id = "";
    static public String token = "";
    //.a_Initial.InitialActivity
//    public  void init(Context context){
//        JPushInterface.init(getApplicationContext());
//    }//這是推播的
    private static final String TAG = "MainActivity";

//    ViewPager viewPager;
//    BottomNavigationView navigation;//底部導航欄
//    List<Fragment> listFragment;//儲存頁面

    private FragmentManager fmgr;
    private FragmentTransaction transaction;
    private BottomNavigationView navigation; //底部導航欄
    //Fragment頁面
    private StoreFragment store;
    private OrderFragment order;
    private RecordFragment record;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 5) //確認是否從 B 回傳
        {
            if (requestCode == 5) //確認所要執行的動作
            {
                String result = data.getExtras().getString("status");

                if (result.equals("google_login")) {
                    new ccon().execute("registered", user_id, "google_login", nick_name,token,user_id);  //這邊是google註冊的
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSateToolbarColor(MainActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_main);
        SharedPreferences userInfo = getSharedPreferences("login", MODE_PRIVATE);
        nick_name = userInfo.getString("nick_name", "");//读取

        user_id = userInfo.getString("user_id", "");//

        fmgr = getSupportFragmentManager();
        //跳頁事件準備
        initView();//初始化
        if (savedInstanceState == null) {
            //初始畫面
            transaction = fmgr.beginTransaction();
            transaction.add(R.id.fragment, store);
            transaction.commit();
        }

//        JPushInterface.setDebugMode(true);//推播 debug
//        init(this);//推播初始化

        //開啟的同時啟用索取token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token1 = task.getResult().getToken();
                            Log.d("7878", token1);
                            token = token1;

                        } else {
                            Log.d("7878", task.getException().toString());
                        }
                    }
                });
        if (Build.VERSION.SDK_INT >= 23) { //  開啟權限使用
            int REQUEST_CODE_CONTACT = 101; //  開啟權限使
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}; //  開啟權限使
            //验证是否许可权限  //  開啟權限使
            for (String str : permissions) {  //  開啟權限使
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {  //  開啟權限使
                    //申请权限  //  開啟權限使
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT); //  開啟權限使
                } //  開啟權限使
            } //  開啟權限使
        } //  開啟權限使

    }

    private class ccon extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String result;
            result = DBConnector.executeQuery(strings[0], strings[1], strings[2], strings[3], strings[4],strings[5]);//google註冊
            return null;
        }
    }

    private void initView() {

//        viewPager = (ViewPager) findViewById(R.id.viewPager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //建構各頁面
        store = new StoreFragment();
        order = new OrderFragment();
        record = new RecordFragment();

        //建構BottomNavigation
        navigation = findViewById(R.id.navigation);
        //監聽BottomNavigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //設置底部導航欄 Icon與Text 顏色
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors1 = new int[]{getResources().getColor(R.color.menu_icon_uncheck_color),
                getResources().getColor(R.color.menu_icon_checked_color)
        };
        int[] colors2 = new int[]{getResources().getColor(R.color.menu_text_uncheck_color),
                getResources().getColor(R.color.menu_text_checked_color)
        };
        ColorStateList csl = new ColorStateList(states, colors1);
        ColorStateList cs2 = new ColorStateList(states, colors2);
        navigation.setItemTextColor(cs2);
        navigation.setItemIconTintList(csl);
    }

    //執行更換Fragment
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = fmgr.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_store:
                    transaction.replace(R.id.fragment, store);
                    transaction.commit();
                    return true;
                case R.id.navigation_order:
                    transaction.replace(R.id.fragment, order);
                    transaction.commit();
                    return true;
                case R.id.navigation_history:
                    transaction.replace(R.id.fragment, record);
                    transaction.commit();
                    if (nick_name.equals("")) {
                        Toast.makeText(MainActivity.this, "請先登入才能觀看紀錄喔", Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }
    };


    //狀態欄顏色設定
    public void setSateToolbarColor(AppCompatActivity appCompatActivity, Integer color) {
        //ToolBar顏色設定
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//            getWindow().setStatusBarColor(ContextCompat.getColor(appCompatActivity, color));
//        }
        //StatusBar顏色設定
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置全屏和状态栏透明
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // 设置状态栏为灰色
            //getWindow().setStatusBarColor(Color.parseColor("#696969"));
            getWindow().setStatusBarColor(ContextCompat.getColor(appCompatActivity, color));
            // getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //登出設定
    public final void setLogout() {
        if (!nick_name.equals("")) {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(R.drawable.ic_person_outline_red_24dp)
                    .setTitle("登出系統")
                    .setMessage("是否登出?")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
                            nick_name = "";
                            SharedPreferences userInfo = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userInfo.edit();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(MainActivity.this, "已經登出", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        } else {
            //沒註冊會先跳去註冊
            Intent intent = new Intent(MainActivity.this, SignInUpActivity.class);
            startActivityForResult(intent, 5);//REQ_FROM_A(識別碼)

        }

    }


}
