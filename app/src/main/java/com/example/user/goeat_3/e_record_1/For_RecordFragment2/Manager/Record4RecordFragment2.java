package com.example.user.goeat_3.e_record_1.For_RecordFragment2.Manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.d_order_1.d_search.IOnSearchClickListener;
import com.example.user.goeat_3.d_order_1.d_search.SearchFragment;
import com.example.user.goeat_3.e_record_1.Manager.Record4RecordFragment;
import com.example.user.goeat_3.e_record_1.Manager.Record4SponsorActivity;
import com.example.user.goeat_3.e_record_1.Manager.RecordForInitAdapter;
import com.example.user.goeat_3.e_record_1.Manager.RecordForOrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Record4RecordFragment2 extends Fragment implements View.OnClickListener, View.OnTouchListener {

    //宣告
    private Record4SponsorActivity2 record4SponsorActivity2;
    private RecyclerView rv_record;
    private RecordForInitAdapter adapter;
    private ArrayList<HashMap<String, String>> maps;
    private ArrayList<HashMap<String, String>> mapsselect;
    private ArrayList<HashMap<String, String>> update_chick;
    private JSONArray jsonArray = null;
    private JSONObject jObj = null;
    private String store_name;

    //for搜尋使用
    private FloatingActionButton fab; //搜尋用
    private SearchFragment searchFragment;
    private String searchInfo = "";  //搜尋使用者
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
    private final int ADAPTER_VALUE = 16;

    private String order_id;
    private boolean check = true;
    private TextView btn_return, btn_sendRecord,check_type,money_type;
    String  money_typee="未排序";
    String  status_typee="未排序";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        record4SponsorActivity2 = (Record4SponsorActivity2) context;
        order_id = record4SponsorActivity2.order_id;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maps = new ArrayList<>();
        mapsselect = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record4_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_return = view.findViewById(R.id.btn_return);
        btn_sendRecord = view.findViewById(R.id.btn_sendRecord);
        check_type=view.findViewById(R.id.check_type);
        money_type=view.findViewById(R.id.money_type);
        fab = view.findViewById(R.id.fab);
        fab.setOnTouchListener(this);
        searchFragment = SearchFragment.newInstance();
        rv_record = view.findViewById(R.id.rv_record);
        rv_record.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_record.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

//        btn_return.setOnClickListener(this);
        check_type.setOnClickListener(this);
        money_type.setOnClickListener(this);
        btn_sendRecord.setOnClickListener(this);
        fab.setOnClickListener(this);
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                searchInfo = keyword;     //搜尋後的結果接資料進來

                for(int i=0;i<maps.size();i++){

                    if(searchInfo.equals(maps.get(i).get("nick_name"))){
                        mapsselect.clear();
                        mapsselect.add( maps.get(i));
                        adapter = new RecordForInitAdapter(mapsselect);
                        rv_record.setAdapter(adapter);
                        adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                            @Override
                            public void OnIvClick(View view, int position) {
                                //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                                Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                                bundle.putString("order_id", maps.get(position).get("order_id"));
                                bundle.putString("nick_name", maps.get(position).get("nick_name"));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        break;
                    }
                    else if(i==maps.size()-1){
                        Toast.makeText(record4SponsorActivity2, "未搜尋到", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        new Record4RecordFragment2.ccon().execute("orderingcontent", order_id);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.check_type: // 打勾的排序

                switch (status_typee){

                    case "未排序":
                        for(int i=0;i<maps.size();i++){
                            int n=0;
                            for(int j=i;j<maps.size();j++) {
                                if(maps.get(j).get("check").equals("0")){
                                    Collections.swap(maps,i,j);
                                }
                            }
                        }
                        adapter = new RecordForInitAdapter(maps);
                        rv_record.setAdapter(adapter);
                        money_typee="未排序";
                        status_typee="未付款";
                        check_type.setText("狀態(未)");
                        money_type.setText("金額"); adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                        break;
                    case  "未付款":
                        Collections.reverse(maps);
                        adapter = new RecordForInitAdapter(maps);
                        rv_record.setAdapter(adapter);
                        status_typee="已付款";
                        check_type.setText("狀態(已)"); adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                        break;

                    case  "已付款":
                        Collections.reverse(maps);
                        adapter = new RecordForInitAdapter(maps);
                        rv_record.setAdapter(adapter);
                        status_typee="未付款";
                        check_type.setText("狀態(未)"); adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                        break;
                }
                break;
            case R.id.money_type:  //金錢的排序

                switch (money_typee){

                    case "未排序":
                        int max=Integer.parseInt(maps.get(0).get("person_money"));
                        int n=0;
                        for(int i=0;i<maps.size();i++){
                            for(int j=i;j<maps.size();j++) {
                                if(Integer.parseInt(maps.get(j).get("person_money"))>max){
                                    max=Integer.parseInt(maps.get(j).get("person_money"));
                                    Collections.swap(maps,i,j);
                                }
                            }
                            max=0;
                        }
                        adapter = new RecordForInitAdapter(maps);
                        rv_record.setAdapter(adapter);

                        money_typee="大到小";
                        money_type.setText("金額(大)");
                        status_typee="未排序";
                        check_type.setText("狀態"); adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                        break;
                    case "大到小":
                        Collections.reverse(maps);
                        adapter = new RecordForInitAdapter(maps);
                        rv_record.setAdapter(adapter);
                        money_typee="小到大";
                        money_type.setText("金額(小)"); adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                        break;
                    case "小到大":
                        Collections.reverse(maps);
                        adapter = new RecordForInitAdapter(maps);
                        rv_record.setAdapter(adapter);
                        money_typee="大到小";
                        money_type.setText("金額(大)"); adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點
                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                        break;
                }


                break;

            case R.id.btn_return:   //回到主畫面MainActivity
//                finish();
                break;
            case R.id.fab:
                searchFragment.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);

                Toast.makeText(getActivity(), "請輸入欲搜尋訂餐者", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_sendRecord: //狀態確認完成,上傳

                if (check) {
                    String check_status = "";
                    update_chick = adapter.getMaps();

                    Log.d("abc", "數量:" + update_chick.size());

                    for (int i = 0; i < update_chick.size(); i++) {
                        //if(!maps.get(i).get("menu_price").equals(""))
                        //Log.e("TTT",maps.get(i).get("meal_price"));
                        if (!update_chick.get(i).get("personorder_id").equals("")) {
                            if (i != 0) {
                                check_status += ",";
                            }
                            check_status += update_chick.get(i).get("personorder_id") + "/" + update_chick.get(i).get("check");
                        }
                    }
                    Log.d("abc", "數量:" + check_status);
                    new Record4RecordFragment2.ccon().execute("UpDate_check", check_status);


                    update_chick.clear();
                    check = false;
                    Toast.makeText(record4SponsorActivity2, "繳款紀錄更新", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Toast.makeText(record4SponsorActivity2, "繳款紀錄已經更新過了", Toast.LENGTH_SHORT).show();
                }

        }
    }

    //設置懸浮按鈕隨手指移動
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                mDownX = (int) event.getRawX();
                mDownY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                int translationX = (int) fab.getTranslationX() + deltaX;
                int translationY = (int) fab.getTranslationY() + deltaY;
                fab.setTranslationX(translationX);
                fab.setTranslationY(translationY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (rangeInDefined(mDownX, (int) event.getRawX() - ADAPTER_VALUE, (int) event.getRawX() + ADAPTER_VALUE)) {
                    if (rangeInDefined(mDownY, (int) event.getRawY() - ADAPTER_VALUE, (int) event.getRawY() + ADAPTER_VALUE)) {
                        fab.callOnClick();
                    }
                }
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        Log.v("hint","mDownX: "+mDownX+",mDownY: "+mDownY);
        Log.v("hint","mLastX: "+mLastX+",mLastY: "+mLastY);
        Log.v("hint","x: "+x+",y: "+y);
        Log.v("hint","ADAPTER_VALUE: "+ADAPTER_VALUE);
        return true;
    }

    private boolean rangeInDefined(int mDownX, int i, int i1) {

        return Math.max(i,mDownX) == Math.min(mDownX,i1);
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            switch (strings[0]) {
                case "orderingcontent":
                    String result = DBConnector.executeQuery("orderingcontent", strings[1]);
                    return result;
                case "UpDate_check":
                    result = DBConnector.executeQuery("UpDate_check", strings[1]);
                    return result;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Log.d("qqq", "這邊是個確認紀錄" + result);
                jObj = new JSONObject(result);
                jsonArray = jObj.getJSONArray("data");
                Log.d("abc", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("personorder_id", jsonData.getString("personorder_id"));

                    map.put("user_id", jsonData.getString("user_id"));
                    map.put("order_id", jsonData.getString("order_id"));
                    map.put("nick_name", jsonData.getString("nick_name"));
                    map.put("order_meal", jsonData.getString("order_meal"));
                    map.put("person_money", jsonData.getString("person_money"));
                    map.put("check", jsonData.getString("check"));

                    maps.add(map);
                    // 將資料交給adapter
                    adapter = new RecordForInitAdapter(maps);
                    rv_record.setAdapter(adapter);
                    adapter.setClickListener(new RecordForInitAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            //跳轉至個人點餐明細  管理者跳進去看每個人餐點

                            Intent intent = new Intent(getActivity(), RecordForOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("personorder_id", maps.get(position).get("personorder_id"));
                            bundle.putString("order_id", maps.get(position).get("order_id"));
                            bundle.putString("nick_name", maps.get(position).get("nick_name"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
