package com.example.user.goeat_3.e_record_1;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.RecordForPersonalActivity;
import com.example.user.goeat_3.b_Main.MainActivity;
import com.example.user.goeat_3.e_record_1.Manager.Record4SponsorActivity;
import com.example.user.goeat_3.e_record_1.Manager.RecordForOrderActivity;
import com.example.user.goeat_3.e_record_1.Order.RecordAllActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;

//新進訂單
public class Record1Fragment extends Fragment {

    //宣告
    private MainActivity mainActivity;
    private RecyclerView rv_record;
    private RecordAdapter adapter;
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();
    private JSONArray josnArray = null;
    private JSONObject jObj = null;
    private Calendar c;  //建立日曆物件
    private String end_Date=""; //結單日期
    private String end_Time=""; //結單時間
    String  orderid;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new RecordAdapter(maps);



        if(!nick_name.equals("")){
            new Record1Fragment.ccon().execute("Record1", nick_name,user_id);//多執緒進入
        }

        rv_record = view.findViewById(R.id.rv_record);
        rv_record.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_record.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        c = Calendar.getInstance();   //建立日曆物件
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!nick_name.equals("")) {
            new Record1Fragment.ccon().execute("Record1", nick_name,user_id);//多執緒進入
            Log.d("7575", "onStart: ");
        }
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if(strings[0].equals("Update_order_time")){

                DBConnector.executeQuery("Update_order_time", strings[1],strings[2]);
                return  "";
            }
            else {
                String result = DBConnector.executeQuery("Record1", strings[1],strings[2]);

                return result;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            maps.clear();
            Log.d("7575", "列印結果"+result);

            if (!result.equals("")) {
                try {
                    jObj = new JSONObject(result);

                    int success = jObj.getInt("success");

                    if (success == 1) {
                        josnArray = jObj.getJSONArray("data");

                        for (int i = 0; i < josnArray.length(); i++) {
                            JSONObject jsonData = josnArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("order_id", jsonData.getString("order_id"));
                            map.put("store_name", jsonData.getString("store_name"));
//                    map.put("order_title", jsonData.getString("order_title"));
//                    map.put("order_end_time", jsonData.getString("order_end_time"));
//                    map.put("order_delivery_time", jsonData.getString("order_delivery_time"));
//                    map.put("order_remarks", jsonData.getString("order_remarks"));
                            map.put("user_name", jsonData.getString("user_name"));
                            map.put("order_status", jsonData.getString("order_status"));
                            map.put("order_price", jsonData.getString("order_price"));
                            maps.add(map);
                        }
                        Log.d("7575", "onPostExecute: ");
                        Log.d("7575", "map" + maps.size());
                        // 將資料交給adapter
                        adapter.change_data(maps);
                        rv_record.setAdapter(adapter);

                        //實現跳轉動作
                        adapter.setClickListener(new RecordAdapter.ItemClickListener() {
                            @Override
                            public void OnIvClick(View view, int position) {
                                if (view.getId() == R.id.btn_collectionRecord) {
                                    Log.d("6565", "位置" + position);
                                    Log.d("6565", maps.get(position) + "測試");
                                    if (maps.get(position) != null) {
//                            //判斷這個訂單的發起者 跟登入發起人是不是相同的人
                                        if (maps.get(position).get("user_name").equals(nick_name)) { //是發起者
                                            //跳轉到發起者頁面
                                            Intent intent = new Intent((MainActivity) getActivity(), Record4SponsorActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("order_id", maps.get(position).get("order_id"));
                                            bundle.putString("store_name", maps.get(position).get("store_name").toString());
                                            bundle.putString("order_status", maps.get(position).get("order_status"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                } else if (view.getId() == R.id.btn_all) {
                                    if (!maps.get(position).get("user_name").equals(nick_name)) {//不是發起者

                                        //跳轉至總點餐明細
                                        Intent intent = new Intent((MainActivity) getActivity(), RecordAllActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("order_id", maps.get(position).get("order_id").toString());
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

                            //長按事件(修改訂單時間)
                            @Override
                            public void OnItemLongClick(View view, int position) {
                                //判斷這個訂單的發起者 跟登入發起人是不是相同的人
                                if (maps.get(position).get("user_name").equals(nick_name)) { //是發起者

                                    final Dialog d = new Dialog(getActivity());
                                    d.setContentView(R.layout.dialog_record_edit_end_time);

                                    TextView order_id, btn_edit_endTime;

                                    order_id = d.findViewById(R.id.order_id);
                                    btn_edit_endTime = d.findViewById(R.id.btn_edit_endTime);

                                    orderid = maps.get(position).get("order_id");
                                    order_id.setText("訂單編號: " + orderid);
                                    d.show();

                                    //更改結單時間
                                    btn_edit_endTime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            d.dismiss();
                                            final Dialog DateTimeDialog = new Dialog(getActivity());
                                            DateTimeDialog.setContentView(R.layout.dialog_datetime);
                                            final TextView txt_end_Date, txt_end_Time;
                                            TextView btn_no, btn_yes;

                                            txt_end_Date = DateTimeDialog.findViewById(R.id.txt_end_Date);
                                            txt_end_Time = DateTimeDialog.findViewById(R.id.txt_end_Time);
                                            btn_no = DateTimeDialog.findViewById(R.id.btn_no);
                                            btn_yes = DateTimeDialog.findViewById(R.id.btn_yes);

                                            //更結單日期
                                            txt_end_Date.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DatePickerDialog dialog = new DatePickerDialog((MainActivity) getActivity(),
                                                            new DatePickerDialog.OnDateSetListener() {
                                                                @Override
                                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                                    c.set(Calendar.YEAR, year);
                                                                    c.set(Calendar.MONTH, month);
                                                                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                                                    if ((month + 1) < 10 && dayOfMonth < 10) {
                                                                        txt_end_Date.setText(year + "-0" + (month + 1) + "-0" + dayOfMonth);
                                                                    } else if ((month + 1) < 10) {
                                                                        txt_end_Date.setText(year + "-0" + (month + 1) + "-" + dayOfMonth);
                                                                    } else if (dayOfMonth < 10) {
                                                                        txt_end_Date.setText(year + "-" + (month + 1) + "-0" + dayOfMonth);
                                                                    } else {
                                                                        txt_end_Date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                                                                    }
                                                                    end_Date = txt_end_Date.getText().toString();
                                                                }
                                                            },
                                                            c.get(Calendar.YEAR),
                                                            c.get(Calendar.MONTH),
                                                            c.get(Calendar.DAY_OF_MONTH));
                                                    txt_end_Date.setText(end_Date);
                                                    dialog.show();

                                                }
                                            });
                                            //更改結單時間
                                            txt_end_Time.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    TimePickerDialog dialog = new TimePickerDialog((MainActivity) getActivity(),
                                                            new TimePickerDialog.OnTimeSetListener() {
                                                                @Override
                                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                                    c.set(Calendar.MINUTE, minute);

                                                                    if (hourOfDay < 10 && minute < 10) {
                                                                        txt_end_Time.setText("0" + hourOfDay + ":0" + minute + ":00");
                                                                    } else if (hourOfDay < 10) {
                                                                        txt_end_Time.setText("0" + hourOfDay + ":" + minute + ":00");
                                                                    } else if (minute < 10) {
                                                                        txt_end_Time.setText(hourOfDay + ":0" + minute + ":00");
                                                                    } else {
                                                                        txt_end_Time.setText(hourOfDay + ":" + minute + ":00");
                                                                    }
                                                                    end_Time = txt_end_Time.getText().toString();
                                                                }
                                                            },
                                                            c.get(Calendar.HOUR_OF_DAY),
                                                            c.get(Calendar.MINUTE), true);
                                                    txt_end_Time.setText(end_Time);
                                                    dialog.show();
                                                }
                                            });

                                            //否,不修改結單時間
                                            btn_no.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DateTimeDialog.dismiss();
                                                }
                                            });

                                            //是,修改截單時間
                                            btn_yes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (end_Date.equals("") || end_Time.equals("")) {
                                                        Toast.makeText(getActivity(), "時間請確實填寫", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        String time = end_Date + " " + end_Time;
                                                        Log.d("g77", time);
                                                        new ccon().execute("Update_order_time", orderid, time);//多執緒進入
                                                        Toast.makeText(getActivity(), "時間修改成功", Toast.LENGTH_SHORT).show();
                                                        DateTimeDialog.dismiss();
                                                    }
                                                }
                                            });
                                            DateTimeDialog.show();
                                        }
                                    });
                                }
                            }
                        });
                    }else {
                        adapter.change_data(maps);
                        rv_record.setAdapter(adapter);
                    }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

            }

        }

    }

}
