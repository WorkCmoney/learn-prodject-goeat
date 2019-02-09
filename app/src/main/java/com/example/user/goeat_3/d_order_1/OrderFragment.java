package com.example.user.goeat_3.d_order_1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.SignInUpActivity;
import com.example.user.goeat_3.b_Main.MainActivity;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.d_order_1.d_search.IOnSearchClickListener;
import com.example.user.goeat_3.d_order_1.d_search.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;

public class OrderFragment extends Fragment implements View.OnClickListener, IOnSearchClickListener {

    //宣告
    private View mView;
    private MainActivity mainActivity;
    private LinearLayout msubView,View1,View2;
    private View order1,order2;

    //for搜尋使用
    private TextView searchView;
    private SearchFragment searchFragment;
    private String searchInfo = "";  //訂單編號

    //for RecyclerView使用
    private RecyclerView rv_order;
    private OrderAdapter adapter;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    private ArrayList<HashMap<String, String>> maps;

    public HashMap<String, String> get_meal = new HashMap<String, String>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TooBar建立
        setHasOptionsMenu(true); //必須添加才能使用
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maps = new ArrayList<HashMap<String, String>>();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        msubView = (LinearLayout) mView.findViewById(R.id.msubView);
        order1 = inflater.inflate(R.layout.order1, null);
        order2 = inflater.inflate(R.layout.order2, null);
        View1 =(LinearLayout) order1.findViewById(R.id.order1);
        View2 =(LinearLayout) order2.findViewById(R.id.order2);

        return mView;
    }

    //onCreateView後執行,通常可以在這邊執行findViewbyId
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        msubView.removeAllViews();
        msubView.addView(View1);

        searchView = view.findViewById(R.id.searchView);
        searchFragment = SearchFragment.newInstance();
        searchView.setOnClickListener(this);
        searchFragment.setOnSearchClickListener(this);

        //ToolBar 設定
        setHasOptionsMenu(true);  //因為Toolbar上的文字和按鈕全是Activity傳過来的,onCreateOptionsMenu()只有Activity調用,Fragment的没有被調用
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//隱藏標題
        Toolbar toolbar = view.findViewById(R.id.toolbar_order);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); //调用Activity的方法来把它設置成ActionBar,會錯亂不要用
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

    @Override
    public void onClick(View v) {
        searchFragment.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        msubView.removeAllViews();
        msubView.addView(View1);
    }

    @Override
    public void OnSearchClick(String keyword) {
        searchInfo = keyword;     //搜尋後的結果接資料進來

        msubView.removeAllViews();
        msubView.addView(View2);

        rv_order = View2.findViewById(R.id.rv_order);
        rv_order.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_order.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        new ccon().execute("Select_Order",searchInfo);//多執緒進入

//        adapter.setClickListener(new OrderAdapter.ItemClickListener(){
//
//            @Override
//            public void OnIvClick(View view, int position) {
//                //跳轉至訂單明細
//                Intent intent = new Intent((MainActivity) getActivity(), OrderInfoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("order_id", maps.get(position).get("order_id").toString());
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
    }

    private class ccon extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String result;

            switch (strings[0]){
                case "person_order1":result = DBConnector.executeQuery(strings[0],strings[1],strings[2]);return  result ;

                case "Select_Order":result = DBConnector.executeQuery(strings[0],strings[1]);return result;
            }
            return  "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("azz", "沒資料"+result);
            if(!result.equals("")) {

                try {
                    jObj = new JSONObject(result);
                    int success = jObj.getInt("success");
                    if (success == 2) {
                        maps.clear();
                        get_meal.clear();
                        jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("order_id", jsonData.getString("order_id"));
                            map.put("store_name", jsonData.getString("store_name"));
                            map.put("order_title", jsonData.getString("order_title"));
                            map.put("order_end_time", jsonData.getString("order_end_time"));
                            map.put("order_delivery_time", jsonData.getString("order_delivery_time"));
                            map.put("user_name", jsonData.getString("user_name"));
                            map.put("order_status", jsonData.getString("order_status"));
                            map.put("order_price", jsonData.getString("order_price"));
                            map.put("order_remarks", jsonData.getString("order_remarks"));
                            maps.add(map);
                        }
                              adapter = new OrderAdapter(maps);
                              rv_order.setAdapter(adapter);
                        new ccon().execute("person_order1",maps.get(0).get("order_id"),user_id);
                            adapter.setClickListener(new OrderAdapter.ItemClickListener() {
                                @Override
                                public void OnIvClick(View view, int position) {

                                if(!nick_name.equals("")) {

                                    if (!get_meal.isEmpty()){ //這邊判斷有沒有點過餐
                                        if (get_meal.get("check").equals("1")) {

                                            Toast.makeText(mainActivity, "已經付款了,請先取消付款", Toast.LENGTH_SHORT).show();

                                        }else{

                                            Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("update","true");
                                            bundle.putString("order_id", maps.get(position).get("order_id"));
                                            bundle.putString("store_name", maps.get(position).get("store_name"));
                                            bundle.putString("person_money", get_meal.get("person_money"));
                                            bundle.putString("personorder_id", get_meal.get("personorder_id"));
                                            bundle.putString("order_meal",get_meal.get("order_meal"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                      }
                                }else{//這邊是沒點過的人
                                        Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("update","false");
                                        bundle.putString("order_id", maps.get(position).get("order_id"));
                                        bundle.putString("store_name", maps.get(position).get("store_name"));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }

                                }

                                else{
                                    Toast.makeText(mainActivity, "參加訂餐請先登入", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent((MainActivity) getActivity(), SignInUpActivity.class);
                                    startActivity(intent);
                                }
                                }
                            });

                    }
                    else if(success == 1){
                        jsonArray = jObj.getJSONArray("data");
                        JSONObject jsonData = jsonArray.getJSONObject(0);

                        get_meal.put("personorder_id",jsonData.getString("personorder_id"));
                        get_meal.put("user_id",jsonData.getString("user_id"));
                        get_meal.put("nick_name", jsonData.getString("nick_name"));
                        get_meal.put("order_id", jsonData.getString("order_id"));
                        get_meal.put("order_meal", jsonData.getString("order_meal"));
                        get_meal.put("person_money", jsonData.getString("person_money"));
                        get_meal.put("check", jsonData.getString("check"));

                        Log.d("azz", jsonData.getString("check"));

                    }
                    else if(success==3){
                        maps.clear();
                        get_meal.clear();
                        msubView.removeAllViews();
                        msubView.addView(View1);
                        Toast.makeText(mainActivity, "沒有該筆訂單", Toast.LENGTH_SHORT).show();
                    }
                    else if(success==4){
                        maps.clear();
                        get_meal.clear();
                        msubView.removeAllViews();
                        msubView.addView(View1);
                        Toast.makeText(mainActivity, "訂單已經結束下單瞜", Toast.LENGTH_SHORT).show();
                    }
                    else{
                       //如果進來這裡代表他沒下單過
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(mainActivity, "連線失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
