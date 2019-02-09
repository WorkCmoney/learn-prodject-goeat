package com.example.user.goeat_3.e_record_1.For_RecordFragment2.Manager;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.e_record_1.Manager.Record4AllFragment;
import com.example.user.goeat_3.e_record_1.Manager.Record4RecordFragment;
import com.example.user.goeat_3.e_record_1.Manager.Record4SponsorActivity;

public class Record4SponsorActivity2 extends AppCompatActivity {

    String order_id,store_name,order_status,order_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        setContentView(R.layout.activity_record4_sponsor);

        //拿到查詢訂單Id
        Bundle bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        store_name = bundle.getString("store_name");
        order_status=bundle.getString("order_status");
        order_price=bundle.getString("order_price");
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

        TabLayout tabLayout = findViewById(R.id.tableLayout);

        tabLayout.addTab(tabLayout.newTab().setText("彙總"));
        tabLayout.addTab(tabLayout.newTab().setText("查核"));  //R.string.task)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final Record4SponsorActivity2.PageAdapter adapter = new Record4SponsorActivity2.PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
                    Record4AllFragment2 record4AllFragment2 = new Record4AllFragment2();
                    return record4AllFragment2;
                case 1:
                    Record4RecordFragment2 record4RecordFragment2 = new Record4RecordFragment2();
                    return record4RecordFragment2;
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
    public void setSateToolbarColor(AppCompatActivity appCompatActivity, Integer color){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(ContextCompat.getColor(appCompatActivity, color));
        }
    }

}
