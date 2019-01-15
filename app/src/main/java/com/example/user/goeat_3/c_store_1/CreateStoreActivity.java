package com.example.user.goeat_3.c_store_1;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.user.goeat_3.R;

public class CreateStoreActivity extends AppCompatActivity implements View.OnClickListener {

    //新增店家元件
    private TextView btn_next,btn_return,btn_finish;
    private EditText edi_storeName;
    private View include_add_store;
    private String storeName="";

    //新增菜單元件
    private RecyclerView rv_add_menu;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSateToolbarColor(CreateStoreActivity.this, R.color.colorAccent);
        setContentView(R.layout.activity_create_store);

        Toolbar toolbar = findViewById(R.id.toolbar_createStore);
        findViews();
        btn_next.setOnClickListener(this);
        btn_return.setOnClickListener(this);

    }

    private void findViews() {
        View subView = findViewById(R.id.add_store);
        btn_next =subView.findViewById(R.id.btn_next);
        btn_return=subView.findViewById(R.id.btn_return);
        edi_storeName=subView.findViewById(R.id.edi_storeName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                storeName=edi_storeName.getText().toString();
                ToAddMenu();        //下一步至新增菜單
                break;
            case R.id.btn_return:
                finish();          //回到主畫面MainActivity
                break;
        }

    }

    //新增菜單
    private void ToAddMenu() {
        setContentView(R.layout.create_menu);

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
