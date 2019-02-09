package com.example.user.goeat_3.e_record_1;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.RecordForPersonalActivity;
import com.example.user.goeat_3.b_Main.MainActivity;
import com.example.user.goeat_3.e_record_1.For_RecordFragment2.Manager.Record4SponsorActivity2;
import com.example.user.goeat_3.e_record_1.For_RecordFragment2.Order.RecordAllActivity2;
import com.example.user.goeat_3.e_record_1.Manager.Record4SponsorActivity;
import com.example.user.goeat_3.e_record_1.Manager.RecordForOrderActivity;
import com.example.user.goeat_3.e_record_1.Order.RecordAllActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;


//歷史訂單
public class Record2Fragment extends Fragment {

    //宣告
    private MainActivity mainActivity;
    private RecyclerView rv_record;
    private RecordAdapter adapter;
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();
    private JSONArray josnArray = null;
    private JSONObject jObj = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RecordAdapter(maps);
        rv_record = view.findViewById(R.id.rv_record);
        rv_record.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_record.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(nick_name.equals("")){
//            Toast.makeText(getActivity(), "登入才能觀看紀錄喔", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            new Record2Fragment.ccon().execute("Record",nick_name,user_id);//多執緒進入
//            Log.d("777", "有進入嗎: ");
//        }
        if (!nick_name.equals("")) {
            new Record2Fragment.ccon().execute("Record", nick_name, user_id);//多執緒進入
            Log.d("777", "有進入嗎: ");
        }
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result;
            if(strings[0].equals("Record")){

                result = DBConnector.executeQuery("Record", strings[1], strings[2]);
                return  result;

            }else if(strings[0].equals("Delect_order")){

                DBConnector.executeQuery("Delect_order", strings[1]);
                return  null;

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                maps.clear();
                try {
                    jObj = new JSONObject(result);

                    josnArray = jObj.getJSONArray("data");

                    for (int i = 0; i < josnArray.length(); i++) {
                        JSONObject jsonData = josnArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();

                        if (jsonData.getString("order_status").equals("尚未結單")) {
                            continue;
                        }
                        map.put("order_id", jsonData.getString("order_id"));
                        map.put("store_name", jsonData.getString("store_name"));
//                    map.put("order_title", jsonData.getString("order_title"));
//                    map.put("order_end_time", jsonData.getString("order_end_time"));
//                    map.put("order_delivery_time", jsonData.getString("order_delivery_time"));
//                    map.put("order_remarks", jsonData.getString("order_remarks"));
                        map.put("user_name", jsonData.getString("user_name"));

                        map.put("order_price", jsonData.getString("order_price"));

                        map.put("order_status", jsonData.getString("order_status"));
                        maps.add(map);

                    }
                    // 將資料交給adapter
                    adapter.change_data(maps);
                    rv_record.setAdapter(adapter);

                    //實現跳轉動作
                    adapter.setClickListener(new RecordAdapter.ItemClickListener() {
                        @Override
                        public void OnIvClick(View view, int position) {
                            if (view.getId() == R.id.btn_collectionRecord) {

//                            //判斷這個訂單的發起者 跟登入發起人是不是相同的人
                                if (maps.get(position).get("user_name").equals(nick_name)) { //是發起者
                                    //跳轉到發起者頁面
                                    Intent intent = new Intent((MainActivity) getActivity(), Record4SponsorActivity2.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("order_id", maps.get(position).get("order_id"));
                                    bundle.putString("store_name", maps.get(position).get("store_name"));
                                    bundle.putString("order_status", maps.get(position).get("order_status"));
                                    bundle.putString("order_price", maps.get(position).get("order_price"));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } else if (view.getId() == R.id.btn_all) {
                                if (!maps.get(position).get("user_name").equals(nick_name)) {//不是發起者

                                    Intent intent = new Intent((MainActivity) getActivity(), RecordAllActivity2.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("order_id", maps.get(position).get("order_id"));
                                    bundle.putString("order_price", maps.get(position).get("order_price"));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } else {

                                //跳轉到訂餐者頁面
                                Intent intent = new Intent((MainActivity) getActivity(), RecordForOrderActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("order_id", maps.get(position).get("order_id"));
                                bundle.putString("nick_name", "not_order_person");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                        //長按事件(刪除)
                        @Override
                        public void OnItemLongClick(View view, final int position) {
                            //判斷這個訂單的發起者 跟登入發起人是不是相同的人
                            if (maps.get(position).get("user_name").equals(nick_name)) { //是發起者

                                final Dialog d = new Dialog(getActivity());
                                d.setContentView(R.layout.dialog_record_edit);

                                TextView order_id, btn_delete;

                                order_id = d.findViewById(R.id.order_id);
                                btn_delete = d.findViewById(R.id.btn_delete);
                                order_id.setText("訂單編號: " + maps.get(position).get("order_id"));
                                d.show();

                                //刪除店家
                                btn_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        d.dismiss();
                                        new AlertDialog.Builder(getActivity())
                                                .setIcon(R.drawable.ic_delete_forever_black_24dp)
                                                .setTitle("確定刪除")
                                                .setMessage("訂單紀錄一旦被刪除將無法回復!\n是否刪除訂單紀錄?")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String order_id = maps.get(position).get("order_id");
                                                        adapter.mDataSet.remove(position);
                                                        adapter.notifyItemRemoved(position);
                                                        Toast.makeText(getActivity(), "訂單編號: " + order_id + "已刪除", Toast.LENGTH_SHORT).show();

                                                        new ccon().execute("Delect_order", order_id);//多執緒進入

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
