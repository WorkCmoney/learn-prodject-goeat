package com.example.user.goeat_3.c_store_1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.b_Main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class StoreFragment extends Fragment {

    //宣告
//    private View mView;
    private MainActivity mainActivity;
    private RecyclerView rv_store;
    private StoreAdapter adapter;
    private SearchView search_view;
    private JSONArray josnarray = null;
    private JSONObject jObj = null;
    private List<HashMap<String, Object>> maps;
    JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_store, container, false);

        maps = new ArrayList<HashMap<String, Object>>();

        return inflater.inflate(R.layout.fragment_store, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        new ccon().execute("AllStore");
        rv_store = view.findViewById(R.id.rv_store);
        rv_store.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_store.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));


        //ToolBar 設定
        setHasOptionsMenu(true);  //重要!
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//隱藏標題
        Toolbar toolbar = view.findViewById(R.id.toolbar_store);
        toolbar.inflateMenu(R.menu.base_toolbar_menu_store);
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


    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                String result = DBConnector.executeQuery(strings[0]);

              return result;

            }
        catch (Exception e){
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String s="";

            try {
                jObj = new JSONObject(result);

                josnarray=jObj.getJSONArray("data");

                    //把資料用迴圈 取出來放到productlist裡面
                    for (int i = 0; i < josnarray.length(); i++) {
                        JSONObject jsonData = josnarray.getJSONObject(i);
                        HashMap<String,Object> map=new HashMap<String,Object>();
                        //Integer.valueOf(c.getString("price"));
                        map.put("store_id",jsonData.getString("store_id"));
                        map.put("store_name",jsonData.getString("store_name"));
                        map.put("store_type", jsonData.getString("store_type"));
                        map.put("store_phone", "電話:" +jsonData.getString("store_phone"));
                        map.put("store_address", "地址:"+jsonData.getString("store_address"));
                        map.put("store_open", "營業時間: 14:00~22:00");
                        map.put("store_img", R.drawable.menu_food);
                        map.put("store_name",jsonData.getString("store_name"));

                        Log.d("abc", jsonData.getString("store_id"));
                        Log.d("abc", jsonData.getString("store_name"));
                        maps.add(map);
                        Log.d("gooda", "5.顯示成功");
                    }
                    // 將資料交給adapter
                    adapter = new StoreAdapter(maps);
                    rv_store.setAdapter(adapter);

                //實現跳轉動作
                adapter.setClickListener(new StoreAdapter.ItemClickListener() {
                    @Override
                    public void OnIvClick(View view, int position) {
                        Intent intent = new Intent((MainActivity)getActivity(),CreateStoreActivity.class);
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                Log.d("error", e.getMessage());
            }

        }
    }
}
