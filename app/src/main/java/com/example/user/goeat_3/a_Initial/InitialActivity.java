package com.example.user.goeat_3.a_Initial;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.goeat_3.b_Main.MainActivity;
import com.example.user.goeat_3.R;

public class InitialActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

//        //全螢幕顯示
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION   //隱藏狀態欄
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN   //標題欄
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN );//全螢幕顯示

        //跳轉頁面
        UIHandler.postDelayed(runnable,500 );
    }

    Handler UIHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(InitialActivity.this, MainActivity.class));
            finish();
        }
    };
}
