package com.example.user.goeat_3.d_order_1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();

    // 建構式，用來接收外部程式傳入的項目資料。

    public OrderAdapter(ArrayList<HashMap<String,String>> data) {
        maps = data;
    }

    //宣告, 添加點擊事件
    private OrderAdapter.ItemClickListener ClickListener;  //創建一個介面

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_order, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txt_storeName.setText(maps.get(i).get("store_name"));
        viewHolder.txt_orderTitle.setText(maps.get(i).get("order_title"));
        viewHolder.txt_username.setText(maps.get(i).get("user_name"));
        viewHolder.txt_end_tim.setText(maps.get(i).get("order_end_time"));
        viewHolder.txt_delivery_time.setText(maps.get(i).get("order_delivery_time"));
        viewHolder.txt_note.setText(maps.get(i).get("order_remarks"));

        if(ClickListener!=null){
            viewHolder.btn_sendOrder.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ClickListener.OnIvClick(v, i); //跳轉至點餐明細
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return maps.size(); //測試用
//        return maps.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position;
    }

    //設置adapter接口
    public OrderAdapter setClickListener(OrderAdapter.ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    //聲明接口ItemClickListener
    public interface ItemClickListener{
        void OnIvClick(View view,int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item; //測試用

        TextView txt_orderTitle,txt_storeName,txt_username,
                txt_end_tim,txt_delivery_time,
                txt_note;

        Button btn_sendOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_storeName=itemView.findViewById(R.id.txt_storeName);
            item = itemView.findViewById(R.id.item);  //測試用
            txt_orderTitle = itemView.findViewById(R.id.txt_orderTitle);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_end_tim = itemView.findViewById(R.id.txt_end_tim);
            txt_delivery_time = itemView.findViewById(R.id.txt_delivery_time);
            txt_note = itemView.findViewById(R.id.txt_note);
            btn_sendOrder = itemView.findViewById(R.id.btn_sendOrder);

        }
    }
}
