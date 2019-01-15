package com.example.user.goeat_3.d_order_1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.goeat_3.b_Main.MainActivity;
import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    /*
       [Create] onAttach -> onCreate -> onCreateView -> (onViewCreated) -> onActivityCreated
    -> [Started] onStart()
    -> [Resumed] onResume() Fragment的生命週期依附在Activity上,resume時可互動
    -> [Paused] onPause()  在背景
    -> [Stopped] onStop()  看不見
    -> [Destroyed] onDestroyView -> onDestroy -> onDetach
    *-----------------------------------------------------------------*
    onDestroyView --------------> onCreateView
    */

    //宣告
    private View mView;
    private MainActivity mainActivity;
    private SearchView searchView;

    //一開始就被呼叫,在Fragment被Activity綁定時,會回傳Activity,此時可綁定Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    //當Fragment被建立或重建時,在onAttach後被呼叫,可以設定一些不需要Activity的初始化
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TooBar建立
        setHasOptionsMenu(true); //必須添加才能使用
    }

    //onCreate後被呼叫,用來回傳Fragment綁定的Layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        return mView;
    }

    //onCreateView後執行,通常可以在這邊執行findViewbyId
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ToolBar 設定
        setHasOptionsMenu(true);  //因為Toolbar上的文字和按鈕全是Activity傳過来的,onCreateOptionsMenu()只有Activity調用,Fragment的没有被調用
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//隱藏標題
        Toolbar toolbar = view.findViewById(R.id.toolbar_order);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar); //调用Activity的方法来把它設置成ActionBar,會錯亂不要用
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //設置返回鑑
        toolbar.inflateMenu(R.menu.base_toolbar_menu_order);
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




    //當Activity創建完後會執行,這時候可以安全地尋找Activity上的元件
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //當Fragment與Activity解除綁定後執行,這邊可執行資源的回收
    @Override
    public void onDetach() {
        super.onDetach();
    }


    //getActivity()再轉型成自己程式中的Activity型態
//    MainActivity mainActivity = (MainActivity) getActivity();
}
