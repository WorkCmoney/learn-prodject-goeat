package com.example.user.goeat_3.c_store_1;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;



public class Menu1Fragment extends Fragment {

    //宣告
    private StoreInfoActivity storeInfoActivity;
    private RecyclerView rv_storeInfo;
    private Menu1Adapter adapter;
    private ArrayList<HashMap<String,String>>maps;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    private String store_name;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        storeInfoActivity = (StoreInfoActivity) context;
        store_name = storeInfoActivity.store_name;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        maps = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_menu1, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        rv_storeInfo = view.findViewById(R.id.rv_storeInfo);
        rv_storeInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_storeInfo.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        new Menu1Fragment.ccon().execute("SelectStoreMenu",store_name);
    }

    private void findViews() {

    }



    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try{
                result = DBConnector.executeQuery(strings[0], strings[1]);
                return result;

            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result =s;
            try {
                jObj = new JSONObject(result);
                jsonArray = jObj.getJSONArray("data");

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    HashMap<String,String> map=new HashMap<>();
                    map.put("meal_id",jsonData.getString("meal_id"));
                    map.put("meal_name",jsonData.getString("meal_name"));
                    map.put("meal_price",jsonData.getString("meal_price"));
                    maps.add(map);
                    Log.d("hint", "ok");
                }
                // 將資料交給adapter
                adapter = new Menu1Adapter(maps);
                rv_storeInfo.setAdapter(adapter);

            }catch (Exception e){
                Log.d("error", e.getMessage());
            }
        }
    }

}
