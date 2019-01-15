package com.example.user.goeat_3.e_history_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.b_Main.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment{

    //宣告
    private View mView;
    private MainActivity mainActivity;
    private RecyclerView rv_history;
    private HistoryAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        List<String> Dataset = new ArrayList<>();
        for(int i =0; i<50; i++){
            Dataset.add("訂單紀錄: 第 "+(i+1)+"項");
        }

        rv_history = mView.findViewById(R.id.rv_history);
        rv_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_history.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new HistoryAdapter(Dataset);
        rv_history.setAdapter(adapter);

        return mView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //ToolBar 設定
        setHasOptionsMenu(true);  //重要!
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//隱藏標題
        Toolbar toolbar = view.findViewById(R.id.toolbar_history);
        toolbar.inflateMenu(R.menu.base_toolbar_menu_history);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_logout:
                        mainActivity.setLogout();
                    default:
                        return true;
                }
            }
        });
    }
}
