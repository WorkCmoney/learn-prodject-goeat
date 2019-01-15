package com.example.user.goeat_3.b_Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.user.goeat_3.c_store_1.StoreFragment;
import com.example.user.goeat_3.e_history_1.HistoryFragment;
import com.example.user.goeat_3.MyFragmentAdapter;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.d_order_1.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity {


    public  void init(Context context){
        JPushInterface.init(getApplicationContext());
    }//這是推播的
    private static final String TAG = "MainActivity";
    ViewPager viewPager;
    BottomNavigationView navigation;//底部導航欄
    List<Fragment> listFragment;//儲存頁面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(MainActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_main);

        initView();//初始化
        JPushInterface.setDebugMode(true);//推播 debug
        init(this);//推播初始化
    }

    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);


        //向ViewPager添加各页面
        listFragment = new ArrayList<>();
        listFragment.add(new StoreFragment());
        listFragment.add(new OrderFragment());
        listFragment.add(new HistoryFragment());
        MyFragmentAdapter myAdapter = new MyFragmentAdapter(getSupportFragmentManager(), this, listFragment);
        viewPager.setAdapter(myAdapter);

        //底部導航欄點擊事件和ViewPager滑動事件,讓兩個控件相互關聯
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //這裡設置為:當點擊到某子項,ViewPager就滑動到對應位置
                switch (item.getItemId()) {
                    case R.id.navigation_store:
                        setSateToolbarColor(MainActivity.this, R.color.colorAccent);
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_order:
                        setSateToolbarColor(MainActivity.this, R.color.colorAccent);
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_history:
                        setSateToolbarColor(MainActivity.this, R.color.colorAccent);
                        viewPager.setCurrentItem(2);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //滑動時調用多次
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                 /*
                 * position:當前所屬頁面,滑動時調用的最後一次絕對是滑動停止所在頁面
                 * positionOffset: 表示從位置的頁面偏移的[0,1]的值
                 * positionOffsetPixels: 以像素為单位的值，表示与位置的偏移
                 */
            }

            //滑動停止時調用
            @Override
            public void onPageSelected(int position) {
                //position滑动停止所在页面位置, 当滑动到某一位置，导航栏对应位置被按下

                navigation.getMenu().getItem(position).setChecked(true);
                /*
                   这里使用navigation.setSelectedItemId(position);无效，
                   setSelectedItemId(position)的官网原句：
                   Set the selected menu item ID. This behaves the same as tapping on an item
                   未找到原因
                */
            }

            //對於何时開始拖動或何时完全停止/空閒
            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                * 在滑動調用三次, 分别对应下面三种状态
                * SCROLL_STATE_IDLE：开始滑动（空闲状态->滑动），实际值为0
                * SCROLL_STATE_DRAGGING：正在被拖动，实际值为1
                * SCROLL_STATE_SETTLING：拖动结束,实际值为2
                */
            }
        });


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

    //狀態欄顏色設定
    public void setSateToolbarColor(AppCompatActivity appCompatActivity,Integer color){
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
    public final void setLogout(){
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.ic_person_outline_red_24dp)
                .setTitle("登出系統")
                .setMessage("是否登出?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


}
