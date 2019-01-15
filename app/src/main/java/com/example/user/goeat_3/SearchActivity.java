package com.example.user.goeat_3;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //設定Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //設定Up按鈕
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //資料配接至ListView
        ListView listView = findViewById(R.id.listView);
        String[]products ={"1","2","3"};
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,products);
        listView.setAdapter(adapter);


        EditText edit = findViewById(R.id.edit);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchActivity.this).adapter.getFilter().filter(s); //與搜尋紀錄資料比對過濾
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
