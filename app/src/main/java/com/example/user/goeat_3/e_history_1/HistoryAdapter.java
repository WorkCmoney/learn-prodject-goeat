package com.example.user.goeat_3.e_history_1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.goeat_3.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    //宣告，儲存要顯示的資料
    private List<String> mDataSet;

    // 建構式，用來接收外部程式傳入的項目資料。
    public HistoryAdapter(List<String> data) {
        mDataSet = data;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HistoryAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.item.setText(mDataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
        }

    }
}
