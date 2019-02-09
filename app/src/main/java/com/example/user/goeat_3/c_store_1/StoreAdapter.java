package com.example.user.goeat_3.c_store_1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.RoundImageView;

import java.util.HashMap;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //宣告，儲存要顯示的資料
    List<HashMap<String, Object>> mDataSet;

    //宣告, 添加點擊事件
    private ItemClickListener ClickListener;  //創建一個介面
    private LayoutInflater mInflater;
    private Context context;

    // 建構式，用來接收外部程式傳入的項目資料。
    public StoreAdapter(List<HashMap<String, Object>> data, Context context) {
        mDataSet = data;
        this.context=context;
    }
    public void change_data(List<HashMap<String, Object>> data){

        mDataSet = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==0){
            return new ViewHolder_2(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.create_store_item, viewGroup, false));
        }else {
            return new ViewHolder_1(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_store, viewGroup, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        if(i==0){
            ((ViewHolder_2)viewHolder).store_add.setText("新增店家");
            //創建view时添加監聽事件,一定要在onBindViewHolder中
            if(ClickListener!=null){
                ((ViewHolder_2)viewHolder).itemView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ClickListener.OnIvClick(v, i); //新增店家
                    }
                });
            }
        }else {
            ((ViewHolder_1)viewHolder).store_name.setText(mDataSet.get(i-1).get("store_name").toString());
            ((ViewHolder_1)viewHolder).store_type.setText(mDataSet.get(i-1).get("store_type").toString());
            ((ViewHolder_1)viewHolder).store_phone.setText(mDataSet.get(i-1).get("store_phone").toString());
            ((ViewHolder_1)viewHolder).store_address.setText(mDataSet.get(i-1).get("store_address").toString());
            ((ViewHolder_1)viewHolder).store_open.setText(mDataSet.get(i-1).get("store_open").toString());
            ((ViewHolder_1)viewHolder).img_star.setImageResource((int) (mDataSet.get(i-1).get("store_star")));

            if(!mDataSet.get(i-1).get("store_pic").toString().equals("")) {//顯示圖片用
                Glide.with(context) //顯示圖片用
                        .load("http://34.80.250.76/" + mDataSet.get(i - 1).get("store_pic").toString()) //顯示圖片用
                        .into(((ViewHolder_1) viewHolder).store_img); //顯示圖片用
            }
//            ((ViewHolder_1)viewHolder).img_star.setImageResource((int) (mDataSet.get(i-1).get("store_star"))); //報錯
            //創建view时添加監聽事件,一定要在onBindViewHolder中
            if(ClickListener!=null){
                ((ViewHolder_1)viewHolder).itemView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ClickListener.OnIvClick(v, i); //查看店家資訊
                    }
                });
                ((ViewHolder_1)viewHolder).btn_initiateAnOrder.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ClickListener.OnIvClick(v, i); // 發起訂單
                    }
                });
                ((ViewHolder_1)viewHolder).img_star.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ClickListener.OnIvClick(v, i); // 發起訂單
                    }
                });
                ((ViewHolder_1)viewHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClickListener.OnItemLongClick(v, i);
                        return true;
                    }
                });

            }
        }
    }

    //設置adapter接口
    public StoreAdapter setClickListener(ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }
    @Override
    public int getItemViewType(int position) {
        if(position ==0){
            return 0;
        }else{
            return 1;
        }
    }
    @Override
    public int getItemCount() {
        return mDataSet.size()+1;
    }

    public class ViewHolder_1 extends RecyclerView.ViewHolder  {

        //資料
        TextView store_name, store_type, store_phone, store_address, store_open;
        RoundImageView store_img;
        //發起訂單按鈕
        Button btn_initiateAnOrder;
        SearchView search_view;
        ImageView img_star;
        public ViewHolder_1(@NonNull View itemView) {
            super(itemView);

            store_name = itemView.findViewById(R.id.store_name);
            store_type = itemView.findViewById(R.id.store_type);
            store_phone = itemView.findViewById(R.id.store_phone);
            store_address = itemView.findViewById(R.id.store_address);
            store_open = itemView.findViewById(R.id.store_open);


            store_img = itemView.findViewById(R.id.store_img);


            search_view = itemView.findViewById(R.id.search_view);
            btn_initiateAnOrder = itemView.findViewById(R.id.btn_initiateAnOrder);
            img_star = itemView.findViewById(R.id.img_star);
//            itemView.setOnClickListener(this);
//            btn_initiateAnOrder.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition(); //getAdapterPosition為點擊的項目位置
//
//            if(v==itemView) {
//                //查看店家資訊
//                Toast.makeText(v.getContext(), "查看店家資訊", Toast.LENGTH_SHORT).show();
//            }
//            if (v.getId() == R.id.btn_initiateAnOrder) {
//                // 發起訂單
//                Toast.makeText(v.getContext(), "發起訂單", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    public class ViewHolder_2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        //新增店家
        TextView store_add;

        public ViewHolder_2(@NonNull View itemView) {
            super(itemView);
            store_add = itemView.findViewById(R.id.store_add);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v==itemView) {
                //新增店家
                Toast.makeText(v.getContext(), "新增店家", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //聲明接口ItemClickListener
    public interface ItemClickListener{
        void OnIvClick(View view,int position);
        void OnItemLongClick(View view,int position);
    }

    // 新增項目
    public void addItem(String text) {
        // 為了示範效果，固定新增在位置3。若要新增在最前面就把3改成0
//        mDataSet.add(3,text);
//        notifyItemInserted(3);
    }

    // 刪除項目
    public void removeItem(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }
}
