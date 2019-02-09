package com.example.user.goeat_3.c_store_1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v7.widget.SearchView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.SignInUpActivity;
import com.example.user.goeat_3.b_Main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;


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
    Context context ;
    private SearchView msearch_view;
    private int Select_count;
    //上方皆為搜尋欄位專用

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_store, container, false);

        maps = new ArrayList<HashMap<String, Object>>();
        Select_count = 0;
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rv_store = view.findViewById(R.id.rv_store);
        rv_store.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_store.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
        context = getContext();
        adapter = new StoreAdapter(maps, context);
        //ToolBar 設定
        setHasOptionsMenu(true);  //重要!
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//隱藏標題
        final Toolbar toolbar = view.findViewById(R.id.toolbar_store);
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
        msearch_view = view.findViewById(R.id.search_view);
        msearch_view.setSubmitButtonEnabled(true);
        msearch_view.setIconifiedByDefault(false);

        msearch_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (Select_count == 0) {
                    Select_count = 1;

                    new ccon().execute("SelectStore", s);//多執緒進入
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        new ccon().execute("AllStore");//多執緒進入

    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                switch (strings[0]) {
                    case "AllStore":
                        result = DBConnector.executeQuery(strings[0]);
                        return result;
                    case "SelectStore":
                        if (strings[1].equals("中式") || strings[1].equals("日式") || strings[1].equals("西式") || strings[1].equals("泰式") || strings[1].equals("韓式") || strings[1].equals("炸物") || strings[1].equals("點心") || strings[1].equals("飲料") || strings[1].equals("複合式")) {
                            result = DBConnector.executeQuery(strings[0], "store_type", strings[1]);
                            return result;
                        } else {
                            result = DBConnector.executeQuery(strings[0], strings[1]);
                            return result;
                        }
                    case "Del_store":
                        DBConnector.executeQuery(strings[0], strings[1]);
                        return "del";
                }
                return null;
            } catch (Exception e) {
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 顯示照片需要context
            String[] time;
            if (!result.equals("")&&!result.equals("del")) {
                maps.clear();
                try {
                    jObj = new JSONObject(result);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        josnarray = jObj.getJSONArray("data");
                        for (int i = 0; i < josnarray.length(); i++) {
                            JSONObject jsonData = josnarray.getJSONObject(i);
                            HashMap<String, Object> map = new HashMap<String, Object>();

                            map.put("store_id", jsonData.getString("store_id"));
                            map.put("store_name", jsonData.getString("store_name"));
                            map.put("store_type", jsonData.getString("store_type"));
                            map.put("store_phone", "電話:" + jsonData.getString("store_phone"));
                            map.put("store_address", "地址:" + jsonData.getString("store_address"));
                            map.put("store_creat_person",jsonData.getString("store_creat_person"));
                            time = jsonData.getString("store_time").split(",");
                            map.put("store_pic", jsonData.getString("store_pic"));//新增圖片用
                            map.put("store_open", "營業時間:" + time[0] + ":" + time[1] + "~" + time[2] + ":" + time[3]);

                            switch (jsonData.getString("store_star")) {
                                case "5":
                                    map.put("store_star", R.drawable.star_five);
                                    break;
                                case "4":
                                    map.put("store_star", R.drawable.star_four);
                                    break;
                                case "3":
                                    map.put("store_star", R.drawable.star_three);
                                    break;
                                case "2":
                                    map.put("store_star", R.drawable.star_two);
                                    break;
                                case "1":
                                    map.put("store_star", R.drawable.star_one);
                                    break;
                            }
//                            map.put("store_img", R.drawable.menu_food);
                            map.put("store_star1", jsonData.getString("store_star"));

                            map.put("store_name", jsonData.getString("store_name"));
                            maps.add(map);
                        }
                        adapter.change_data(maps); // 顯示照片需要context
                        rv_store.setAdapter(adapter);

                        adapter.setClickListener(new StoreAdapter.ItemClickListener() {
                            @Override
                            public void OnIvClick(View view, int position) {
                                if (position == 0) {
                                    //跳轉至新增店家
                                    if (nick_name.equals("")) {//沒註冊會先跳去註冊
                                        Toast.makeText(mainActivity, "新增店家請先登入", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), SignInUpActivity.class);
                                        startActivity(intent);
                                    } else {//其他可以辦
                                        Intent intent = new Intent((MainActivity) getActivity(), CreateStoreActivity.class);
                                        startActivity(intent);
                                    }
                                } else if (view.getId() == R.id.img_star || view.getId() == R.id.btn_initiateAnOrder) {
                                    //判斷有沒有登入
                                    if (nick_name.equals("")) {
                                        Toast.makeText(mainActivity, "發起訂單請先登入", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent((MainActivity) getActivity(), SignInUpActivity.class);
                                        startActivity(intent);
                                    } else {
                                        //跳轉至發起訂單
                                        InitAnOrderDiFragment dialog = new InitAnOrderDiFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("store_name", maps.get(position - 1).get("store_name").toString());
                                        bundle.putString("store_id", maps.get(position - 1).get("store_id").toString());
                                        dialog.setArguments(bundle);
                                        dialog.show(getFragmentManager(), "init_an_order");
                                    }
                                } else {
                                    //跳轉至查詢店家內容
                                    Intent intent = new Intent((MainActivity) getActivity(), StoreInfoActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("store_name", maps.get(position - 1).get("store_name").toString());
                                    bundle.putString("store_id", maps.get(position - 1).get("store_id").toString());
                                    bundle.putString("store_star", maps.get(position - 1).get("store_star1").toString());
                                    Log.d("zzzz", "船進去的星星" + maps.get(position - 1).get("store_star1").toString());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }

                            //長按事件(編輯店家/編輯菜單/刪除)
                            @Override
                            public void OnItemLongClick(View view, final int position) {
                                //判斷有沒有登入
                                if (nick_name.equals("")) {
                                    Toast.makeText(mainActivity, "發起訂單請先登入", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent((MainActivity) getActivity(), SignInUpActivity.class);
                                    startActivity(intent);
                                } else {
                                    final Dialog d = new Dialog(getActivity());
                                    d.setContentView(R.layout.dialog_store_edit);

                                    TextView store_name, btn_edit_store, btn_edit_menu, btn_delete;

                                    store_name = d.findViewById(R.id.store_name);
                                    btn_edit_store = d.findViewById(R.id.btn_edit_store);
                                    btn_edit_menu = d.findViewById(R.id.btn_edit_menu);
                                    btn_delete = d.findViewById(R.id.btn_delete);
                                    store_name.setText(maps.get(position - 1).get("store_name").toString());
                                    d.show();

                                    //跳轉至編輯店家
                                    btn_edit_store.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            d.dismiss();
                                            Intent intent = new Intent((MainActivity) getActivity(), EditStoreActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("store_name", maps.get(position - 1).get("store_name").toString());
                                            intent.putExtras(bundle);
                                            startActivity(intent);

                                        }
                                    });
                                    //跳轉至編輯菜單
                                    btn_edit_menu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            d.dismiss();
                                            Intent intent = new Intent((MainActivity) getActivity(), EditMenuActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("store_name", maps.get(position - 1).get("store_name").toString());
                                            intent.putExtras(bundle);
                                            startActivity(intent);

                                        }
                                    });
                                    //刪除店家
                                    btn_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            d.dismiss();
                                            new AlertDialog.Builder(getActivity())
                                                    .setIcon(R.drawable.ic_delete_forever_black_24dp)
                                                    .setTitle("確定刪除")
                                                    .setMessage("店家一旦被刪除將無法回復!\n是否刪除店家?")
                                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            if(user_id.equals( maps.get(position-1).get("store_creat_person").toString())) {

                                                                String str = maps.get(position - 1).get("store_name").toString();
                                                                new ccon().execute("Del_store", str);
                                                            }else{
                                                                Toast.makeText(getActivity(), "你不是商家創建者無法刪除店家", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    })
                                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                }
                                }

                        });
                        Select_count = 0;
                    } else {
                        adapter = new StoreAdapter(maps, context); // 顯示照片需要context
                        rv_store.setAdapter(adapter);
                        Toast.makeText(mainActivity, "店家顯示失敗", Toast.LENGTH_SHORT).show();
                        Select_count = 0;
                    }
                } catch (JSONException e) {
                    Log.d("error", e.getMessage());
                }
            } else {
                if(result.equals("")){

                    Toast.makeText(mainActivity, "連線失敗", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
}
