package com.example.user.goeat_3.d_order_1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //宣告，儲存要顯示的資料
    private int count = 0;
    private int total = 0;
    private TextView person_price;
    private TextView person_count;
    private Activity a;
    int person_money = 0;
    int person_cout = 0;
    String order_meal = "";

        private ArrayList<HashMap<String, String>> food_maps = new ArrayList<>();
//    private ArrayList<HashMap<String, String>> store_maps = new ArrayList<>();


    // 建構式，用來接收外部程式傳入的項目資料。
    public OrderInfoAdapter(ArrayList<HashMap<String, String>> data1,Activity a) {
        this.a = a;
//        store_maps=data;
        food_maps = data1;


        person_price = a.findViewById(R.id.person_price);
        person_count = a.findViewById(R.id.person_count);
    }
    public void get(int money){

        person_money=money;
//        person_cout=count;
    }



    //宣告, 添加點擊事件
    private OrderInfoAdapter.ItemClickListener ClickListener;  //創建一個介面

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==0){

            return new ViewHolder_store(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_menu_1, viewGroup, false));

        }else {
            return new ViewHolder_order(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_order_info, viewGroup, false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        if(i ==0){
//            ((ViewHolder_store)viewHolder).rv_orderInfo.setText(sotre_maps.get(i).get("rv_orderInfo").toString());
            ((ViewHolder_store)viewHolder).store_name.setText(food_maps.get(0).get("store_name"));
            ((ViewHolder_store)viewHolder).store_type.setText(food_maps.get(0).get("store_type"));
            ((ViewHolder_store)viewHolder).store_phone.setText(food_maps.get(0).get("store_phone"));
            ((ViewHolder_store)viewHolder).store_address.setText(food_maps.get(0).get("store_address"));
            ((ViewHolder_store)viewHolder).store_open.setText(food_maps.get(0).get("store_open"));

            if(!food_maps.get(0).get("store_pic").toString().equals("")) {//顯示圖片用
                Glide.with(a) //顯示圖片用
                        .load("http://34.80.250.76/" + food_maps.get(i).get("store_pic").toString()) //顯示圖片用
                        .into(((ViewHolder_store)viewHolder).store_img); //顯示圖片用
            }
//            Log.d("qqq", store_maps.get().get("store_name").toString());
        }else {
            ((ViewHolder_order) viewHolder).menu_name.setText(food_maps.get(i).get("meal_name"));
            ((ViewHolder_order) viewHolder).menu_price.setText(food_maps.get(i).get("meal_price"));
            ((ViewHolder_order) viewHolder).menu_count.setText(food_maps.get(i).get("meal_count"));
            //創建view时添加監聽事件,一定要在onBindViewHolder中



                ((ViewHolder_order) viewHolder).btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = Integer.parseInt(food_maps.get(i).get("meal_count"));
                        count -= 1;
                        if (count >= 0) {
                            food_maps.get(i).put("meal_count", String.valueOf(count));
                            ((ViewHolder_order) viewHolder).menu_count.setText(String.valueOf(count));
                            person_money = 0;
                            person_cout = 0;
                            for (int i = 1; i < food_maps.size(); i++) {
                                if (!food_maps.get(i).get("meal_count").equals("0")) {
                                    person_money += Integer.parseInt(food_maps.get(i).get("meal_count")) * Integer.parseInt(food_maps.get(i).get("meal_price"));
                                    person_cout += Integer.parseInt(food_maps.get(i).get("meal_count"));
                                }
                            }
                            person_price.setText("總金額:" + String.valueOf(person_money) + "元");
                            person_count.setText("總數量:" + person_cout + "個");
                        }
                    }
                });
                ((ViewHolder_order) viewHolder).btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = Integer.parseInt(food_maps.get(i).get("meal_count"));
                        count += 1;
                        if (count >= 0) {
                            food_maps.get(i).put("meal_count", String.valueOf(count));
                            ((ViewHolder_order) viewHolder).menu_count.setText(String.valueOf(count));
                            person_money = 0;
                            person_cout = 0;
                            for (int i = 1; i < food_maps.size(); i++) {
                                if (!food_maps.get(i).get("meal_count").equals("0")) {
                                    person_money += Integer.parseInt(food_maps.get(i).get("meal_count")) * Integer.parseInt(food_maps.get(i).get("meal_price"));
                                    person_cout += Integer.parseInt(food_maps.get(i).get("meal_count"));
                                }
                            }
                            person_price.setText("總金額:" + String.valueOf(person_money) + "元");
                            person_count.setText("總數量:" + person_cout + "個");
                        }
                    }
                });


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return food_maps.size();
    }


    public class ViewHolder_order extends RecyclerView.ViewHolder {

        private TextView menu_name, menu_price, menu_count;
        private ImageView btn_remove, btn_add;

        public ViewHolder_order(@NonNull View itemView) {
            super(itemView);

            menu_name = itemView.findViewById(R.id.menu_name);
            menu_price = itemView.findViewById(R.id.menu_price);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            menu_count = itemView.findViewById(R.id.menu_count);
            btn_add = itemView.findViewById(R.id.btn_add);

        }
    }

    public class ViewHolder_store extends RecyclerView.ViewHolder {

        private TextView rv_orderInfo, store_name, store_type, store_phone, store_address, store_open;
        ImageView store_img;

        public ViewHolder_store(@NonNull View itemView) {
            super(itemView);

            rv_orderInfo = itemView.findViewById(R.id.rv_orderInfo);
            store_name = itemView.findViewById(R.id.store_name);
            store_type = itemView.findViewById(R.id.store_type);
            store_phone = itemView.findViewById(R.id.store_phone);
            store_address = itemView.findViewById(R.id.store_address);
            store_open = itemView.findViewById(R.id.store_open);
            store_img=itemView.findViewById(R.id.store_img);

        }
    }

    public HashMap<String, String> getFood_maps() {

        String order_meal = "";

        for (int i = 1; i < food_maps.size(); i++) {

            if (!food_maps.get(i).get("meal_count").equals("0")) {

                order_meal += food_maps.get(i).get("meal_count") + "/" + food_maps.get(i).get("meal_name") + ",";
            }
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("person_money", String.valueOf(person_money));
        map.put("order_meal", order_meal);
        return map;
    }

    //設置adapter接口
    public OrderInfoAdapter setClickListener(OrderInfoAdapter.ItemClickListener ClickListener) {
        this.ClickListener = ClickListener;
        return this;
    }

    //聲明接口ItemClickListener
    public interface ItemClickListener {
        void OnIvClick(View view, int position);
    }
}
