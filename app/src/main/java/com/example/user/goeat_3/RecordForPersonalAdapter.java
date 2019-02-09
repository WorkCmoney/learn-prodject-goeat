package com.example.user.goeat_3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordForPersonalAdapter extends RecyclerView.Adapter<RecordForPersonalAdapter.ViewHolder> {

    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();

    // 建構式，用來接收外部程式傳入的項目資料。
    public RecordForPersonalAdapter(ArrayList<HashMap<String, String>> data) {
        maps = data;
    }

    @NonNull
    @Override
    public RecordForPersonalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecordForPersonalAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_record_for_order, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordForPersonalAdapter.ViewHolder viewHolder, int i) {
        viewHolder.txt_orderName.setText(maps.get(i).get("meal_name"));
        viewHolder.txt_Amount.setText(maps.get(i).get("meal_count"));

    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    @Override
    public int getItemViewType(int position) {
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
