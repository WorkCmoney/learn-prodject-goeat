package com.example.user.goeat_3.c_store_1;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.goeat_3.b_Main.MainActivity;


import com.example.user.goeat_3.R;


public class StoreInfoActivity extends AppCompatActivity {

//    public static String store_name;



    String store_name,store_id,store_star;
    Menu2Fragment menu2Fragment;
    Menu1Fragment menu1Fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        setSateToolbarColor(StoreInfoActivity.this, R.color.colorAccent);

        //拿到查詢店家名字
        Bundle bundle = getIntent().getExtras();
        store_name = bundle.getString("store_name");

        store_star = bundle.getString("store_star");
        //ToolBar 設定
        Toolbar toolbar = findViewById(R.id.toolbar_store);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        toolbar.inflateMenu(R.menu.base_toolbar_menu_back);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.action_back:
////                        menu2Fragment.fresh_like();
//
//                        finish();
//                    default:
//                        return true;
//                }
//            }
//        });



        TabLayout tabLayout = findViewById(R.id.tableLayout);

        tabLayout.addTab(tabLayout.newTab().setText("菜單明細"));
        tabLayout.addTab(tabLayout.newTab().setText("評價"));  //R.string.task)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }

    public class PageAdapter extends FragmentStatePagerAdapter {

        private int mNumOfTabs;

        public PageAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.mNumOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                     menu1Fragment = new Menu1Fragment();
                    return menu1Fragment;
                case 1:
                    menu2Fragment = new Menu2Fragment();

                    return menu2Fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }



        //狀態欄顏色設定
    public void setSateToolbarColor(AppCompatActivity appCompatActivity,Integer color){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(ContextCompat.getColor(appCompatActivity, color));
        }
    }

}
