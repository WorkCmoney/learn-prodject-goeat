package com.example.user.goeat_3.e_record_1.Order;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.e_record_1.meal_class;

import java.util.ArrayList;

public class RecordAllAdapter extends RecyclerView.Adapter<RecordAllAdapter.ViewHolder>{

    //宣告，儲存要顯示的資料
    private ArrayList<meal_class>  maps = new ArrayList<>();
    String Status;

    // 建構式，用來接收外部程式傳入的項目資料。
    public RecordAllAdapter(ArrayList<meal_class>  data) {
        maps = data;
    }

    @NonNull
    @Override
    public RecordAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecordAllAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_record_all, viewGroup, false));
    }
    @Override
    public void onBindViewHolder(@NonNull RecordAllAdapter.ViewHolder viewHolder, int i) {
        viewHolder.txt_orderName.setText(maps.get(i).getMeal());
        viewHolder.txt_Amount.setText(maps.get(i).getCount() + " 份");
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_orderName, txt_Amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_orderName = itemView.findViewById(R.id.txt_orderName);
            txt_Amount = itemView.findViewById(R.id.txt_num);
        }
    }
}
