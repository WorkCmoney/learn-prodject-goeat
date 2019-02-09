package com.example.user.goeat_3.e_record_1;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.c_store_1.StoreAdapter;
import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;

import java.util.ArrayList;
import java.util.HashMap;

//訂單紀錄
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    //宣告，儲存要顯示的資料
    ArrayList<HashMap<String, String>> mDataSet = new ArrayList<>();

    // 建構式，用來接收外部程式傳入的項目資料。
    public RecordAdapter(ArrayList<HashMap<String, String>> data) {
        mDataSet = data;
    }

    //宣告, 添加點擊事件
    private ItemClickListener ClickListener;  //創建一個介面
    private LayoutInflater mInflater;

    public  void  change_data(ArrayList<HashMap<String, String>> data){


        mDataSet = data;
    }
    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecordAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_record, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.txt_storeName.setText(mDataSet.get(i).get("store_name").toString());
        viewHolder.txt_orderUserName.setText(mDataSet.get(i).get("user_name").toString());
        viewHolder.txt_totalAmount.setText(mDataSet.get(i).get("order_id"));
        viewHolder.txt_orderStatus .setText(mDataSet.get(i).get("order_status"));

        if( mDataSet.get(i).get("user_name").equals(nick_name)){
//            viewHolder.txt_storeName.getPaint().setFakeBoldText(true);
            viewHolder.txt_orderUserName.getPaint().setFakeBoldText(true);
//            viewHolder.txt_totalAmount.getPaint().setFakeBoldText(true);
//            viewHolder.txt_orderStatus.getPaint().setFakeBoldText(true);
        }


        Log.v("hint","nick_name:"+nick_name+",user_name: "+mDataSet.get(i).get("user_name"));
        if(nick_name.equals(mDataSet.get(i).get("user_name"))){
            viewHolder.btn_collectionRecord.setVisibility(View.VISIBLE);
            viewHolder.btn_all.setVisibility(View.GONE);
        }else {
            viewHolder.btn_all.setVisibility(View.VISIBLE);
            viewHolder.btn_collectionRecord.setVisibility(View.GONE);
        }

        if(ClickListener!=null){
            viewHolder.btn_all.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ClickListener.OnIvClick(v, i); //跳轉至所有人彙總
                }
            });
            viewHolder.btn_collectionRecord.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ClickListener.OnIvClick(v, i); //跳轉至發起人記錄細
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ClickListener.OnIvClick(v, i); //跳轉至總餐點明細 -> 跳轉至個人記錄細項面
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClickListener.OnItemLongClick(v, i);
                    return true;
                }
            });
        }

    }

    //設置adapter接口
    public RecordAdapter setClickListener(ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    //聲明接口ItemClickListener
    public interface ItemClickListener{
        void OnIvClick(View view,int position);
        void OnItemLongClick(View view,int position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //資料
        TextView txt_storeName, txt_orderUserName,txt_totalAmount,txt_orderStatus;
        Button btn_collectionRecord,btn_all;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

            txt_storeName = itemView.findViewById(R.id.txt_storeName);
            txt_orderUserName = itemView.findViewById(R.id.txt_orderUserName);
            txt_totalAmount = itemView.findViewById(R.id.txt_totalAmount);
            txt_orderStatus = itemView.findViewById(R.id.txt_orderStatus);
            btn_collectionRecord = itemView.findViewById(R.id.btn_collectionRecord);
//            btn_collectionRecord.setText("收款\n紀錄");
            btn_collectionRecord.setText("管\n理");
            btn_all = itemView.findViewById(R.id.btn_all);
//            btn_all.setText("彙總\n紀錄");
            btn_all.setText("查\n看");

        }

    }
}
