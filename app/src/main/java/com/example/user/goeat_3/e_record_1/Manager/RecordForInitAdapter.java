package com.example.user.goeat_3.e_record_1.Manager;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.HashMap;


public class RecordForInitAdapter extends RecyclerView.Adapter<RecordForInitAdapter.ViewHolder> {

    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();
    private ArrayList<HashMap<String, String>> update_check = new ArrayList<>();
    String Status="";
    HashMap<String, String> map;
    // 建構式，用來接收外部程式傳入的項目資料。
    public RecordForInitAdapter(ArrayList<HashMap<String,String>> data)
    {
        maps = data;
//        Log.d("abc", "這裡跳轉了幾次");
//        map=new HashMap<String, String>();
//        map.put("personorder_id","123");
//        map.put("check","123");
//        update_check.add(map);
    }

    //宣告, 添加點擊事件
    private RecordForInitAdapter.ItemClickListener ClickListener;  //創建一個介面


    @NonNull
    @Override
    public RecordForInitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecordForInitAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_record_for_init, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordForInitAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.txt_orderName.setText(maps.get(i).get("nick_name"));
        viewHolder.txt_Amount.setText(maps.get(i).get("person_money")+" 元");


        if( maps.get(i).get("check").equals("0")){
            Status="未繳款";
            viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#c30d23"));
            viewHolder.cb_Select.setChecked(false);
        }
        else if( maps.get(i).get("check").equals("1")){
            Status="已繳款";
            viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#4CAF50"));
            viewHolder.cb_Select.setChecked(true);
        }
        else{
            Status="資料錯誤:((";
            viewHolder.cb_Select.setChecked(false);
        }

        viewHolder.txt_orderStatus.setText(Status);


        viewHolder.cb_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(maps.get(i).get("check").equals("0")){
                    if(!update_check.isEmpty()) {
                        for (int c = 0; c < update_check.size(); c++) {
                            Log.d("abc", c + "迴圈幾次");
                            if (maps.get(i).get("personorder_id").equals(update_check.get(c).get("personorder_id"))) {
                                update_check.get(c).put("check", "1"); //這個是要準備傳回去的
                                maps.get(i).put("check", "1");
                                viewHolder.txt_orderStatus.setText("已繳款");
                                viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#4CAF50"));
                                break;
                            } else if (c == update_check.size() - 1) {

                                Log.d("abc", i + "第幾個");
                                map = new HashMap<String, String>();
                                map.put("personorder_id", maps.get(i).get("personorder_id"));
                                map.put("check", "1");   //這個是要準備傳回去的
                                maps.get(i).put("check", "1");
                                update_check.add(map);
                                viewHolder.txt_orderStatus.setText("已繳款");
                                viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#4CAF50"));
                                break;
                            }
                        }
                    }
                    else{
                        map = new HashMap<String, String>();
                        map.put("personorder_id", maps.get(i).get("personorder_id"));
                        map.put("check", "1");   //這個是要準備傳回去的
                        maps.get(i).put("check", "1");
                        update_check.add(map);
                        viewHolder.txt_orderStatus.setText("已繳款");
                        viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#4CAF50"));

                    }
                }else if(maps.get(i).get("check").equals("1")) {

                    if (!update_check.isEmpty()) {
                        for (int c = 0; c < update_check.size(); c++) {
                            Log.d("abc", c + "迴圈幾次");
                            if (maps.get(i).get("personorder_id").equals(update_check.get(c).get("personorder_id"))) {
                                update_check.get(c).put("check", "0");  //這個是要準備傳回去的
                                maps.get(i).put("check", "0");  //這個是原本的
                                viewHolder.txt_orderStatus.setText("未繳款");
                                viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#c30d23"));
                                break;
                            } else if (c == update_check.size() - 1) {

                                Log.d("abc", i + "第幾個");
                                map = new HashMap<String, String>();
                                map.put("personorder_id", maps.get(i).get("personorder_id"));
                                map.put("check", "0");    //這個是要準備傳回去的
                                maps.get(i).put("check", "0");//這個是原本的
                                update_check.add(map);
                                viewHolder.txt_orderStatus.setText("未繳款");
                                viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#c30d23"));
                                break;
                            }
                        }
                    }
                    else {
                        Log.d("abc", i + "第幾個");
                        map = new HashMap<String, String>();
                        map.put("personorder_id", maps.get(i).get("personorder_id"));
                        map.put("check", "0");    //這個是要準備傳回去的
                        maps.get(i).put("check", "0");//這個是原本的
                        update_check.add(map);
                        viewHolder.txt_orderStatus.setText("未繳款");
                        viewHolder.txt_orderStatus.setTextColor(Color.parseColor("#c30d23"));
                    }
                }
            }
        });

        if(ClickListener!=null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ClickListener.OnIvClick(v, i); //跳轉至個人點餐明細
                }
            });
        }

    }

    //設置adapter接口
    public RecordForInitAdapter setClickListener(RecordForInitAdapter.ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    //聲明接口ItemClickListener
    public interface ItemClickListener{
        void OnIvClick(View view,int position);
    }

    public  ArrayList<HashMap<String, String>>  getMaps(){

        return  update_check;
    }
    @Override
    public int getItemCount() {
        return maps.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_orderName,txt_Amount,txt_orderStatus;
        private CheckBox cb_Select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_orderName = itemView.findViewById(R.id.txt_orderName);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_orderStatus = itemView.findViewById(R.id.txt_orderStatus);
            cb_Select=itemView.findViewById(R.id.cb_Select);
        }
    }
}

