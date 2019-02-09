package com.example.user.goeat_3.e_record_1.Manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordForOrderAdapter extends RecyclerView.Adapter<RecordForOrderAdapter.ViewHolder> {

    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();

    // 建構式，用來接收外部程式傳入的項目資料。
    public RecordForOrderAdapter(ArrayList<HashMap<String, String>> data) {
        maps = data;
    }

    @NonNull
    @Override
    public RecordForOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecordForOrderAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_record_for_order, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordForOrderAdapter.ViewHolder viewHolder, int i) {
        viewHolder.txt_orderName.setText(maps.get(i).get("meal_name"));
        viewHolder.txt_Amount.setText(maps.get(i).get("meal_count"));

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

        private TextView txt_orderName, txt_Amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_orderName = itemView.findViewById(R.id.txt_orderName);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
        }
    }
}
