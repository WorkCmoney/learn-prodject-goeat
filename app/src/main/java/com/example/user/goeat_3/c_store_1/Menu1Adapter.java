package com.example.user.goeat_3.c_store_1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu1Adapter extends RecyclerView.Adapter<Menu1Adapter.ViewHolder> {

    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();

    // 建構式，用來接收外部程式傳入的項目資料。
    public Menu1Adapter(ArrayList<HashMap<String,String>> data) {
        maps = data;
    }

    @NonNull
    @Override
    public Menu1Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Menu1Adapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_store_info, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Menu1Adapter.ViewHolder viewHolder, int i) {
        viewHolder.txt_meal_name.setText(maps.get(i).get("meal_name"));
        viewHolder.txt_meal_price.setText(maps.get(i).get("meal_price")+" 元");
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

        private TextView txt_meal_name,txt_meal_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_meal_name = (TextView) itemView.findViewById(R.id.txt_meal_name);
            txt_meal_price = (TextView) itemView.findViewById(R.id.txt_meal_price);
        }
    }
}
